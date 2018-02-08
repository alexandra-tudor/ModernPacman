package com.example.alexandra.pacmantouch;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


class Background {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private FloatBuffer uvBuffer;
    private static float uvs[] = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

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

    private final int program;
    private int level;
    private int backgroundTexture;
    // number of coordinates per vertex in this array
    private static float[] squareCoords = {
            -1f, 1f, 0.0f,   // top left
            -1f, -1f, 0.0f,   // bottom left
            1f, -1f, 0.0f,   // bottom right
            1f, 1f, 0.0f}; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    Background(Context ctx, int level) {
        program = GraphicTools.CreateShaderProgram(vertexShaderCode, fragmentShaderCode);
        this.level = level;

        CreateBackground();
        LoadTextures(ctx);

    }

    private void CreateBackground()
    {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
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

    private void LoadTextures(Context ctx) {
        switch (this.level) {
            case Constants.EasyLevel:
                this.backgroundTexture = GraphicTools.SetupImage(com.example.alexandra.pacmantouch.R.drawable.grass, ctx);
                break;
            case Constants.MediumLevel:
                this.backgroundTexture = GraphicTools.SetupImage(com.example.alexandra.pacmantouch.R.drawable.leaves, ctx);
                break;
            case Constants.HardLevel:
                this.backgroundTexture = GraphicTools.SetupImage(com.example.alexandra.pacmantouch.R.drawable.ice, ctx);
                break;
        }
    }

    void setBackgroundTexture(int backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    void Draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(program);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        int vertexStride = Constants.CoordsPerVertex * 4;
        GLES20.glVertexAttribPointer(mPositionHandle, Constants.CoordsPerVertex,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        /// Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(program, "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (program, "s_texture" );

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,  this.backgroundTexture);

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }
}
