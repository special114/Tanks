import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Init {
    private Socket connection;


    private JDialog dialog;

    public Init() {
        chooseAction(getProgramType());
    }

    public int getProgramType() {
        Object[] options = {"Serwer", "Klient", "Wyjdź"};

        return JOptionPane.showOptionDialog(null, "Wybierz typ swojego programu",
                "Tanks", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

    }

    public void chooseAction(int type) {
        switch (type) {
            case JOptionPane.YES_OPTION:
                createServer();
                break;
            case JOptionPane.NO_OPTION:
                connectToServer();
                break;
            default:
                System.exit(0);
        }
    }

    public void connectToServer() {
        try {
            String host;
            int port;
            IPField ipInput = new IPField();
            PortField portInput = new PortField();
            Object[] c = {ipInput, portInput};
            JOptionPane.showConfirmDialog(null, c, "Tanks", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
            host = ipInput.getText();
            port = Integer.parseInt(portInput.getText());

            connection = new Socket(host, port);
            if (connection.isConnected())
                startGame(connection, Gameplay.CLIENT);
        } catch (ConnectException ce) {
            Object[] options = {"Ponów próbę", "Wyjdź"};
            int opt = JOptionPane.showOptionDialog(null, "Nie udało się połączyć. Wybierz co chcesz zrobić dalej.",
                    "Tanks", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    options, null);
            if (opt == JOptionPane.YES_OPTION)
                connectToServer();
            else
                System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException numEx) {
            System.out.println("Błędny format portu.");
        }
    }

    public void createServer() {
        try {
            ServerSocket server_socket = new ServerSocket(10110);
            Thread accept_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection = server_socket.accept();
                        if (connection.isConnected()) {
                            dialog.dispose();
                            startGame(connection, Gameplay.SERVER);
                        }
                    } catch (SocketException s) {
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            accept_thread.start();
            Object[] option = {"Anuluj"};
            JOptionPane jop = new JOptionPane("Oczekiwanie na połączenie z klientem. Naciśnij anuluj, aby wyjść.",
                    JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, option);
            dialog = jop.createDialog(null, "Tanks");
            dialog.setVisible(true);
            String dialog_val = (String) jop.getValue();
            if (dialog_val.equals(option[0])) {
                server_socket.close();
            }
            dialog.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame(Socket soc, int type) {
        TanksGUI tanks = new TanksGUI();
        tanks.add(new Gameplay(soc, type));
        EventQueue.invokeLater(tanks);
    }


    public static void main(String[] args) {
        Init init = new Init();
    }

    private class IPField extends JComponent {
        JTextField ip_field;
        JLabel ip_label;

        public IPField() {
            setLayout(new FlowLayout());
            ip_label = new JLabel("Adres IP: ");
            ip_label.setPreferredSize(new Dimension(90, 20));
            ip_field = new JTextField();
            ip_field.setPreferredSize(new Dimension(150, 20));
            add(ip_label);
            add(ip_field);
        }

        public String getText() {
            return ip_field.getText();
        }
    }
    private class PortField extends JComponent {
        JTextField port_field;
        JLabel port_label;

        public PortField() {
            setLayout(new FlowLayout());
            port_label = new JLabel("Port: ");
            port_label.setPreferredSize(new Dimension(90, 20));
            port_field = new JTextField();
            port_field.setPreferredSize(new Dimension(150, 20));
            add(port_label);
            add(port_field);
        }

        public String getText() {
            return port_field.getText();
        }
    }
}
