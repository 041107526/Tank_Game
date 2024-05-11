package com.tankGame.map;

import com.tankGame.game.Bullet;
import com.tankGame.util.BulletsPool;
import com.tankGame.util.Constant;
import com.tankGame.util.MyUtil;

import java.awt.*;
import java.util.List;

/**
 * class map element block,
 */
public class MapTile {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_COVER = 2;
    public static final int TYPE_HARD = 3;


    public static int tileW = 40;
    public static int radius = tileW >> 1;
    private int type = TYPE_NORMAL;

    private static Image[] tileImg;
    static{
        tileImg = new Image[4];
        tileImg[TYPE_NORMAL] = MyUtil.createImage("res/tile.png");
        tileImg[TYPE_HOUSE] = MyUtil.createImage("res/house.png");
        tileImg[TYPE_COVER] = MyUtil.createImage("res/cover.png");
        tileImg[TYPE_HARD] = MyUtil.createImage("res/hard.png");

        if(tileW <=0){
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    //Upper left corner of the image resource
    private int x,y;
    private boolean visible = true;

    public MapTile() {
    }

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
        if(tileW <=0){
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    public void draw(Graphics g){
        if(!visible)
            return;

        if(tileW <=0){
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }

        g.drawImage(tileImg[type],x,y,null);

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * Whether the map block and several bullets collide
     * @param bullets tanks' bullets
     * @return true or false
     */
    public boolean isCollideBullet(List<Bullet> bullets){
        if(!visible || type == TYPE_COVER)
            return false;
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean collide = MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if(collide){
                //destroy bullets
                bullet.setVisible(false);
                BulletsPool.theReturn(bullet);
                return true;
            }
        }
        return false;
    }

    //Determine if the current map block is the player tank base camp
    public boolean isHouse(){
        return type == TYPE_HOUSE;
    }

    @Override
    public String toString() {
        return "MapTile{" +
                "x=" + x +
                ", y=" + y +
                ", visible=" + visible +
                '}';
    }
}