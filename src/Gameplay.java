import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class Gameplay extends JPanel {
    Socket socket;
    ConnectionManagement con;
    public static final int CLIENT = 1;
    public static final int SERVER = 2;

    private Timer timer;
    TanksManagement tanks_man;
    int gameType;

    private boolean enemy_restart;
    private boolean restart;
    private boolean in_game;
    Thread con_thread;
    Thread tank_thread;
    GamePanel game_panel;
    InfoPanel info_panel;


    public Gameplay(Socket soc, int type) {
        socket = soc;
        setLayout(new BorderLayout());
        gameType = type;
        con = new ConnectionManagement(socket, this);
        initValues();
        con_thread = new Thread(con);
        con_thread.start();
        tank_thread.start();
        timer.start();
    }

    private void initValues() {
        game_panel = new GamePanel(gameType);
        info_panel = new InfoPanel(this);
        con.addInfoPanel(info_panel);
        tanks_man = new TanksManagement(game_panel, info_panel, con, gameType);
        game_panel.setTank(tanks_man);

        tank_thread = new Thread(tanks_man);

        addKeyListener(tanks_man);
        setFocusable(true);


        add(info_panel, BorderLayout.EAST);
        add(game_panel, BorderLayout.CENTER);

        restart = false;
        enemy_restart = false;
        in_game = true;

        timer = new Timer(10, game_panel);
        timer.addActionListener(info_panel);
    }

    public void endGame(int num) { // Avengers
        con.sendInfo(ConnectionManagement.END);
        stopGame(num);
    }

    public void stopGame(int num) {
        if (in_game) {
            in_game = false;
            timer.stop();
            tanks_man.setInGame(false);
            String s = "Wygrał gracz nr " + num;
            Object[] message = {s, "Wybierz, co chcesz zrobić dalej."};
            Object[] options = {"Zagraj ponownie", "Wyjdź"};
            int option = JOptionPane.showOptionDialog(null, message,
                    "Tanks", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    options, null);
            if (option == JOptionPane.YES_OPTION)
                restart();
            else {
                try {
                    socket.close();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
                System.exit(0);
            }
        }
    }

    private void restart() {
        con.sendInfo(ConnectionManagement.RESTART);
        if (!enemy_restart)
            restart = true;
        else {
            remove(game_panel);
            remove(info_panel);
            initValues();
            revalidate();
            con.sendInfo(ConnectionManagement.READY);
        }
    }
    public void enemyRestart() {
        enemy_restart = true;
        if (restart) {
            remove(game_panel);
            remove(info_panel);
            initValues();
            revalidate();
            con.sendInfo(ConnectionManagement.READY);
        }
    }
    public void enemyReady() {
            tank_thread.start();
            timer.start();
    }
    public void enemyDisconnected() {
        Object[] options = {"Wyjdź"};
        JOptionPane.showOptionDialog(null, "Niestety przeciwnik opuścił grę.",
                "Tanks", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);
        try {
            socket.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        System.exit(0);
    }

}
