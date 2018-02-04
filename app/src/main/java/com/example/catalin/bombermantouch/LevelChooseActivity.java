package com.example.catalin.bombermantouch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelChooseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_choose);


        Button btnEasy = (Button) findViewById(R.id.btnEasy);
        btnEasy.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(LevelChooseActivity.this, MainActivity.class);
                myIntent.putExtra("Level", "easy");
                startActivity(myIntent);
            }
        });


        Button btnMedium = (Button) findViewById(R.id.btnMedium);
        btnMedium.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(LevelChooseActivity.this, MainActivity.class);
                myIntent.putExtra("Level", "medium");
                startActivity(myIntent);
            }
        });

        Button btnHard = (Button) findViewById(R.id.btnHard);
        btnHard.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(LevelChooseActivity.this, MainActivity.class);
                myIntent.putExtra("Level", "hard");
                startActivity(myIntent);
            }
        });

    }
}
