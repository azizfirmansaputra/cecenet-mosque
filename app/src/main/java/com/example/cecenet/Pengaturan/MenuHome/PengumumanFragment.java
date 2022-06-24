package com.example.cecenet.Pengaturan.MenuHome;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.Pengaturan.Album.AlbumActivity;
import com.example.cecenet.Pengaturan.HapusGambar;
import com.example.cecenet.Pengaturan.MenuHome.PengumumanAdapter.MyAdapterPengumuman;
import com.example.cecenet.Pengaturan.MenuHome.PengumumanAdapter.Pengumuman;
import com.example.cecenet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.rengwuxian.materialedittext.MaterialEditText;
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
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class PengumumanFragment extends Fragment implements View.OnClickListener {
    private RadioGroup rgPengumuman, rgPengumumanMode;
    private LinearLayout LLSettingPengumuman;
    private RadioButton rbPengumumanAktif, rbPengumumanTidakAktif, rbPengumumanModeInsert, rbPengumumanModeFull;
    private CheckBox cbPengumumanDelay;
    private EditText txtDurasiMenitPengumuman, txtDurasiDetikPengumuman, txtIntervalMenitPengumuman, txtIntervalDetikPengumuman;
    private Spinner spinnerPosisiPengumuman, spinnerUrutanPengumuman, spinnerAnimasiPengumuman;
    private CardView CVSettingPengumumanInsert, CVSettingPengumumanFull;
    private Button btnBackgroundPengumuman, btnWarnaJudulPengumuman, btnWarnaIsiPengumuman, btnWarnaHeaderPengumuman;
    private TextView btnVisibleSettingPengumuman;
    private SwipeRefreshLayout SRLPengumuman;
    private ListView LVPengumuman;

    private Dialog dialog;
    private TextView txtJudulTambahEditPengumuman;
    private ImageView imgTambahEditPengumuman;
    private MaterialEditText txtTambahEditJudulPengumuman, txtTambahEditIsiPengumuman;
    private RadioGroup rgTambahEditPengumuman;
    private RadioButton rbTambahEditPengumumanAktif, rbTambahEditPengumumanArsip;
    private Button btnBatalSimpanPengumuman, btnSimpanPengumuman;

    private Pengumuman pengumuman;
    private MyAdapterPengumuman myAdapterPengumuman;
    private static ArrayList<Pengumuman> PengumumanArrayList    = new ArrayList<>();
    private ArrayList<String> PengumumanAktifAction             = new ArrayList<>();

    private File imgFile;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    private int Simpan;
    private boolean VisiblePengumuman, Tampil;
    private String IDPengumuman, JudulPengumuman, Nama_Background, IsiPengumuman;

    private String[] posisiPengumuman   = {"Kanan", "Kiri", "Tengah"};
    private String[] urutanPengumuman   = {"Gambar", "Gambar-Text", "Text", "Text-Gambar"};
    private String[] animasiPengumuman  = {"backInDown", "backInLeft", "backInRight", "backInUp", "bounceIn", "bounceInDown", "bounceInLeft", "bounceInRight", "bounceInUp",
                                            "fadeIn", "fadeInBottomLeft", "fadeInBottomRight", "fadeInDown", "fadeInDownBig", "fadeInLeft", "fadeInLeftBig", "fadeInRight",
                                            "fadeInRightBig", "fadeInTopLeft", "fadeInTopRight", "fadeInUp", "fadeInUpBig", "flipInX", "flipInY", "lightSpeedInLeft",
                                            "lightSpeedInRight", "rollIn", "rotateIn", "rotateInDownLeft", "rotateInDownRight", "rotateInUpLeft", "rotateInUpRight",
                                            "slideInDown", "slideInLeft", "slideInRight", "slideInUp", "zoomIn", "zoomInDown", "zoomInLeft", "zoomInRight", "zoomInUp"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                                   = inflater.inflate(R.layout.fragment_pengumuman, container, false);

        rgPengumuman                                = view.findViewById(R.id.rgPengumuman);
        rgPengumumanMode                            = view.findViewById(R.id.rgPengumumanMode);
        LLSettingPengumuman                         = view.findViewById(R.id.LLSettingPengumuman);
        rbPengumumanAktif                           = view.findViewById(R.id.rbPengumumanAktif);
        rbPengumumanTidakAktif                      = view.findViewById(R.id.rbPengumumanTidakAktif);
        rbPengumumanModeInsert                      = view.findViewById(R.id.rbPengumumanModeInsert);
        rbPengumumanModeFull                        = view.findViewById(R.id.rbPengumumanModeFull);
        cbPengumumanDelay                           = view.findViewById(R.id.cbPengumumanDelay);
        txtDurasiMenitPengumuman                    = view.findViewById(R.id.txtDurasiMenitPengumuman);
        txtDurasiDetikPengumuman                    = view.findViewById(R.id.txtDurasiDetikPengumuman);
        txtIntervalMenitPengumuman                  = view.findViewById(R.id.txtIntervalMenitPengumuman);
        txtIntervalDetikPengumuman                  = view.findViewById(R.id.txtIntervalDetikPengumuman);
        spinnerPosisiPengumuman                     = view.findViewById(R.id.spinnerPosisiPengumuman);
        spinnerUrutanPengumuman                     = view.findViewById(R.id.spinnerUrutanPengumuman);
        spinnerAnimasiPengumuman                    = view.findViewById(R.id.spinnerAnimasiPengumuman);
        CVSettingPengumumanInsert                   = view.findViewById(R.id.CVSettingPengumumanInsert);
        CVSettingPengumumanFull                     = view.findViewById(R.id.CVSettingPengumumanFull);
        btnBackgroundPengumuman                     = view.findViewById(R.id.btnBackgroundPengumuman);
        btnWarnaJudulPengumuman                     = view.findViewById(R.id.btnWarnaJudulPengumuman);
        btnWarnaIsiPengumuman                       = view.findViewById(R.id.btnWarnaIsiPengumuman);
        Button btnBGGambarPengumuman                = view.findViewById(R.id.btnBGGambarPengumuman);
        btnWarnaHeaderPengumuman                    = view.findViewById(R.id.btnWarnaHeaderPengumuman);
        Button btnSimpanSettingPengumuman           = view.findViewById(R.id.btnSimpanSettingPengumuman);
        btnVisibleSettingPengumuman                 = view.findViewById(R.id.btnVisibleSettingPengumuman);
        SRLPengumuman                               = view.findViewById(R.id.SRLPengumuman);
        LVPengumuman                                = view.findViewById(R.id.LVPengumuman);
        FloatingActionButton btnTambahPengumuman    = view.findViewById(R.id.btnTambahPengumuman);
        progressDialog                              = new ProgressDialog(getContext());

        btnBackgroundPengumuman.setOnClickListener(this);
        btnWarnaJudulPengumuman.setOnClickListener(this);
        btnWarnaIsiPengumuman.setOnClickListener(this);
        btnBGGambarPengumuman.setOnClickListener(this);
        btnWarnaHeaderPengumuman.setOnClickListener(this);
        btnSimpanSettingPengumuman.setOnClickListener(this);
        btnVisibleSettingPengumuman.setOnClickListener(this);
        btnTambahPengumuman.setOnClickListener(this);

        if(getContext() != null){
            spinnerPosisiPengumuman.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, posisiPengumuman));
            spinnerPosisiPengumuman.setPadding(0, 0, 0, 0);

            spinnerUrutanPengumuman.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, urutanPengumuman));
            spinnerUrutanPengumuman.setPadding(0, 0, 0, 0);

            spinnerAnimasiPengumuman.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, animasiPengumuman));
            spinnerAnimasiPengumuman.setPadding(0, 0, 0, 0);

            sharedPreferences   = getContext().getSharedPreferences("VisibleSetting", Context.MODE_PRIVATE);

            myAdapterPengumuman = new MyAdapterPengumuman(getContext(), PengumumanArrayList);
            LVPengumuman.setAdapter(myAdapterPengumuman);
            LVPengumuman.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            LVPengumuman.setMultiChoiceModeListener(modeListener);
            LVPengumuman.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

            dialog              = new Dialog(getContext());
            dialog.setContentView(R.layout.tambah_edit_pengumuman);

            if(dialog.getWindow() != null){
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
            }
        }

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        SRLPengumuman.setRefreshing(true);
        SRLPengumuman.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listPengumuman();
            }
        });

        listPengumuman();
        settingPengumuman();
        pilihModePengumuman();
        pilihStatusPengumuman();
        validasiDurasiInterval();
        visibleSettingPengumuman();
        PushDownAnim.setPushDownAnimTo(btnBGGambarPengumuman, btnSimpanSettingPengumuman, btnVisibleSettingPengumuman, btnTambahPengumuman);

        txtJudulTambahEditPengumuman    = dialog.findViewById(R.id.txtJudulTambahEditPengumuman);
        imgTambahEditPengumuman         = dialog.findViewById(R.id.imgTambahEditPengumuman);
        txtTambahEditJudulPengumuman    = dialog.findViewById(R.id.txtTambahEditJudulPengumuman);
        txtTambahEditIsiPengumuman      = dialog.findViewById(R.id.txtTambahEditIsiPengumuman);
        rgTambahEditPengumuman          = dialog.findViewById(R.id.rgTambahEditPengumuman);
        rbTambahEditPengumumanAktif     = dialog.findViewById(R.id.rbTambahEditPengumumanAktif);
        rbTambahEditPengumumanArsip     = dialog.findViewById(R.id.rbTambahEditPengumumanArsip);
        btnBatalSimpanPengumuman        = dialog.findViewById(R.id.btnBatalSimpanPengumuman);
        btnSimpanPengumuman             = dialog.findViewById(R.id.btnSimpanPengumuman);

        txtTambahEditJudulPengumuman.setSingleLine();
        PushDownAnim.setPushDownAnimTo(imgTambahEditPengumuman, btnBatalSimpanPengumuman, btnSimpanPengumuman);

        return view;
    }

    private void settingPengumuman(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanSettingPengumuman")
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

                                    if(object.getString("Nama_Setting").equalsIgnoreCase("Aktifkan")){
                                        if(object.getString("Isi_Setting").equalsIgnoreCase("Ya")){
                                            rbPengumumanAktif.setChecked(true);
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("Tidak")){
                                            rbPengumumanTidakAktif.setChecked(true);
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Mode")){
                                        if(object.getString("Isi_Setting").equalsIgnoreCase("Insert_Home")){
                                            rbPengumumanModeInsert.setChecked(true);
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("Full_Screen")){
                                            rbPengumumanModeFull.setChecked(true);
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Delay")){
                                        if(object.getString("Isi_Setting").equalsIgnoreCase("Ya")){
                                            cbPengumumanDelay.setChecked(true);
                                        }
                                        else if(object.getString("Isi_Setting").equalsIgnoreCase("Tidak")){
                                            cbPengumumanDelay.setChecked(false);
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Durasi")){
                                        txtDurasiMenitPengumuman.setText(object.getString("Isi_Setting").substring(0, 2));
                                        txtDurasiDetikPengumuman.setText(object.getString("Isi_Setting").substring(3));
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Interval")){
                                        txtIntervalMenitPengumuman.setText(object.getString("Isi_Setting").substring(0, 2));
                                        txtIntervalDetikPengumuman.setText(object.getString("Isi_Setting").substring(3));
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Insert_Home")){
                                        for(int j = 0; j < posisiPengumuman.length; j++){
                                            if(posisiPengumuman[j].equalsIgnoreCase(object.getString("Isi_Setting"))){
                                                spinnerPosisiPengumuman.setSelection(j);
                                            }
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Background_Warna")){
                                        cekWarna(object, btnBackgroundPengumuman);
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Warna_Text_Title")){
                                        cekWarna(object, btnWarnaJudulPengumuman);
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Warna_Text_Isi")){
                                        cekWarna(object, btnWarnaIsiPengumuman);
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Full_Screen")){
                                        for(int j = 0; j < urutanPengumuman.length; j++){
                                            if(urutanPengumuman[j].equalsIgnoreCase(object.getString("Isi_Setting"))){
                                                spinnerUrutanPengumuman.setSelection(j);
                                            }
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Animasi")){
                                        for(int j = 0; j < animasiPengumuman.length; j++){
                                            if(animasiPengumuman[j].equalsIgnoreCase(object.getString("Isi_Setting"))){
                                                spinnerAnimasiPengumuman.setSelection(j);
                                            }
                                        }
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Background_Header")){
                                        cekWarna(object, btnWarnaHeaderPengumuman);
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Background_Gambar")){
                                        Simpan          = 0;
                                        Nama_Background = object.getString("Isi_Setting");
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

    private void listPengumuman(){
        AndroidNetworking.post(getResources().getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanPengumuman")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        PengumumanArrayList.clear();
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object           = jsonArray.getJSONObject(i);

                                    String ID_Pengumuman        = object.getString("ID_Pengumuman");
                                    String Jenis_Pengumuman     = object.getString("Jenis_Pengumuman");
                                    String Gambar_Pengumuman    = object.getString("Gambar_Pengumuman");
                                    String Isi_Pengumuman       = object.getString("Isi_Pengumuman");
                                    Tampil                      = object.getString("Tampil").equalsIgnoreCase("Ya");

                                    pengumuman                  = new Pengumuman(ID_Pengumuman, Jenis_Pengumuman, Gambar_Pengumuman, Isi_Pengumuman, Tampil);
                                    PengumumanArrayList.add(pengumuman);
                                    LVPengumuman.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                    myAdapterPengumuman.notifyDataSetChanged();
                                }
                                SRLPengumuman.setRefreshing(false);
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        SRLPengumuman.setRefreshing(false);
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pilihStatusPengumuman(){
        rgPengumuman.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Animation animation = new RotateAnimation(0, 180, btnVisibleSettingPengumuman.getWidth()/2, btnVisibleSettingPengumuman.getHeight()/2);
                animation.setDuration(250);
                btnVisibleSettingPengumuman.startAnimation(animation);

                switch(checkedId){
                    case R.id.rbPengumumanAktif :
                        statusPengumuman("Ya");
                        visibleSettingPengumuman();
                        btnVisibleSettingPengumuman.setEnabled(true);
                        LVPengumuman.setVisibility(View.VISIBLE);

                        break;
                    case R.id.rbPengumumanTidakAktif :
                        statusPengumuman("Tidak");

                        if(getContext() != null){
                            btnVisibleSettingPengumuman.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.show));
                            btnVisibleSettingPengumuman.setEnabled(false);
                            LLSettingPengumuman.setVisibility(View.GONE);
                            LVPengumuman.setVisibility(View.GONE);

                            YoYo.with(Techniques.SlideInUp).duration(300).playOn(LLSettingPengumuman);
                        }

                        break;
                }
            }
        });
    }

    private void statusPengumuman(final String Status){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "StatusPengumuman")
                .addBodyParameter("Status", Status)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Tersimpan")){
                            if(Status.equalsIgnoreCase("Ya")){
                                visibleSettingPengumuman();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void visibleSettingPengumuman(){
        if(sharedPreferences.getBoolean("VisibleSettingPengumuman", true)){
            VisiblePengumuman   = true;
            LLSettingPengumuman.setVisibility(View.VISIBLE);
        }
        else{
            VisiblePengumuman   = false;
            LLSettingPengumuman.setVisibility(View.GONE);
        }
    }

    private void pilihModePengumuman(){
        rgPengumumanMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rbPengumumanModeInsert :
                        YoYo.with(Techniques.FlipInX).duration(300).playOn(CVSettingPengumumanInsert);
                        YoYo.with(Techniques.FlipInX).duration(300).playOn(CVSettingPengumumanFull);

                        CVSettingPengumumanInsert.setVisibility(View.VISIBLE);
                        CVSettingPengumumanFull.setVisibility(View.GONE);

                        break;
                    case R.id.rbPengumumanModeFull :
                        YoYo.with(Techniques.FlipInX).duration(300).playOn(CVSettingPengumumanInsert);
                        YoYo.with(Techniques.FlipInX).duration(300).playOn(CVSettingPengumumanFull);

                        CVSettingPengumumanInsert.setVisibility(View.GONE);
                        CVSettingPengumumanFull.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });
    }

    private void validasiDurasiInterval(){
        txtDurasiMenitPengumuman.setSelectAllOnFocus(true);
        txtDurasiDetikPengumuman.setSelectAllOnFocus(true);
        txtIntervalMenitPengumuman.setSelectAllOnFocus(true);
        txtIntervalDetikPengumuman.setSelectAllOnFocus(true);

        txtDurasiMenitPengumuman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtDurasiDetikPengumuman.requestFocus();
                }
            }
        });

        txtDurasiDetikPengumuman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtIntervalMenitPengumuman.requestFocus();
                }
            }
        });

        txtIntervalMenitPengumuman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtIntervalDetikPengumuman.requestFocus();
                }
            }
        });

        txtIntervalDetikPengumuman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtIntervalDetikPengumuman.clearFocus();
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
            case R.id.btnVisibleSettingPengumuman :
                Animation animation = new RotateAnimation(0, 180, btnVisibleSettingPengumuman.getWidth()/2, btnVisibleSettingPengumuman.getHeight()/2);
                animation.setDuration(250);
                btnVisibleSettingPengumuman.startAnimation(animation);

                if(VisiblePengumuman && getContext() != null){
                    btnVisibleSettingPengumuman.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.show));
                    YoYo.with(Techniques.SlideInUp).duration(300).playOn(LLSettingPengumuman);
                    LLSettingPengumuman.setVisibility(View.GONE);
                    VisiblePengumuman = false;

                    sharedPreferences.edit().putBoolean("VisibleSettingPengumuman", false).apply();
                }
                else{
                    if(getContext() != null){
                        btnVisibleSettingPengumuman.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hide));
                        YoYo.with(Techniques.SlideInDown).duration(300).playOn(LLSettingPengumuman);
                        LLSettingPengumuman.setVisibility(View.VISIBLE);
                        VisiblePengumuman = true;

                        sharedPreferences.edit().putBoolean("VisibleSettingPengumuman", true).apply();
                    }
                }

                break;
            case R.id.btnBackgroundPengumuman :
                dialogPickerView(btnBackgroundPengumuman, getString(R.string.PILIH_BACKGROUND_PENGUMUMAN), "BACKGROUND_PENGUMUMAN", true);

                break;
            case R.id.btnWarnaJudulPengumuman :
                dialogPickerView(btnWarnaJudulPengumuman, getString(R.string.PILIH_WARNA_JUDUL_PENGUMUMAN), "JUDUL_PENGUMUMAN", false);

                break;
            case R.id.btnWarnaIsiPengumuman :
                dialogPickerView(btnWarnaIsiPengumuman, getString(R.string.PILIH_WARNA_ISI_PENGUMUMAN), "ISI_PENGUMUMAN", false);

                break;
            case R.id.btnWarnaHeaderPengumuman :
                dialogPickerView(btnWarnaHeaderPengumuman, getString(R.string.PILIH_WARNA_HEADER_PENGUMUMAN), "HEADER_PENGUMUMAN", true);

                break;
            case R.id.btnBGGambarPengumuman :
                backgroundGambarPengumuman();

                break;
            case R.id.btnSimpanSettingPengumuman :
                simpanSettingPengumuman();

                break;
            case R.id.btnTambahPengumuman :
                tambahPengumuman();

                break;
        }
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
            CPV.setActionMode(com.skydoves.colorpickerview.ActionMode.ALWAYS);
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

    private void backgroundGambarPengumuman(){
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
                    pilihBackgroundIntent(getString(R.string.Pilih_Background_dari), "PILIH_GAMBAR_BACKGROUND", false);
                }
            }
        }
        else{
            progressDialog.dismiss();
            pilihBackgroundIntent(getString(R.string.Pilih_Background_dari), "PILIH_GAMBAR_BACKGROUND",false);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 2){
            pilihBackgroundIntent(getString(R.string.Pilih_Background_dari), "PILIH_GAMBAR_BACKGROUND",false);
        }
    }

    private void pilihBackgroundIntent(String Judul, String Data, boolean Hapus){
        Intent Galeri       = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent Cecenet      = new Intent(getContext(), AlbumActivity.class);
        Intent HapusGambar  = new Intent(getContext(), HapusGambar.class);
        Intent pilihIntent  = new Intent(Intent.ACTION_CHOOSER);

        pilihIntent.putExtra(Data, true);
        pilihIntent.putExtra(Intent.EXTRA_INTENT, Galeri);
        pilihIntent.putExtra(Intent.EXTRA_TITLE, Judul);

        if(Hapus){
            pilihIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{Cecenet, HapusGambar});
        }
        else{
            pilihIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{Cecenet});
        }

        EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).edit().putBoolean(Data, true).apply();
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

                imgTambahEditPengumuman.setColorFilter(Color.TRANSPARENT);
                imgTambahEditPengumuman.setImageBitmap(bitmapSkala);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(resultCode == 2){
            Nama_Background                     = Objects.requireNonNull(data).getStringExtra("ALBUM");
            Simpan                              = 2;

            imgTambahEditPengumuman.setColorFilter(Color.TRANSPARENT);
            Picasso.with(getContext()).load(getString(R.string.URL_Pengumuman) + Nama_Background).into(imgTambahEditPengumuman);
        }
        else if(resultCode == 3){
            Nama_Background                     = "";
            Simpan                              = 0;

            imgTambahEditPengumuman.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.textColor));
            imgTambahEditPengumuman.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.add));
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

    private void simpanSettingPengumuman(){
        progressDialog.setMessage(getString(R.string.Menyimpan_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        String Mode = "Full_Screen";
        if(rgPengumumanMode.getCheckedRadioButtonId() == rbPengumumanModeInsert.getId()){
            Mode    = "Insert_Home";
        }
        else if(rgPengumumanMode.getCheckedRadioButtonId() == rbPengumumanModeFull.getId()){
            Mode    = "Full_Screen";
        }

        String Delay;
        if(cbPengumumanDelay.isChecked()){
            Delay   = "Ya";
        }
        else{
            Delay   = "Tidak";
        }

        String DurasiMenit      = "00";
        String DurasiDetik      = "00";
        String IntervalMenit    = "00";
        String IntervalDetik    = "00";

        if(txtDurasiMenitPengumuman.getText().length() == 1){
            DurasiMenit         = "0" + txtDurasiMenitPengumuman.getText().toString();
        }
        else if(txtDurasiMenitPengumuman.getText().length() == 2){
            DurasiMenit         = txtDurasiMenitPengumuman.getText().toString();
        }

        if(txtDurasiDetikPengumuman.getText().length() == 1){
            DurasiDetik         = "0" + txtDurasiDetikPengumuman.getText().toString();
        }
        else if(txtDurasiDetikPengumuman.getText().length() == 2){
            DurasiDetik         = txtDurasiDetikPengumuman.getText().toString();
        }

        if(txtIntervalMenitPengumuman.getText().length() == 1){
            IntervalMenit       = "0" + txtIntervalMenitPengumuman.getText().toString();
        }
        else if(txtIntervalMenitPengumuman.getText().length() == 2){
            IntervalMenit       = txtIntervalMenitPengumuman.getText().toString();
        }

        if(txtIntervalDetikPengumuman.getText().length() == 1){
            IntervalDetik       = "0" + txtIntervalDetikPengumuman.getText().toString();
        }
        else if(txtIntervalDetikPengumuman.getText().length() == 2){
            IntervalDetik       = txtIntervalDetikPengumuman.getText().toString();
        }

        String WarnaJudul       = "#" + Integer.toHexString(((ColorDrawable)btnWarnaJudulPengumuman.getBackground()).getColor()).substring(2).toUpperCase();
        String WarnaIsi         = "#" + Integer.toHexString(((ColorDrawable)btnWarnaIsiPengumuman.getBackground()).getColor()).substring(2).toUpperCase();

        String BackgroundWarna  = (Integer.toHexString(((ColorDrawable)btnBackgroundPengumuman.getBackground()).getColor()).length() == 6) ? "#" +
                                    Integer.toHexString(((ColorDrawable)btnBackgroundPengumuman.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                    Integer.toHexString(((ColorDrawable)btnBackgroundPengumuman.getBackground()).getColor()).substring(2).toUpperCase() +
                                    Integer.toHexString(((ColorDrawable)btnBackgroundPengumuman.getBackground()).getColor()).substring(0, 2).toUpperCase();

        String BackgroundHeader = (Integer.toHexString(((ColorDrawable)btnWarnaHeaderPengumuman.getBackground()).getColor()).length() == 6) ? "#" +
                                    Integer.toHexString(((ColorDrawable)btnWarnaHeaderPengumuman.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                    Integer.toHexString(((ColorDrawable)btnWarnaHeaderPengumuman.getBackground()).getColor()).substring(2).toUpperCase() +
                                    Integer.toHexString(((ColorDrawable)btnWarnaHeaderPengumuman.getBackground()).getColor()).substring(0, 2).toUpperCase();

        final String finalDurasiMenit   = DurasiMenit;
        final String finalDurasiDetik   = DurasiDetik;
        final String finalIntervalMenit = IntervalMenit;
        final String finalIntervalDetik = IntervalDetik;

        if(Simpan == 0){
            simpanSetting(Mode, Delay, DurasiMenit, DurasiDetik, IntervalMenit, IntervalDetik, BackgroundWarna, WarnaJudul, WarnaIsi, BackgroundHeader, finalDurasiMenit, finalDurasiDetik, finalIntervalMenit, finalIntervalDetik);
        }
        else if(Simpan == 1){
            AndroidNetworking.upload(getString(R.string.URL) + "Beranda.php")
                    .addMultipartParameter("Aksi", "SimpanSettingPengumuman")
                    .addMultipartParameter("Gambar", "Galeri")
                    .addMultipartParameter("Mode", Mode)
                    .addMultipartParameter("Delay", Delay)
                    .addMultipartParameter("Durasi", DurasiMenit + ":" + DurasiDetik)
                    .addMultipartParameter("Interval", IntervalMenit + ":" + IntervalDetik)
                    .addMultipartParameter("Insert_Home", spinnerPosisiPengumuman.getSelectedItem().toString())
                    .addMultipartParameter("Background_Warna", BackgroundWarna)
                    .addMultipartParameter("Warna_Text_Title", WarnaJudul)
                    .addMultipartParameter("Warna_Text_Isi", WarnaIsi)
                    .addMultipartParameter("Full_Screen", spinnerUrutanPengumuman.getSelectedItem().toString())
                    .addMultipartParameter("Animasi", spinnerAnimasiPengumuman.getSelectedItem().toString())
                    .addMultipartParameter("Background_Header", BackgroundHeader)
                    .addMultipartParameter("Nama_Background", Nama_Background)
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
                            if(response.equalsIgnoreCase("Tersimpan")){
                                txtDurasiMenitPengumuman.setText(finalDurasiMenit);
                                txtDurasiDetikPengumuman.setText(finalDurasiDetik);
                                txtIntervalMenitPengumuman.setText(finalIntervalMenit);
                                txtIntervalDetikPengumuman.setText(finalIntervalDetik);

                                Simpan = 0;
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
        else if(Simpan == 2){
            simpanSetting(Mode, Delay, DurasiMenit, DurasiDetik, IntervalMenit, IntervalDetik, BackgroundWarna, WarnaJudul, WarnaIsi, BackgroundHeader, finalDurasiMenit, finalDurasiDetik, finalIntervalMenit, finalIntervalDetik);
        }
    }

    private void simpanSetting(String Mode, String Delay, String DurasiMenit, String DurasiDetik, String IntervalMenit, String IntervalDetik, String BackgroundWarna, String WarnaJudul, String WarnaIsi, String BackgroundHeader, final String finalDurasiMenit, final String finalDurasiDetik, final String finalIntervalMenit, final String finalIntervalDetik){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "SimpanSettingPengumuman")
                .addBodyParameter("Gambar", "Cecenet")
                .addBodyParameter("Mode", Mode)
                .addBodyParameter("Delay", Delay)
                .addBodyParameter("Durasi", DurasiMenit + ":" + DurasiDetik)
                .addBodyParameter("Interval", IntervalMenit + ":" + IntervalDetik)
                .addBodyParameter("Insert_Home", spinnerPosisiPengumuman.getSelectedItem().toString())
                .addBodyParameter("Background_Warna", BackgroundWarna)
                .addBodyParameter("Warna_Text_Title", WarnaJudul)
                .addBodyParameter("Warna_Text_Isi", WarnaIsi)
                .addBodyParameter("Full_Screen", spinnerUrutanPengumuman.getSelectedItem().toString())
                .addBodyParameter("Animasi", spinnerAnimasiPengumuman.getSelectedItem().toString())
                .addBodyParameter("Background_Header", BackgroundHeader)
                .addBodyParameter("Nama_Background", Nama_Background)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Tersimpan")){
                            txtDurasiMenitPengumuman.setText(finalDurasiMenit);
                            txtDurasiDetikPengumuman.setText(finalDurasiDetik);
                            txtIntervalMenitPengumuman.setText(finalIntervalMenit);
                            txtIntervalDetikPengumuman.setText(finalIntervalDetik);

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

    private AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
            switch(LVPengumuman.getCheckedItemCount()){
                case 0  :
                    mode.setTitle(null);
                    break;
                case 1  :
                    mode.setTitle(R.string.SATU_PENGUMUMAN);
                    break;
                default :
                    mode.setTitle(LVPengumuman.getCheckedItemCount() + " " + getString(R.string.PENGUMUMAN));
                    break;
            }

            if(checked){
                IDPengumuman    = PengumumanArrayList.get(position).getID_Pengumuman();
                Nama_Background = PengumumanArrayList.get(position).getGambar_Pengumuman();
                JudulPengumuman = PengumumanArrayList.get(position).getJenis_Pengumuman();
                IsiPengumuman   = PengumumanArrayList.get(position).getIsi_Pengumuman();
                Tampil          = PengumumanArrayList.get(position).isTampil();

                PengumumanAktifAction.add(PengumumanArrayList.get(position).getID_Pengumuman());
            }
            else{
                PengumumanAktifAction.remove(PengumumanArrayList.get(position).getID_Pengumuman());
            }

            mode.invalidate();
        }

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_informasi, menu);
            PengumumanAktifAction.clear();

            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            menu.findItem(R.id.action_aktif).setVisible(false);
            menu.findItem(R.id.action_arsip).setVisible(false);
            LVPengumuman.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

            if(LVPengumuman.getCheckedItemCount() == 1){
                menu.findItem(R.id.action_edit).setVisible(true);
            }
            else{
                menu.findItem(R.id.action_edit).setVisible(false);
            }

            return true;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
                case R.id.action_edit :
                    EditPengumuman(mode);
                    break;
                case R.id.action_hapus :
                    KonfirmasiHapus(mode);
                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) { }
    };

    private void EditPengumuman(final ActionMode mode){
        txtJudulTambahEditPengumuman.setText(getString(R.string.EDIT_PENGUMUMAN));
        txtTambahEditJudulPengumuman.setText(JudulPengumuman);
        txtTambahEditIsiPengumuman.setText(IsiPengumuman);
        dialog.show();

        if(!Nama_Background.isEmpty()){
            Simpan  = 3;
            imgTambahEditPengumuman.setColorFilter(Color.TRANSPARENT);
            Picasso.with(getContext()).load(getString(R.string.URL_Pengumuman) + Nama_Background).into(imgTambahEditPengumuman);
        }
        else{
            Simpan  = 4;
            imgTambahEditPengumuman.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.textColor));
            imgTambahEditPengumuman.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.add));
        }

        if(Tampil){
            rbTambahEditPengumumanAktif.setChecked(true);
        }
        else{
            rbTambahEditPengumumanArsip.setChecked(true);
        }

        imgTambahEditPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Simpan == 1 || Simpan == 2 || Simpan == 3){
                    pilihBackgroundIntent(getString(R.string.Pilih_Gambar_dari), "PILIH_GAMBAR_PENGUMUMAN", true);
                }
                else{
                    pilihBackgroundIntent(getString(R.string.Pilih_Gambar_dari), "PILIH_GAMBAR_PENGUMUMAN", false);
                }
            }
        });

        btnBatalSimpanPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode.finish();
                dialog.dismiss();
                settingPengumuman();
            }
        });

        btnSimpanPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                progressDialog.setCancelable(false);
                progressDialog.show();

                String TampilPengumuman;
                if(rgTambahEditPengumuman.getCheckedRadioButtonId() == rbTambahEditPengumumanAktif.getId()){
                    TampilPengumuman    = "Ya";
                }
                else{
                    TampilPengumuman    = "Tidak";
                }

                if(Objects.requireNonNull(txtTambahEditJudulPengumuman.getText()).toString().trim().isEmpty()){
                    txtTambahEditJudulPengumuman.setError(getString(R.string.Jangan_Kosong_));
                    txtTambahEditJudulPengumuman.requestFocus();
                    progressDialog.dismiss();
                }
                else if(Objects.requireNonNull(txtTambahEditIsiPengumuman.getText()).toString().trim().isEmpty() && Simpan == 0 ||
                        Objects.requireNonNull(txtTambahEditIsiPengumuman.getText()).toString().trim().isEmpty() && Simpan == 4){
                    Toast.makeText(getContext(), getString(R.string.Isi_Pengumuman_atau_Gambar_Tidak_Ada), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(txtTambahEditJudulPengumuman.getText().toString().equals(JudulPengumuman) && txtTambahEditIsiPengumuman.getText().toString().equals(IsiPengumuman)
                        && TampilPengumuman.equalsIgnoreCase((Tampil) ? "Ya" : "Tidak") && Simpan == 3 || txtTambahEditJudulPengumuman.getText().toString().equals(JudulPengumuman) &&
                        txtTambahEditIsiPengumuman.getText().toString().equals(IsiPengumuman) && TampilPengumuman.equalsIgnoreCase((Tampil) ? "Ya" : "Tidak") && Simpan == 4){
                    mode.finish();
                    dialog.dismiss();
                    settingPengumuman();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.Tidak_Ada_Perubahan_yang_Disimpan), Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Simpan == 0 || Simpan == 2 || Simpan == 3 || Simpan == 4){
                        mode.finish();
                        simpanTambahEditPengumumanCecenet("EditPengumuman", TampilPengumuman, true);
                    }
                    else if(Simpan == 1){
                        mode.finish();
                        simpanTambahEditPengumumanGaleri("EditPengumuman", TampilPengumuman, true);
                    }
                }
            }
        });
    }

    private void simpanTambahEditPengumumanCecenet(String Aksi, String Tampil, final boolean toast){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", Aksi)
                .addBodyParameter("Gambar", "Cecenet")
                .addBodyParameter("IDPengumuman", IDPengumuman)
                .addBodyParameter("JudulPengumuman", Objects.requireNonNull(txtTambahEditJudulPengumuman.getText()).toString())
                .addBodyParameter("IsiPengumuman", Objects.requireNonNull(txtTambahEditIsiPengumuman.getText()).toString())
                .addBodyParameter("GambarPengumuman", Nama_Background)
                .addBodyParameter("Tampil", Tampil)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Tersimpan")){
                            settingPengumuman();
                            listPengumuman();
                            dialog.dismiss();

                            if(toast){
                                Toast.makeText(getContext(), getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
                            }
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

    private void simpanTambahEditPengumumanGaleri(String Aksi, String Tampil, final boolean toast){
        AndroidNetworking.upload(getString(R.string.URL) + "Beranda.php")
                .addMultipartParameter("Aksi", Aksi)
                .addMultipartParameter("Gambar", "Galeri")
                .addMultipartParameter("IDPengumuman", IDPengumuman)
                .addMultipartParameter("JudulPengumuman", Objects.requireNonNull(txtTambahEditJudulPengumuman.getText()).toString())
                .addMultipartParameter("IsiPengumuman", Objects.requireNonNull(txtTambahEditIsiPengumuman.getText()).toString())
                .addMultipartParameter("GambarPengumuman", Nama_Background)
                .addMultipartParameter("Tampil", Tampil)
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
                        if(response.equalsIgnoreCase("Tersimpan")){
                            settingPengumuman();
                            listPengumuman();
                            dialog.dismiss();

                            if(toast){
                                Toast.makeText(getContext(), getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
                            }
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

    private void KonfirmasiHapus(final ActionMode mode){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle(getString(R.string.Konfirmasi))
                .setMessage(getString(R.string.Apakah_Yakin_Ingin_Menghapus) + " " + PengumumanAktifAction.size() + " " + getString(R.string.Pengumuman_Ini_))
                .setIcon(R.drawable.hapus_black)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.TIDAK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mode.finish();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.YA), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HapusAction(mode);
                    }
                })
                .create().show();
    }

    private void HapusAction(final ActionMode mode){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "HapusPengumuman")
                .addBodyParameter("PengumumanAktifAction", PengumumanAktifAction.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Pengumuman Terhapus")){
                                mode.finish();
                                listPengumuman();
                                Toast.makeText(getContext(), PengumumanAktifAction.size() + " " + getString(R.string.Pengumuman_Terhapus), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mode.finish();
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mode.finish();
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void tambahPengumuman(){
        Simpan          = 0;
        IDPengumuman    = "";
        Nama_Background = "";

        txtJudulTambahEditPengumuman.setText(getString(R.string.tambah_pengumuman));
        imgTambahEditPengumuman.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.textColor));
        imgTambahEditPengumuman.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.add));
        txtTambahEditJudulPengumuman.setText("");
        txtTambahEditIsiPengumuman.setText("");
        rbTambahEditPengumumanAktif.setChecked(true);
        dialog.show();

        imgTambahEditPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Simpan == 1 || Simpan == 2 || Simpan == 3){
                    pilihBackgroundIntent(getString(R.string.Pilih_Gambar_dari), "PILIH_GAMBAR_PENGUMUMAN", true);
                }
                else{
                    pilihBackgroundIntent(getString(R.string.Pilih_Gambar_dari), "PILIH_GAMBAR_PENGUMUMAN", false);
                }
            }
        });

        btnBatalSimpanPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                settingPengumuman();
            }
        });

        btnSimpanPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                progressDialog.setCancelable(false);
                progressDialog.show();

                String TampilPengumuman;
                if(rgTambahEditPengumuman.getCheckedRadioButtonId() == rbTambahEditPengumumanAktif.getId()){
                    TampilPengumuman    = "Ya";
                }
                else{
                    TampilPengumuman    = "Tidak";
                }

                if(Objects.requireNonNull(txtTambahEditJudulPengumuman.getText()).toString().trim().isEmpty()){
                    txtTambahEditJudulPengumuman.setError(getString(R.string.Jangan_Kosong_));
                    txtTambahEditJudulPengumuman.requestFocus();
                    progressDialog.dismiss();
                }
                else if(Objects.requireNonNull(txtTambahEditIsiPengumuman.getText()).toString().trim().isEmpty() && Simpan == 0){
                    Toast.makeText(getContext(), getString(R.string.Isi_Pengumuman_atau_Gambar_Tidak_Ada), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    if(Simpan == 0 || Simpan == 2){
                        simpanTambahEditPengumumanCecenet("SimpanPengumuman", TampilPengumuman, false);
                    }
                    else if(Simpan == 1){
                        simpanTambahEditPengumumanGaleri("SimpanPengumuman", TampilPengumuman, false);
                    }
                }
            }
        });
    }
}