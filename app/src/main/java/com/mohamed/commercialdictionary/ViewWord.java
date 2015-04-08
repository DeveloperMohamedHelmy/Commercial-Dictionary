package com.mohamed.commercialdictionary;


import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class ViewWord extends ActionBarActivity {

    Button fromCopy , toCopy ;
    TextView FROM_word , TO_word  ;
    String FromText , ToText ;
    TextToSpeech tts ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_word);

        FROM_word = (TextView) findViewById(R.id.EN_word);
        TO_word = (TextView) findViewById(R.id.AR_word);
        fromCopy = (Button) findViewById(R.id.FROM_COPY);
        toCopy = (Button) findViewById(R.id.TO_COPY);
        Intent intent = getIntent();
        FromText = intent.getStringExtra("FromText");
        ToText = intent.getStringExtra("ToText");
        FROM_word.setText(FromText);
        TO_word.setText(ToText);

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        fromCopy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                clipboardManager.setText(FromText);
                Toast.makeText(getBaseContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        toCopy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                clipboardManager.setText(ToText);
                Toast.makeText(getBaseContext(), "Copied to clipboard" , Toast.LENGTH_SHORT).show();
            }
        });

/*
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR){
                    Toast.makeText(getBaseContext(), "This Language is NOT SUPPORTED!" , Toast.LENGTH_LONG).show();
                }else {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        fromSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tts.setSpeechRate(1.0f);
                tts.speak(FromText , TextToSpeech.QUEUE_FLUSH , null);
            }
        });

        toSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.setSpeechRate(1.0f);
                tts.speak(ToText , TextToSpeech.QUEUE_FLUSH , null);
            }
        });


*/

    }
/*

    @Override
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewword, menu);
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
}
