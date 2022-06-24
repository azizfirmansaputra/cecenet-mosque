package com.example.cecenet.Pengaturan.MenuBedug;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.cecenet.Pengaturan.Album.AlbumActivity;
import com.example.cecenet.R;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class BedugFragment extends Fragment implements View.OnClickListener {
    private ImageView imgBackgroundMenu;
    private Button btnWarna1Menu, btnWarna2Menu, btnWarna3Menu;
    private Spinner spinnerAnimasiBackgroundMenu, spinnerAnimasiBackgroundMenu2;
    private CheckBox cbSuaraMenu;

    private File imgFile;
    private ProgressDialog progressDialog;

    private int Simpan;
    private String Nama_Background;
    private String[] Animasi        = {"bounceIn", "fadeIn", "flip", "jello", "rollIn", "rotateIn", "zoomIn"};
    private String[] Animasi_Elemen = {"backIn", "bounceIn", "fadeIn", "fadeInBottom", "fadeInTop", "lightSpeedIn", "rotateInDown", "rotateInUp", "slideIn", "zoomIn"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                               = inflater.inflate(R.layout.fragment_tampilan, container, false);

        imgBackgroundMenu                       = view.findViewById(R.id.imgBackgroundMenu);
        Button btnGantiBackgroundMenu           = view.findViewById(R.id.btnGantiBackgroundMenu);
        btnWarna1Menu                           = view.findViewById(R.id.btnWarna1Menu);
        btnWarna2Menu                           = view.findViewById(R.id.btnWarna2Menu);
        btnWarna3Menu                           = view.findViewById(R.id.btnWarna3Menu);
        Button btnSimpanMenu                    = view.findViewById(R.id.btnSimpanMenu);
        TextView txtJudulAnimasi                = view.findViewById(R.id.txtJudulAnimasi);
        TextView txtJudulWarna1                 = view.findViewById(R.id.txtJudulWarna1);
        TextView txtJudulWarna2                 = view.findViewById(R.id.txtJudulWarna2);
        TextView txtJudulWarna3                 = view.findViewById(R.id.txtJudulWarna3);
        TextView txtJudulDurasi_Spinner         = view.findViewById(R.id.txtJudulDurasi_Spinner);
        TextView txtJudulSuara_Spinner          = view.findViewById(R.id.txtJudulSuara_Spinner);
        spinnerAnimasiBackgroundMenu            = view.findViewById(R.id.spinnerAnimasiBackgroundMenu);
        spinnerAnimasiBackgroundMenu2           = view.findViewById(R.id.spinnerAnimasiBackgroundMenu2);
        Spinner spinnerAnimasiBackgroundMenu3   = view.findViewById(R.id.spinnerAnimasiBackgroundMenu3);
        EditText txtDurasiTampilMenu            = view.findViewById(R.id.txtDurasiTampilMenu);
        cbSuaraMenu                             = view.findViewById(R.id.cbSuaraMenu);
        progressDialog                          = new ProgressDialog(getContext());

        btnGantiBackgroundMenu.setOnClickListener(this);
        btnWarna1Menu.setOnClickListener(this);
        btnWarna2Menu.setOnClickListener(this);
        btnWarna3Menu.setOnClickListener(this);
        btnSimpanMenu.setOnClickListener(this);

        txtJudulAnimasi.setText(R.string.Animasi_Background);
        txtJudulWarna1.setText(R.string.Text_Header);
        txtJudulWarna2.setText(R.string.Background_Header);
        txtJudulWarna3.setText(R.string.Elemen_Text);
        txtJudulDurasi_Spinner.setText(R.string.Animasi_Elemen);
        txtJudulSuara_Spinner.setText(R.string.Bunyikan_Suara_Bedug);

        spinnerAnimasiBackgroundMenu2.setVisibility(View.VISIBLE);
        spinnerAnimasiBackgroundMenu3.setVisibility(View.GONE);
        txtDurasiTampilMenu.setVisibility(View.GONE);

        if(getContext() != null){
            spinnerAnimasiBackgroundMenu.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, Animasi));
            spinnerAnimasiBackgroundMenu.setPadding(0, 0, 0, 0);

            spinnerAnimasiBackgroundMenu2.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, Animasi_Elemen));
            spinnerAnimasiBackgroundMenu2.setPadding(0, 0, 0, 0);
        }

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        settingBedug();
        pilihSuaraMenu();
        PushDownAnim.setPushDownAnimTo(btnGantiBackgroundMenu, btnSimpanMenu);

        return view;
    }

    private void settingBedug(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanSettingBedug")
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
                                    JSONObject object   = jsonArray.getJSONObject(i);
                                    String Nama_Setting = object.getString("Nama_Setting");

                                    if(Nama_Setting.equalsIgnoreCase("Background")){
                                        Simpan          = 0;
                                        Nama_Background = object.getString("Isi_Setting");
                                        Picasso.with(getContext()).load(getString(R.string.URL_Background) + object.getString("Isi_Setting")).into(imgBackgroundMenu);
                                    }
                                    else if(Nama_Setting.equalsIgnoreCase("Animasi_Background")){
                                        for(int j = 0; j < Animasi.length; j++){
                                            if(Animasi[j].equalsIgnoreCase(object.getString("Isi_Setting"))){
                                                spinnerAnimasiBackgroundMenu.setSelection(j);
                                            }
                                        }
                                    }
                                    else if(Nama_Setting.equalsIgnoreCase("Header_Warna_Text")){
                                        cekWarna(object, btnWarna1Menu);
                                    }
                                    else if(Nama_Setting.equalsIgnoreCase("Header_Background")){
                                        cekWarna(object, btnWarna2Menu);
                                    }
                                    else if(Nama_Setting.equalsIgnoreCase("Elemen_Warna_Text")){
                                        cekWarna(object, btnWarna3Menu);
                                    }
                                    else if(Nama_Setting.equalsIgnoreCase("Animasi_Elemen")){
                                        for(int j = 0; j < Animasi_Elemen.length; j++){
                                            if(Animasi_Elemen[j].equalsIgnoreCase(object.getString("Isi_Setting"))){
                                                spinnerAnimasiBackgroundMenu2.setSelection(j);
                                            }
                                        }
                                    }
                                    else if(Nama_Setting.equalsIgnoreCase("Suara")){
                                        if(object.getString("Isi_Setting").equalsIgnoreCase("Ya")){
                                            cbSuaraMenu.setText(getString(R.string.ya));
                                            cbSuaraMenu.setChecked(true);
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("Tidak")){
                                            cbSuaraMenu.setText(getString(R.string.tidak));
                                            cbSuaraMenu.setChecked(false);
                                        }
                                    }
                                }
                                progressDialog.dismiss();
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

    private void cekWarna(JSONObject object, Button btnWarna){
        int Color_Warna;
        try{
            if(object.getString("Isi_Setting").length() <= 0){
                Color_Warna     = 0x00FFFFFF;
            }
            else if(object.getString("Isi_Setting").length() <= 7){
                String Warna    = object.getString("Isi_Setting").replace("#", "FF");
                Color_Warna     = Integer.parseInt(Warna.substring(2), 16) + (Integer.parseInt(Warna.substring(0, 2), 16) << 24);
            }
            else{
                String Warna    = object.getString("Isi_Setting").substring(7) + object.getString("Isi_Setting").substring(1, 7);
                Color_Warna     = Integer.parseInt(Warna.substring(2), 16) + (Integer.parseInt(Warna.substring(0, 2), 16) << 24);
            }

            btnWarna.setBackgroundColor(Color_Warna);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void pilihSuaraMenu(){
        cbSuaraMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbSuaraMenu.setText(getString(R.string.ya));
                }
                else{
                    cbSuaraMenu.setText(getString(R.string.tidak));
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
            case R.id.btnGantiBackgroundMenu :
                progressDialog.setMessage(getString(R.string.Tunggu_Sebentar_));
                progressDialog.setCancelable(false);
                progressDialog.show();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(getContext() != null && getActivity() != null){
                        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            progressDialog.dismiss();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }
                        else{
                            progressDialog.dismiss();
                            pilihBackgroundIntent(getString(R.string.Pilih_Background_dari));
                        }
                    }
                }
                else{
                    progressDialog.dismiss();
                    pilihBackgroundIntent(getString(R.string.Pilih_Background_dari));
                }

                break;
            case R.id.btnWarna1Menu :
                dialogPickerView(btnWarna1Menu, getString(R.string.PILIH_WARNA_HEADER_TEXT), "WARNA_HEADER_TEXT", false);

                break;
            case R.id.btnWarna2Menu :
                dialogPickerView(btnWarna2Menu, getString(R.string.PILIH_WARNA_BACKGROUND_HEADER), "WARNA_BACKGROUND_HEADER", true);

                break;
            case R.id.btnWarna3Menu :
                dialogPickerView(btnWarna3Menu, getString(R.string.PILIH_WARNA_ELEMEN_TEXT), "WARNA_ELEMEN_TEXT", false);

                break;
            case R.id.btnSimpanMenu :
                simpanMenu();

                break;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 2){
            pilihBackgroundIntent(getString(R.string.Pilih_Background_dari));
        }
    }

    private void pilihBackgroundIntent(String Judul){
        Intent Galeri       = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent Cecenet      = new Intent(getContext(), AlbumActivity.class);
        Intent pilihIntent  = new Intent(Intent.ACTION_CHOOSER);

        pilihIntent.putExtra(Intent.EXTRA_INTENT, Galeri);
        pilihIntent.putExtra(Intent.EXTRA_TITLE, Judul);
        pilihIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{Cecenet});

        EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).edit().putBoolean("PILIH_GAMBAR_BACKGROUND", true).apply();
        startActivityForResult(Intent.createChooser(pilihIntent, getString(R.string.PILIH)), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).edit().putBoolean("PILIH_GAMBAR_BACKGROUND", false).apply();
        EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).edit().putBoolean("PILIH_GAMBAR_PENGUMUMAN", false).apply();

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && getActivity() != null && getContext() != null){
            try{
                final Uri BackgroundSelected    = data.getData();
                final String imgPath            = getPath(BackgroundSelected, getActivity());
                imgFile                         = new File(Objects.requireNonNull(imgPath));
                Nama_Background                 = imgFile.getName();
                Simpan                          = 1;

                final Bitmap bitmap             = BitmapFactory.decodeFile(imgPath);
                final int height                = (int)(bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                final Bitmap bitmapSkala        = Bitmap.createScaledBitmap(bitmap, 512, height, true);

                imgBackgroundMenu.setColorFilter(Color.TRANSPARENT);
                imgBackgroundMenu.setImageBitmap(bitmapSkala);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(resultCode == 2){
            Nama_Background                     = Objects.requireNonNull(data).getStringExtra("ALBUM");
            Simpan                              = 2;

            imgBackgroundMenu.setColorFilter(Color.TRANSPARENT);
            Picasso.with(getContext()).load(getString(R.string.URL_Background) + Nama_Background).into(imgBackgroundMenu);
        }
    }

    @SuppressWarnings("deprecation")
    private String getPath(Uri uri, Activity activity){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor       = activity.managedQuery(uri, projection, null, null, null);

        if(cursor == null){
            return null;
        }

        int kolom_index     = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        if(cursor.moveToFirst()){
            return cursor.getString(kolom_index);
        }

        return null;
    }

    private void dialogPickerView(final Button btnWarna, final String txtJudul, final String preferenceName, Boolean AlphaSlideBar){
        if(getContext() != null){
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.color_picker);

            if(dialog.getWindow() != null){
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

            final TextView txtJudulCPV  = dialog.findViewById(R.id.txtJudulCPV);
            final LinearLayout LLCPV    = dialog.findViewById(R.id.LLCPV);
            final ColorPickerView CPV   = dialog.findViewById(R.id.CPV);
            final AlphaSlideBar ASB     = dialog.findViewById(R.id.ASB);

            final Button btnSampel1     = dialog.findViewById(R.id.btnSampel1);
            final Button btnSampel2     = dialog.findViewById(R.id.btnSampel2);
            final Button btnSampel3     = dialog.findViewById(R.id.btnSampel3);
            final Button btnSampel4     = dialog.findViewById(R.id.btnSampel4);
            final Button btnSampel5     = dialog.findViewById(R.id.btnSampel5);
            final Button btnSampel6     = dialog.findViewById(R.id.btnSampel6);
            final Button btnSampel7     = dialog.findViewById(R.id.btnSampel7);
            final Button btnSampel8     = dialog.findViewById(R.id.btnSampel8);
            final Button btnSampel9     = dialog.findViewById(R.id.btnSampel9);
            final Button btnSampel10    = dialog.findViewById(R.id.btnSampel10);

            final Button btnCPVBatal    = dialog.findViewById(R.id.btnCPVBatal);
            final Button btnCPVYa       = dialog.findViewById(R.id.btnCPVYa);

            txtJudulCPV.setText(txtJudul);
            CPV.setPreferenceName(preferenceName);
            CPV.setActionMode(ActionMode.ALWAYS);
            PushDownAnim.setPushDownAnimTo(btnCPVBatal, btnCPVYa);

            if(AlphaSlideBar){
                CPV.attachAlphaSlider(ASB);
                ASB.setVisibility(View.VISIBLE);
            }
            else{
                ASB.setVisibility(View.GONE);
            }

            CPV.setColorListener(new ColorEnvelopeListener() {
                @Override
                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                    LLCPV.setBackgroundColor(envelope.getColor());
                }
            });

            btnSampel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel1.getBackground()).getColor());
                }
            });
            btnSampel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel2.getBackground()).getColor());
                }
            });
            btnSampel3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel3.getBackground()).getColor());
                }
            });
            btnSampel4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel4.getBackground()).getColor());
                }
            });
            btnSampel5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel5.getBackground()).getColor());
                }
            });
            btnSampel6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel6.getBackground()).getColor());
                }
            });
            btnSampel7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel7.getBackground()).getColor());
                }
            });
            btnSampel8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel8.getBackground()).getColor());
                }
            });
            btnSampel9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel9.getBackground()).getColor());
                }
            });
            btnSampel10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LLCPV.setBackgroundColor(((ColorDrawable) btnSampel10.getBackground()).getColor());
                }
            });

            btnCPVBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnCPVYa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnWarna.setBackgroundColor(((ColorDrawable) LLCPV.getBackground()).getColor());
                    dialog.dismiss();
                }
            });
        }
    }

    private void simpanMenu(){
        String Text_Header          = "#" + (Integer.toHexString(((ColorDrawable)btnWarna1Menu.getBackground()).getColor())).substring(2).toUpperCase();
        String Elemen_Text          = "#" + (Integer.toHexString(((ColorDrawable)btnWarna3Menu.getBackground()).getColor())).substring(2).toUpperCase();

        String Background_Header    = (Integer.toHexString(((ColorDrawable)btnWarna2Menu.getBackground()).getColor()).length() == 6) ? "#" +
                                        Integer.toHexString(((ColorDrawable)btnWarna2Menu.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                        Integer.toHexString(((ColorDrawable)btnWarna2Menu.getBackground()).getColor()).substring(2).toUpperCase() +
                                        Integer.toHexString(((ColorDrawable)btnWarna2Menu.getBackground()).getColor()).substring(0, 2).toUpperCase();

        progressDialog.setMessage(getString(R.string.Menyimpan_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(Simpan == 0 || Simpan == 2){
            AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                    .addBodyParameter("Aksi", "SimpanSettingBedug")
                    .addBodyParameter("Gambar", "Cecenet")
                    .addBodyParameter("Nama_Background", Nama_Background)
                    .addBodyParameter("Animasi_Background", spinnerAnimasiBackgroundMenu.getSelectedItem().toString())
                    .addBodyParameter("Header_Warna_Text", Text_Header)
                    .addBodyParameter("Header_Background", Background_Header)
                    .addBodyParameter("Elemen_Warna_Text", Elemen_Text)
                    .addBodyParameter("Animasi_Elemen", spinnerAnimasiBackgroundMenu2.getSelectedItem().toString())
                    .addBodyParameter("Suara", (cbSuaraMenu.isChecked()) ? "Ya" : "Tidak")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Tersimpan")){
                                Toast.makeText(getContext(), getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
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
        else if(Simpan == 1){
            AndroidNetworking.upload(getString(R.string.URL) + "Beranda.php")
                    .addMultipartParameter("Aksi", "SimpanSettingBedug")
                    .addMultipartParameter("Gambar", "Galeri")
                    .addMultipartParameter("Nama_Background", Nama_Background)
                    .addMultipartParameter("Animasi_Background", spinnerAnimasiBackgroundMenu.getSelectedItem().toString())
                    .addMultipartParameter("Header_Warna_Text", Text_Header)
                    .addMultipartParameter("Header_Background", Background_Header)
                    .addMultipartParameter("Elemen_Warna_Text", Elemen_Text)
                    .addMultipartParameter("Animasi_Elemen", spinnerAnimasiBackgroundMenu2.getSelectedItem().toString())
                    .addMultipartParameter("Suara", (cbSuaraMenu.isChecked()) ? "Ya" : "Tidak")
                    .addMultipartFile("Background", imgFile)
                    .setPriority(Priority.MEDIUM)
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
                            if(response.equalsIgnoreCase("Tersimpan")){
                                Toast.makeText(getContext(), getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
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
}