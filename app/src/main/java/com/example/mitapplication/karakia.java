package com.example.mitapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class karakia extends AppCompatActivity {
ImageButton btn;
TextView txtView;

TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karakia);
        btn = findViewById(R.id.playButton);
        txtView = findViewById(R.id.karakiaText);

        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.ENGLISH);
                    t1.setSpeechRate(0.4f);
                    t1.setPitch((float) 0.7);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txtView.getText().toString();
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}