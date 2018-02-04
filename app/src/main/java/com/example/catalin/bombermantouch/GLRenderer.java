package com.example.catalin.bombermantouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Display;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;

/**
 * Created by Catalin on 19-Nov-16.
 */

public class GLRenderer implements GLSurfaceView.Renderer {


    // Objects
    private Background background;
    private Labyrinth labyrinth;
    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    public static float vertices[];
    public static short indices[];

    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;


    // Our screenresolution
    float   mScreenWidth = 1280;
    float   mScreenHeight = 720;
    Point screenSize;
    // Misc
    Context mContext;
    long mLastTime;
    int mProgram;
    int frames;
    int level;

    public GLRenderer(Context c, Point resolution, int level)
    {
        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;
        mScreenWidth = resolution.y;
        mScreenHeight = resolution.x;
        this.screenSize = resolution;
        this.level = level;
        frames= 0;
    }

    public void performAction(Constants.ActionType actionType, float x, float y) {
        switch (actionType) {
            case Move:
                labyrinth.setPlayerLocation(x, y);
                break;
        }
    }


    public void onPause()
    {
        /* Do stuff to pause the renderer */
    }

    public void onResume()
    {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND);
        // Create the triangle
        background = new Background(this.mContext, this.level);
        switch (this.level) {
            case Constants.EasyLevel:
                labyrinth = new Labyrinth(this.mContext, 6, screenSize, level);
                break;
            case Constants.MediumLevel:
                labyrinth = new Labyrinth(this.mContext, 6, screenSize, level);
                break;
            case Constants.HardLevel:
                labyrinth = new Labyrinth(this.mContext, 9, screenSize, level);
                break;
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        float ratio = (float) width / height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        frames ++;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

       // long now = System.currentTimeMillis();
        //Log.d("Action_Frames", "Frames: " + frames);

        background.Draw();
        try {
            labyrinth.Draw(frames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        // We should make sure we are valid and sane
        if (mLastTime > now) return;

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);


        // Update our example
        // Render our example
        Render(mtrxProjectionAndView);

        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
        mLastTime = now;
        */
    }
}
