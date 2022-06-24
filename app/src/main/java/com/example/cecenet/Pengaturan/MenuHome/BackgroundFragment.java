package com.example.cecenet.Pengaturan.MenuHome;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter.AktifBackgroundFragment;
import com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter.Animasi;
import com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter.MyAdapterAnimasi;
import com.example.cecenet.R;
import com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class BackgroundFragment extends Fragment implements View.OnClickListener {
    private RadioGroup rgBackground;
    private RadioButton rbBackgroundGambar, rbBackgroundVideo;
    private LinearLayout LLSettingBackground, LLGambar, LLVideo;
    private MaterialEditText txtDurasiGantiBackground;
    private TextView btnVisibleSettingBackground;
    private CheckBox cbBackgroundSuara;
    private Spinner spinnerModeTampilan;
    private TabLayout TLBackground;
    private ViewPager VPBackground;

    private Animasi animasi;
    private MyAdapterAnimasi myAdapterAnimasi;
    private ArrayList<Animasi> AnimasiArrayList = new ArrayList<>();

    private String Tampil, ModeTampilan;
    private boolean VisibleBackground;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences, SPModeBackground;

    private String[] modeTampilan   = {"Semua", "Hanya Waktu Salat", "Full Screen"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                                       = inflater.inflate(R.layout.fragment_background, container, false);

        rgBackground                                    = view.findViewById(R.id.rgBackground);
        rbBackgroundGambar                              = view.findViewById(R.id.rbBackgroundGambar);
        rbBackgroundVideo                               = view.findViewById(R.id.rbBackgroundVideo);
        txtDurasiGantiBackground                        = view.findViewById(R.id.txtDurasiGantiBackground);
        Spinner spinnerAnimasibackground                = view.findViewById(R.id.spinnerAnimasiBackground);
        spinnerModeTampilan                             = view.findViewById(R.id.spinnerModeTampilan);
        Button btnSimpanSettingBackground               = view.findViewById(R.id.btnSimpanSettingBackground);
        LLSettingBackground                             = view.findViewById(R.id.LLSettingBackground);
        LLGambar                                        = view.findViewById(R.id.LLGambar);
        LLVideo                                         = view.findViewById(R.id.LLVideo);
        btnVisibleSettingBackground                     = view.findViewById(R.id.btnVisibleSettingBackground);
        cbBackgroundSuara                               = view.findViewById(R.id.cbBackgroundSuara);
        TLBackground                                    = view.findViewById(R.id.TLBackground);
        VPBackground                                    = view.findViewById(R.id.VPBackground);
        FloatingActionButton btnTambahBackground        = view.findViewById(R.id.btnTambahBackground);

        AktifBackgroundFragment aktifBackgroundFragment = new AktifBackgroundFragment();
        progressDialog                                  = new ProgressDialog(getContext());

        btnVisibleSettingBackground.setOnClickListener(this);
        btnSimpanSettingBackground.setOnClickListener(this);
        btnTambahBackground.setOnClickListener(this);

        TLBackground.addTab(TLBackground.newTab().setText(getString(R.string.AKTIF)));
        TLBackground.addTab(TLBackground.newTab().setText(getString(R.string.ARSIP)));
        TLBackground.setTabGravity(TabLayout.GRAVITY_FILL);

        if(getActivity() != null && getContext() != null){
            myAdapterAnimasi = new MyAdapterAnimasi(getContext(), AnimasiArrayList);
            spinnerAnimasibackground.setAdapter(myAdapterAnimasi);

            spinnerModeTampilan.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, modeTampilan));
            spinnerModeTampilan.setPadding(0, 0, 0, 0);

            VPBackground.setAdapter(new ViewPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), TLBackground.getTabCount()));
            VPBackground.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TLBackground));
            TLBackground.setupWithViewPager(VPBackground, true);

            FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.VPBackground, aktifBackgroundFragment);
            fragmentTransaction.commit();

            SPModeBackground    = getContext().getSharedPreferences("ModeBackground", Context.MODE_PRIVATE);
            sharedPreferences   = getContext().getSharedPreferences("VisibleSetting", Context.MODE_PRIVATE);

            if(sharedPreferences.getBoolean("VisibleSettingBackground", true)){
                VisibleBackground   = true;
                LLSettingBackground.setVisibility(View.VISIBLE);
            }
            else{
                VisibleBackground   = false;
                LLSettingBackground.setVisibility(View.GONE);
            }
        }

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        settingBackground();
        animasiBackground();
        pilihModeBackground();
        PushDownAnim.setPushDownAnimTo(btnVisibleSettingBackground, btnSimpanSettingBackground, btnTambahBackground);

        return view;
    }

    private void settingBackground(){
        txtDurasiGantiBackground.requestFocus();
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanSettingBackground")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if(object.getString("Nama_Setting").equalsIgnoreCase("Durasi_Ganti(Detik)")){
                                        txtDurasiGantiBackground.setText(object.getString("Isi_Setting"));
                                        txtDurasiGantiBackground.setSelection(txtDurasiGantiBackground.length());
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Gambar-Video")){
                                        if(object.getString("Isi_Setting").equalsIgnoreCase("Gambar")){
                                            rbBackgroundGambar.setChecked(true);
                                            SPModeBackground.edit().putString("Jenis", "Gambar").apply();
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("Video")){
                                            rbBackgroundVideo.setChecked(true);
                                            SPModeBackground.edit().putString("Jenis", "Video").apply();
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Suara_Video")){
                                        if(object.getString("Isi_Setting").equalsIgnoreCase("Ya")){
                                            cbBackgroundSuara.setChecked(true);
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("Tidak")){
                                            cbBackgroundSuara.setChecked(false);
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Mode")){
                                        if(object.getString("Isi_Setting").equalsIgnoreCase("-1")){
                                            spinnerModeTampilan.setSelection(0);
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("999")){
                                            spinnerModeTampilan.setSelection(1);
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("99999")){
                                            spinnerModeTampilan.setSelection(2);
                                        }
                                    }
                                }
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void animasiBackground(){
        AndroidNetworking.post(getResources().getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanAnimasiBackground")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        AnimasiArrayList.clear();
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object       = jsonArray.getJSONObject(i);

                                    String ID_Animasi       = object.getString("ID_Animasi");
                                    String Nama_Animasi     = object.getString("Nama_Animasi");
                                    String Kategori_Animasi = object.getString("Kategori_Animasi");
                                    String Animate          = object.getString("Animate");
                                    String Tampil           = object.getString("Tampil");

                                    boolean Tampil_Animasi  = Tampil.equalsIgnoreCase("Ya");

                                    animasi = new Animasi(ID_Animasi, Kategori_Animasi, Nama_Animasi, Animate, Tampil_Animasi);
                                    AnimasiArrayList.add(animasi);
                                    myAdapterAnimasi.notifyDataSetChanged();
                                }
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
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pilihModeBackground(){
        rgBackground.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Animation animation = new RotateAnimation(0, 180, btnVisibleSettingBackground.getWidth()/2, btnVisibleSettingBackground.getHeight()/2);
                animation.setDuration(250);
                btnVisibleSettingBackground.startAnimation(animation);
                txtDurasiGantiBackground.requestFocus();

                switch(checkedId){
                    case R.id.rbBackgroundGambar :
                        LLGambar.setVisibility(View.VISIBLE);
                        LLVideo.setVisibility(View.GONE);

                        YoYo.with(Techniques.FlipInX).duration(300).playOn(LLGambar);
                        YoYo.with(Techniques.FlipInX).duration(300).playOn(LLVideo);

                        break;
                    case R.id.rbBackgroundVideo :
                        LLGambar.setVisibility(View.GONE);
                        LLVideo.setVisibility(View.VISIBLE);

                        YoYo.with(Techniques.FlipInX).duration(300).playOn(LLGambar);
                        YoYo.with(Techniques.FlipInX).duration(300).playOn(LLVideo);

                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(getContext() != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null){
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

        switch(v.getId()){
            case R.id.btnVisibleSettingBackground :
                Animation animation = new RotateAnimation(0, 180, btnVisibleSettingBackground.getWidth()/2, btnVisibleSettingBackground.getHeight()/2);
                animation.setDuration(250);
                btnVisibleSettingBackground.startAnimation(animation);
                txtDurasiGantiBackground.requestFocus();

                if(VisibleBackground && getContext() != null){
                    btnVisibleSettingBackground.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.show));
                    YoYo.with(Techniques.SlideInUp).duration(300).playOn(LLSettingBackground);
                    LLSettingBackground.setVisibility(View.GONE);
                    VisibleBackground = false;

                    sharedPreferences.edit().putBoolean("VisibleSettingBackground", false).apply();
                }
                else{
                    if(getContext() != null){
                        btnVisibleSettingBackground.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hide));
                        YoYo.with(Techniques.SlideInDown).duration(300).playOn(LLSettingBackground);
                        LLSettingBackground.setVisibility(View.VISIBLE);
                        VisibleBackground = true;

                        sharedPreferences.edit().putBoolean("VisibleSettingBackground", true).apply();
                    }
                }

                break;
            case R.id.btnSimpanSettingBackground :
                if(txtDurasiGantiBackground.getText() != null){
                    if(txtDurasiGantiBackground.getText().toString().isEmpty()){
                        txtDurasiGantiBackground.setError(getString(R.string.Jangan_Kosong_));
                        txtDurasiGantiBackground.requestFocus();
                    }
                    else if(Integer.parseInt(txtDurasiGantiBackground.getText().toString()) < 5){
                        txtDurasiGantiBackground.setError(getString(R.string.Durasi_Ganti_Terlalu_Cepat));
                        txtDurasiGantiBackground.requestFocus();
                    }
                    else if(Integer.parseInt(txtDurasiGantiBackground.getText().toString()) > 3600){
                        txtDurasiGantiBackground.setError(getString(R.string.Durasi_Ganti_Terlalu_Lama));
                        txtDurasiGantiBackground.requestFocus();
                    }
                    else{
                        progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        if(spinnerModeTampilan.getSelectedItem().toString().equalsIgnoreCase(modeTampilan[0])){
                            ModeTampilan    = "-1";
                        }
                        else if(spinnerModeTampilan.getSelectedItem().toString().equalsIgnoreCase(modeTampilan[1])){
                            ModeTampilan    = "999";
                        }
                        else if(spinnerModeTampilan.getSelectedItem().toString().equalsIgnoreCase(modeTampilan[2])){
                            ModeTampilan    = "99999";
                        }

                        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                                .addBodyParameter("Aksi", "SimpanSettingBackground")
                                .addBodyParameter("DurasiGantiBackground", txtDurasiGantiBackground.getText().toString())
                                .addBodyParameter("AnimasiAktifAction", myAdapterAnimasi.AnimasiAktifAction.toString())
                                .addBodyParameter("AnimasiTidakAktifAction", myAdapterAnimasi.AnimasiTidakAktifAction.toString())
                                .addBodyParameter("ModeBackground", (rbBackgroundGambar.isChecked()) ? "Gambar" : "Video")
                                .addBodyParameter("SuaraVideo", (cbBackgroundSuara.isChecked()) ? "Ya" : "Tidak")
                                .addBodyParameter("ModeTampilan", ModeTampilan)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equalsIgnoreCase("Tersimpan")){
                                            animasiBackground();

                                            SPModeBackground.edit().putBoolean("Refresh", true).apply();
                                            SPModeBackground.edit().putString("Jenis", (rbBackgroundGambar.isChecked()) ? "Gambar" : "Video").apply();
                                            Toast.makeText(getContext(), R.string.Berhasil_Menyimpan_Perubahan, Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                break;
            case R.id.btnTambahBackground :
                progressDialog.setMessage(getString(R.string.Tunggu_Sebentar_));
                progressDialog.setCancelable(false);
                progressDialog.show();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        if(getActivity() != null) {
                            progressDialog.dismiss();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }
                    }
                    else{
                        progressDialog.dismiss();
                        openGallery();
                    }
                }
                else{
                    progressDialog.dismiss();
                    openGallery();
                }

                break;
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
        photoPickerIntent.setType((SPModeBackground.getString("Jenis", "Gambar").equals("Gambar")) ? "image/*" : "video/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && getActivity() != null && getContext() != null){
            try{
                final Uri BackgroundSelected    = data.getData();
                final String imgPath            = getPath(BackgroundSelected, getActivity());
                final File imgFile              = new File(Objects.requireNonNull(imgPath));
                final String Nama_Background    = imgFile.getName();

                final Dialog dialog             = new Dialog(getContext());
                dialog.setContentView(R.layout.tambah_background);

                if(dialog.getWindow() != null){
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.show();
                }

                final ImageView imgTambahBackground     = dialog.findViewById(R.id.imgTambahBackground);
                final VideoView vdTambahBackground      = dialog.findViewById(R.id.vdTambahBackground);
                final TextView txtDurasiVideo           = dialog.findViewById(R.id.txtDurasiVideo);
                final RadioGroup rgBackground           = dialog.findViewById(R.id.rgBackground);
                final RadioButton rbBackgroundAktif     = dialog.findViewById(R.id.rbBackgroundAktif);
                final RadioButton rbBackgroundArsip     = dialog.findViewById(R.id.rbBackgroundArsip);
                final Button btnSimpanBackground        = dialog.findViewById(R.id.btnSimpanBackground);
                final Button btnBatalSimpanBackground   = dialog.findViewById(R.id.btnBatalSimpanBackground);

                if(SPModeBackground.getString("Jenis", "Gambar").equals("Gambar")){
                    final Bitmap bitmap         = BitmapFactory.decodeFile(imgPath);
                    final int height            = (int)(bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                    final Bitmap bitmapSkala    = Bitmap.createScaledBitmap(bitmap, 512, height, true);

                    imgTambahBackground.setImageBitmap(bitmapSkala);
                    imgTambahBackground.setVisibility(View.VISIBLE);
                    vdTambahBackground.setVisibility(View.GONE);
                    txtDurasiVideo.setVisibility(View.GONE);
                }
                else{
                    progressDialog.setMessage(getString(R.string.Memuat_Video));
                    vdTambahBackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(final MediaPlayer mp) {
                            mp.setLooping(true);

                            final Handler handler   = new Handler();
                            Runnable runnable       = new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        String format;

                                        int durasi  = (mp.getDuration() / 1000) - (mp.getCurrentPosition() / 1000);
                                        int jam     = durasi / 3600;
                                        int menit   = (durasi / 60) - (jam * 60);
                                        int detik   = durasi - (jam * 3600) - (menit * 60);

                                        if(jam > 0){
                                            format  = String.format(Locale.getDefault(), "%02d:%02d:%02d", jam, menit, detik);
                                        }
                                        else{
                                            format  = String.format(Locale.getDefault(), "%02d:%02d", menit, detik);
                                        }

                                        txtDurasiVideo.setText(format);
                                        handler.postDelayed(this, 1000);
                                    }
                                    catch(IllegalStateException e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                    });

                    vdTambahBackground.setVideoURI(BackgroundSelected);
                    vdTambahBackground.requestFocus();
                    vdTambahBackground.start();
                    vdTambahBackground.isPlaying();

                    imgTambahBackground.setVisibility(View.GONE);
                    vdTambahBackground.setVisibility(View.VISIBLE);
                    txtDurasiVideo.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

                PushDownAnim.setPushDownAnimTo(btnBatalSimpanBackground, btnSimpanBackground);

                btnBatalSimpanBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnSimpanBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        if(rgBackground.getCheckedRadioButtonId() == rbBackgroundAktif.getId()){
                            Tampil  = "Ya";
                        }
                        else if(rgBackground.getCheckedRadioButtonId() == rbBackgroundArsip.getId()){
                            Tampil  = "Tidak";
                        }

                        AndroidNetworking.upload(getResources().getString(R.string.URL) + "Beranda.php")
                                .addMultipartParameter("Aksi", "UploadBackground")
                                .addMultipartParameter("Jenis", (SPModeBackground.getString("Jenis", "Gambar").equals("Gambar")) ? "Gambar" : "Video")
                                .addMultipartParameter("Nama_Background", Nama_Background)
                                .addMultipartParameter("Tampil", Tampil)
                                .addMultipartFile("file", imgFile)
                                .setPriority(Priority.HIGH)
                                .build()
                                .setUploadProgressListener(new UploadProgressListener() {
                                    @Override
                                    public void onProgress(long bytesUploaded, long totalBytes) {
                                        dialog.dismiss();
                                        progressDialog.setMessage((bytesUploaded * 100) / totalBytes + " " + getString(R.string.Terupload) + (((int)(totalBytes / 1000) >= 1000) ? ((float)(totalBytes / 1000000)) + " MB)" : (float)(totalBytes / 1000) + " KB)"));
                                    }
                                })
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equalsIgnoreCase("Terupload")){
                                            if(getActivity() != null && VPBackground.getAdapter() != null){
                                                VPBackground.setAdapter(new ViewPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), TLBackground.getTabCount()));
                                                VPBackground.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TLBackground));
                                                VPBackground.getAdapter().notifyDataSetChanged();

                                                if(Tampil.equalsIgnoreCase("Ya")){
                                                    TabLayout.Tab tab = TLBackground.getTabAt(0);

                                                    if(tab != null){
                                                        tab.select();
                                                    }
                                                }
                                                else{
                                                    TabLayout.Tab tab = TLBackground.getTabAt(1);

                                                    if(tab != null){
                                                        tab.select();
                                                    }
                                                }
                                            }
                                        }
                                        else if(response.equalsIgnoreCase("File Sudah Ada")){
                                            Toast.makeText(getContext(), getString(R.string.Background_Sudah_Ada), Toast.LENGTH_SHORT).show();
                                        }
                                        else if(response.equalsIgnoreCase("File Tidak Ada")){
                                            Toast.makeText(getContext(), getString(R.string.Background_Tidak_Ada), Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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
}