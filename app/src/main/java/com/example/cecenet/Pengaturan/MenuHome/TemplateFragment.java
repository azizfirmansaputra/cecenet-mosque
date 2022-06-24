package com.example.cecenet.Pengaturan.MenuHome;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.cecenet.R;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TemplateFragment extends Fragment implements View.OnClickListener{
    private RadioGroup rgTemplateHome;
    private RadioButton rbTemplateHome1, rbTemplateHome2, rbTemplateHome3;
    private Button btnWarnaTanggal, btnWarnaJudul, btnWarnaAlamat, btnWarnaJam, btnWarnaCountdown, btnWarnaJadwalSalat, btnWarnaTimeSalat, btnWarnaInformasi, btnWarnaHighlight,
            btnBGTanggal, btnBGJudul, btnBGAlamat, btnBGJam, btnBGCountdown, btnBGJadwalSalat, btnBGTimeSalat, btnBGInformasi, btnBGHighlight;
    private CheckBox cbCadangkanTemplate;
    private Button btnResetTemplate;

    private ProgressDialog progressDialog;
    private SharedPreferences SPTemplate1, SPTemplate2, SPTemplate3;
    private SharedPreferences.Editor SPTemplateEditor;
    private String Template;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                   = inflater.inflate(R.layout.fragment_template, container, false);

        rgTemplateHome              = view.findViewById(R.id.rgTemplateHome);
        rbTemplateHome1             = view.findViewById(R.id.rbTemplateHome1);
        rbTemplateHome2             = view.findViewById(R.id.rbTemplateHome2);
        rbTemplateHome3             = view.findViewById(R.id.rbTemplateHome3);

        btnWarnaTanggal             = view.findViewById(R.id.btnWarnaTanggal);
        btnWarnaJudul               = view.findViewById(R.id.btnWarnaJudul);
        btnWarnaAlamat              = view.findViewById(R.id.btnWarnaAlamat);
        btnWarnaJam                 = view.findViewById(R.id.btnWarnaJam);
        btnWarnaCountdown           = view.findViewById(R.id.btnWarnaCountdown);
        btnWarnaJadwalSalat         = view.findViewById(R.id.btnWarnaJadwalSalat);
        btnWarnaTimeSalat           = view.findViewById(R.id.btnWarnaTimeSalat);
        btnWarnaInformasi           = view.findViewById(R.id.btnWarnaInformasi);
        btnWarnaHighlight           = view.findViewById(R.id.btnWarnaHighlight);

        btnBGTanggal                = view.findViewById(R.id.btnBGTanggal);
        btnBGJudul                  = view.findViewById(R.id.btnBGJudul);
        btnBGAlamat                 = view.findViewById(R.id.btnBGAlamat);
        btnBGJam                    = view.findViewById(R.id.btnBGJam);
        btnBGCountdown              = view.findViewById(R.id.btnBGCountdown);
        btnBGJadwalSalat            = view.findViewById(R.id.btnBGJadwalSalat);
        btnBGTimeSalat              = view.findViewById(R.id.btnBGTimeSalat);
        btnBGInformasi              = view.findViewById(R.id.btnBGInformasi);
        btnBGHighlight              = view.findViewById(R.id.btnBGHighlight);

        Button btnSimpanTemplate    = view.findViewById(R.id.btnSimpanTemplate);
        btnResetTemplate            = view.findViewById(R.id.btnResetTemplate);
        cbCadangkanTemplate         = view.findViewById(R.id.cbCadangkanTemplate);
        progressDialog              = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(getContext() != null){
            SPTemplate1             = getContext().getSharedPreferences("TEMPLATE_1", Context.MODE_PRIVATE);
            SPTemplate2             = getContext().getSharedPreferences("TEMPLATE_2", Context.MODE_PRIVATE);
            SPTemplate3             = getContext().getSharedPreferences("TEMPLATE_3", Context.MODE_PRIVATE);
        }

        onClick();
        cekTemplate();
        pilihTemplate();
        cekBagianTemplate();
        PushDownAnim.setPushDownAnimTo(btnSimpanTemplate, btnResetTemplate);

        btnSimpanTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanTemplate();
            }
        });

        btnResetTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTemplate();
            }
        });

        return view;
    }

    private void cekTemplate(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "CekTemplate")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject   = response.getJSONObject(i);
                                Template                = jsonObject.getString("Isi_Setting");

                                if(Template.equalsIgnoreCase("Template 1")){
                                    rbTemplateHome1.setChecked(true);
                                }
                                else if(Template.equalsIgnoreCase("Template 2")){
                                    rbTemplateHome2.setChecked(true);
                                }
                                else if(Template.equalsIgnoreCase("Template 3")){
                                    rbTemplateHome3.setChecked(true);
                                }
                            }
                            progressDialog.dismiss();
                        }
                        catch (Exception e){
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

    private void pilihTemplate(){
        rgTemplateHome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbTemplateHome1 :
                        if(SPTemplate1.getAll().size() != 0){
                            sharedPreferences(SPTemplate1);
                        }
                        else{
                            btnWarnaTanggal.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJudul.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaAlamat.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJam.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaCountdown.setBackgroundColor(0xFFFF0000);
                            btnWarnaJadwalSalat.setBackgroundColor(0xFFFFFF00);
                            btnWarnaTimeSalat.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaInformasi.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaHighlight.setBackgroundColor(0x00FFFFFF);

                            btnBGTanggal.setBackgroundColor(0x00FFFFFF);
                            btnBGJudul.setBackgroundColor(0x00FFFFFF);
                            btnBGAlamat.setBackgroundColor(0x00FFFFFF);
                            btnBGJam.setBackgroundColor(0x00FFFFFF);
                            btnBGCountdown.setBackgroundColor(0x00FFFFFF);
                            btnBGJadwalSalat.setBackgroundColor(0x00FFFFFF);
                            btnBGTimeSalat.setBackgroundColor(0x00FFFFFF);
                            btnBGInformasi.setBackgroundColor(0xFF000000);
                            btnBGHighlight.setBackgroundColor(0x80FFFFFF);
                        }

                        if(rbTemplateHome1.getText().toString().equalsIgnoreCase(Template)){
                            btnResetTemplate.setVisibility(View.VISIBLE);
                            cbCadangkanTemplate.setVisibility(View.VISIBLE);
                        }
                        else{
                            btnResetTemplate.setVisibility(View.INVISIBLE);
                            cbCadangkanTemplate.setVisibility(View.INVISIBLE);
                        }

                        break;
                    case R.id.rbTemplateHome2 :
                        if(SPTemplate2.getAll().size() != 0){
                            sharedPreferences(SPTemplate2);
                        }
                        else{
                            btnWarnaTanggal.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJudul.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaAlamat.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJam.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaCountdown.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJadwalSalat.setBackgroundColor(0xFFFFFF00);
                            btnWarnaTimeSalat.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaInformasi.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaHighlight.setBackgroundColor(0x00FFFFFF);

                            btnBGTanggal.setBackgroundColor(0x33FFFFFF);
                            btnBGJudul.setBackgroundColor(0x00FFFFFF);
                            btnBGAlamat.setBackgroundColor(0x00FFFFFF);
                            btnBGJam.setBackgroundColor(0xFFA97850);
                            btnBGCountdown.setBackgroundColor(0xFF665A4E);
                            btnBGJadwalSalat.setBackgroundColor(0xB3CC914D);
                            btnBGTimeSalat.setBackgroundColor(0x00FFFFFF);
                            btnBGInformasi.setBackgroundColor(0x80000000);
                            btnBGHighlight.setBackgroundColor(0x80FFFFFF);
                        }

                        if(rbTemplateHome2.getText().toString().equalsIgnoreCase(Template)){
                            btnResetTemplate.setVisibility(View.VISIBLE);
                            cbCadangkanTemplate.setVisibility(View.VISIBLE);
                        }
                        else{
                            btnResetTemplate.setVisibility(View.INVISIBLE);
                            cbCadangkanTemplate.setVisibility(View.INVISIBLE);
                        }

                        break;
                    case R.id.rbTemplateHome3 :
                        if(SPTemplate3.getAll().size() != 0){
                            sharedPreferences(SPTemplate3);
                        }
                        else{
                            btnWarnaTanggal.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJudul.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaAlamat.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJam.setBackgroundColor(0xFFFFFF00);
                            btnWarnaCountdown.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaJadwalSalat.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaTimeSalat.setBackgroundColor(0xFFFFFFFF);
                            btnWarnaInformasi.setBackgroundColor(0xFF000000);
                            btnWarnaHighlight.setBackgroundColor(0x00FFFFFF);

                            btnBGTanggal.setBackgroundColor(0xFF0F326A);
                            btnBGJudul.setBackgroundColor(0xFF1F4D89);
                            btnBGAlamat.setBackgroundColor(0x00FFFFFF);
                            btnBGJam.setBackgroundColor(0xFF161D47);
                            btnBGCountdown.setBackgroundColor(0xB30C2343);
                            btnBGJadwalSalat.setBackgroundColor(0xFFFE0000);
                            btnBGTimeSalat.setBackgroundColor(0XFF1F4D89);
                            btnBGInformasi.setBackgroundColor(0x80FFFFFF);
                            btnBGHighlight.setBackgroundColor(0x80FFFFFF);
                        }

                        if(rbTemplateHome3.getText().toString().equalsIgnoreCase(Template)){
                            btnResetTemplate.setVisibility(View.VISIBLE);
                            cbCadangkanTemplate.setVisibility(View.VISIBLE);
                        }
                        else{
                            btnResetTemplate.setVisibility(View.INVISIBLE);
                            cbCadangkanTemplate.setVisibility(View.INVISIBLE);
                        }

                        break;
                }
            }
        });
    }

    private void sharedPreferences(SharedPreferences SPTemplate){
        btnWarnaTanggal.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaTanggal", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaTanggal", "").substring(0, 2), 16) << 24));
        btnWarnaJudul.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaJudul", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaJudul", "").substring(0, 2), 16) << 24));
        btnWarnaAlamat.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaAlamat", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaAlamat", "").substring(0, 2), 16) << 24));
        btnWarnaJam.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaJam", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaJam", "").substring(0, 2), 16) << 24));
        btnWarnaCountdown.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaCountdown", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaCountdown", "").substring(0, 2), 16) << 24));
        btnWarnaJadwalSalat.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaJadwalSalat", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaJadwalSalat", "").substring(0, 2), 16) << 24));
        btnWarnaTimeSalat.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaTimeSalat", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaTimeSalat", "").substring(0, 2), 16) << 24));
        btnWarnaInformasi.setBackgroundColor(Integer.parseInt(SPTemplate.getString("WarnaInformasi", "").substring(2), 16) + (Integer.parseInt(SPTemplate.getString("WarnaInformasi", "").substring(0, 2), 16) << 24));

        btnBGTanggal.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGTanggal", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGTanggal", "").substring(6), 16) << 24));
        btnBGJudul.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGJudul", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGJudul", "").substring(6), 16) << 24));
        btnBGJam.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGJam", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGJam", "").substring(6), 16) << 24));
        btnBGCountdown.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGCountdown", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGCountdown", "").substring(6), 16) << 24));
        btnBGJadwalSalat.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGJadwalSalat", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGJadwalSalat", "").substring(6), 16) << 24));
        btnBGTimeSalat.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGTimeSalat", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGTimeSalat", "").substring(6), 16) << 24));
        btnBGInformasi.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGInformasi", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGInformasi", "").substring(6), 16) << 24));
        btnBGHighlight.setBackgroundColor(Integer.parseInt(SPTemplate.getString("BGHighlight", "").substring(0, 6), 16) + (Integer.parseInt(SPTemplate.getString("BGHighlight", "").substring(6), 16) << 24));
    }

    private void cekBagianTemplate(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "CekBagianTemplate")
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

                                    if(object.getString("Bagian").equalsIgnoreCase("Date")){
                                        cekWarna(object, btnWarnaTanggal, btnBGTanggal);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Title")){
                                        cekWarna(object, btnWarnaJudul, btnBGJudul);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Address")){
                                        cekWarna(object, btnWarnaAlamat, btnBGAlamat);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Clock")){
                                        cekWarna(object, btnWarnaJam, btnBGJam);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Countdown")){
                                        cekWarna(object, btnWarnaCountdown, btnBGCountdown);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Jadwal_Salat")){
                                        cekWarna(object, btnWarnaJadwalSalat, btnBGJadwalSalat);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Time_Salat")){
                                        cekWarna(object, btnWarnaTimeSalat, btnBGTimeSalat);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Informasi")){
                                        cekWarna(object, btnWarnaInformasi, btnBGInformasi);
                                    }
                                    else if(object.getString("Bagian").equalsIgnoreCase("Highlight")){
                                        cekWarna(object, btnWarnaHighlight, btnBGHighlight);
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
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cekWarna(JSONObject object, Button btnWarna, Button btnBG){
        int Color_Warna, Color_BG;
        try{
            if(object.getString("Warna").length() <= 0){
                Color_Warna     = 0x00FFFFFF;
            }
            else{
                String Warna    = object.getString("Warna").replace("#", "FF");
                Color_Warna     = Integer.parseInt(Warna.substring(2), 16) + (Integer.parseInt(Warna.substring(0, 2), 16) << 24);
            }

            if(object.getString("Background").length() <= 0){
                Color_BG        = 0x00FFFFFF;
            }
            else{
                String BG       = object.getString("Background").substring(7) + object.getString("Background").substring(1, 7);
                Color_BG        = Integer.parseInt(BG.substring(2), 16) + (Integer.parseInt(BG.substring(0, 2), 16) << 24);
            }

            btnWarna.setBackgroundColor(Color_Warna);
            btnBG.setBackgroundColor(Color_BG);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void onClick(){
        btnWarnaTanggal.setOnClickListener(this);
        btnWarnaJudul.setOnClickListener(this);
        btnWarnaJam.setOnClickListener(this);
        btnWarnaAlamat.setOnClickListener(this);
        btnWarnaCountdown.setOnClickListener(this);
        btnWarnaJadwalSalat.setOnClickListener(this);
        btnWarnaTimeSalat.setOnClickListener(this);
        btnWarnaInformasi.setOnClickListener(this);
        btnWarnaHighlight.setOnClickListener(this);

        btnBGTanggal.setOnClickListener(this);
        btnBGJudul.setOnClickListener(this);
        btnBGJam.setOnClickListener(this);
        btnBGAlamat.setOnClickListener(this);
        btnBGCountdown.setOnClickListener(this);
        btnBGJadwalSalat.setOnClickListener(this);
        btnBGTimeSalat.setOnClickListener(this);
        btnBGInformasi.setOnClickListener(this);
        btnBGHighlight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnWarnaTanggal :
                dialogPickerView(btnWarnaTanggal, getString(R.string.PILIH_WARNA_TANGGAL), "WARNA_TANGGAL", false);
                break;
            case R.id.btnWarnaJudul :
                dialogPickerView(btnWarnaJudul, getString(R.string.PILIH_WARNA_JUDUL), "WARNA_JUDUL", false);
                break;
            case R.id.btnWarnaJam :
                dialogPickerView(btnWarnaJam, getString(R.string.PILIH_WARNA_JAM), "WARNA_JAM", false);
                break;
            case R.id.btnWarnaAlamat :
                dialogPickerView(btnWarnaAlamat, getString(R.string.PILIH_WARNA_ALAMAT), "WARNA_ALAMAT", false);
                break;
            case R.id.btnWarnaCountdown :
                dialogPickerView(btnWarnaCountdown, getString(R.string.PILIH_WARNA_COUNTDOWN), "WARNA_COUNTDOWN", false);
                break;
            case R.id.btnWarnaJadwalSalat :
                dialogPickerView(btnWarnaJadwalSalat, getString(R.string.PILIH_WARNA_JADWAL_SALAT), "WARNA_JADWAL_SALAT", false);
                break;
            case R.id.btnWarnaTimeSalat :
                dialogPickerView(btnWarnaTimeSalat, getString(R.string.PILIH_WARNA_TIME_SALAT), "WARNA_TIME_SALAT", false);
                break;
            case R.id.btnWarnaInformasi :
                dialogPickerView(btnWarnaInformasi, getString(R.string.PILIH_WARNA_INFORMASI), "WARNA_INFORMASI", false);
                break;
            case R.id.btnWarnaHighlight :
                btnWarnaHighlight.setEnabled(false);
                break;
            case R.id.btnBGTanggal :
                dialogPickerView(btnBGTanggal, getString(R.string.PILIH_BACKGROUND_TANGGAL), "BACKGROUND_TANGGAL", true);
                break;
            case R.id.btnBGJudul :
                dialogPickerView(btnBGJudul, getString(R.string.PILIH_BACKGROUND_JUDUL), "BACKGROUND_JUDUL", true);
                break;
            case R.id.btnBGJam :
                dialogPickerView(btnBGJam, getString(R.string.PILIH_BACKGROUND_JAM), "BACKGROUND_JAM", true);
                break;
            case R.id.btnBGAlamat :
                btnBGAlamat.setEnabled(false);
                break;
            case R.id.btnBGCountdown :
                dialogPickerView(btnBGCountdown, getString(R.string.PILIH_BACKGROUND_COUNTDOWN), "BACKGROUND_COUNTDOWN", true);
                break;
            case R.id.btnBGJadwalSalat :
                dialogPickerView(btnBGJadwalSalat, getString(R.string.PILIH_BACKGROUND_JADWAL_SALAT), "BACKGROUND_JADWAL_SALAT", true);
                break;
            case R.id.btnBGTimeSalat :
                dialogPickerView(btnBGTimeSalat, getString(R.string.PILIH_BACKGROUND_TIME_SALAT), "BACKGROUND_TIME_SALAT", true);
                break;
            case R.id.btnBGInformasi :
                dialogPickerView(btnBGInformasi, getString(R.string.PILIH_BACKGROUND_INFORMASI), "BACKGROUND_INFORMASI", true);
                break;
            case R.id.btnBGHighlight :
                dialogPickerView(btnBGHighlight, getString(R.string.PILIH_WARNA_HIGHLIGHT), "WARNA_HIGHLIGHT", true);
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

    private void simpanTemplate(){
        progressDialog.setMessage(getString(R.string.Menyimpan_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(rgTemplateHome.getCheckedRadioButtonId() == R.id.rbTemplateHome1){
            Template                    = "Template 1";
            SPTemplateEditor            = SPTemplate1.edit();
        }
        else if(rgTemplateHome.getCheckedRadioButtonId() == R.id.rbTemplateHome2){
            Template                    = "Template 2";
            SPTemplateEditor            = SPTemplate2.edit();
        }
        else if(rgTemplateHome.getCheckedRadioButtonId() == R.id.rbTemplateHome3){
            Template                    = "Template 3";
            SPTemplateEditor            = SPTemplate3.edit();
        }

        final String WarnaTanggal       = "#" + Integer.toHexString(((ColorDrawable)btnWarnaTanggal.getBackground()).getColor()).substring(2).toUpperCase();
        final String WarnaJudul         = "#" + Integer.toHexString(((ColorDrawable)btnWarnaJudul.getBackground()).getColor()).substring(2).toUpperCase();
        final String WarnaJam           = "#" + Integer.toHexString(((ColorDrawable)btnWarnaJam.getBackground()).getColor()).substring(2).toUpperCase();
        final String WarnaAlamat        = "#" + Integer.toHexString(((ColorDrawable)btnWarnaAlamat.getBackground()).getColor()).substring(2).toUpperCase();
        final String WarnaCountdown     = "#" + Integer.toHexString(((ColorDrawable)btnWarnaCountdown.getBackground()).getColor()).substring(2).toUpperCase();
        final String WarnaJadwalSalat   = "#" + Integer.toHexString(((ColorDrawable)btnWarnaJadwalSalat.getBackground()).getColor()).substring(2).toUpperCase();
        final String WarnaTimeSalat     = "#" + Integer.toHexString(((ColorDrawable)btnWarnaTimeSalat.getBackground()).getColor()).substring(2).toUpperCase();
        final String WarnaInformasi     = "#" + Integer.toHexString(((ColorDrawable)btnWarnaInformasi.getBackground()).getColor()).substring(2).toUpperCase();

        final String BGTanggal          = (Integer.toHexString(((ColorDrawable)btnBGTanggal.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGTanggal.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGTanggal.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGTanggal.getBackground()).getColor()).substring(0, 2).toUpperCase();
        final String BGJudul            = (Integer.toHexString(((ColorDrawable)btnBGJudul.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGJudul.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGJudul.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGJudul.getBackground()).getColor()).substring(0, 2).toUpperCase();
        final String BGJam              = (Integer.toHexString(((ColorDrawable)btnBGJam.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGJam.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGJam.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGJam.getBackground()).getColor()).substring(0, 2).toUpperCase();
        final String BGCountdown        = (Integer.toHexString(((ColorDrawable)btnBGCountdown.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGCountdown.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGCountdown.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGCountdown.getBackground()).getColor()).substring(0, 2).toUpperCase();
        final String BGJadwalSalat      = (Integer.toHexString(((ColorDrawable)btnBGJadwalSalat.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGJadwalSalat.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGJadwalSalat.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGJadwalSalat.getBackground()).getColor()).substring(0, 2).toUpperCase();
        final String BGTimeSalat        = (Integer.toHexString(((ColorDrawable)btnBGTimeSalat.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGTimeSalat.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGTimeSalat.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGTimeSalat.getBackground()).getColor()).substring(0, 2).toUpperCase();
        final String BGInformasi        = (Integer.toHexString(((ColorDrawable)btnBGInformasi.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGInformasi.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGInformasi.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGInformasi.getBackground()).getColor()).substring(0, 2).toUpperCase();
        final String BGHighlight        = (Integer.toHexString(((ColorDrawable)btnBGHighlight.getBackground()).getColor()).length() == 6) ? "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGHighlight.getBackground()).getColor()).toUpperCase() + "00" : "#" +
                                            Integer.toHexString(((ColorDrawable)btnBGHighlight.getBackground()).getColor()).substring(2).toUpperCase() +
                                            Integer.toHexString(((ColorDrawable)btnBGHighlight.getBackground()).getColor()).substring(0, 2).toUpperCase();

        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "SimpanTemplate")
                .addBodyParameter("Template", Template)
                .addBodyParameter("WarnaTanggal", WarnaTanggal)
                .addBodyParameter("WarnaJudul", WarnaJudul)
                .addBodyParameter("WarnaJam", WarnaJam)
                .addBodyParameter("WarnaAlamat", WarnaAlamat)
                .addBodyParameter("WarnaCountdown", WarnaCountdown)
                .addBodyParameter("WarnaJadwalSalat", WarnaJadwalSalat)
                .addBodyParameter("WarnaTimeSalat", WarnaTimeSalat)
                .addBodyParameter("WarnaInformasi", WarnaInformasi)
                .addBodyParameter("BGTanggal", BGTanggal)
                .addBodyParameter("BGJudul", BGJudul)
                .addBodyParameter("BGJam", BGJam)
                .addBodyParameter("BGCountdown", BGCountdown)
                .addBodyParameter("BGJadwalSalat", BGJadwalSalat)
                .addBodyParameter("BGTimeSalat", BGTimeSalat)
                .addBodyParameter("BGInformasi", BGInformasi)
                .addBodyParameter("BGHighlight", BGHighlight)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Tersimpan")){
                            progressDialog.dismiss();
                            cbCadangkanTemplate.setVisibility(View.VISIBLE);
                            btnResetTemplate.setVisibility(View.VISIBLE);

                            if(cbCadangkanTemplate.isChecked()){
                                SPTemplateEditor.putString("WarnaTanggal", WarnaTanggal.replace("#", "FF"));
                                SPTemplateEditor.putString("WarnaJudul", WarnaJudul.replace("#", "FF"));
                                SPTemplateEditor.putString("WarnaJam", WarnaJam.replace("#", "FF"));
                                SPTemplateEditor.putString("WarnaAlamat", WarnaAlamat.replace("#", "FF"));
                                SPTemplateEditor.putString("WarnaCountdown", WarnaCountdown.replace("#", "FF"));
                                SPTemplateEditor.putString("WarnaJadwalSalat", WarnaJadwalSalat.replace("#", "FF"));
                                SPTemplateEditor.putString("WarnaTimeSalat", WarnaTimeSalat.replace("#", "FF"));
                                SPTemplateEditor.putString("WarnaInformasi", WarnaInformasi.replace("#", "FF"));
                                SPTemplateEditor.putString("BGTanggal", BGTanggal.replace("#", ""));
                                SPTemplateEditor.putString("BGJudul", BGJudul.replace("#", ""));
                                SPTemplateEditor.putString("BGJam", BGJam.replace("#", ""));
                                SPTemplateEditor.putString("BGCountdown", BGCountdown.replace("#", ""));
                                SPTemplateEditor.putString("BGJadwalSalat", BGJadwalSalat.replace("#", ""));
                                SPTemplateEditor.putString("BGTimeSalat", BGTimeSalat.replace("#", ""));
                                SPTemplateEditor.putString("BGInformasi", BGInformasi.replace("#", ""));
                                SPTemplateEditor.putString("BGHighlight", BGHighlight.replace("#", ""));
                                SPTemplateEditor.apply();
                            }
                            Toast.makeText(getContext(), getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetTemplate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle(getString(R.string.Konfirmasi));
        builder.setMessage(getString(R.string.Apakah_Yakin_Ingin_Mereset) + " " + Template + " ?");
        builder.setIcon(R.drawable.hapus_black);
        builder.setCancelable(false);
        builder.setNegativeButton(getString(R.string.batal), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.YA), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Template.equalsIgnoreCase("Template 1")){
                    SPTemplateEditor   = SPTemplate1.edit();
                }
                else if(Template.equalsIgnoreCase("Template 2")){
                    SPTemplateEditor   = SPTemplate2.edit();
                }
                else if(Template.equalsIgnoreCase("Template 3")){
                    SPTemplateEditor   = SPTemplate3.edit();
                }

                SPTemplateEditor.remove("WarnaTanggal");
                SPTemplateEditor.remove("WarnaJudul");
                SPTemplateEditor.remove("WarnaJam");
                SPTemplateEditor.remove("WarnaAlamat");
                SPTemplateEditor.remove("WarnaCountdown");
                SPTemplateEditor.remove("WarnaJadwalSalat");
                SPTemplateEditor.remove("WarnaTimeSalat");
                SPTemplateEditor.remove("WarnaInformasi");
                SPTemplateEditor.remove("WarnaHighlight");
                SPTemplateEditor.remove("BGTanggal");
                SPTemplateEditor.remove("BGJudul");
                SPTemplateEditor.remove("BGJam");
                SPTemplateEditor.remove("BGAlamat");
                SPTemplateEditor.remove("BGCountdown");
                SPTemplateEditor.remove("BGJadwalSalat");
                SPTemplateEditor.remove("BGTimeSalat");
                SPTemplateEditor.remove("BGInformasi");
                SPTemplateEditor.remove("BGHighlight");
                SPTemplateEditor.apply();

                rgTemplateHome.clearCheck();
                cekTemplate();
                simpanTemplate();
            }
        });
        builder.create().show();
    }
}