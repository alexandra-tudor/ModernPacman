package com.example.alexandra.pacmantouch;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

class Player {
    private FloatBuffer vertexBuffer;
    private FloatBuffer uvBuffer;
    private ShortBuffer drawListBuffer;


    private int program;
    private int texture;
    private int brickNumber;
    private int powerUPTimer;
    private int lives;
    private boolean trapsAreSet;
    private Constants.PowerUP powerUp;

    private static float uvs[] = new float[]{
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    // number of coordinates per vertex in this array
    private float[] playerCoords;

    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    private final int vertexStride = Constants.CoordsPerVertex * 4; // 4 bytes per vertex

    int getTexture() {
        return this.texture;
    }

    void setTexture(int texture) {
        this.texture = texture;
    }

    Player(float[] position, int program, int texture, int brickNumber, int lives) {
        this.playerCoords = position;
        this.program = program;
        this.texture = texture;
        this.brickNumber = brickNumber;
        this.powerUp = Constants.PowerUP.Normal;
        this.powerUPTimer = 0;
        this.lives = lives;
        this.trapsAreSet = false;
        CreateBuffers();
    }

    void setTraps() {
        this.trapsAreSet = true;
    }

    void resetTraps() {
        this.trapsAreSet = false;
    }

    boolean getTraps() {
        return trapsAreSet;
    }

    int getPowerUPTimer() {
        return powerUPTimer;
    }

    void decreasePowerUPTimer() {
        this.powerUPTimer --;
    }
    boolean hasPowerUp() {
        return powerUp != Constants.PowerUP.Normal;
    }

    void decreaseLives() {

        this.lives --;
        this.lives = Math.max(0, this.lives);
    }

    int getLives() {
        return this.lives;
    }
    boolean areAllLivesLost() {
        return this.lives <= 0;
    }

    void setPowerUpTimer(int powerUPTimer) {
        this.powerUPTimer = powerUPTimer;
    }

    Constants.PowerUP getPowerUp() {
        return powerUp;
    }

    void setPowerUP(Constants.PowerUP powerUp) {
        this.powerUp = powerUp;
    }

    void move(float[] position, int brickNumber, ArrayList<Brick> bricks) {
        bricks.get(this.brickNumber).setPlayerHere(false);
        this.brickNumber = brickNumber;
        playerCoords = position;
        bricks.get(this.brickNumber).setPlayerHere(true);
        updateVertexBuffer();
    }

    private void updateVertexBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                playerCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(playerCoords);
        vertexBuffer.position(0);
    }

    int getBrickNumber() {
        return brickNumber;
    }

    private void CreateBuffers() {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                playerCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(playerCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // The texture buffer
        ByteBuffer tb = ByteBuffer.allocateDirect(uvs.length * 4);
        tb.order(ByteOrder.nativeOrder());
        uvBuffer = tb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    void Draw() {
        GLES20.glUseProgram(program);
        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, Constants.CoordsPerVertex,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        /// Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(program, "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(program, "s_texture");

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }
}
