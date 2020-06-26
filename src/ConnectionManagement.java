import java.io.*;
import java.net.Socket;

public class ConnectionManagement implements Runnable{
    private BufferedReader input_reader;
    private PrintWriter output_buff;
    private boolean in_game;
    private InfoPanel info_panel;
    private Gameplay gameplay;
    public static final int END = 1;
    public static final int RESTART = 2;
    public static final int READY = 3;

    private int[] enemy_info = {0, 0, 0, 0, -1, -1, -1, 0, 0, 0, 0};

    public ConnectionManagement(Socket soc, Gameplay g) {
        try {
            input_reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            output_buff = new PrintWriter(soc.getOutputStream(), true);
            in_game = true;
            gameplay = g;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(int tank_x, int tank_y, int tank_dir, int is_fired, int[] bull_info, int[] hit) {
            String out_info = tank_x + "," + tank_y + "," + tank_dir + "," + is_fired + "," + bull_info[0]
                    + "," + bull_info[1] + "," + bull_info[2] + "," + hit[0] + "," + hit[1] + "," + hit[2] + "," + hit[3];
            output_buff.println(out_info);
    }

    public void sendInfo(int val) {
        String out_info = Integer.toString(val);
        output_buff.println(out_info);
    }

    public void getInfo() {
        try {
            String[] in_info = input_reader.readLine().split(",");
            if (in_info.length == 1) {
                if (Integer.parseInt(in_info[0]) == END)
                    gameplay.stopGame(2);
                else if (Integer.parseInt(in_info[0]) == RESTART)
                    gameplay.enemyRestart();
                else if (Integer.parseInt(in_info[0]) == READY)
                    gameplay.enemyReady();
            }
            else if (in_info.length == 11) {
                int[] info;
                info = new int[11];
                for (int i = 0; i < 11; ++i) {
                    info[i] = Integer.parseInt(in_info[i]);
                }
                enemy_info = info;
                checkHit();
            }
        }
        catch (NullPointerException | IOException ne) {
            gameplay.enemyDisconnected();
        }
    }
    private void checkHit() {
        if (enemy_info[7] == 1 && enemy_info[10] == 1)
            info_panel.addEnemyPoint();
    }

    @Override
    public void run () {
        while (in_game) {
            getInfo();
            synchronized (this) {
                this.notify();
            }
        }
    }

    public int[] getEnemyInfo() {
        return enemy_info;
    }
    public void addInfoPanel(InfoPanel ip) {
        info_panel = ip;
    }

}
