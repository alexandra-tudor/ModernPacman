package com.example.catalin.bombermantouch;

/**
 * Created by Catalin on 11-Dec-16.
 */

public class Constants {

    public static final int BackgroundTexture = 0;
    public static final int FreeTexture = 1;
    public static final int WallTexture = 2;
    public static final int ConcreteTexture = 3;

    public static final int EasyEnemy = 0;
    public static final int MediumEnemy = 1;
    public static final int HardEnemy = 2;

    public static final int EasyLevel = 0;
    public static final int MediumLevel = 1;
    public static final int HardLevel = 2;


    public static int CoordsPerVertex = 3;


    public enum BrickType {
       Free, Wall, Concrete, PowerUp
    }
    public enum ActionType {
        Move, PlaceBomb
    }

    public enum PowerUP {
        Normal, Invicibility
    }
}
