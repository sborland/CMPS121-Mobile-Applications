package com.mobileapp.sab.tictactoe;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {


    //buttons
     ImageButton a1, a2, a3, b1,b2,b3, c1,c2,c3;
    Button rBut;
    ImageButton[] bArray;

    //cat is True and dog is false
    boolean player = true;

    //counter
    int count = 0;
    int boxNum = 3;
    int[][]checker= new int[boxNum][boxNum];
    //Top text
    TextView topText;
    boolean win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initalize variables
        count = 0;
        topText = (TextView) findViewById(R.id.Top);
        for(int r = 0; r <boxNum; r++){
            for (int c = 0; c <boxNum; c++){
                checker[r][c]=0;
            }
        }

        //Linking buttons to their counterparts in the layout file
        a1 = (ImageButton) findViewById (R.id.A1);
        a2 = (ImageButton) findViewById(R.id.A2);
        a3 = (ImageButton) findViewById(R.id.A3);
        b1 = (ImageButton) findViewById(R.id.B1);
        b2 = (ImageButton) findViewById(R.id.B2);
        b3 = (ImageButton) findViewById(R.id.B3);
        c1 = (ImageButton) findViewById(R.id.C1);
        c2 = (ImageButton) findViewById(R.id.C2);
        c3 = (ImageButton) findViewById(R.id.C3);
        rBut = (Button) findViewById(R.id.restart);
        bArray = new ImageButton[]{a1, a2, a3, b1,b2,b3, c1,c2,c3};

        rBut.setOnClickListener(this);
        //event listeners for the buttons
        for (ImageButton b : bArray){
            b.setOnClickListener(this);
        }
    }



    @Override
    public void onClick(View view){
        String vStr = (String) view.getTag();
       // int vInt = (view.getId());
       // test(Integer.toString(vInt));
        if (vStr.equalsIgnoreCase("restart")) {
            //restart button
            restartGame();
        } else  {
            //any of the unclicked squares
            ImageButton b = (ImageButton) view;
            buttonClicked(b);
        }
    }

    public void buttonClicked(ImageButton b){
        count++;
        if(player){
            //cat's turn
            b.setImageResource(R.drawable.cat);
            topText.setText("Dog's Turn");
           // b.setTag("cats");
            checkForWinner(b);
        } else{
            //dog's turn
            b.setImageResource(R.drawable.dog);
            topText.setText("Cat's Turn");
            checkForWinner(b);
           // b.setTag("dogs");
        }
        //change player
        b.setClickable(false);
        player =!player;



    }

    public void restartGame(){
        for (ImageButton b : bArray){
            b.setImageResource(R.drawable.house);
            b.setClickable(true);
            b.setBackgroundColor(Color.rgb(128,171,47));
        }

        for(int r = 0; r <boxNum; r++){
            for (int c = 0; c <boxNum; c++){
                checker[r][c]= 0;
            }
        }
        //randomly picks the player who starts first
        int rand = (int)(Math.random()*10);
        if (rand>= 5){
            topText.setText("Cat Starts!");
            player= true;
        }else{
            topText.setText("Dog Starts!");
            player= false;
        }
        count = 0;
        win = false;
        //test("RESTART GAME");
    }

    public void checkForWinner(ImageButton b){
        //set the button location in the matrix to be true
        String bStr = (String)b.getTag();

        int row = Character.getNumericValue(bStr.charAt(0));
        int col = Character.getNumericValue(bStr.charAt(1));
        if(player){
            //cat is 1
            checker[row][col] = 1;
        } else {
            //dog is 2
            checker[row][col] = 2;
        }
        int checkC = boxNum;
        int checkD = boxNum;

        //sweep through the matrix horizontally to see if there is 3 in the row
        for(int r = 0; r <boxNum; r++) {
                checkC = boxNum;
                checkD = boxNum;
            for (int c = 0; c < boxNum; c++) {
                if (checker[r][c] == 1) { checkC--;}
                if (checker[r][c] == 2) { checkD--;}
                if (checkC <= 0 || checkD <=0) {
                    winGame(0, r);
                }
            }
        }
        //sweep through the matrix vertically to see if there is 3 in the row
        for(int c = 0; c <boxNum; c++) {
            checkC = boxNum;
            checkD = boxNum;
            for (int r = 0; r < boxNum; r++) {
                if (checker[r][c] == 1) { checkC--;}
                if (checker[r][c] == 2) { checkD--;}
                if (checkC <= 0 || checkD <= 0) {
                    winGame(1, c);
                }
            }
        }

        //sweep through the matrix left diagonal to see if there is 3 in the row
        checkC = boxNum;
        checkD = boxNum;
        for(int n = 0; n <boxNum; n++) {
            if (checker[n][n] == 1) { checkC--;}
            if (checker[n][n] == 2) { checkD--;}
            if (checkC <= 0 || checkD <=0) {
                winGame(2, n);
            }
        }

        //sweep through the matrix left diagonal to see if there is 3 in the row
        int num = boxNum-1;
        if (checker[0][num] == checker[(num/2)][(num/2)] && checker[(num/2)][(num/2)] == checker[num][0] &&!b2.isClickable() ) {
            winGame(3, num);
        }
        /*checkC = boxNum;
        checkD = boxNum;
        int num = boxNum-1;
        for(int c = num; c >0; c--) {
            if (checker[num-c][c] == 1) { test(bStr);checkC--;}
            if (checker[num-c][c] == 2) {test(bStr); checkD--;}
            if (checkC <= 0 || checkD <= 0) {

            }
        }*/

        if (count >= 9 && win == false){
            topText.setText("It's a tie!");
        }

    }

    public void winGame (int cases, int rowcol){
        //win state cases:
        // 0 = horizontal win with rowcol having row num
        // 1 = vertical win with rowcol having col num
        // 2 = left diagonal win
        // 3 = right diagonal win
        win = true;
        switch (cases) {
            case 0:
               // test("horizontal win");
                int hdex = rowcol;
                for (int i = 0; i < boxNum; i++) {
                    bArray[(hdex*boxNum)+i].setBackgroundColor(Color.YELLOW);
                }
                break;
            case 1:
                //test("vertical win");
                for (int vdex = rowcol; vdex < boxNum * boxNum; vdex = vdex + boxNum) {
                    bArray[vdex].setBackgroundColor(Color.YELLOW);
                }
                break;
            case 2:
                //test("left diagonal win");
                for (int ldex = 0; ldex < boxNum * boxNum; ldex = ldex + (boxNum + 1)) {
                    bArray[ldex].setBackgroundColor(Color.YELLOW);
                }
                break;
            case 3:
                //test("right diagonal win");
                for (int rdex = boxNum-1; rdex <= boxNum + boxNum; rdex = rdex + (boxNum - 1)) {
                    bArray[rdex].setBackgroundColor(Color.YELLOW);
                }
                break;
        }
        if(player){
            topText.setText("Cat Won!");
        } else {
            topText.setText("Dog Won!");
        }

        for (ImageButton b : bArray){
            b.setClickable(false);
        }
    }

    private void test(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

