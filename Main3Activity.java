package com.example.androidmusicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout parentRelativeLayout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";


    private ImageView pausePlayBtn, nextBtn, previousBtn;   //**Added play pause button
    private TextView songNameTxt;

    private ImageView imageView;
    private RelativeLayout lowerRelativeLayout;
    private Button voiceEnabledBtn;
    private String mode = "ON";


    private MediaPlayer myMediaPlayer;
    private int position;
    private ArrayList<File> mySongs;
    private String mSongName;

    private SeekBar seekBar;
    private Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);



        checkVoiceCommandPermission();

        seekBar = findViewById(R.id.seekbar);

        pausePlayBtn = findViewById(R.id.play_pause_btn);
        nextBtn = findViewById(R.id.next_btn);
        previousBtn = findViewById(R.id.previous_btn);
        imageView = findViewById(R.id.logo);

        lowerRelativeLayout = findViewById(R.id.lower);
        voiceEnabledBtn = findViewById(R.id.voice_enabled_btn);
        songNameTxt = findViewById(R.id.songName);

        parentRelativeLayout = findViewById(R.id.parentRelativeLayout);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(Main3Activity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());












        validateReceiveValuesAndStartPlaying();
        imageView.setBackgroundResource(R.drawable.logo);       //Adding All the pictures in drawable folder

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matchesFound = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matchesFound != null) {
                    if (mode.equals("ON")) {
                        keeper = matchesFound.get(0);

                        if (keeper.equals("pause the song") || (keeper.equals("stop it")) || (keeper.equals("shut up")) ) {
                            playPauseSong();
                            Toast.makeText(Main3Activity.this, "Command = " + keeper, Toast.LENGTH_LONG).show();
                        } else if (keeper.equals("play the song")) {
                            playPauseSong();
                            Toast.makeText(Main3Activity.this, "Command = " + keeper, Toast.LENGTH_LONG).show();
                        } else if (keeper.equals("play next song") || (keeper.equals("I don't like it")) || (keeper.equals("Next one please"))) {
                            playNextSong();
                            Toast.makeText(Main3Activity.this, "Command = " + keeper, Toast.LENGTH_LONG).show();
                        } else if (keeper.equals("play previous song")) {
                            playPreviousSong();
                            Toast.makeText(Main3Activity.this, "Command = " + keeper, Toast.LENGTH_LONG).show();
                        }
                    }


                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


        //For long press listen

        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper = "";
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;
                }

                return false;

            }
        });


        //Voice Enable Button

        voiceEnabledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("ON")) {
                    mode = "OFF";
                    voiceEnabledBtn.setText("Voice Enable Mode - OFF");
                    lowerRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    mode = "ON";
                    voiceEnabledBtn.setText("Voice Enable Mode - ON");
                    lowerRelativeLayout.setVisibility(View.GONE);
                }

            }
        });


        pausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseSong();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMediaPlayer.getCurrentPosition() > 0) {
                    playPreviousSong();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMediaPlayer.getCurrentPosition() > 0) {
                    playNextSong();
                }
            }
        });

    }

    private void changeSeekbar() {
            seekBar.setProgress(myMediaPlayer.getCurrentPosition());

            if (myMediaPlayer.isPlaying())
            {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        changeSeekbar();
                    }
                };

                handler.postDelayed(runnable, 1000);
            }

    }


    private void validateReceiveValuesAndStartPlaying() {
        if (myMediaPlayer != null) {
            myMediaPlayer.stop();
            myMediaPlayer.release();

        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("song");
        mSongName = mySongs.get(position).getName();
        String songName = intent.getStringExtra("name");

        songNameTxt.setText(songName);
        songNameTxt.setSelected(true);


        position = bundle.getInt("position", 0);
        Uri uri = Uri.parse(mySongs.get(position).toString());


        myMediaPlayer = MediaPlayer.create(Main3Activity.this, uri);
        myMediaPlayer.start();          //Start Playing songs
    }


    private void checkVoiceCommandPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }


    private void playPauseSong() {
        imageView.setBackgroundResource(R.drawable.two);           //logo of button

        if (myMediaPlayer.isPlaying()) {
            pausePlayBtn.setImageResource(R.drawable.play);         //When the song is playing it will display the pause button instead of play button
            myMediaPlayer.pause();
        } else {
            pausePlayBtn.setImageResource(R.drawable.pause);         //When the song is playing it will display the pause button instead of play button
            myMediaPlayer.start();

            imageView.setBackgroundResource(R.drawable.two);
        }
    }


    private void playNextSong() {
        myMediaPlayer.pause();
        myMediaPlayer.stop();                  //When user will click on the next or prev button then it will pause and stop the ongoing song
        myMediaPlayer.release();

        position = ((position + 1) % mySongs.size());       //Playing next song where mySongs is the ArrayList
        Uri uri = Uri.parse(mySongs.get(position).toString());
        myMediaPlayer = MediaPlayer.create(Main3Activity.this, uri);

        mSongName = mySongs.get(position).toString();
        songNameTxt.setText(mSongName);           //Displaying Next Song
        myMediaPlayer.start();          //After passing start the song

        imageView.setBackgroundResource(R.drawable.two);

        if (myMediaPlayer.isPlaying()) {
            pausePlayBtn.setImageResource(R.drawable.pause);         //When the song is playing it will display the pause button instead of play button

        } else {
            pausePlayBtn.setImageResource(R.drawable.play);         //When the song is playing it will display the pause button instead of play button

            imageView.setBackgroundResource(R.drawable.two);
        }

    }


    private void playPreviousSong() {
        myMediaPlayer.pause();
        myMediaPlayer.stop();
        myMediaPlayer.release();

        position = ((position - 1) < 0 ? (mySongs.size() - 1) : (position - 1));

        Uri uri = Uri.parse(mySongs.get(position).toString());
        myMediaPlayer = MediaPlayer.create(Main3Activity.this, uri);

        mSongName = mySongs.get(position).toString();
        songNameTxt.setText(mSongName);           //Displaying Next Song
        myMediaPlayer.start();          //After passing start the song

        imageView.setBackgroundResource(R.drawable.two);

        if (myMediaPlayer.isPlaying()) {
            pausePlayBtn.setImageResource(R.drawable.pause);         //When the song is playing it will display the pause button instead of play button

        } else {
            pausePlayBtn.setImageResource(R.drawable.play);         //When the song is playing it will display the pause button instead of play button

            imageView.setBackgroundResource(R.drawable.two);
        }


    }

    //Seek working with play pause btn

    @Override
    public void onClick(View v) {
            switch (imageView.getId()){
                case R.id.play_pause_btn:
                if (myMediaPlayer.isPlaying()){
                    myMediaPlayer.pause();

                }
                else {

                    myMediaPlayer.start();
                    changeSeekbar();
                }
                break;
                case R.id.next_btn:
                myMediaPlayer.seekTo(myMediaPlayer.getCurrentPosition()+5000);

                break;
                case R.id.previous_btn:
                myMediaPlayer.seekTo(myMediaPlayer.getCurrentPosition()-5000);

                break;


            }
    }
}





