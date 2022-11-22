package com.example.mitapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class karakia extends AppCompatActivity {
ImageButton btn;
TextView txtView, stop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_karakia);
        btn = findViewById(R.id.playButton);
        stop = findViewById(R.id.stop);
        MediaPlayer mp = MediaPlayer.create(this, R.raw.karakia_music);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()== true) {
                    mp.pause();
                    stop.setVisibility(View.INVISIBLE);
                }
                else {
                    mp.start();
                    stop.setVisibility(View.VISIBLE);
                }

            }
        });
        setupHyperlink();

    }

    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.hyperlink);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}