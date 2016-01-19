package com.mobileapp.sab.tictactoe;

import android.content.res.Resources;
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

    //X is True and O is false
    boolean player = true;

    //counter
    int count = 0;
    //Top text
    TextView topText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initalize variables
        count = 0;
        topText = (TextView) findViewById(R.id.Top);
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

        //event listeners for the buttons
        for (ImageButton b : bArray){
            b.setOnClickListener(this);
        }
        rBut.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view){
        Button b = (Button)view;
        buttonClicked(b);
        //test("TESTING");
    }

    public void buttonClicked(Button b){
        if(player){
            //cat's turn
            b.setImageResource(R.drawable.cat);
            topText.setText("O's Turn");
        } else{
            //dog's turn
            b.setImageResource(R.drawable.dog);
            topText.setText("X's Turn");
        }
        //change player
        player =!player;


    }


    private void test(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
