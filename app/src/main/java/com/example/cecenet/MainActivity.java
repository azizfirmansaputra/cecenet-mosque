package com.example.cecenet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.Pengaturan.MenuAdzan.AdzanFragment;
import com.example.cecenet.Pengaturan.MenuBedug.BedugFragment;
import com.example.cecenet.Pengaturan.MenuDoaAdzan.DoaAdzanFragment;
import com.example.cecenet.Pengaturan.MenuHome.BackgroundFragment;
import com.example.cecenet.Pengaturan.MenuHome.IdentitasFragment;
import com.example.cecenet.Pengaturan.MenuWaktuSalat.WaktuSalatInformasiFragment;
import com.example.cecenet.Pengaturan.MenuIqomah.IqomahFragment;
import com.example.cecenet.Pengaturan.MenuHome.KotaDaerahFragment;
import com.example.cecenet.Pengaturan.MenuMenujuAdzan.MenujuAdzanFragment;
import com.example.cecenet.Pengaturan.MenuMenujuIqomah.MenujuIqomahCountdownFragment;
import com.example.cecenet.Pengaturan.MenuMenujuIqomah.MenujuIqomahTampilanFragment;
import com.example.cecenet.Pengaturan.PengaturanUmumFragment;
import com.example.cecenet.Pengaturan.MenuHome.InformasiFragment;
import com.example.cecenet.Pengaturan.MenuHome.PengumumanFragment;
import com.example.cecenet.Pengaturan.MenuHome.TemplateFragment;
import com.example.cecenet.Pengaturan.MenuWaktuSalat.WaktuSalatCountdownFragment;
import com.example.cecenet.Pengaturan.MenuWaktuSalat.WaktuSalatTampilanFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szugyi.circlemenu.view.CircleLayout;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CircleLayout.OnItemSelectedListener, CircleLayout.OnItemClickListener {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MenuItem menuItemClose;

    CircleLayout circleLayout;
    View Home, MenujuAdzan, Bedug, Adzan, DoaAdzan, MenujuIqomah, Iqomah, WaktuSalat;

    TemplateFragment templateFragment;
    IdentitasFragment identitasFragment;
    BackgroundFragment backgroundFragment;
    InformasiFragment informasiFragment;
    KotaDaerahFragment kotaDaerahFragment;
    PengumumanFragment pengumumanFragment;
    MenujuAdzanFragment menujuAdzanFragment;
    BedugFragment bedugFragment;
    AdzanFragment adzanFragment;
    DoaAdzanFragment doaAdzanFragment;
    MenujuIqomahTampilanFragment menujuIqomahTampilanFragment;
    MenujuIqomahCountdownFragment menujuIqomahCountdownFragment;
    IqomahFragment iqomahFragment;
    WaktuSalatTampilanFragment waktuSalatTampilanFragment;
    WaktuSalatCountdownFragment waktuSalatCountdownFragment;
    WaktuSalatInformasiFragment infoSalatFragment;
    PengaturanUmumFragment pengaturanUmumFragment;

    ProgressDialog progressDialog;
    String SSID, PasswordWifi, NamaPengguna, PasswordLama;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout            = findViewById(R.id.menu);
        actionBarDrawerToggle   = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView          = findViewById(R.id.navigasi);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        drawerLayout.bringToFront();

        circleLayout            = findViewById(R.id.CircleLayout);
        circleLayout.setOnItemSelectedListener(this);
        circleLayout.setOnItemClickListener(this);

        Home                    = findViewById(R.id.Home);
        MenujuAdzan             = findViewById(R.id.MenujuAdzan);
        Bedug                   = findViewById(R.id.Bedug);
        Adzan                   = findViewById(R.id.Adzan);
        DoaAdzan                = findViewById(R.id.DoaAdzan);
        MenujuIqomah            = findViewById(R.id.MenujuIqomah);
        Iqomah                  = findViewById(R.id.Iqomah);
        WaktuSalat              = findViewById(R.id.WaktuSalat);

        PushDownAnim.setPushDownAnimTo(
                findViewById(R.id.btnMenuTemplateHome), findViewById(R.id.btnMenuIdentitasHome),
                findViewById(R.id.btnMenuBackgroundHome), findViewById(R.id.btnMenuInformasiHome),
                findViewById(R.id.btnMenuKotaHome), findViewById(R.id.btnMenuPengumumanHome),
                findViewById(R.id.btnMenuSettingMenujuAdzan), findViewById(R.id.btnMenuSettingBedug),
                findViewById(R.id.btnMenuSettingAdzan), findViewById(R.id.btnMenuSettingDoaAdzan),
                findViewById(R.id.btnMenuTampilanMenujuIqomah), findViewById(R.id.btnMenuCountdownMenujuIqomah),
                findViewById(R.id.btnMenuSettingIqomah), findViewById(R.id.btnMenuTampilanWaktuSalat),
                findViewById(R.id.btnMenuCountdownWaktuSalat), findViewById(R.id.btnMenuInfoWaktuSalat));

        templateFragment                = new TemplateFragment();
        identitasFragment               = new IdentitasFragment();
        backgroundFragment              = new BackgroundFragment();
        informasiFragment               = new InformasiFragment();
        kotaDaerahFragment              = new KotaDaerahFragment();
        pengumumanFragment              = new PengumumanFragment();
        menujuAdzanFragment             = new MenujuAdzanFragment();
        bedugFragment                   = new BedugFragment();
        adzanFragment                   = new AdzanFragment();
        doaAdzanFragment                = new DoaAdzanFragment();
        menujuIqomahTampilanFragment    = new MenujuIqomahTampilanFragment();
        menujuIqomahCountdownFragment   = new MenujuIqomahCountdownFragment();
        iqomahFragment                  = new IqomahFragment();
        waktuSalatTampilanFragment      = new WaktuSalatTampilanFragment();
        waktuSalatCountdownFragment     = new WaktuSalatCountdownFragment();
        infoSalatFragment               = new WaktuSalatInformasiFragment();
        pengaturanUmumFragment          = new PengaturanUmumFragment();

        progressDialog                  = new ProgressDialog(this);
        ActionBar actionBar             = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        CekKoneksi();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pengaturan_umum :
                PengaturanUmum();
                break;
            case R.id.pengaturan_privasi :
                PengaturanPrivasi();
                break;
            case R.id.pengaturan_wifi :
                PengaturanWifi();
                break;
            case R.id.segarkan :
                Segarkan();
                break;
            case R.id.mulai_ulang :
                MulaiUlang();
                break;
            case R.id.matikan :
                Matikan();
                break;
            case R.id.keluar :
                startActivity(new Intent(MainActivity.this, CekWifiActivity.class));
                finish();
                break;
        }

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.close, menu);
        menuItemClose = menu.findItem(R.id.close);
        menuItemClose.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    public void PengaturanUmum(){
        OpenFragment(pengaturanUmumFragment, getString(R.string.PENGATURAN_UMUM));
    }

    public void PengaturanPrivasi(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pengaturan_privasi);

        if(dialog.getWindow() != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        }

        final MaterialEditText txtNamaPengguna          = dialog.findViewById(R.id.txtNamaPengguna);
        final CardView CVShowGantiPassword              = dialog.findViewById(R.id.CVShowGantiPassword);
        final TextView btnShowGantiPassword             = dialog.findViewById(R.id.btnShowGantiPassword);
        final TextInputLayout TILPasswordLama           = dialog.findViewById(R.id.TILPasswordLama);
        final MaterialEditText txtPasswordLama          = dialog.findViewById(R.id.txtPasswordLama);
        final TextInputLayout TILPasswordBaru           = dialog.findViewById(R.id.TILPasswordBaru);
        final MaterialEditText txtPasswordBaru          = dialog.findViewById(R.id.txtPasswordBaru);
        final TextInputLayout TILPasswordBaruUlangi     = dialog.findViewById(R.id.TILPasswordBaruUlangi);
        final MaterialEditText txtPasswordBaruUlangi    = dialog.findViewById(R.id.txtPassworBaruUlangi);
        final Button btnBatalPengaturanPrivasi          = dialog.findViewById(R.id.btnBatalPengaturanPrivasi);
        final Button btnSimpanPengaturanPrivasi         = dialog.findViewById(R.id.btnSimpanPengaturanPrivasi);

        PushDownAnim.setPushDownAnimTo(btnShowGantiPassword, btnBatalPengaturanPrivasi, btnSimpanPengaturanPrivasi);
        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                .addBodyParameter("Aksi", "DapatkanPengaturanPrivasi")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject   = response.getJSONObject(i);

                                NamaPengguna    = jsonObject.getString("Nama");
                                PasswordLama    = jsonObject.getString("Password");

                                txtNamaPengguna.setText(jsonObject.getString("Nama"));
                                progressDialog.dismiss();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        TILPasswordLama.setVisibility(View.GONE);
        TILPasswordBaru.setVisibility(View.GONE);
        TILPasswordBaruUlangi.setVisibility(View.GONE);
        btnShowGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.SlideInUp).duration(500).playOn(CVShowGantiPassword);
                YoYo.with(Techniques.SlideInUp).duration(500).playOn(TILPasswordLama);
                YoYo.with(Techniques.SlideInRight).duration(500).playOn(btnSimpanPengaturanPrivasi);

                btnSimpanPengaturanPrivasi.setText(getString(R.string.LANJUT));
                btnSimpanPengaturanPrivasi.setCompoundDrawablesWithIntrinsicBounds(btnSimpanPengaturanPrivasi.getContext().getDrawable(android.R.drawable.ic_media_next), null, null, null);
                CVShowGantiPassword.setCardBackgroundColor(Color.WHITE);
                CVShowGantiPassword.setVisibility(View.GONE);
                TILPasswordLama.setVisibility(View.VISIBLE);
                txtPasswordLama.requestFocus();
            }
        });

        btnBatalPengaturanPrivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSimpanPengaturanPrivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager   = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(btnSimpanPengaturanPrivasi.getText().toString().equalsIgnoreCase(getString(R.string.LANJUT))){
                    if(txtPasswordLama.getText() != null) {
                        if (txtPasswordLama.getText().toString().equals(PasswordLama)) {
                            YoYo.with(Techniques.SlideInUp).duration(500).playOn(TILPasswordLama);
                            YoYo.with(Techniques.SlideInUp).duration(500).playOn(TILPasswordBaru);
                            YoYo.with(Techniques.SlideInUp).duration(500).playOn(TILPasswordBaruUlangi);
                            YoYo.with(Techniques.SlideInRight).duration(500).playOn(btnSimpanPengaturanPrivasi);

                            btnSimpanPengaturanPrivasi.setText(getString(R.string.SIMPAN));
                            btnSimpanPengaturanPrivasi.setCompoundDrawablesWithIntrinsicBounds(btnSimpanPengaturanPrivasi.getContext().getDrawable(android.R.drawable.ic_menu_save), null, null, null);
                            TILPasswordLama.setVisibility(View.GONE);
                            TILPasswordBaru.setVisibility(View.VISIBLE);
                            TILPasswordBaruUlangi.setVisibility(View.VISIBLE);
                            txtPasswordBaru.requestFocus();
                        }
                        else if (txtPasswordLama.getText().toString().isEmpty()) {
                            YoYo.with(Techniques.Shake).duration(300).playOn(txtPasswordLama);
                            txtPasswordLama.setError(getString(R.string.Jangan_Kosong_));
                            txtPasswordLama.requestFocus();
                        }
                        else if (!txtPasswordLama.getText().toString().equals(PasswordLama)) {
                            YoYo.with(Techniques.Shake).duration(300).playOn(txtPasswordLama);
                            txtPasswordLama.setText("");
                            txtPasswordLama.setError(getString(R.string.Password_Salah_));
                            txtPasswordLama.requestFocus();
                        }
                    }
                }
                else if(btnSimpanPengaturanPrivasi.getText().toString().equalsIgnoreCase(getString(R.string.SIMPAN))) {
                    if (txtNamaPengguna.getText() != null && txtPasswordBaru.getText() != null && txtPasswordBaruUlangi.getText() != null && inputMethodManager != null) {
                        if (txtNamaPengguna.getText().toString().trim().isEmpty()) {
                            YoYo.with(Techniques.Shake).duration(300).playOn(txtNamaPengguna);
                            txtNamaPengguna.setError(getString(R.string.Jangan_Kosong_));
                            txtNamaPengguna.requestFocus();
                        }
                        else if (txtNamaPengguna.getText().toString().length() <= 2) {
                            YoYo.with(Techniques.Shake).duration(300).playOn(txtNamaPengguna);
                            txtNamaPengguna.setError(getString(R.string.Nama_Pengguna_Telalu_Singkat_));
                            txtNamaPengguna.requestFocus();
                        }
                        else if (txtPasswordBaru.getText().length() >= 1 && txtPasswordBaru.getText().length() <= 5) {
                            YoYo.with(Techniques.Shake).duration(300).playOn(txtPasswordBaru);
                            txtPasswordBaru.setError(getString(R.string.Telalu_Pendek_Minimal_6_Angka));
                            txtPasswordBaru.requestFocus();
                        }
                        else if (!txtPasswordBaru.getText().toString().equals(txtPasswordBaruUlangi.getText().toString())) {
                            YoYo.with(Techniques.Shake).duration(300).playOn(txtPasswordBaruUlangi);
                            txtPasswordBaruUlangi.setText("");
                            txtPasswordBaruUlangi.setError(getString(R.string.Password_Tidak_Sama_));
                            txtPasswordBaruUlangi.requestFocus();
                        }
                        else {
                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            if (txtNamaPengguna.getText().toString().equals(NamaPengguna) && txtPasswordBaru.getText().toString().equals(PasswordLama)) {
                                dialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, R.string.Tidak_Ada_Perubahan_yang_Disimpan, Toast.LENGTH_SHORT).show();
                            }
                            else if(txtNamaPengguna.getText().toString().equals(NamaPengguna) && txtPasswordBaru.getText().toString().isEmpty()){
                                dialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, R.string.Tidak_Ada_Perubahan_yang_Disimpan, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String Password;
                                if (txtPasswordBaru.getText().toString().isEmpty()) {
                                    Password = PasswordLama;
                                }
                                else {
                                    Password = txtPasswordBaru.getText().toString();
                                }

                                AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                                        .addBodyParameter("Aksi", "SimpanPengaturanPrivasi")
                                        .addBodyParameter("NamaPengguna", txtNamaPengguna.getText().toString())
                                        .addBodyParameter("PasswordBaru", Password)
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsString(new StringRequestListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equalsIgnoreCase("Tersimpan")) {
                                                    dialog.dismiss();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, R.string.Berhasil_Menyimpan_Perubahan, Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 1){
            PengaturanWifi();
        }
    }

    @SuppressWarnings("deprecation")
    public void PengaturanWifi(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.pengaturan_wifi);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
            }

            final MaterialEditText txtSSID          = dialog.findViewById(R.id.txtSSID);
            final MaterialEditText txtPasswordWifi  = dialog.findViewById(R.id.txtPasswordWifi);
            final Button btnSimpanPengaturanWifi    = dialog.findViewById(R.id.btnSimpanPengaturanWifi);
            final Button btnBatalPengaturanWifi     = dialog.findViewById(R.id.btnBatalPengaturanWifi);

            PushDownAnim.setPushDownAnimTo(btnSimpanPengaturanWifi, btnBatalPengaturanWifi);
            progressDialog.setMessage(getString(R.string.Memuat_Data));
            progressDialog.setCancelable(false);
            progressDialog.show();

            AndroidNetworking.post(getString(R.string.URL) + "Menu.php")
                    .addBodyParameter("Aksi", "DapatkanPengaturanWifi")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject   = new JSONObject(response);
                                String Sukses           = jsonObject.getString("Sukses");
                                JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                                if (Sukses.equals("1")) {
                                    for(int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object   = jsonArray.getJSONObject(i);
                                        String Nama_Setting = object.getString("Nama_Setting");

                                        if(Nama_Setting.equalsIgnoreCase("SSID")) {
                                            SSID            = object.getString("Isi_Setting");
                                            txtSSID.setText(object.getString("Isi_Setting"));
                                        }
                                        else if(Nama_Setting.equalsIgnoreCase("Password")) {
                                            PasswordWifi    = object.getString("Isi_Setting");
                                            txtPasswordWifi.setText(object.getString("Isi_Setting"));
                                        }
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            btnBatalPengaturanWifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnSimpanPengaturanWifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (txtSSID.getText() != null && txtPasswordWifi.getText() != null && inputMethodManager != null) {
                        if (txtSSID.getText().toString().trim().isEmpty()) {
                            txtSSID.setError(getString(R.string.Jangan_Kosong_));
                            txtSSID.requestFocus();
                        }
                        else if (txtSSID.getText().length() <= 4) {
                            txtSSID.setError(getString(R.string.Minimal_5_Karakter_));
                            txtSSID.requestFocus();
                        }
                        else if (txtPasswordWifi.getText().toString().trim().isEmpty()) {
                            txtPasswordWifi.setError(getString(R.string.Jangan_Kosong_));
                            txtPasswordWifi.requestFocus();
                        }
                        else if (txtPasswordWifi.getText().length() <= 7) {
                            txtPasswordWifi.setError(getString(R.string.Minimal_8_Karakter_));
                            txtPasswordWifi.requestFocus();
                        }
                        else {
                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            if(txtSSID.getText().toString().equals(SSID) && txtPasswordWifi.getText().toString().equals(PasswordWifi)) {
                                dialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, getString(R.string.Tidak_Ada_Perubahan_yang_Disimpan), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                                        .addBodyParameter("Aksi", "SimpanPengaturanWifi")
                                        .addBodyParameter("SSID", SSID)
                                        .addBodyParameter("PasswordWifi", PasswordWifi)
                                        .addBodyParameter("SSID_Update", txtSSID.getText().toString())
                                        .addBodyParameter("PasswordWifi_Update", txtPasswordWifi.getText().toString())
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsString(new StringRequestListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equalsIgnoreCase("Tersimpan")) {
                                                    File root   = Environment.getExternalStorageDirectory();
                                                    File dir    = new File(root.getAbsolutePath() + "/Cecenet");

                                                    if(!dir.exists()) {
                                                        if(dir.mkdirs()) {
                                                            BuatFile(dir, txtSSID.getText().toString(), txtPasswordWifi.getText().toString(), dialog);
                                                        }
                                                    }
                                                    else{
                                                        BuatFile(dir, txtSSID.getText().toString(), txtPasswordWifi.getText().toString(), dialog);
                                                    }
                                                }
                                                else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                }
            });
        }
    }

    public void BuatFile(File dir, String txtSSID, String txtPasswordWifi, Dialog dialog){
        File file = new File(dir, getString(R.string.Setting_Wifi) + ".txt");
        try{
            FileOutputStream fileOutputStream   = new FileOutputStream(file);
            PrintWriter printWriter             = new PrintWriter(fileOutputStream);
            printWriter.println("~~~" + getString(R.string.PENGATURAN_Wi_Fi) + "~~~\n");
            printWriter.println("SSID    \t :" + txtSSID);
            printWriter.println("Password\t :" + txtPasswordWifi);
            printWriter.flush();
            printWriter.close();

            dialog.dismiss();
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Segarkan(){
        progressDialog.setMessage(getString(R.string.Tunggu_Sebentar_));
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                .addBodyParameter("Aksi", "Segarkan")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Refresh")){
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, R.string.Berhasil_di_Segarkan, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void MulaiUlang(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle(getString(R.string.Konfirmasi));
        builder.setMessage(getString(R.string.Apakah_Yakin_Ingin_Memulai_Ulang_Perangkat_Raspberry_Pi_));
        builder.setIcon(R.drawable.mulai_ulang);
        builder.setCancelable(false);
        builder.setNegativeButton(getString(R.string.TIDAK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.YA), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage(getString(R.string.Tunggu_Sebentar_));
                progressDialog.setCancelable(false);
                progressDialog.show();

                AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                        .addBodyParameter("Aksi", "MulaiUlang")
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("Sukses")){
                                    progressDialog.dismiss();
                                    startActivity(new Intent(MainActivity.this, CekWifiActivity.class));
                                    finish();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, CekWifiActivity.class));
                                finish();
                            }
                        });
            }
        });
        builder.create().show();
    }

    public void Matikan(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle(getString(R.string.Konfirmasi));
        builder.setMessage(getString(R.string.Apakah_Yakin_Ingin_Mematikan_Perangkat_Raspberry_Pi_));
        builder.setIcon(R.drawable.matikan);
        builder.setCancelable(false);
        builder.setNegativeButton(getString(R.string.TIDAK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.YA), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage(getString(R.string.Tunggu_Sebentar_));
                progressDialog.setCancelable(false);
                progressDialog.show();

                AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                        .addBodyParameter("Aksi", "Matikan")
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("Sukses")){
                                    progressDialog.dismiss();
                                    startActivity(new Intent(MainActivity.this, CekWifiActivity.class));
                                    finish();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, CekWifiActivity.class));
                                finish();
                            }
                        });
            }
        });
        builder.create().show();
    }

    @Override
    public void onItemClick(View view) {
        circleMenu(view);
    }

    @Override
    public void onItemSelected(View view) {
        circleMenu(view);
    }

    public void circleMenu(View view){
        switch (view.getId()){
            case R.id.CicleHome :
                Home.setVisibility(View.VISIBLE);
                MenujuAdzan.setVisibility(View.GONE);
                Bedug.setVisibility(View.GONE);
                Adzan.setVisibility(View.GONE);
                DoaAdzan.setVisibility(View.GONE);
                MenujuIqomah.setVisibility(View.GONE);
                Iqomah.setVisibility(View.GONE);
                WaktuSalat.setVisibility(View.GONE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(Home);
                break;
            case R.id.CircleMenujuAdzan :
                Home.setVisibility(View.GONE);
                MenujuAdzan.setVisibility(View.VISIBLE);
                Bedug.setVisibility(View.GONE);
                Adzan.setVisibility(View.GONE);
                DoaAdzan.setVisibility(View.GONE);
                MenujuIqomah.setVisibility(View.GONE);
                Iqomah.setVisibility(View.GONE);
                WaktuSalat.setVisibility(View.GONE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(MenujuAdzan);
                break;
            case R.id.CicleBedug :
                Home.setVisibility(View.GONE);
                MenujuAdzan.setVisibility(View.GONE);
                Bedug.setVisibility(View.VISIBLE);
                Adzan.setVisibility(View.GONE);
                DoaAdzan.setVisibility(View.GONE);
                MenujuIqomah.setVisibility(View.GONE);
                Iqomah.setVisibility(View.GONE);
                WaktuSalat.setVisibility(View.GONE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(Bedug);
                break;
            case R.id.CicleAdzan :
                Home.setVisibility(View.GONE);
                MenujuAdzan.setVisibility(View.GONE);
                Bedug.setVisibility(View.GONE);
                Adzan.setVisibility(View.VISIBLE);
                DoaAdzan.setVisibility(View.GONE);
                MenujuIqomah.setVisibility(View.GONE);
                Iqomah.setVisibility(View.GONE);
                WaktuSalat.setVisibility(View.GONE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(Adzan);
                break;
            case R.id.CircleDoaAdzan :
                Home.setVisibility(View.GONE);
                MenujuAdzan.setVisibility(View.GONE);
                Bedug.setVisibility(View.GONE);
                Adzan.setVisibility(View.GONE);
                DoaAdzan.setVisibility(View.VISIBLE);
                MenujuIqomah.setVisibility(View.GONE);
                Iqomah.setVisibility(View.GONE);
                WaktuSalat.setVisibility(View.GONE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(DoaAdzan);
                break;
            case R.id.CicleMenujuIqomah :
                Home.setVisibility(View.GONE);
                MenujuAdzan.setVisibility(View.GONE);
                Bedug.setVisibility(View.GONE);
                Adzan.setVisibility(View.GONE);
                DoaAdzan.setVisibility(View.GONE);
                MenujuIqomah.setVisibility(View.VISIBLE);
                Iqomah.setVisibility(View.GONE);
                WaktuSalat.setVisibility(View.GONE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(MenujuIqomah);
                break;
            case R.id.CircleIqomah :
                Home.setVisibility(View.GONE);
                MenujuAdzan.setVisibility(View.GONE);
                Bedug.setVisibility(View.GONE);
                Adzan.setVisibility(View.GONE);
                DoaAdzan.setVisibility(View.GONE);
                MenujuIqomah.setVisibility(View.GONE);
                Iqomah.setVisibility(View.VISIBLE);
                WaktuSalat.setVisibility(View.GONE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(Iqomah);
                break;
            case R.id.CircleWaktuSalat :
                Home.setVisibility(View.GONE);
                MenujuAdzan.setVisibility(View.GONE);
                Bedug.setVisibility(View.GONE);
                Adzan.setVisibility(View.GONE);
                DoaAdzan.setVisibility(View.GONE);
                MenujuIqomah.setVisibility(View.GONE);
                Iqomah.setVisibility(View.GONE);
                WaktuSalat.setVisibility(View.VISIBLE);

                YoYo.with(Techniques.FadeInDown).duration(300).playOn(WaktuSalat);
                break;
        }

        Animation animation = new RotateAnimation(0, 360, view.getWidth()/2, view.getHeight()/2);
        animation.setDuration(250);
        view.startAnimation(animation);
    }

    public void btnMenuTemplateHome(View view) {
        OpenFragment(templateFragment, getString(R.string.TEMPLATE_HOME));
    }

    public void btnMenuIdentitasHome(View view) {
        OpenFragment(identitasFragment, getString(R.string.IDENTITAS_HOME));
    }

    public void btnMenuBackgroundHome(View view) {
        OpenFragment(backgroundFragment, getString(R.string.BACKGROUND_HOME));
    }

    public void btnMenuInformasiHome(View view) {
        OpenFragment(informasiFragment, getString(R.string.INFORMASI_HOME));
    }

    public void btnMenuKotaHome(View view) {
        OpenFragment(kotaDaerahFragment, getString(R.string.KOTA_DAERAH_HOME));
    }

    public void btnMenuPengumumanHome(View view) {
        OpenFragment(pengumumanFragment, getString(R.string.PENGUMUMAN_HOME));
    }

    public void btnMenuSettingMenujuAdzan(View view) {
        OpenFragment(menujuAdzanFragment, getString(R.string.TAMPILAN_MENUJU_ADZAN));
    }

    public void btnMenuSettingBedug(View view) {
        OpenFragment(bedugFragment, getString(R.string.TAMPILAN_BEDUG));
    }

    public void btnMenuSettingAdzan(View view) {
        OpenFragment(adzanFragment, getString(R.string.TAMPILAN_ADZAN));
    }

    public void btnMenuSettingDoaAdzan(View view) {
        OpenFragment(doaAdzanFragment, getString(R.string.TAMPILAN_DOA_ADZAN));
    }

    public void btnMenuTampilanMenujuIqomah(View view) {
        OpenFragment(menujuIqomahTampilanFragment, getString(R.string.TAMPILAN_MENUJU_IQOMAH));
    }

    public void btnMenuCountdownMenujuIqomah(View view) {
        OpenFragment(menujuIqomahCountdownFragment, getString(R.string.COUNTDOWN_MENUJU_IQOMAH));
    }

    public void btnMenuSettingIqomah(View view) {
        OpenFragment(iqomahFragment, getString(R.string.TAMPILAN_IQOMAH));
    }

    public void btnMenuTampilanWaktuSalat(View view) {
        OpenFragment(waktuSalatTampilanFragment, getString(R.string.TAMPILAN_WAKTU_SALAT));
    }

    public void btnMenuCountdownWaktuSalat(View view) {
        OpenFragment(waktuSalatCountdownFragment, getString(R.string.COUNTDOWN_WAKTU_SALAT));
    }

    public void btnMenuInfoWaktuSalat(View view) {
        OpenFragment(infoSalatFragment, getString(R.string.INFORMASI_WAKTU_SALAT));
    }

    public void OpenFragment(Fragment fragment, String actionBarTitle){
        ActionBar actionBar                     = getSupportActionBar();
        LinearLayout linearLayout               = findViewById(R.id.LinearLayout);
        FragmentManager fragmentManager         = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        if(actionBar != null){
            actionBar.setTitle(actionBarTitle);
            menuItemClose.setVisible(true);
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void CloseFragment(MenuItem item) {
        if(item.getItemId() == R.id.close ){
            CloseFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(getSupportFragmentManager().getFragments().size() > 0){
            CloseFragment();
        }
        else{
            if(mBackPressed + TIME_INTERVAL > System.currentTimeMillis()){
                super.onBackPressed();
                return;
            }
            else {
                Toast.makeText(MainActivity.this, getString(R.string.Tekan_Sekali_Lagi_untuk_Keluar), Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    public void CloseFragment(){
        ActionBar actionBar                     = getSupportActionBar();
        InputMethodManager inputMethodManager   = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        LinearLayout linearLayout               = findViewById(R.id.LinearLayout);

        if(actionBar != null && inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(navigationView.getWindowToken(), 0);

            for(int i = 0; i < getSupportFragmentManager().getFragments().size(); i++){
                Fragment fragment   = getSupportFragmentManager().getFragments().get(i);
                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    actionBar.setTitle(getString(R.string.app_name));
                    linearLayout.setVisibility(View.VISIBLE);
                    menuItemClose.setVisible(false);
                }
            }
        }

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
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
        startActivity(new Intent(MainActivity.this, CekWifiActivity.class));
        finish();
    }

    public void CekKoneksi(){
        AndroidNetworking.post(getString(R.string.URL) + "Database.php")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Koneksi Gagal !")){
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.Gagal_Terhubung_dengan_Raspberry), Toast.LENGTH_SHORT).show();
                        }
                        else if(response.isEmpty()){
                            NotifikasiTerhubung();
                        }
                        else{
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.Gagal_Menghubungkan_ke_Server), Toast.LENGTH_SHORT).show();
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
}