import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener {
    public static final int WIDTH = 700;        //najlepiej wielokrotnosci brick size
    public static final int HEIGHT = 500;
    public static final int BRICK_SIZE = 50;


    public static final int LEFT_SIDE = 0;
    public static final int RIGHT_SIDE = 1;
    public static final int TOP_SIDE = 0;
    public static final int BOT_SIDE = 1;

    enum GameBoard {BRICK, TANK, EMPTY}

    public GameBoard getBoard(int i, int j) {   // jak to sie robiiii
        return board[i][j];
    }
    public void setBoard(int i, int j, GameBoard val) {  // exception?
        board[i][j] = val;
    }
    private GameBoard[][] board = new GameBoard[WIDTH][HEIGHT];

    ConnectionManagement connection;
    TanksManagement tank;

    private int[] bullet_info;
    private int[] enemy_bullet_info;
    private boolean paint_bullet;
    private boolean paint_enemy_bullet;
    private boolean paint_explosion;
    private int[] explosion_info = {0, 0};
    private boolean paint_enemy_explosion;
    private int[] enemy_explosion_info = {0, 0};

    Image tank_img;

    Image tank_img_U;
    Image tank_img_D;
    Image tank_img_L;
    Image tank_img_R;
    Image enemy_tank_img_U;
    Image enemy_tank_img_D;
    Image enemy_tank_img_L;
    Image enemy_tank_img_R;
    Image bullet_img;
    Image bullet_img_U;
    Image bullet_img_D;
    Image bullet_img_L;
    Image bullet_img_R;
    Image brick;
    Image explosion;

    private int[][] bricks =   {{0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 1, 0, 0, 1, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                                {0, 0, 1, 0, 0, 1, 0, 0, 0, 0},
                                {0, 0, 1, 0, 0, 1, 1, 0, 0, 0},
                                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
                                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0}} ;


    public GamePanel(int type) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        initValues(type);
    }

    private void initValues(int type) {
        try {
            if (type == Gameplay.CLIENT) {
                tank_img_U = ImageIO.read(getClass().getResource("/tank7U.png"));
                tank_img_D = ImageIO.read(getClass().getResource("/tank7D.png"));
                tank_img_L = ImageIO.read(getClass().getResource("/tank7L.png"));
                tank_img_R = ImageIO.read(getClass().getResource("/tank7R.png"));
                enemy_tank_img_U = ImageIO.read(getClass().getResource("/tank8U.png"));
                enemy_tank_img_D = ImageIO.read(getClass().getResource("/tank8D.png"));
                enemy_tank_img_L = ImageIO.read(getClass().getResource("/tank8L.png"));
                enemy_tank_img_R = ImageIO.read(getClass().getResource("/tank8R.png"));
            }
            else {
                tank_img_U = ImageIO.read(getClass().getResource("/tank8U.png"));
                tank_img_D = ImageIO.read(getClass().getResource("/tank8D.png"));
                tank_img_L = ImageIO.read(getClass().getResource("/tank8L.png"));
                tank_img_R = ImageIO.read(getClass().getResource("/tank8R.png"));
                enemy_tank_img_U = ImageIO.read(getClass().getResource("/tank7U.png"));
                enemy_tank_img_D = ImageIO.read(getClass().getResource("/tank7D.png"));
                enemy_tank_img_L = ImageIO.read(getClass().getResource("/tank7L.png"));
                enemy_tank_img_R = ImageIO.read(getClass().getResource("/tank7R.png"));
            }
            bullet_img_U = ImageIO.read(getClass().getResource("/bulletU.png"));
            bullet_img_D = ImageIO.read(getClass().getResource("/bulletD.png"));
            bullet_img_L = ImageIO.read(getClass().getResource("/bulletL.png"));
            bullet_img_R = ImageIO.read(getClass().getResource("/bulletR.png"));
            brick = ImageIO.read(getClass().getResource("/solid_brick.jpg"));
            explosion = ImageIO.read(getClass().getResource("/explosion.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                board[i][j] = GameBoard.EMPTY;


        for (int i = 0; i < WIDTH / BRICK_SIZE; ++i)
            for (int j = 0; j < HEIGHT / BRICK_SIZE; ++j)
                if(bricks[i][j] == 1){
                    for (int k = 0; k < BRICK_SIZE; ++k)
                        for (int l = 0; l < BRICK_SIZE; ++l)
                            board[i * BRICK_SIZE + k][j * BRICK_SIZE + l] = GameBoard.BRICK;
                }
        paint_explosion = false;
        paint_enemy_bullet = false;
    }

    public int getBricks(int i, int j) {
        return bricks[i][j];
    }
    public void setBricks(int i, int j, int val) {
        bricks[i][j] = val;
    }


    @Override
    public void actionPerformed(ActionEvent action) {
        Toolkit.getDefaultToolkit().sync();
        if (tank.getIsFired()) {
            paint_bullet = true;
            bullet_info = tank.getBulletInfo();
        } else
            paint_bullet = false;

        if (tank.getIsEnemyFired()) {
            paint_enemy_bullet = true;
            enemy_bullet_info = tank.getEnemyBullInfo();
        } else
            paint_enemy_bullet = false;

        if (tank.getExplosion().isExplosion()) {
            paint_explosion = true;
            explosion_info[0] = tank.getExplosion().getX_pos();
            explosion_info[1] = tank.getExplosion().getY_pos();
        } else
            paint_explosion = false;

        if (tank.getExplosion().isEnemyExplosion()) {
            paint_enemy_explosion = true;
            enemy_explosion_info[0] = tank.getExplosion().getEnemyX_pos();
            enemy_explosion_info[1] = tank.getExplosion().getEnemyY_pos();
        } else
            paint_enemy_explosion = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (tank.getDir1()) {
            case TanksManagement.UP:
                tank_img = tank_img_U;
                break;
            case TanksManagement.DOWN:
                tank_img = tank_img_D;
                break;
            case TanksManagement.LEFT:
                tank_img = tank_img_L;
                break;
            case TanksManagement.RIGHT:
                tank_img = tank_img_R;
                break;
        }
        g.drawImage(tank_img, tank.getX1(), tank.getY1(), this);

        switch (tank.getDir2()) {
            case TanksManagement.UP:
                tank_img = enemy_tank_img_U;
                break;
            case TanksManagement.DOWN:
                tank_img = enemy_tank_img_D;
                break;
            case TanksManagement.LEFT:
                tank_img = enemy_tank_img_L;
                break;
            case TanksManagement.RIGHT:
                tank_img = enemy_tank_img_R;
                break;
        }
        g.drawImage(tank_img, tank.getX2(), tank.getY2(), this);

        if (paint_bullet) {
            switch (bullet_info[2]) {
                case TanksManagement.UP:
                    bullet_img = bullet_img_U;
                    break;
                case TanksManagement.DOWN:
                    bullet_img = bullet_img_D;
                    break;
                case TanksManagement.LEFT:
                    bullet_img = bullet_img_L;
                    break;
                case TanksManagement.RIGHT:
                    bullet_img = bullet_img_R;
                    break;
            }
            g.drawImage(bullet_img, bullet_info[0], bullet_info[1], this);
        }
        if (paint_enemy_bullet) {
            switch (enemy_bullet_info[2]) {
                case TanksManagement.UP:
                    bullet_img = bullet_img_U;
                    break;
                case TanksManagement.DOWN:
                    bullet_img = bullet_img_D;
                    break;
                case TanksManagement.LEFT:
                    bullet_img = bullet_img_L;
                    break;
                case TanksManagement.RIGHT:
                    bullet_img = bullet_img_R;
                    break;
            }
            g.drawImage(bullet_img, enemy_bullet_info[0], enemy_bullet_info[1], this);
        }


        for (int i = 0; i < WIDTH / BRICK_SIZE; ++i)
            for (int j = 0; j < HEIGHT / BRICK_SIZE; ++j)
                if(bricks[i][j] == 1)
                    g.drawImage(brick, i * BRICK_SIZE, j * BRICK_SIZE, this);



        if(paint_explosion) {
            g.drawImage(explosion, explosion_info[0], explosion_info[1], this);
        }
        if(paint_enemy_explosion) {
            g.drawImage(explosion, enemy_explosion_info[0], enemy_explosion_info[1], this);
        }
    }
    public void setTank(TanksManagement t) {
        tank = t;
    }

}
