package com.example.cecenet.Pengaturan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HapusGambar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("HAPUS_GAMBAR", true);
        setResult(3, resultIntent);
        finish();
    }
}