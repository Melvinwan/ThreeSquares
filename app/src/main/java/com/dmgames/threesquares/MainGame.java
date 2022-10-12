package com.dmgames.threesquares;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;



public class MainGame extends AppCompatActivity {
    CountDownTimer timer;
    int level = 3;
    int [] dangerZone = new int[1000];

    boolean idNotInArray = true;
    boolean idIsNotInDangerZone = true;
    String playerList[] = {"Player 1", "Player 2"};
    int playerNumber = 1;
    int id = 0;
    int [] arrayId = new int[1000];
    int changeColorNumber = 0;
    int numberClickedAPlayer=0;
    int numberClicked = 0;

    private boolean isPlaying = true;
    private int heightContainer;
    private int widthContainer;
    // Getter
    public boolean getIsPlaying() {
        return isPlaying;
    }
    public int getHeightContainer() {
        return heightContainer;
    }
    public int getWidthContainer() {
        return widthContainer;
    }

    // Setter
    public void setIsPlaying(boolean isPlayingNow) {
        this.isPlaying = isPlayingNow;
    }
    public void setHeightContainer(int currentHeight) {
        this.heightContainer = currentHeight;
    }
    public void setWidthContainer(int currentWidth) {
        this.widthContainer = currentWidth;
    }

    Dialog resultDialog;
//    Dialog changePlayerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        resultDialog = new Dialog(this);

        TextView countDownStart = new TextView(this);
        countDownStart.setTextSize(120);
        countDownStart.setTextColor(getColorFunction(MainGame.this,R.color.black));
        countDownStart.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        countDownStart.setGravity(Gravity.CENTER);
        LinearLayout VerticalLayout = (LinearLayout) findViewById(R.id.gameContainer);
        VerticalLayout.addView(countDownStart);

        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String durationLeft = String.format(Locale.ENGLISH,"%2d"
                        , TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                countDownStart.setText(durationLeft);
            }

            @Override
            public void onFinish() {
                countDownStart.setVisibility(View.GONE);
                //Layout game
                showDialogChangePlayer(1);
                layoutGame();
                countDownTime();
            }
        }.start();
    }
    public static int getColorFunction(Context context, @ColorRes int colorId){
        return ContextCompat.getColor(context,colorId);
    }
    public void showDialogResult(int playerNumberWin){
        resultDialog.setContentView(R.layout.result_game);
        TextView winnerName = (TextView) resultDialog.findViewById(R.id.playerWin);
        winnerName.setText(playerList[playerNumberWin-1]);
        winnerName.setTextColor(playerNumberWin==1?getColorFunction(MainGame.this,R.color.player1name):getColorFunction(MainGame.this,R.color.player2name));
        resultDialog.setCancelable(false);
        resultDialog.show();
    }
    public void showDialogChangePlayer(int currentPlayerNumber){
        resultDialog.setContentView(R.layout.change_player);
        TextView currentName = (TextView) resultDialog.findViewById(R.id.currentPlayer);
        currentName.setText(playerList[currentPlayerNumber-1]);
        currentName.setTextColor(currentPlayerNumber==1?getColorFunction(MainGame.this,R.color.player1name):getColorFunction(MainGame.this,R.color.player2name));
        resultDialog.setCancelable(false);
        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resultDialog.show();
                timer.start();
            }
            @Override
            public void onFinish() {
               resultDialog.dismiss();
                timer.start();
            }
        }.start();
    }
    public void exitAlert(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainGame.this);
        dialog.setTitle( getString(R.string.exit) ).setMessage("Are you sure to quit?").setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
            dialoginterface.cancel();
          }}).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
