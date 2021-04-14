package com.example.bluetoothcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpActivity extends Activity {
    Instructions instructions = new Instructions(); //Inicializáljuk az Instructions osztályt, hogy el tudjuk érni a benne található String-eket

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initializeComponents();
    }

    private void initializeComponents(){
        createHomeButton();
        createComponents();
    }

    private void createHomeButton(){
        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HelpActivity.this, MainActivity.class));
            }
        });
    }

    private void createComponents(){
        Button followOption = (Button) findViewById(R.id.helpOptionFollow);
        Button controlOption = (Button) findViewById(R.id.helpOptionControl);
        TextView helpScreenTextView = (TextView) findViewById(R.id.helpScreenTextView);

        followOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpScreenTextView.setText(instructions.getFollowInstruction());
            }
        });

        controlOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpScreenTextView.setText(instructions.getControlInstruction());
            }
        });

    }
}