package com.prasunpersonal.birthdaysurprise;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class ShowDialog extends AppCompatActivity {
    Button open, cancel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            AudioManager mobileMode = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
            mobileMode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mobileMode.setStreamVolume(AudioManager.STREAM_MUSIC, mobileMode.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            mobileMode.setStreamVolume(AudioManager.STREAM_RING, mobileMode.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
            mobileMode.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mobileMode.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
            mobileMode.setStreamVolume(AudioManager.STREAM_ALARM, mobileMode.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
            mobileMode.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mobileMode.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_layout,null);
        builder.setView(view).setCancelable(false);
        builder.create().show();

        open = view.findViewById(R.id.open);
        cancel = view.findViewById(R.id.cancel);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this,0, i ,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(ShowDialog.this, "BirthdaySurprise")
                    .setContentTitle("Happy Birthday Anindita ❤")
                    .setContentText("Warm Greeting from Prasun... \uD83C\uDF89 \nClick here to open the app and see the surprise.")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Warm Greeting from Prasun... \uD83C\uDF89 \nClick here to open the app and see the surprise."))
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pi)
                    .addAction(R.drawable.logo,"Open",pi);

            NotificationChannel channel = new NotificationChannel("BirthdaySurprise", "Birthday Surprise", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(Uri.parse("android.resource://com.softskill.birthdaysurprise/" + R.raw.hbd),audioAttributes);
            channel.setVibrationPattern(new long[] { 0, 1000, 500, 1000, 500, 1000 });

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder1.build());
        }
    }
}