//                        game=false;
                        backToMain(v);
                    }
                }).show();
    }
    public void backToMain(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public  void countDownTime(){
        TextView textTimer = (TextView) findViewById(R.id.timerText);

    timer = new CountDownTimer(8000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String durationLeft = String.format(Locale.ENGLISH,"%02d seconds"
                    , TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            textTimer.setText(durationLeft);
        }

        @Override
        public void onFinish() {
            gameOver(playerNumber);
        }
    }.start();
}
public void gameOver(int currentPlayerNumber){
    if(currentPlayerNumber == 1){
        showDialogResult(2);}
    else if(currentPlayerNumber == 2){
        showDialogResult(1);}
    setIsPlaying(false);
    timer.cancel();
}
public boolean idInArrayAndDangerZoneCheck(int id){
    for (int element : arrayId) {
        if (element == id) {
            return true;
        }
    }
    for (int element : dangerZone) {
                if (element ==id) {
                    return true;}
}return false;}
    public void layoutGame(){
        LinearLayout VerticalLayout = (LinearLayout) findViewById(R.id.gameContainer);

        //Player TextView
        TextView currentPlayer = (TextView) findViewById(R.id.player);

        //Screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthContainerGame = size.x-60;
        int heightContainerGame = size.y-60;
        int numberSquareHorizontal = 8;
        int widthSquare = (widthContainerGame-(numberSquareHorizontal*20))/(numberSquareHorizontal+1);
        double numberSquareVertical = Math.floor(heightContainerGame/(widthSquare+20)-2);

//        int width =View.MeasureSpec.makeMeasureSpec( 0 ,View.MeasureSpec.UNSPECIFIED);
//        int height =View.MeasureSpec.makeMeasureSpec( 0 ,View.MeasureSpec.UNSPECIFIED);
//        VerticalLayout.measure(width,height);
//        setHeightContainer(VerticalLayout.getMeasuredHeight());
//        setWidthContainer(VerticalLayout.getMeasuredWidth());

        for(int i=0; i<(numberSquareVertical/level);i++){
            for(int j=0;j<level;j++){
                int x = (int)(Math.round(Math.random()*((numberSquareVertical*numberSquareHorizontal-(i*numberSquareHorizontal))+1))+(i*numberSquareHorizontal));
                dangerZone[j+(i*level)]=x;
            }}
        for (int i = 0; i < numberSquareVertical; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setGravity(Gravity.CENTER_HORIZONTAL);

            for (int j = 0; j < numberSquareHorizontal; j++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10,10,10,10);

                TextView Square = new TextView(this);
                Square.setWidth(widthSquare);
                Square.setHeight(widthSquare);
                Square.setLayoutParams(layoutParams);
                Square.setId(j + 1 + (i * numberSquareHorizontal));
                for (int element : dangerZone) {
                    if (element == Square.getId()) {
                        Square.setBackgroundColor(getColorFunction(MainGame.this,R.color.dangerZone));
                        break;
                    } else {
                        Square.setBackgroundColor(getColorFunction(MainGame.this,R.color.squareColorPast));
                    }
                }

                // add view to the inner LinearLayout
                row.addView(Square);
//                Square.setText(""+numberSquareVertical);
                Square.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        for (int element : dangerZone) {
                            if (element == Square.getId()) {
                                idIsNotInDangerZone = false;
                                break;
                            } else {
                                idIsNotInDangerZone = true;
                            }
                        }
                        for (int element : arrayId) {
                            if (element == Square.getId()) {
                                idNotInArray = false;
                                break;
                            } else {
                                idNotInArray = true;
                            }
                        }
//                        Square.setText(""+arrayId);
                        if((idNotInArray&&idIsNotInDangerZone)&&(id == 0|| Square.getId() == (id+numberSquareHorizontal)
                                ||Square.getId() == (id-numberSquareHorizontal)
                                ||((Square.getId() == (id+1))&&(id%numberSquareHorizontal!=0))
                                ||((Square.getId() == (id-1))&&(id%numberSquareHorizontal!=1)))){
                        if(id!=0){
                        TextView pastSquare = (TextView) findViewById(id);
                        pastSquare.setBackgroundColor(getColorFunction(MainGame.this,R.color.main_color_variant));}
                        if ((getIsPlaying())){

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override public void run() {
                                    if(changeColorNumber==0&&Square.getId()==id) {
                                        Square.setBackgroundColor(Color.parseColor("#ff1c14"));
                                        changeColorNumber++;
                                    }
                                    else if(changeColorNumber==1&&Square.getId()==id) {
                                        Square.setBackgroundColor(Color.parseColor("#49f251"));
                                        changeColorNumber++;
                                    }
                                    else if(changeColorNumber==2&&Square.getId()==id){Square.setBackgroundColor(Color.parseColor("#3de3fc"));
                                        changeColorNumber = 0;}
                                }
                            }, 0, 200);
                        id = Square.getId();
                        arrayId[numberClicked] = Square.getId();
                        numberClicked++;}

                        TextView moveLeft = (TextView) findViewById(R.id.moveLeft);
                            View boardToolbarGame = (View) findViewById(R.id.boardToolbarGame);
                            View mainGameLayout = (View) findViewById(R.id.mainGameLayout);
                            if(getIsPlaying()){
                                numberClickedAPlayer++;
                                if(numberClickedAPlayer<3){
                            moveLeft.setText(String.format(Locale.ENGLISH,"%2d Moves Left", 3-numberClickedAPlayer));
                            if(numberClickedAPlayer == 2){
                                moveLeft.setText(getString(R.string.onemovesleft));
                            }
                        }
                        if(numberClickedAPlayer==3){
                            moveLeft.setText(getString(R.string.threemovesleft));
                            numberClickedAPlayer = 0;
                            if(playerNumber == 1){
                                currentPlayer.setText(playerList[1]);
                                playerNumber++;
                                showDialogChangePlayer(playerNumber);
                                boardToolbarGame.setBackgroundColor(getColorFunction(MainGame.this,R.color.player2name));
                                mainGameLayout.setBackgroundColor(getColorFunction(MainGame.this,R.color.player2background));
                            } else {
                                currentPlayer.setText(playerList[0]);
                                playerNumber--;
                                showDialogChangePlayer(playerNumber);
                                boardToolbarGame.setBackgroundColor(getColorFunction(MainGame.this,R.color.player1name));
                                mainGameLayout.setBackgroundColor(getColorFunction(MainGame.this,R.color.player1background));
                            }

                            timer.start();
                        }}
                            if((idInArrayAndDangerZoneCheck(id+numberSquareHorizontal)||((id+numberSquareHorizontal)>(numberSquareHorizontal*numberSquareVertical)))
                                    &&(idInArrayAndDangerZoneCheck(id-numberSquareHorizontal)||((id-numberSquareHorizontal)<1))
                                    &&((idInArrayAndDangerZoneCheck(id+1))||(id%numberSquareHorizontal==0))
                                    &&((idInArrayAndDangerZoneCheck(id-1))||(id%numberSquareHorizontal==1))){
                                gameOver(playerNumber);
                            }
                    }}
                });
            }
            // add view to the outer LinearLayout
            VerticalLayout.addView(row);
        }
    }

}