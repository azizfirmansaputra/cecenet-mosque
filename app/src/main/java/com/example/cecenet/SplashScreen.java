package com.example.cecenet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(SplashScreen.this, CekWifiActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finishAfterTransition();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            YoYo.with(Techniques.ZoomIn).duration(300).playOn(findViewById(R.id.logo));
        }
    }

    @Override
    public void onBackPressed() {

    }
}