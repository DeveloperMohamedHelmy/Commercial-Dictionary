package com.mohamed.commercialdictionary;


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

    Button fromSpeak , toSpeak , descSpeak ;
    TextView FROM_word , TO_word , DESC_word ;
    String FromText , ToText , descText ;
    TextToSpeech tts ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_word);

        FROM_word = (TextView) findViewById(R.id.EN_word);
        TO_word = (TextView) findViewById(R.id.AR_word);
        DESC_word = (TextView) findViewById(R.id.DESC_word);
        fromSpeak = (Button) findViewById(R.id.FROM_speak);
        toSpeak = (Button) findViewById(R.id.TO_speak);
        descSpeak = (Button) findViewById(R.id.DESC_speak);
        Intent intent = getIntent();
        FromText = intent.getStringExtra("FromText");
        ToText = intent.getStringExtra("ToText");
        descText = intent.getStringExtra("descText");
        FROM_word.setText(FromText);
        TO_word.setText(ToText);
        DESC_word.setText(descText);


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

                tts.setSpeechRate(0.7f);
                tts.speak(FromText , TextToSpeech.QUEUE_FLUSH , null);
            }
        });

        toSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.setSpeechRate(0.7f);
                tts.speak(ToText , TextToSpeech.QUEUE_FLUSH , null);
            }
        });

        descSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.setSpeechRate(0.7f);
                tts.speak(descText , TextToSpeech.QUEUE_FLUSH , null);
            }
        });




    }




    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < Character.codePointCount(s, 0, s.length()); i++) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <=0x06E0)
                return true;
        }
        return false;
    }



    @Override
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }



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
