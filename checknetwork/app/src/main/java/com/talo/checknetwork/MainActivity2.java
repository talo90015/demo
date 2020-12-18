package com.talo.checknetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private SoundPool soundPool;
    private int music;
    private TextView Drive_information, Ipadder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = findViewById(R.id.tool);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        Ipadder = findViewById(R.id.ipadders);

        String brand = Build.BRAND; //手機品牌
        String device = Build.MODEL;   //裝置型號
        Drive_information = findViewById(R.id.drive_information);
        Drive_information.setText(brand + "\n" + device);

        GifImageView imageView = findViewById(R.id.img);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.net);
            imageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Click(View v){
        play();

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        Ipadder.setText("IP: " + Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));

        NotificationManager msg_str = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap wifi = BitmapFactory.decodeResource(getResources(), R.mipmap.wifi);
        Bitmap signal = BitmapFactory.decodeResource(getResources(), R.mipmap.signal);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0,
                        new Intent(this, MainActivity.class), 0);

        ConnectivityManager connectivityManager =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null != networkInfo){
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                //Toast.makeText(this, "Signal", Toast.LENGTH_SHORT).show();
                Ipadder.setText("");
                Notification notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_baseline_network_check_24)
                        .setLargeIcon(signal)
                        .setContentTitle("網路型態")
                        .setContentText("行動數據")
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLights(Color.GREEN, 1000, 1000)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                msg_str.notify(0, notification);
            }else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                //Toast.makeText(this, "Wifi", Toast.LENGTH_SHORT).show();
                Ipadder.setText("IP: " + Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));
                Notification notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_baseline_network_check_24)
                        .setLargeIcon(wifi)
                        .setContentTitle("網路型態")
                        .setContentText("WI-FI")
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLights(Color.GREEN, 1000, 1000)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                msg_str.notify(0, notification);
            }
        }else{
            Toast.makeText(this, "無網路可用", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("目前無網路");
            builder.setMessage("是否前往設定?");
            builder.setCancelable(true);
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("設定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }
    @SuppressLint("NewApi")
    private void init(){
        soundPool = new SoundPool.Builder().build();
        music = soundPool.load(this, R.raw.click, 1);
    }
    private void play(){
        soundPool.play(music, 1, 1, 0, 0, 1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}