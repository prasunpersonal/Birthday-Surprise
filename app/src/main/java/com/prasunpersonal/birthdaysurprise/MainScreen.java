package com.prasunpersonal.birthdaysurprise;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainScreen extends AppCompatActivity {
    VideoView vdo1;
    TextView txt1;
    MediaPlayer music;
    Runnable runnable;
    String[] str = "The day 6th August. Too much special for me. Something special happened today. Someone I love the most was born. So happy birthday to you Anindita. It's the start of another beautiful year of your life. Stay happy and blessed. May god fulfill your all birthday wishes. Don't be sad on this special day. I always wish you success in life. And also keep hard working. One day your dreams will come true. And with the starting of this year you turned 18. And your responsibilities have increased a lot. Now you have to be more responsible. So be more responsible. And more mature also. It's your day make it more special. And don't forget about the treat. I love you.... ❤❤❤❤❤".split("\\. ");
    int i = 0, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        vdo1 = findViewById(R.id.vdo1);
        txt1 = findViewById(R.id.txt1);

        music = MediaPlayer.create(this, Uri.parse("android.resource://com.softskill.birthdaysurprise/" +  R.raw.mainmusic));
        music.start();

        vdo1.setVideoURI(Uri.parse("android.resource://com.softskill.birthdaysurprise/" + R.raw.wishvideo));
        vdo1.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                if(i<str.length) {
                    txt1.setText(str[i]);
                    i++;
                    Handler handler = new Handler();
                    handler.postDelayed(this, 5000);
                }
            }
        };
        runnable.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        music.pause();
        vdo1.pause();
        position = vdo1.getCurrentPosition();

    }

    @Override
    protected void onResume() {
        super.onResume();
        vdo1.seekTo(position);
        vdo1.start();
        music.start();
    }
}