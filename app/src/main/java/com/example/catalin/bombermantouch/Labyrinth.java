package com.example.catalin.bombermantouch;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Labyrinth {
    private static final String vertexShaderCode  =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    private static final String fragmentShaderCode  =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";

    private static final int[] InitialMapSize = new int[] {
            16, 19, 6, 7, 9, 11
    };

    /*
    private static final int[] InitialMap = new int[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1,
            1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1,
            1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
    };
    */



    private static final int[] InitialMap = new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1,
            1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1,
            0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0,
            1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1,
            1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1,
            1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1,
            1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1
    };

    private Context ctx;
    private MediaPlayer mediaPlayerEat;
    private MediaPlayer mediaPlayerTrap;
    private MediaPlayer mediaPlayerNoPath;
    private MediaPlayer mediaPlayerEnemyEat;
    private MediaPlayer mediaPlayerLoseLife;
    private MediaPlayer mediaPlayerDangerBackground;

    private int labyrinthSizeX;
    private int labyrinthSizeY;
    private float playerSizeX;
    private float playerSizeY;
    private int numberOEnemies;
    private Point screenSize;


    private final int program;
    private int wallTexture;
    private int concreteTexture;
    private int redDotTexture;
    private int yellowDotTexture;
    private int powerUpTexture;
    private int greenDotTexture;

    private int pacmanUpTexture;
    private int pacmanDownTexture;
    private int pacmanLeftTexture;
    private int pacmanRightTexture;

    private int pacmanWhiteUpTexture;
    private int pacmanWhiteDownTexture;
    private int pacmanWhiteLeftTexture;
    private int pacmanWhiteRightTexture;

    private int trapTexture;

    private int enemy1Texture;
    private int enemy1TextureEat;
    private int enemy2Texture;
    private int enemy2TextureEat;
    private int enemy3Texture;
    private int enemy3TextureEat;
    private int enemy4Texture;
    private int enemy4TextureEat;
    private int enemy5Texture;
    private int enemy5TextureEat;
    private int enemyAngelTexture;
    private int level;

    private int powerUp;

    private ArrayList<Brick> bricks;
    private ArrayList<Enemy> enemies;
    private ArrayList<Integer> playerSteps;
    private Player player;

    private void CreateSounds() {
        mediaPlayerEat = MediaPlayer.create(ctx, R.raw.sound_eat);
        mediaPlayerTrap = MediaPlayer.create(ctx, R.raw.trap);
        mediaPlayerNoPath = MediaPlayer.create(ctx, R.raw.sound_no_path);
        mediaPlayerEnemyEat = MediaPlayer.create(ctx, R.raw.sound_enemy_eat);
        mediaPlayerDangerBackground = MediaPlayer.create(ctx, R.raw.danger_background_music);
        mediaPlayerLoseLife = MediaPlayer.create(ctx, R.raw.lost_life);
    }

    Labyrinth(Context ctx, int numberOfEnemies, Point screenSize, int level) {
        // initialize vertex byte buffer for shape coordinates

        this.ctx = ctx;
        this.screenSize = screenSize;
        this.numberOEnemies = numberOfEnemies;
        this.labyrinthSizeY = InitialMapSize[0];
        this.labyrinthSizeX = InitialMapSize[1];
        this.level = level;

        //if (labyrinthSizeY % 2 == 0) {
        //    labyrinthSizeX += 3;
        //} else {
        //    labyrinthSizeX += 4;
        //}

        //this.numberOfWalls = ((labyrinthSizeY - 2) * (labyrinthSizeX - 2)) / 4;

        program = GraphicTools.CreateShaderProgram(vertexShaderCode, fragmentShaderCode);

        playerSizeY = 2f / labyrinthSizeY;
        playerSizeX = 2f / labyrinthSizeY * ((float) screenSize.y / screenSize.x);

        playerSteps = new ArrayList<>();
        powerUp = 0;
        LoadTextures();
        CreateLabyrinth();
        CreatePlayer();
        CreateEnemies();
        CreateSounds();
    }

    private float manhattan(int x, int y) {
        int coordXX = x % labyrinthSizeX;
        int coordXY = x / labyrinthSizeY;

        int coordYX = y % labyrinthSizeX;
        int coordYY = y / labyrinthSizeY;

        return Math.abs(coordXX - coordYX) + Math.abs(coordXY - coordYY);

    }

    private ArrayList<Integer> getNeighbors(int current, boolean allNeighbors) {
        int currentTop = current + labyrinthSizeX;
        int currentBottom = current - labyrinthSizeX;
        int currentLeft = current - 1;
        int currentRight = current + 1;



        // Log.d("[getNeighbors]", "neighbors: " +  new Integer[] {currentTop, currentBottom, currentLeft, currentRight});
        ArrayList<Integer> neighbors = new ArrayList<>();
        if (currentTop < labyrinthSizeX * labyrinthSizeY) {
            if (bricks.get(currentTop).isFree() || allNeighbors) {
                neighbors.add(currentTop);
            }
        }

        if (currentBottom > 0 ) {
            if (bricks.get(currentBottom).isFree() || allNeighbors)  {
                neighbors.add(currentBottom);
            }
        }

        if (currentLeft / labyrinthSizeX  == current / labyrinthSizeX && currentLeft > -1 ) {
            if (bricks.get(currentLeft).isFree() || allNeighbors) {
                neighbors.add(currentLeft);
            }
        }

        if (currentRight / labyrinthSizeX  == current / labyrinthSizeX &&
                currentRight < labyrinthSizeY * labyrinthSizeX) {
            if (bricks.get(currentRight).isFree() || allNeighbors)  {
                neighbors.add(currentRight);
            }
        }
        return neighbors;
    }

    private ArrayList<Integer> reconstructPath(HashMap<Integer, Integer> cameFrom, int current) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.keySet().contains(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }
        path.remove(0);
        return path;
    }

    private ArrayList<Integer> AStar(int startBrick, int destinationBrick) {

        ArrayList<Integer> openSet = new ArrayList<>();
        ArrayList<Integer> closedSet = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();

        HashMap<Integer, Integer> cameFrom = new HashMap<>();
        HashMap<Integer, Float> gScore = new HashMap<>();
        HashMap<Integer, Float> fScore = new HashMap<>();
        for (int i = 0; i < labyrinthSizeY * labyrinthSizeX; i++) {
            gScore.put(i, 99999f);
            fScore.put(i, 99999f);
        }

        gScore.put(startBrick, 0f);
        fScore.put(startBrick, manhattan(startBrick, destinationBrick));
        openSet.add(startBrick);
        while (!openSet.isEmpty()) {
            int current = 0;
            int currentPosition = 0;
            float currentFScore = 999999f;
            for (int i = 0; i < openSet.size(); i++) {
                if (currentFScore > fScore.get(openSet.get(i))) {
                    current = openSet.get(i);
                    currentPosition = i;
                    currentFScore = fScore.get(openSet.get(i));
                }
            }
            if (current == destinationBrick) {
                return reconstructPath(cameFrom, current);
            }
            openSet.remove(currentPosition);
            closedSet.add(current);
            ArrayList<Integer> neighbors = getNeighbors(current, false);
            for (int i = 0; i < neighbors.size(); i ++) {
                int neighbor = neighbors.get(i);
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                float tentative_gScore = gScore.get(current) + 1;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if ( tentative_gScore >= gScore.get(neighbor))  {
                    continue;
                }

                cameFrom.put(neighbor,current);
                gScore.put(neighbor, tentative_gScore);
                fScore.put(neighbor, gScore.get(neighbor) + manhattan(neighbor, destinationBrick));
            }
        }
        return path;
    }

    private ArrayList<Integer> getPlayerSteps(float x, float y) {

        int destinationBrick = (int) (y * labyrinthSizeX + x);
        int startBrick = player.getBrickNumber();

        ArrayList<Integer> path = AStar(startBrick, destinationBrick);

        if (path.isEmpty()) {
            mediaPlayerNoPath.start();
        }
        return path;
    }

    void setPlayerLocation(float x, float y) {

        float resolutionX = ((playerSizeX * labyrinthSizeX) * screenSize.x) / 2;

        float stepY = (float) screenSize.y / labyrinthSizeY;
        float stepX = resolutionX / labyrinthSizeX;

        float calcY = labyrinthSizeY - (int) (y / stepY) - 1;
        float calcX = (int) (x / stepX);
        playerSteps = getPlayerSteps(calcX, calcY);
    }

    private void CreateLabyrinth() {

        float brickSizeY = 2f / labyrinthSizeY;
        float brickSizeX = 2f / labyrinthSizeY * ((float) screenSize.y / screenSize.x);

        // x = 1280
        // y = 720
        // Log.d("Resolution", screenSize.x + " dsfdsa " + screenSize.y);
        // Log.d("BrickSize", brickSizeX + "" + brickSizeY);


        bricks = new ArrayList<>();

        int crtBrick = 0;
        // Y coordinate
        for (int i = 0; i < labyrinthSizeY; i++) {
            // X coordinate
            for (int j = 0; j < labyrinthSizeX; j++) {
                float xPos = (-1f + brickSizeX * j);
                float yPos = -1f + brickSizeY * i;

                float brickCoords[] = {xPos, yPos, 0.1f,
                        xPos, yPos + brickSizeY, 0.1f,
                        xPos + brickSizeX, yPos + brickSizeY, 0.1f,
                        xPos + brickSizeX, yPos, 0.1f
                };

                switch (GetBrickTypeFromMap(crtBrick)) {
                    case Wall:
                        bricks.add(new Brick(crtBrick, GetBrickTypeFromMap(crtBrick), brickCoords, program, wallTexture, false));
                        break;
                    case Concrete:
                        bricks.add(new Brick(crtBrick, GetBrickTypeFromMap(crtBrick), brickCoords, program, concreteTexture, false));
                        break;
                    case Free:
                        bricks.add(new Brick(crtBrick, Constants.BrickType.Free, brickCoords, program, getCurrentDotTexture(i % 3), true));
                        break;
                    case PowerUp:
                        bricks.add(new Brick(crtBrick, Constants.BrickType.PowerUp, brickCoords, program, powerUpTexture, true));
                        break;
                    default:
                        bricks.add(new Brick(crtBrick, GetBrickTypeFromMap(crtBrick), brickCoords, program, wallTexture, false));
                        break;
                }
                crtBrick++;
            }
        }

        generateTraps(5);
    }

    void generateTraps(int numberOfTraps) {
        for(int nt = 0; nt < numberOfTraps; nt++) {
            boolean noTrapBrick = false;
            while (!noTrapBrick) {
                int crtBrick = new Random().nextInt(labyrinthSizeX * labyrinthSizeY);
                if(bricks.get(crtBrick).getBrickType() == Constants.BrickType.Free) {
                    bricks.get(crtBrick).setTexture(trapTexture);
                    bricks.get(crtBrick).setBrickType(Constants.BrickType.Trap);
                    noTrapBrick = true;
                }
            }
        }
    }

    private int getCurrentDotTexture(int crtDotTextureIndex) {
        switch (crtDotTextureIndex) {
            case 0:
                return redDotTexture;
            case 1:
                return yellowDotTexture;
            case 2:
                return greenDotTexture;
            default:
                return yellowDotTexture;
        }
    }

    private int getCurrentEnemyTexture(int crtEnemyTextureIndex) {
        switch (crtEnemyTextureIndex) {
            case 0:
                return enemy1Texture;
            case 1:
                return enemy2Texture;
            case 2:
                return enemy3Texture;
            case 3:
                return enemy4Texture;
            case 4:
                return enemy5Texture;
            default:
                return enemy1Texture;
        }
    }

    private void CreatePlayer() {
        float playerSizeY = 2f / labyrinthSizeY;
        float playerSizeX = 2f / labyrinthSizeY * ((float) screenSize.y / screenSize.x);

        float xPos = -1f + playerSizeX ;
        float yPos = -1f + playerSizeY;

        float playerCoords[] = {xPos, yPos, 0.1f,
                xPos, yPos + playerSizeY, 0.1f,
                xPos + playerSizeX, yPos + playerSizeY, 0.1f,
                xPos + playerSizeX, yPos, 0.1f
        };
        player = new Player(playerCoords, program, pacmanLeftTexture, labyrinthSizeX + 1, Constants.StartLives);
        //player.setPowerUP(Constants.PowerUP.Invicibility);
        bricks.get(labyrinthSizeX + 1).setPlayerHere(true);
    }

    private void CreateEnemies() {
        enemies = new ArrayList<>();

        Random rnd = new Random();


        //private static final int[] InitialMapSize = new int[] {
        //        16, 19, 4, 5, 7, 9
        //};

        int enemyHouseStartY = InitialMapSize[2] + 1;
        int enemyHouseStartX = InitialMapSize[3] + 1;

        int enemyHouseEndY = InitialMapSize[4] - 1;
        int enemyHouseEndX = InitialMapSize[5] - 1;

        int enemyHouseHeight = Math.abs(enemyHouseEndY - enemyHouseStartY) + 1;
        int enemyHouseWidth = Math.abs(enemyHouseEndX - enemyHouseStartX) + 1;

        for (int i = 0; i < numberOEnemies; i++) {
            if (i > 0 && i % enemyHouseWidth == 0) {
                enemyHouseStartY++;
            }

            int enemyPosition = enemyHouseStartY * labyrinthSizeX + enemyHouseStartX + i % enemyHouseWidth;
            int enemyType = 0;
            switch (level) {
                case Constants.EasyLevel:
                    enemyType = 0;
                    break;
                case Constants.MediumLevel:
                    enemyType = rnd.nextInt(2);
                    break;
                case Constants.HardLevel:
                    enemyType = rnd.nextInt(3);
                    break;
            }

            enemies.add(new Enemy(bricks.get(enemyPosition).getBrickCoords(), program, getCurrentEnemyTexture(new Random().nextInt(5)), enemyPosition, enemyType, false));
            bricks.get(enemyPosition).setEnemyHere(true);
        }
    }

    private void LoadTextures() {
        wallTexture = GraphicTools.SetupImage(R.drawable.wall, ctx);
        concreteTexture = GraphicTools.SetupImage(R.drawable.concrete, ctx);

        pacmanDownTexture = GraphicTools.SetupImage(R.drawable.pacman_down, ctx);
        pacmanUpTexture = GraphicTools.SetupImage(R.drawable.pacman_up, ctx);
        pacmanRightTexture = GraphicTools.SetupImage(R.drawable.pacman_right, ctx);
        pacmanLeftTexture = GraphicTools.SetupImage(R.drawable.pacman_left, ctx);

        pacmanWhiteDownTexture = GraphicTools.SetupImage(R.drawable.white_pacman_down, ctx);
        pacmanWhiteUpTexture = GraphicTools.SetupImage(R.drawable.white_pacman_up, ctx);
        pacmanWhiteRightTexture = GraphicTools.SetupImage(R.drawable.white_pacman_right, ctx);
        pacmanWhiteLeftTexture = GraphicTools.SetupImage(R.drawable.white_pacman_left, ctx);

        enemy1Texture = GraphicTools.SetupImage(R.drawable.enemy1, ctx);
        enemy1TextureEat = GraphicTools.SetupImage(R.drawable.enemy1_eat, ctx);
        enemy2Texture = GraphicTools.SetupImage(R.drawable.enemy2, ctx);
        enemy2TextureEat = GraphicTools.SetupImage(R.drawable.enemy2_eat, ctx);
        enemy3Texture = GraphicTools.SetupImage(R.drawable.enemy3, ctx);
        enemy3TextureEat = GraphicTools.SetupImage(R.drawable.enemy3_eat, ctx);
        enemy4Texture = GraphicTools.SetupImage(R.drawable.enemy4, ctx);
        enemy4TextureEat = GraphicTools.SetupImage(R.drawable.enemy4_eat, ctx);
        enemy5Texture = GraphicTools.SetupImage(R.drawable.enemy5, ctx);
        enemy5TextureEat = GraphicTools.SetupImage(R.drawable.enemy5_eat, ctx);
        enemyAngelTexture = GraphicTools.SetupImage(R.drawable.enemy_angel, ctx);

        redDotTexture = GraphicTools.SetupImage(R.drawable.dot_red, ctx);
        yellowDotTexture = GraphicTools.SetupImage(R.drawable.dot_yellow, ctx);
        greenDotTexture = GraphicTools.SetupImage(R.drawable.dot_green, ctx);
        powerUpTexture = GraphicTools.SetupImage(R.drawable.dot_power_up, ctx);

        trapTexture = GraphicTools.SetupImage(R.drawable.trap, ctx);
    }

    void Draw(int frames) throws IOException {
        for (Brick b : bricks) {
            b.Draw();
        }
        if (frames % 2 == 0 && !playerSteps.isEmpty()) {
            int nextPosition = playerSteps.get(0);
            // Log.d("[Draw]", "playerPosition: " + nextPosition);
            playerSteps.remove(0);

            if (player.hasPowerUp()) {
                if (player.getPowerUPTimer() > 0) {
                    player.decreasePowerUPTimer();
                } else {
                    player.setPowerUP(Constants.PowerUP.Normal);
                }
            }

            Brick nextBrick = bricks.get(nextPosition);
            player.setTexture(SwitchPlayerTexture(player.getBrickNumber(), nextBrick.getBrickNumber()));
            player.move(nextBrick.getBrickCoords(), nextPosition, bricks);
            if (nextBrick.canEatItem()) {

                nextBrick.setHasItem(false);
                Log.d("can eat item ", nextBrick.getBrickType().toString());
                Constants.BrickType brickType = nextBrick.getBrickType();
                if (brickType == Constants.BrickType.PowerUp) {
                    Log.d("Collect PowerUP", "true");
                    player.setPowerUP(Constants.PowerUP.Invicibility);
                    player.setPowerUpTimer(Constants.PowerUPTimer);
                }
                mediaPlayerEat.start();
            }
        } else if (player.getPowerUp() == Constants.PowerUP.Immune) {
            if (player.getTexture() == pacmanDownTexture) {
                player.setTexture(pacmanWhiteDownTexture);
            }
            if (player.getTexture() == pacmanUpTexture) {
                player.setTexture(pacmanWhiteUpTexture);
            }
            if (player.getTexture() == pacmanLeftTexture) {
                player.setTexture(pacmanWhiteLeftTexture);
            }
            if (player.getTexture() == pacmanRightTexture) {
                player.setTexture(pacmanWhiteRightTexture);
            }
        }


        player.Draw();

        SetPowerUpEffects();

        for (Enemy enemy : enemies) {
            if (enemy.getReturnHome()) {
                if (!enemy.getStepsHome().isEmpty()) {
                    if (frames % 4 == 0) {
                        enemy.move(enemy.getStepsHome().get(0), bricks);
                        enemy.getStepsHome().remove(0);
                    }
                } else {
                    enemy.setReturnHome(false);
                    enemy.setTexture(getCurrentEnemyTexture(new Random().nextInt(5)));
                    enemy.stepsRandom.clear();
                }
            } else {
                moveEnemy(enemy, frames);
            }

            enemy.Draw();
            ChooseEnemyPlayerAction(enemy);
        }

        playBackgroundSoundEffects();
    }

    void playBackgroundSoundEffects() throws IOException {
        boolean enemyClose = false;
        if (mediaPlayerDangerBackground != null) {
            // Log.d("[playBackgroundSound]", "here");
            for (Enemy enemy : enemies) {
                if (AStar(player.getBrickNumber(), enemy.getBrickNumber()).size() < 10) {
                    enemyClose = true;
                    break;
                }
            }

            if (enemyClose) {
                if (!mediaPlayerDangerBackground.isPlaying()) {
                    mediaPlayerDangerBackground.start();
                }
            } else if (mediaPlayerDangerBackground.isPlaying()) {
                mediaPlayerDangerBackground.pause();
                mediaPlayerDangerBackground.seekTo(0);
            }
        }
    }

    private void moveEnemy(Enemy enemy, int frames) {
        if (enemy.stepsRandom.isEmpty()) {
            Random rand = new Random();
            int destinationBrick = rand.nextInt(labyrinthSizeX * labyrinthSizeY);
            boolean isDestinationFree = bricks.get(destinationBrick).isFree();
            while (Math.abs(destinationBrick - enemy.getBrickNumber()) < 10 || !isDestinationFree) {
                destinationBrick = rand.nextInt(labyrinthSizeX * labyrinthSizeY);
                isDestinationFree = bricks.get(destinationBrick).isFree();
            }
            enemy.stepsRandom = AStar(enemy.getBrickNumber(), destinationBrick);
        }

        if (!enemy.stepsRandom.isEmpty()) {
            switch (enemy.getEnemyType()) {
                case Constants.EasyEnemy:
                    if (frames % 14 == 0) {
                        int nextPosition = enemy.stepsRandom.get(0);
                        // Log.d("[moveEnemy]", "Enemy:" + enemy.getBrickNumber() + " RandomSteps:" + enemy.stepsRandom.toString());
                        enemy.move(nextPosition, bricks);
                        enemy.stepsRandom.remove(0);

                        if (bricks.get(nextPosition).getBrickType() == Constants.BrickType.Trap) {
                            enemy.setCanBeEaten(false);
                            enemy.setTexture(enemyAngelTexture);
                            enemy.setStepsHome(GetEnemyStepsHome(enemy));
                            enemy.setReturnHome(true);
                            AwardPlayerPoints();
                            mediaPlayerTrap.start();
                        }
                    }
                    break;
                case Constants.MediumEnemy:
                    if (frames % 10 == 0) {
                        //e.move(nextPosition, bricks);
                    }
                    break;
                case Constants.HardEnemy:
                    if (frames % 20 == 0) {
                        //e.move(nextPosition, bricks);
                    }
                    break;
            }
        }
    }

    private void ChooseEnemyPlayerAction(Enemy enemy) {
        if (enemy.getBrickNumber() == player.getBrickNumber()) {
            if (enemy.getCanBeEaten()) {
                enemy.setCanBeEaten(false);
                enemy.setTexture(enemyAngelTexture);
                enemy.setStepsHome(GetEnemyStepsHome(enemy));
                enemy.setReturnHome(true);
                AwardPlayerPoints();
                mediaPlayerEnemyEat.start();
            }
            if (player.getPowerUp() != Constants.PowerUP.Normal) {

            } else {
                PlayerLoseLife();
            }
        }
    }

    ArrayList<Integer> GetEnemyStepsHome(Enemy enemy) {
        // Log.d("[GetEnemyStepsHome]", "enemyHouseCenter: " + GetEnemyHomeCenter());
        ArrayList<Integer> stepsHome = AStar(enemy.getBrickNumber(), GetEnemyHomeCenter());
        // Log.d("[GetEnemyStepsHome]", "steps home: " + stepsHome);
        return stepsHome;
    }

    private int GetEnemyHomeCenter() {
        int enemyHouseStartY = InitialMapSize[2];
        int enemyHouseStartX = InitialMapSize[3];

        int enemyHouseEndY = InitialMapSize[4];
        int enemyHouseEndX = InitialMapSize[5];

        int centerX = enemyHouseEndX - enemyHouseStartX;
        int centerY = enemyHouseEndY - enemyHouseStartY;

        return (labyrinthSizeY - 1)  / 2  * labyrinthSizeX + labyrinthSizeX / 2;
    }

    private void PlayerLoseLife() {
        mediaPlayerLoseLife.start();
        player.setPowerUP(Constants.PowerUP.Immune);
        player.setPowerUpTimer(Constants.LoseLifeTimer);
        player.decreaseLives();
    }

    private void AwardPlayerPoints() {}

    private Constants.BrickType GetBrickType(int crtBrick) {
        if (crtBrick < labyrinthSizeX ||
                crtBrick % labyrinthSizeX == 0 ||
                crtBrick / labyrinthSizeX >= labyrinthSizeY - 1 ||
                crtBrick % labyrinthSizeX == labyrinthSizeX - 1) {
            return Constants.BrickType.Concrete;
        }

        /* for (int i = 0; i < labyrinthSizeX; i ++) {
            if (crtBrick % 2 == 0 && (crtBrick / labyrinthSizeX) % 2 == 0) {
                return Constants.BrickType.Concrete;
            } else {
                return Constants.BrickType.Free;
            }
        }
        */
        return Constants.BrickType.Free;

        /*
        if (crtBrick % 2 == 0) {
            brickType = Constants.BrickType.Free;
        } else {
            brickType = Constants.BrickType.Concrete;
        }

        return brickType;
        */
    }

    private Constants.BrickType GetBrickTypeFromMap(int crtBrick) {
        if (InitialMap[crtBrick] == 0) {
            return Constants.BrickType.Free;
        }
        if (InitialMap[crtBrick] == 1) {
            return Constants.BrickType.Concrete;
        }
       if (InitialMap[crtBrick] == 2) {
           return Constants.BrickType.PowerUp;
       }
        return Constants.BrickType.Free;
    }

    private int SwitchPlayerTexture(int currentBrickNumber, int nextBrickNumber) {
        switch(GetNeighborType(currentBrickNumber, nextBrickNumber)) {
            case 0:
                return player.getPowerUp() == Constants.PowerUP.Immune ? pacmanWhiteUpTexture : pacmanUpTexture;
            case 1:
                return player.getPowerUp() == Constants.PowerUP.Immune ? pacmanWhiteDownTexture : pacmanDownTexture;
            case 2:
                return player.getPowerUp() == Constants.PowerUP.Immune ?  pacmanWhiteRightTexture : pacmanRightTexture;
            case 3:
                return player.getPowerUp() == Constants.PowerUP.Immune ?  pacmanWhiteLeftTexture : pacmanLeftTexture;
            default:
                return player.getPowerUp() == Constants.PowerUP.Immune ?  pacmanWhiteUpTexture : pacmanUpTexture;
        }
    }

    private int GetNeighborType(int currentPosition, int nextPosition) {
        if (nextPosition == currentPosition + 1) {
            return 3;
        }
        if (nextPosition == currentPosition - 1) {
            return 2;
        }
        if (nextPosition == currentPosition + labyrinthSizeX) {
            return 0;
        } else {
            return 1;
        }
    }

    private void SetPowerUpEffects() {
        if (player.getPowerUp() != Constants.PowerUP.Normal) {
            switch (player.getPowerUp()) {
                case Invicibility:
                    for (Enemy enemy : enemies) {
                        if (!enemy.getCanBeEaten() && !enemy.getReturnHome()) {
                            enemy.setTexture(enemy.getTexture() + 1);
                        }
                        enemy.setCanBeEaten(true);
                    }
                    break;
            }
        } else {
            for (Enemy enemy : enemies) {
                if (enemy.getCanBeEaten()) {
                    enemy.setTexture(enemy.getTexture() - 1);
                }
                enemy.setCanBeEaten(false);
            }
        }
    }


}
