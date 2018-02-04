package com.example.catalin.bombermantouch;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

class Enemy {
    private FloatBuffer vertexBuffer;
    private FloatBuffer uvBuffer;
    private ShortBuffer drawListBuffer;


    private int program;
    private int texture;
    private int brickNumber;
    private boolean canBeEaten;
    private boolean returnHome;
    ArrayList<Integer> stepsHome;
    ArrayList<Integer> stepsRandom;
    private static float uvs[] = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    // number of coordinates per vertex in this array
    private float[] playerCoords;

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int vertexStride = Constants.CoordsPerVertex * 4; // 4 bytes per vertex
    private int enemyType;


    Enemy(float[] position, int program, int texture, int brickNumber, int enemyType, boolean canBeEaten)
    {
        this.playerCoords = position;
        this.program = program;
        this.texture = texture;
        this.brickNumber = brickNumber;
        this.enemyType = enemyType;
        this.canBeEaten = canBeEaten;
        this.returnHome = false;

        stepsHome = new ArrayList<>();
        stepsRandom = new ArrayList<>();
        CreateBuffers();
    }

    void setCanBeEaten(boolean canBeEaten) {
        this.canBeEaten = canBeEaten;
    }

    boolean getCanBeEaten() {
        return canBeEaten;
    }

    void setTexture(int texture) {
        this.texture = texture;
    }

    int getTexture() {
        return this.texture;
    }

    void setReturnHome(boolean returnHome) {
        this.returnHome = returnHome;
    }

    boolean getReturnHome() {
        return returnHome;
    }

    void setStepsHome(ArrayList<Integer> stepsHome) {
        this.stepsHome = stepsHome;
    }

    ArrayList<Integer> getStepsHome() {
        return stepsHome;
    }


    int getEnemyType()
    {
        return enemyType;
    }


    void move(int newPosition, ArrayList<Brick> bricks) {
        this.brickNumber = newPosition;
        this.playerCoords = bricks.get(newPosition).getBrickCoords();
        bricks.get(newPosition).setEnemyHere(false);
        updateVertexBuffer();
    }

    int getBrickNumber() {
        return brickNumber;
    }

    private void CreateBuffers()
    {
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

    private void updateVertexBuffer()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                playerCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(playerCoords);
        vertexBuffer.position(0);
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
