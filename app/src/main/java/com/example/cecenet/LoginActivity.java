package com.example.cecenet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class LoginActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    MaterialEditText txtPassword;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPassword     = findViewById(R.id.txtPassword);
        progressDialog  = new ProgressDialog(this);

        CekKoneksi();
        PushDownAnim.setPushDownAnimTo(findViewById(R.id.btnPassword));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(getString(R.string.LOGIN_CECENET));
        }
    }

    public void CekKoneksi(){
        AndroidNetworking.post(getString(R.string.URL) + "Database.php")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Koneksi Gagal !")){
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.Gagal_Terhubung_dengan_Raspberry), Toast.LENGTH_SHORT).show();
                        }
                        else if(response.isEmpty()){
                            NotifikasiTerhubung();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.Gagal_Menghubungkan_ke_Server), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void NotifikasiTerhubung(){
        NotificationManager notificationManager;
        NotificationCompat.Builder builder      = new NotificationCompat.Builder(this, "notify_cecenet");
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        bigText.setBigContentTitle(getString(R.string.Tuhubung_Raspberry));
        bigText.setSummaryText(getString(R.string.Terhubung_dengan_Raspberry));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.terhubung));
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle(getString(R.string.Tuhubung_Raspberry));
        builder.setContentText(getString(R.string.Berhasil_Menghubungkan_ke_Server_Raspberry));
        builder.setOngoing(true);
        builder.setStyle(bigText);

        notificationManager  = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager != null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String channelId            = "Channel_ID_Cecenet";
                NotificationChannel channel = new NotificationChannel(channelId, "Notifikasi Cecenet", NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            notificationManager.notify(0, builder.build());
        }
    }

    public void btnPassword(View view) {
        InputMethodManager inputMethodManager   = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(txtPassword.getText() != null && inputMethodManager != null){
            if(txtPassword.getText().toString().isEmpty()){
                YoYo.with(Techniques.Shake).duration(300).playOn(txtPassword);
                txtPassword.setError(getString(R.string.Jangan_Kosong_));
                txtPassword.requestFocus();
            }
            else if(txtPassword.getText().length() <= 5){
                YoYo.with(Techniques.Shake).duration(300).playOn(txtPassword);
                txtPassword.setError(getString(R.string.Password_Terlalu_Pendek));
                txtPassword.requestFocus();
            }
            else{
                progressDialog.setMessage(getString(R.string.Tunggu_Sebentar_));
                progressDialog.setCancelable(false);
                progressDialog.show();

                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                CekLogin();
            }
        }
    }

    public void CekLogin(){
        if(txtPassword.getText() != null){
            AndroidNetworking.post(getString(R.string.URL) + "Login.php")
                    .addBodyParameter("Password", txtPassword.getText().toString())
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Ada")){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                progressDialog.dismiss();
                            }
                            else if(response.equalsIgnoreCase("Tidak Ada")){
                                YoYo.with(Techniques.Shake).duration(300).playOn(txtPassword);
                                progressDialog.dismiss();
                                txtPassword.setText("");
                                txtPassword.setError(getString(R.string.Password_Salah_));
                                txtPassword.requestFocus();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, getString(R.string.Gagal_Menghubungkan_ke_Server), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.cancelAll();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 1000);
                CekWifi();
                CekKoneksi();
            }
        }, 1000);
        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @SuppressWarnings("deprecation")
    public void CekWifi(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager                 = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);

        if(connectivityManager != null && wifiManager != null){
            NetworkInfo networkInfo             = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    String IP        = Formatter.formatIpAddress(wifiInfo.getIpAddress());
                    String IPAddress = IP.substring(0, 12);

                    if(!IPAddress.equals("192.168.100.")){
                        toCekWifiActivity();
                    }
                }
                else{
                    toCekWifiActivity();
                }
            }
            else{
                toCekWifiActivity();
            }
        }
    }

    public void toCekWifiActivity(){
        startActivity(new Intent(LoginActivity.this, CekWifiActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if(mBackPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(LoginActivity.this, getString(R.string.Tekan_Sekali_Lagi_untuk_Keluar), Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}