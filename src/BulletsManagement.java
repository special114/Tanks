public class BulletsManagement implements Runnable{

    private int[] x_pos = new int[2];
    private int[] y_pos = new int[2];
    private TanksManagement tank;
    private int dir;
    private boolean is_fired;
    private int velocity;

    public BulletsManagement(TanksManagement t) {
        tank = t;
        is_fired = false;
        velocity = 1;
    }

    public void run() {
        while (is_fired ) {
                switch (dir) {
                    case TanksManagement.UP:
                        if (tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.BRICK) {
                            hitBrick(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.BRICK) {
                            hitBrick(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.TOP_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.TANK
                                || tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.TANK) {
                            hitTank();
                        }
                        else if (y_pos[GamePanel.TOP_SIDE] > 0) {
                            y_pos[GamePanel.TOP_SIDE] -= velocity;
                            y_pos[GamePanel.BOT_SIDE] -= velocity;
                        }
                        else {
                            is_fired = false;
                            tank.resetIsFired();
                        }
                        break;
                    case TanksManagement.DOWN:
                        if (tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.BRICK){
                            hitBrick(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.BOT_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.BRICK) {
                            hitBrick(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.BOT_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.TANK
                                || tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.TANK) {
                            hitTank();
                        }
                        else if (y_pos[GamePanel.BOT_SIDE] < GamePanel.HEIGHT - 1) {
                            y_pos[GamePanel.TOP_SIDE] += velocity;
                            y_pos[GamePanel.BOT_SIDE] += velocity;
                        }
                        else {
                            is_fired = false;
                            tank.resetIsFired();
                        }
                        break;
                    case TanksManagement.LEFT:
                        if (tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.BRICK){
                            hitBrick(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.BRICK){
                            hitBrick(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.BOT_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.TANK
                                || tank.getGamePanel().getBoard(x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.TANK) {
                            hitTank();
                        }
                        else if (x_pos[GamePanel.LEFT_SIDE] > 0) {
                            x_pos[GamePanel.RIGHT_SIDE] -= velocity;
                            x_pos[GamePanel.LEFT_SIDE] -= velocity;
                        }
                        else {
                            is_fired = false;
                            tank.resetIsFired();
                        }
                        break;
                    case TanksManagement.RIGHT:
                        if (tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.BRICK){
                            hitBrick(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.TOP_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.BRICK){
                            hitBrick(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.BOT_SIDE]);
                        }
                        else if (tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.TOP_SIDE]) == GamePanel.GameBoard.TANK
                                || tank.getGamePanel().getBoard(x_pos[GamePanel.RIGHT_SIDE], y_pos[GamePanel.BOT_SIDE]) == GamePanel.GameBoard.TANK) {
                            hitTank();
                        }
                        else if (x_pos[GamePanel.RIGHT_SIDE] < GamePanel.WIDTH - 1) {
                            x_pos[GamePanel.LEFT_SIDE] += velocity;
                            x_pos[GamePanel.RIGHT_SIDE] += velocity;
                        }
                        else {
                            is_fired = false;
                            tank.resetIsFired();
                        }
                        break;
            }
            try {
                Thread.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void hitBrick(int x, int y) {
        int i = x / GamePanel.BRICK_SIZE;
        int j = y / GamePanel.BRICK_SIZE;
        tank.setHit(1, i * GamePanel.BRICK_SIZE, j * GamePanel.BRICK_SIZE, 0);
        tank.getExplosion().setExplosion(true);
        tank.getExplosion().setX_pos(i * GamePanel.BRICK_SIZE);
        tank.getExplosion().setY_pos(j * GamePanel.BRICK_SIZE);
        for (int k = 0; k < GamePanel.BRICK_SIZE; ++k)
            for (int l = 0; l < GamePanel.BRICK_SIZE; ++l)
                tank.getGamePanel().setBoard(i * GamePanel.BRICK_SIZE + k, j * GamePanel.BRICK_SIZE + l, GamePanel.GameBoard.EMPTY);

        tank.getGamePanel().setBricks(i , j, 0);

        tank.resetIsFired();
        is_fired = false;
    }

    private void hitTank() {
        tank.getInfoPanel().addPoint();
        tank.setHit(1, tank.getX2(), tank.getY2(), 1);
        tank.getExplosion().setExplosion(true);
        tank.getExplosion().setX_pos(tank.getX2());
        tank.getExplosion().setY_pos(tank.getY2());
        tank.resetIsFired();
        is_fired = false;
    }

    public void setIsFired() {
        is_fired = true;
    }
    public int[] getBulletInfo() {
        return new int[]{x_pos[GamePanel.LEFT_SIDE], y_pos[GamePanel.TOP_SIDE], dir};
    }
    public void setBulletInfo(int x, int y, int tank_dir) {
        dir = tank_dir;
        switch (dir) {
            case TanksManagement.UP:
                    x_pos[GamePanel.LEFT_SIDE] = x;
                    x_pos[GamePanel.RIGHT_SIDE] = x + 9;
                    y_pos[GamePanel.BOT_SIDE] = y;
                    y_pos[GamePanel.TOP_SIDE] = y - 14;
                break;
            case TanksManagement.DOWN:
                    x_pos[GamePanel.LEFT_SIDE] = x;
                    x_pos[GamePanel.RIGHT_SIDE] = x + 9;
                    y_pos[GamePanel.TOP_SIDE] = y;
                    y_pos[GamePanel.BOT_SIDE] = y + 14;
                break;
            case TanksManagement.LEFT:
                    x_pos[GamePanel.RIGHT_SIDE] = x;
                    x_pos[GamePanel.LEFT_SIDE] = x - 14;
                    y_pos[GamePanel.TOP_SIDE] = y;
                    y_pos[GamePanel.BOT_SIDE] = y + 9;
                break;
            case TanksManagement.RIGHT:
                    x_pos[GamePanel.LEFT_SIDE] = x;
                    x_pos[GamePanel.RIGHT_SIDE] = x + 14;
                    y_pos[GamePanel.TOP_SIDE] = y;
                    y_pos[GamePanel.BOT_SIDE] = y + 9;
                break;
        }
    }

}
