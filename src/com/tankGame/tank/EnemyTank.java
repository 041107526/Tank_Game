package com.tankGame.tank;

import com.tankGame.game.GameFrame;
import com.tankGame.game.LevelInof;
import com.tankGame.util.Constant;
import com.tankGame.util.EnemyTanksPool;
import com.tankGame.util.MyUtil;

import java.awt.*;

/**
 * class enemy tank
 */
public class EnemyTank extends Tank {
    public static final int TYPE_GREEN = 0;
    public static final int TYPE_YELLOW = 1;
    private int type = TYPE_GREEN;

    private static Image[] greenImg;
    private static Image[] yellowImg;
    //记录5秒开始的时间
    private long aiTime;

    static {
        greenImg = new Image[4];
        greenImg[0] = MyUtil.createImage("res/ul.png");
        greenImg[1] = MyUtil.createImage("res/dl.png");
        greenImg[2] = MyUtil.createImage("res/ll.png");
        greenImg[3] = MyUtil.createImage("res/rl.png");
        yellowImg = new Image[4];
        yellowImg[0] = MyUtil.createImage("res/u.png");
        yellowImg[1] = MyUtil.createImage("res/d.png");
        yellowImg[2] = MyUtil.createImage("res/l.png");
        yellowImg[3] = MyUtil.createImage("res/r.png");
    }

    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        //Enemies are timed once created
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0,2);
    }

    public EnemyTank(){
        type = MyUtil.getRandomNumber(0,2);
        aiTime = System.currentTimeMillis();
    }

    //Used to create an enemy tank
    public static Tank createEnemy(){
        int x = MyUtil.getRandomNumber(0,2) == 0 ? RADIUS :
                Constant.FRAME_WIDTH-RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
        EnemyTank enemy = (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true);
        enemy.setState(STATE_MOVE);
        //Set the amount of enemy blood according to the difficulty of the game
        int maxHp = Tank.DEFAULT_HP*LevelInof.getInstance().getLevelType();
        enemy.setHp(maxHp);
        enemy.setMaxHP(maxHp);
        //Set the type of the currently born enemy
        // by using the enemy type in the level information
        int enemyType = LevelInof.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);

        return enemy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void drawImgTank(Graphics g){
        ai();
        g.drawImage(type == TYPE_GREEN ? greenImg[getDir()] :
                yellowImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
    }

    /**
     * simple AI of enemy tanks
     */
    private void ai(){
        if(System.currentTimeMillis() - aiTime > Constant.ENEMY_AI_INTERVAL){
            //Randomised status at 5 second intervals
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2) == 0 ? STATE_STAND : STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        //There's a relatively small chance of firing.
        if(Math.random() < Constant.ENEMY_FIRE_PERCENT){
            fire();
        }
    }

}

