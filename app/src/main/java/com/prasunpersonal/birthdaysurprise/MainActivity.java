package com.prasunpersonal.birthdaysurprise;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    TextView t;
    Button b;
    int Bmm = 8, Bdd = 5;
    int dd = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
    int mm = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
    boolean firstTime;

    private void requestOverlayDisplayPermission() {
        AlertDialog builder = new AlertDialog.Builder(this)
            .setCancelable(false)
            .setIcon(R.drawable.overlay)
            .setTitle("Display over other apps")
            .setMessage("This app needs the screen overlay permission to show the main popup window.")
            .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, RESULT_OK);
            }
        })
        .setNeutralButton("Not Now", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog builder1 = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setIcon(R.drawable.warning)
                .setTitle("Permission Denied!")
                .setMessage("If you deny this permission you will miss the main thing. Are you sure you want to deny this permission?")
                .setNeutralButton("Yes",null)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestOverlayDisplayPermission();
                    }
                }).create();
                builder1.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        builder1.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00ff00"));
                        builder1.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#a0a0a0"));
                    }
                });
                builder1.show();
            }
        }).create();
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00ff00"));
                builder.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#a0a0a0"));
            }
        });
        builder.show();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void chkBtn(View v){
        if(mm == Bmm && dd >= Bdd) {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            finish();
        }else {
            t.setText("Your surprise will be visible on or after\n\n" + String.format("%02d", Bdd) + "-" + String.format("%02d", Bmm) + "-" + new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
            t.setVisibility(View.VISIBLE);
            b.setEnabled(false);
            new Handler().postDelayed(() -> {
                t.setVisibility(View.GONE);
                b.setEnabled(true);
                t.setText("");
            },5000);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = findViewById(R.id.show);
        b = findViewById(R.id.btn);

        SharedPreferences bs = getSharedPreferences("BirthdaySurprise", MODE_PRIVATE);
        String currentDate = new SimpleDateFormat("dd/MM-HH/mm/ss", Locale.getDefault()).format(new Date());
        String birthDate = String.format("%02d",Bdd)+"/"+String.format("%02d",Bmm)+"-00/00/00";
        long diff = 0;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getBaseContext())) {
                requestOverlayDisplayPermission();
            }
        }

        {
            {
                firstTime = bs.getBoolean("firstTime", true);
                if(firstTime) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.DAY_OF_MONTH, Bdd);
                    calendar.set(Calendar.MONTH, Bmm - 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Intent i = new Intent(this, ShowDialog.class);
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                    }
                }
            }
            SimpleDateFormat date = new SimpleDateFormat("dd/MM-HH/mm/ss");
            try {
                Date d1 = date.parse(currentDate);
                Date d2 = date.parse(birthDate);
                diff = d1.getTime() - d2.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(diff >= 0 && firstTime) {
                SharedPreferences.Editor e = bs.edit();
                e.putBoolean("firstTime", false);
                e.commit();
            }
        }
    }
}