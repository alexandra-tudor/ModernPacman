package com.example.alexandra.pacmantouch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLSurf extends GLSurfaceView {

    private final GLRenderer mRenderer;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private Context ctx;

    public GLSurf(Context context, Point resolution, int level) {
        super(context);

        this.ctx = context;
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new GLRenderer(context, resolution, level);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public boolean playerHasLost() {
        return mRenderer.playerHasLost();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        if(playerHasLost()) {
            Intent i = new Intent(ctx, LevelChooseActivity.class);
            ctx.startActivity(i);
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // Log.d("Action_Move", "x: " + x + " y: " + y);
                break;
            case MotionEvent.ACTION_UP:
                // Log.d("Action_UP", "x: " + x + " y: " + y);
                mRenderer.performAction(Constants.ActionType.Move, x, y);
        }



        mPreviousX = x;
        mPreviousY = y;
        return true;
    }




    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mRenderer.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mRenderer.onResume();
    }
}
