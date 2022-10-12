package com.dmgames.threesquares;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int changeColorNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override public void run() {

                TextView ClickToPlay = (TextView) findViewById(R.id.play);
                if(changeColorNumber==0) { ClickToPlay.setTextColor(getColorFunction(MainActivity.this,R.color.black));
                    changeColorNumber++;
                }
                else if(changeColorNumber==1) {
                    ClickToPlay.setTextColor(getColorFunction(MainActivity.this,R.color.gray));
                    changeColorNumber--;
                }
            }
        }, 0, 500);//wait 0 ms before doing the action and do it every 1000ms (1second)
    }
    public static int getColorFunction(Context context, @ColorRes int colorId){
        return ContextCompat.getColor(context,colorId);
    }
    public void launchSettings(View v){
        Intent i = new Intent(this, SettingActivity.class);
        startActivity(i);
    }

    public void launchGame(View v){
        Intent i = new Intent(this, MainGame.class);
        startActivity(i);
    }




}