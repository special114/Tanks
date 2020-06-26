public class ExplosionManagement implements Runnable{

    private boolean Explosion;
    private int x_pos;
    private int y_pos;
    private int i = 0;

    private boolean enemyExplosion;
    private int enemy_x_pos;
    private int enemy_y_pos;
    private int j = 0;
    public ExplosionManagement() {
        Explosion = false;
        enemyExplosion = false;
    }




    @Override
    public void run() {
        while(Explosion) {
            ++i;
            if (i > 100) {
                Explosion = false;
                i = 0;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        i = 0;
        while(enemyExplosion) {
            ++j;
            if (j > 100) {
                enemyExplosion = false;
                j = 0;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setExplosion(boolean explosion) {
        Explosion = explosion;
    }
    public boolean isExplosion() {
        return Explosion;
    }
    public int getX_pos() {
        return x_pos;
    }
    public void setX_pos(int x_pos) {
        this.x_pos = x_pos - 25;
    }
    public int getY_pos() {
        return y_pos;
    }
    public void setY_pos(int y_pos) {
        this.y_pos = y_pos - 25;
    }

    public void setEnemyExplosion(boolean explosion) {
        enemyExplosion = explosion;
    }
    public boolean isEnemyExplosion() {
        return enemyExplosion;
    }
    public int getEnemyX_pos() {
        return enemy_x_pos;
    }
    public void setEnemyX_pos(int x_pos) {
        this.enemy_x_pos = x_pos - 25;
    }
    public int getEnemyY_pos() {
        return enemy_y_pos;
    }
    public void setEnemyY_pos(int y_pos) {
        this.enemy_y_pos = y_pos - 25;
    }
}
