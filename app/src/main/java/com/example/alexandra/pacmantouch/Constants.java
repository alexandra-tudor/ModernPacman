package com.example.alexandra.pacmantouch;

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
    public static int PowerUPTimer = 200;
    public static int LoseLifeTimer = 50;
    public static int StartLives = 3;
    public static int NumberOfTraps = 4;

    public enum BrickType {
       Free, Wall, Concrete, PowerUp, Trap
    }
    public enum ActionType {
        Move, PlaceBomb
    }

    public enum PowerUP {
        Normal, Immune, Invincibility, Trap
    }
}
