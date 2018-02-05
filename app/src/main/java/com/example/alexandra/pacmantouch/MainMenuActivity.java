package com.example.alexandra.pacmantouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.alexandra.pacmantouch.R.layout.activity_main_menu);


        Button btnNewGame = (Button) findViewById(com.example.alexandra.pacmantouch.R.id.btnNewGame);
        btnNewGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenuActivity.this, LevelChooseActivity.class);
                startActivity(myIntent);
            }
        });


        Button btnExitGame = (Button) findViewById(com.example.alexandra.pacmantouch.R.id.btnExit);
        btnExitGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

    }



}
