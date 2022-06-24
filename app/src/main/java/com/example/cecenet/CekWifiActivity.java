package com.example.cecenet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class CekWifiActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_wifi);

        CekWifi();

        Button btnCobaLagi = findViewById(R.id.btnCobaLagi);
        PushDownAnim.setPushDownAnimTo(btnCobaLagi);
        btnCobaLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CekWifi();
            }
        });

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.cancelAll();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 1){
            CekWifi();
        }
    }

    @SuppressWarnings("deprecation")
    public void CekWifi(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new  String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null){
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null && networkInfo.isConnected()){
                    if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                        InfoWifi();
                    }
                    else{
                        YoYo.with(Techniques.Shake).duration(100).playOn(findViewById(R.id.boxPetunjuk));
                        snackBar(getString(R.string.Tidak_ada_Koneksi_Wi_Fi));
                    }
                }
                else{
                    YoYo.with(Techniques.Shake).duration(100).playOn(findViewById(R.id.boxPetunjuk));
                    snackBar(getString(R.string.Tidak_Ada_Koneksi_dari_Manapun));
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void InfoWifi(){
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiManager != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            String IP        = Formatter.formatIpAddress(wifiInfo.getIpAddress());
            String IPAddress = IP.substring(0, 12);

            if(IPAddress.equals("192.168.100.") || wifiInfo.getSSID().equalsIgnoreCase("\"Kosan R-tiga\"")){
                startActivity(new Intent(CekWifiActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finishAfterTransition();
            }
            else{
                YoYo.with(Techniques.Shake).duration(100).playOn(findViewById(R.id.boxPetunjuk));
                snackBar(getString(R.string.Tidak_Terhubung_dengan_Wi_Fi_Raspberry));
            }
        }
    }

    public void snackBar(String text){
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), text, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getColor(R.color.colorPrimary));
        snackbar.setTextColor(Color.RED);
        snackbar.setDuration(3000);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        if(mBackPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(CekWifiActivity.this, getResources().getString(R.string.Tekan_Sekali_Lagi_untuk_Keluar), Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}