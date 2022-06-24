package com.example.cecenet.Pengaturan.Album;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.R;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class AlbumActivity extends AppCompatActivity {
    GridView GVAlbum;
    CardView CVALbumTampil;
    LinearLayout LLDaftarAlbum;
    TextView txtAlbumBackground, txtAlbumPengumuman;
    ImageView imgAlbumBackground, imgAlbumPengumuman, imgAlbumTampil;

    Album album;
    MyAdapterAlbum myAdapterAlbumBackground, myAdapterAlbumPengumuman;
    ArrayList<Album> albumArrayListBackground   = new ArrayList<>();
    ArrayList<Album> albumArrayListPengumuman   = new ArrayList<>();

    ProgressDialog progressDialog;
    MenuItem menuTambah, menuBackground, menuHapus;
    String ID_Background, Nama_Background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        LLDaftarAlbum       = findViewById(R.id.LLDaftarAlbum);
        imgAlbumBackground  = findViewById(R.id.imgAlbumBackground);
        imgAlbumPengumuman  = findViewById(R.id.imgAlbumPengumuman);
        imgAlbumTampil      = findViewById(R.id.imgAlbumTampil);
        GVAlbum             = findViewById(R.id.GVAlbum);
        txtAlbumBackground  = findViewById(R.id.txtAlbumBackground);
        txtAlbumPengumuman  = findViewById(R.id.txtAlbumPengumuman);
        CVALbumTampil       = findViewById(R.id.CVALbumTampil);
        progressDialog      = new ProgressDialog(this);

        GVAlbum.setVisibility(View.GONE);
        CVALbumTampil.setVisibility(View.GONE);

        myAdapterAlbumBackground    = new MyAdapterAlbum(this, albumArrayListBackground);
        myAdapterAlbumPengumuman    = new MyAdapterAlbum(this, albumArrayListPengumuman);

        albumBackground();
        albumPengumuman();
        tampilAlbumGridView();
        settingDaftarAlbum();
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.ALBUM_CECENET));
        PushDownAnim.setPushDownAnimTo(imgAlbumBackground, imgAlbumPengumuman);
    }

    private void albumBackground(){
        AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                .addBodyParameter("Aksi", "Album")
                .addBodyParameter("Album", "Background")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    @SuppressLint("SetTextI18n")
                    public void onResponse(String response) {
                        albumArrayListBackground.clear();
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object       = jsonArray.getJSONObject(i);

                                    String ID_Background    = object.getString("ID_Background");
                                    String Nama_Background  = object.getString("Nama_Background");
                                    boolean Tampil          = object.getString("Tampil").equalsIgnoreCase("Ya");

                                    album                   = new Album(ID_Background, Nama_Background, Tampil);
                                    albumArrayListBackground.add(album);
                                    myAdapterAlbumBackground.notifyDataSetChanged();
                                }
                                txtAlbumBackground.setText(getString(R.string.Album_Background) + "\n(" + albumArrayListBackground.size() + ")");
                                Picasso.with(AlbumActivity.this).load(getString(R.string.URL_Background) + albumArrayListBackground.get(0).getNama()).into(imgAlbumBackground);
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AlbumActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void albumPengumuman(){
        AndroidNetworking.post(getResources().getString(R.string.URL) + "Menu.php")
                .addBodyParameter("Aksi", "Album")
                .addBodyParameter("Album", "Pengumuman")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    @SuppressLint("SetTextI18n")
                    public void onResponse(String response) {
                        albumArrayListPengumuman.clear();
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object   = jsonArray.getJSONObject(i);

                                    if(!object.getString("Gambar_Pengumuman").isEmpty()){
                                        album           = new Album("", object.getString("Gambar_Pengumuman"), false);
                                        albumArrayListPengumuman.add(album);
                                        myAdapterAlbumPengumuman.notifyDataSetChanged();
                                    }
                                }
                                txtAlbumPengumuman.setText(getString(R.string.Album_Pengumuman) + "\n(" + albumArrayListPengumuman.size() + ")");
                                Picasso.with(AlbumActivity.this).load(getString(R.string.URL_Pengumuman) + albumArrayListPengumuman.get(0).getNama()).into(imgAlbumPengumuman);
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AlbumActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void tampilAlbumGridView(){
        boolean Background  = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("PILIH_GAMBAR_BACKGROUND", false);
        boolean Pengumuman  = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("PILIH_GAMBAR_PENGUMUMAN", false);

        if(Background){
            pilihAlbum(myAdapterAlbumBackground, true, false, albumArrayListBackground);
        }
        else if(Pengumuman){
            pilihAlbum(myAdapterAlbumPengumuman, false, true, albumArrayListPengumuman);
        }
    }

    private void pilihAlbum(MyAdapterAlbum myAdapterAlbum, boolean pilihBackground, boolean pilihPengumuman, final ArrayList<Album> albumArrayList){
        GVAlbum.setAdapter(myAdapterAlbum);
        GVAlbum.setVisibility(View.VISIBLE);
        LLDaftarAlbum.setVisibility(View.GONE);
        CVALbumTampil.setVisibility(View.GONE);
        myAdapterAlbum.notifyDataSetChanged();

        YoYo.with(Techniques.SlideInUp).duration(500).playOn(GVAlbum).isRunning();
        YoYo.with(Techniques.SlideInDown).duration(500).playOn(LLDaftarAlbum).isRunning();

        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR", false).apply();
        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_BACKGROUND", pilihBackground).apply();
        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_PENGUMUMAN", pilihPengumuman).apply();

        GVAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent   = new Intent();
                intent.putExtra("ALBUM", albumArrayList.get(position).getNama());
                setResult(2, intent);
                finish();
            }
        });
    }

    private void settingDaftarAlbum(){
        imgAlbumBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDaftarAlbum(myAdapterAlbumBackground, true, false, albumArrayListBackground);
            }
        });

        imgAlbumPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDaftarAlbum(myAdapterAlbumPengumuman, false, true, albumArrayListPengumuman);
            }
        });
    }

    private void onClickDaftarAlbum(MyAdapterAlbum myAdapterAlbum, boolean pilihBackground, final boolean pilihPengumuman, final ArrayList<Album> albumArrayList){
        GVAlbum.setAdapter(myAdapterAlbum);
        GVAlbum.setVisibility(View.VISIBLE);
        LLDaftarAlbum.setVisibility(View.GONE);
        CVALbumTampil.setVisibility(View.GONE);
        myAdapterAlbum.notifyDataSetChanged();

        YoYo.with(Techniques.SlideInUp).duration(500).playOn(GVAlbum).isRunning();
        YoYo.with(Techniques.SlideInDown).duration(500).playOn(LLDaftarAlbum).isRunning();

        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR", true).apply();
        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_BACKGROUND", pilihBackground).apply();
        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_PENGUMUMAN", pilihPengumuman).apply();

        menuHapus.setVisible(false);
        menuBackground.setVisible(false);

        if(!pilihPengumuman){
            menuTambah.setVisible(true);
        }
        else{
            menuTambah.setVisible(false);
        }

        GVAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GVAlbum.setVisibility(View.GONE);
                CVALbumTampil.setVisibility(View.VISIBLE);

                YoYo.with(Techniques.FadeIn).duration(500).playOn(GVAlbum).isRunning();
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(imgAlbumTampil).isRunning();

                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("TAMPIL_GAMBAR", true).apply();
                menuTambah.setVisible(false);

                if(!pilihPengumuman){
                    if(albumArrayList.get(position).isTampil()){
                        menuBackground.setTitle(getString(R.string.non_aktifkan));
                    }
                    else{
                        menuBackground.setTitle(getString(R.string.aktifkan));
                    }

                    Nama_Background = albumArrayList.get(position).getNama();
                    ID_Background   = albumArrayList.get(position).getID_Background();

                    menuHapus.setVisible(true);
                    menuBackground.setVisible(true);
                    Picasso.with(AlbumActivity.this).load(getString(R.string.URL_Background) + albumArrayList.get(position).getNama()).into(imgAlbumTampil);
                }
                else{
                    menuHapus.setVisible(false);
                    menuBackground.setVisible(false);
                    Picasso.with(AlbumActivity.this).load(getString(R.string.URL_Pengumuman) + albumArrayList.get(position).getNama()).into(imgAlbumTampil);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean Tampil      = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("TAMPIL_GAMBAR", false);
        boolean Background  = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("PILIH_GAMBAR_BACKGROUND", false);
        boolean Pengumuman  = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("PILIH_GAMBAR_PENGUMUMAN", false);

        if(!Background && !Pengumuman && !Tampil){
            getMenuInflater().inflate(R.menu.menu_album, menu);

            menuHapus       = menu.findItem(R.id.action_hapus);
            menuTambah      = menu.findItem(R.id.action_tambah);
            menuBackground  = menu.findItem(R.id.action_background);

            menuHapus.setVisible(false);
            menuTambah.setVisible(false);
            menuBackground.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_tambah :
                TambahGambar();

                break;
            case R.id.action_background :
                StatusBackground();

                break;
            case R.id.action_hapus :
                KonfirmasiHapus();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void TambahGambar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
            else{
                openGallery();
            }
        }
        else{
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 2){
            openGallery();
        }
    }

    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            try{
                final Uri BackgroundSelected    = data.getData();
                final String imgPath            = getPath(BackgroundSelected, this);
                final File imgFile              = new File(Objects.requireNonNull(imgPath));
                final String Nama_Background    = imgFile.getName();

                progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                progressDialog.setCancelable(false);
                progressDialog.show();

                AndroidNetworking.upload(getResources().getString(R.string.URL) + "Beranda.php")
                        .addMultipartParameter("Aksi", "UploadBackground")
                        .addMultipartParameter("Nama_Background", Nama_Background)
                        .addMultipartParameter("Tampil", "")
                        .addMultipartFile("file", imgFile)
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {
                                progressDialog.setMessage((bytesUploaded * 100) / totalBytes + " " + getString(R.string.Terupload) + (((int)(totalBytes / 1000) >= 1000) ? ((float)(totalBytes / 1000000)) + " MB)" : (float)(totalBytes / 1000) + " KB)"));
                            }
                        })
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("Terupload")){
                                    albumBackground();
                                }
                                else if(response.equalsIgnoreCase("File Sudah Ada")){
                                    Toast.makeText(AlbumActivity.this, getString(R.string.Background_Sudah_Ada), Toast.LENGTH_SHORT).show();
                                }
                                else if(response.equalsIgnoreCase("File Tidak Ada")){
                                    Toast.makeText(AlbumActivity.this, getString(R.string.Background_Tidak_Ada), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AlbumActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Toast.makeText(AlbumActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private String getPath(Uri uri, Activity activity){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor       = activity.managedQuery(uri, projection, null, null, null);

        if(cursor == null){
            return  null;
        }

        int kolom_index     = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        if(cursor.moveToFirst()){
            return cursor.getString(kolom_index);
        }

        return null;
    }

    private void StatusBackground(){
        if(menuBackground.getTitle().toString().equalsIgnoreCase(getString(R.string.aktifkan))){
            switchBackground("BackgroundAktif", "Background di Aktifkan", getString(R.string.non_aktifkan));
        }
        else{
            switchBackground("BackgroundArsip", "Background di Arsipkan", getString(R.string.aktifkan));
        }
    }

    private void switchBackground(String Status, final String balasan, final String setTitle){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", Status)
                .addBodyParameter("BackgroundAktifAction", ID_Background)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase(balasan)){
                                YoYo.with(Techniques.ZoomIn).duration(500).playOn(imgAlbumTampil);
                                menuBackground.setTitle(setTitle);
                            }
                            else{
                                Toast.makeText(AlbumActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AlbumActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void KonfirmasiHapus(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle(getString(R.string.Konfirmasi))
                .setMessage(getString(R.string.Apakah_Yakin_Ingin_Menghapus) + " " + getString(R.string.Background_ini_))
                .setIcon(R.drawable.hapus_black)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.TIDAK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.YA), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HapusAction();
                    }
                })
                .create().show();
    }

    private void HapusAction(){
        AndroidNetworking.post(getResources().getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "HapusBackground")
                .addBodyParameter("BackgroundAktifAction", ID_Background)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Background Terhapus")){
                                albumBackground();

                                menuHapus.setVisible(false);
                                menuTambah.setVisible(true);
                                menuBackground.setVisible(false);

                                GVAlbum.setVisibility(View.VISIBLE);
                                LLDaftarAlbum.setVisibility(View.GONE);
                                CVALbumTampil.setVisibility(View.GONE);

                                YoYo.with(Techniques.SlideInUp).duration(500).playOn(GVAlbum).isRunning();
                                YoYo.with(Techniques.FadeIn).duration(500).playOn(CVALbumTampil).isRunning();

                                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR", true).apply();
                                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("TAMPIL_GAMBAR", false).apply();
                                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_BACKGROUND", true).apply();
                                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_PENGUMUMAN", false).apply();

                                Toast.makeText(AlbumActivity.this, getString(R.string.Background_Terhapus), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AlbumActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AlbumActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();

        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR", false).apply();
        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("TAMPIL_GAMBAR", false).apply();
        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_BACKGROUND", false).apply();
        EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_PENGUMUMAN", false).apply();
    }

    @Override
    public void onBackPressed() {
        boolean Tampil      = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("TAMPIL_GAMBAR", false);
        boolean Gambar      = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("PILIH_GAMBAR", false);
        boolean Background  = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("PILIH_GAMBAR_BACKGROUND", false);
        boolean Pengumuman  = EasySettings.retrieveSettingsSharedPrefs(this).getBoolean("PILIH_GAMBAR_PENGUMUMAN", false);

        if((Background && Gambar) || (Pengumuman && Gambar)){
            if(Tampil){
                GVAlbum.setVisibility(View.VISIBLE);
                CVALbumTampil.setVisibility(View.GONE);

                YoYo.with(Techniques.SlideInUp).duration(500).playOn(GVAlbum).isRunning();
                YoYo.with(Techniques.FadeIn).duration(500).playOn(CVALbumTampil).isRunning();

                menuHapus.setVisible(false);
                menuBackground.setVisible(false);

                if(Pengumuman){
                    menuTambah.setVisible(false);
                }
                else{
                    menuTambah.setVisible(true);
                }

                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("TAMPIL_GAMBAR", false).apply();
            }
            else{
                GVAlbum.setVisibility(View.GONE);
                LLDaftarAlbum.setVisibility(View.VISIBLE);

                YoYo.with(Techniques.SlideInDown).duration(500).playOn(GVAlbum).isRunning();
                YoYo.with(Techniques.SlideInDown).duration(500).playOn(LLDaftarAlbum).isRunning();

                menuHapus.setVisible(false);
                menuTambah.setVisible(false);
                menuBackground.setVisible(false);

                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR", false).apply();
                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_BACKGROUND", false).apply();
                EasySettings.retrieveSettingsSharedPrefs(AlbumActivity.this).edit().putBoolean("PILIH_GAMBAR_PENGUMUMAN", false).apply();
            }
        }
        else{
            finish();
        }
    }
}