import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class TanksManagement implements KeyListener, Runnable {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int TANK_SIZE = 40;

    private ConnectionManagement connection;
    private GamePanel gamePanel;
    private InfoPanel infoPanel;

    private int direction;
    private boolean is_moving;
    private int velocity;

    private BulletsManagement bullet;

    private ExplosionManagement explosion;
    Thread bullets_thread;
    Thread explosions_thread;
    private boolean fire = false;
    private boolean is_fired = false;
    private boolean is_fired_enemy = false;
    private boolean is_ready;

    private int[] x_pos = new int[2];
    private int[] y_pos = new int[2];

    private int[] tank2_info = {0, 0, 0, 0, -1, -1, -1, 0, 0, 0, 0};
    private int[] enemy_bullet_info = {0, 0, 0};
    private int[] bullet_info = {0, 0, 0};

    private boolean in_game;
    private int[] hit = {0, 0, 0, 0};

    private Timer bullet_timer;



    public TanksManagement(GamePanel gp, InfoPanel ip, ConnectionManagement con, int type) {
        setTankPosition(type);
        is_moving = false;
        velocity = 10;
        connection = con;
        gamePanel = gp;
        infoPanel = ip;
        bullet = new BulletsManagement(this);
        explosion = new ExplosionManagement();
        bullets_thread = new Thread(bullet);
        explosions_thread = new Thread(explosion);
        is_ready = true;
        connection.sendInfo(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE], direction, 0, bullet_info, hit);
        in_game = true;
        bullet_timer = new Timer();

    }
    @Override
    public void keyTyped(KeyEvent key) {}

    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                direction = UP;
                is_moving = true;
                break;
            case KeyEvent.VK_DOWN:
                direction = DOWN;
                is_moving = true;
                break;
            case KeyEvent.VK_LEFT:
                direction = LEFT;
                is_moving = true;
                break;
            case KeyEvent.VK_RIGHT:
                direction = RIGHT;
                is_moving = true;
                break;
            case KeyEvent.VK_SPACE:
                fire = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key)
    {
        is_moving = false;
    }

    public void checkStatus() {
        if (fire && is_ready) {
            manageBullets();
            is_ready = false;
            bullet_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    is_ready = true;
                }
            }, 500);
        }
        else if(!is_ready)
            fire = false;
        if (is_moving) {
            tankNotOnBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]);
            move();
            tankOnBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]);
        }
    }

    private void manageBullets() {
        if (fire && !is_fired) {
            switch (direction) {
                case UP:
                    if (y_pos[GamePanel.TOP_SIDE] >= 15) {
                        bullet.setBulletInfo(x_pos[GamePanel.LEFT_SIDE] + 15, y_pos[GamePanel.TOP_SIDE] - 1, direction);
                        bullet.setIsFired();
                        is_fired = true;
                    }
                    break;
                case DOWN:
                    if (y_pos[GamePanel.BOT_SIDE] <= GamePanel.HEIGHT - 16) {
                        bullet.setBulletInfo(x_pos[GamePanel.LEFT_SIDE] + 15, y_pos[GamePanel.BOT_SIDE] + 1, direction);
                        bullet.setIsFired();
                        is_fired = true;
                    }
                    break;
                case LEFT:
                    if (x_pos[GamePanel.LEFT_SIDE] >= 15) {
                    bullet.setBulletInfo(x_pos[GamePanel.LEFT_SIDE] - 1, y_pos[GamePanel.TOP_SIDE] + 15, direction);
                        bullet.setIsFired();
                        is_fired = true;
                }
                    break;
                case RIGHT:
                    if (x_pos[GamePanel.RIGHT_SIDE] <= GamePanel.WIDTH - 16) {
                        bullet.setBulletInfo(x_pos[GamePanel.RIGHT_SIDE] + 1, y_pos[GamePanel.TOP_SIDE] + 15, direction);
                        bullet.setIsFired();
                        is_fired = true;
                    }
                    break;
            }
        }
        fire = false;

        if (!bullets_thread.isAlive()) {
            bullets_thread = new Thread(bullet);
            bullets_thread.start();
        }

    }

    private void move() {
        if (!checkColissions()) {
            switch (direction) {
                case UP:
                    y_pos[GamePanel.TOP_SIDE] -= velocity;
                    y_pos[GamePanel.BOT_SIDE] -= velocity;
                    
                    break;
                case DOWN:
                    y_pos[GamePanel.TOP_SIDE] += velocity;
                    y_pos[GamePanel.BOT_SIDE] += velocity;
                    break;
                case LEFT:
                    x_pos[GamePanel.LEFT_SIDE] -= velocity;
                    x_pos[GamePanel.RIGHT_SIDE] -= velocity;
                    break;
                case RIGHT:
                    x_pos[GamePanel.LEFT_SIDE] += velocity;
                    x_pos[GamePanel.RIGHT_SIDE] += velocity;
                    break;
            }
        }
    }


    private boolean checkColissions() {
        switch (direction) {
            case UP: {
                int y = y_pos[GamePanel.TOP_SIDE];
                if (y < velocity)
                    return true;
                for (int i = 0; i < velocity; ++i) {
                    if (gamePanel.getBoard(x_pos[GamePanel.LEFT_SIDE], y - i - 1) != GamePanel.GameBoard.EMPTY
                            || gamePanel.getBoard(x_pos[GamePanel.RIGHT_SIDE], y - i - 1) != GamePanel.GameBoard.EMPTY)
                        return true;
                }
                break;
            }
            case DOWN: {
                int y = y_pos[GamePanel.BOT_SIDE];
                if (y > GamePanel.HEIGHT - velocity - 1)
                    return true;
                for (int i = 0; i < velocity; ++i) {
                    if (gamePanel.getBoard(x_pos[GamePanel.LEFT_SIDE], y + i + 1) != GamePanel.GameBoard.EMPTY
                            || gamePanel.getBoard(x_pos[GamePanel.RIGHT_SIDE], y + i + 1) != GamePanel.GameBoard.EMPTY)
                        return true;
                }
                break;
            }
            case LEFT: {
                int x = x_pos[GamePanel.LEFT_SIDE];
                if (x < velocity)
                    return true;
                for (int i = 0; i < velocity; ++i) {
                    if (gamePanel.getBoard(x - i - 1, y_pos[GamePanel.TOP_SIDE]) != GamePanel.GameBoard.EMPTY
                            || gamePanel.getBoard(x - i - 1, y_pos[GamePanel.BOT_SIDE]) != GamePanel.GameBoard.EMPTY)
                        return true;
                }
                break;
            }
            case RIGHT: {
                int x = x_pos[GamePanel.RIGHT_SIDE];
                if (x > GamePanel.WIDTH - velocity - 1)
                    return true;
                for (int i = 0; i < velocity; ++i) {
                    if (gamePanel.getBoard(x + i + 1, y_pos[GamePanel.TOP_SIDE]) != GamePanel.GameBoard.EMPTY
                            || gamePanel.getBoard(x + i + 1, y_pos[GamePanel.BOT_SIDE]) != GamePanel.GameBoard.EMPTY)
                        return true;
                    }
                break;
            }
        }
        return false;
    }

    public void run() {
        while (in_game) {
            tankNotOnBoard(tank2_info[0], tank2_info[1]);
            tank2_info = connection.getEnemyInfo();
            if (tank2_info[3] == 1) {
                is_fired_enemy = true;
                enemy_bullet_info = new int[]{tank2_info[4], tank2_info[5], tank2_info[6]};

            } else
                is_fired_enemy = false;
            if (tank2_info[7] == 1) {
                explosion.setEnemyExplosion(true);
                explosion.setEnemyX_pos(tank2_info[8]);
                explosion.setEnemyY_pos(tank2_info[9]);

                if (tank2_info[10] == 0) {
                    for (int k = 0; k < GamePanel.BRICK_SIZE; ++k)
                        for (int l = 0; l < GamePanel.BRICK_SIZE; ++l)
                            gamePanel.setBoard(tank2_info[8] + k, tank2_info[9] + l, GamePanel.GameBoard.EMPTY);

                    gamePanel.setBricks(tank2_info[8] / GamePanel.BRICK_SIZE, tank2_info[9] / GamePanel.BRICK_SIZE, 0);
                }
                is_fired_enemy = false;

                tank2_info[7] = 0;
            }


            tankOnBoard(tank2_info[0], tank2_info[1]);


            checkStatus();
            if (!explosions_thread.isAlive()) {
                explosions_thread = new Thread(explosion);
                explosions_thread.start();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (is_fired) {
                bullet_info = bullet.getBulletInfo();
                connection.sendInfo(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE], direction, 1, bullet_info, hit);
            } else
                connection.sendInfo(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE], direction, 0, bullet_info, hit);

            hit[0] = 0;
            synchronized (connection) {
                try {
                    connection.wait();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    private void setTankPosition(int type) {
        if (type == Gameplay.SERVER) {
            x_pos[GamePanel.LEFT_SIDE] = 330; // jakas domyslna pozycja
            x_pos[GamePanel.RIGHT_SIDE] = 330 + TANK_SIZE - 1;
            y_pos[GamePanel.TOP_SIDE] = 30;
            y_pos[GamePanel.BOT_SIDE] = 30 + TANK_SIZE - 1;
            direction = DOWN;
        }
        else {
            x_pos[GamePanel.LEFT_SIDE] = 330; // jakas domyslna pozycja
            x_pos[GamePanel.RIGHT_SIDE] = 330 + TANK_SIZE - 1;
            y_pos[GamePanel.TOP_SIDE] = 430;
            y_pos[GamePanel.BOT_SIDE] = 430 + TANK_SIZE - 1;
            direction = UP;
        }
    }



    private void tankOnBoard(int x, int y) {
        for(int i = 0; i < TANK_SIZE; ++i)
            for(int j = 0; j < TANK_SIZE; ++j)
                gamePanel.setBoard(x + i, y + j, GamePanel.GameBoard.TANK);
    }
    private void tankNotOnBoard(int x, int y) {
        for(int i = 0; i < TANK_SIZE; ++i)
            for (int j = 0; j < TANK_SIZE; ++j)
                gamePanel.setBoard(x + i, y + j, GamePanel.GameBoard.EMPTY);
    }

    public void setHit(int hit1, int x, int y, int obj) {
        hit[0] = hit1;
        hit[1] = x;
        hit[2] = y;
        hit[3] = obj;
    }
    public int getX1()
    {
        return x_pos[GamePanel.LEFT_SIDE];
    }
    public int getY1()
    {
        return y_pos[GamePanel.TOP_SIDE];
    }
    public int getDir1()
    {
        return direction;
    }
    public int getX2()
    {
        return tank2_info[0];
    }
    public int getY2()
    {
        return tank2_info[1];
    }
    public int getDir2()
    {
        return tank2_info[2];
    }
    public boolean getIsFired() {
        return is_fired;
    }
    public boolean getIsEnemyFired() {
        return is_fired_enemy;
    }
    public void resetIsFired() {
        is_fired = false;
    }
    public int[] getBulletInfo() {
        return bullet_info;
    }
    public int[] getEnemyBullInfo() {
        return enemy_bullet_info;
    }
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    public InfoPanel getInfoPanel() {
        return infoPanel;
    }
    public void setInGame(boolean b) {
        in_game = b;
    }
    public ExplosionManagement getExplosion() {
        return explosion;
    }
}
