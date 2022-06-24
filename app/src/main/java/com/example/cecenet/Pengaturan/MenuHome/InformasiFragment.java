package com.example.cecenet.Pengaturan.MenuHome;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.Pengaturan.MenuHome.InformasiAdapter.AktifInformasiFragment;
import com.example.cecenet.Pengaturan.MenuHome.InformasiAdapter.ViewPagerAdapter;
import com.example.cecenet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InformasiFragment extends Fragment implements View.OnClickListener {
    private MaterialEditText txtDurasiGantiInformasi;
    private Spinner spinnerAnimasiInformasi;
    private LinearLayout LLSettingInformasi;
    private TextView btnVisibleSettingInformasi;
    private TabLayout TLInformasi;
    private ViewPager VPInformasi;

    private String Tampil;
    private boolean VisibleInformasi;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    private String[] Animasi = {"Geser Atas", "Geser Bawah", "Memantul", "Membalik", "Membalik X", "Membalik Y", "Memperbesar", "Memudar", "Menggulung", "Teks Berjalan"};
    private String[] Animate = {"slideInUp", "slideInDown", "bounceIn", "flip", "flipInX", "flipInY", "zoomIn", "fadeIn", "rollIn", "marquee"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                                       = inflater.inflate(R.layout.fragment_informasi, container, false);

        txtDurasiGantiInformasi                         = view.findViewById(R.id.txtDurasiGantiInformasi);
        spinnerAnimasiInformasi                         = view.findViewById(R.id.spinnerAnimasiInformasi);
        Button btnSimpanSettingInformasi                = view.findViewById(R.id.btnSimpanSettingInformasi);
        LLSettingInformasi                              = view.findViewById(R.id.LLSettingInformasi);
        btnVisibleSettingInformasi                      = view.findViewById(R.id.btnVisibleSettingInformasi);
        TLInformasi                                     = view.findViewById(R.id.TLInformasi);
        VPInformasi                                     = view.findViewById(R.id.VPInformasi);
        FloatingActionButton btnTambahInformasi         = view.findViewById(R.id.btnTambahInformasi);

        AktifInformasiFragment aktifInformasiFragment   = new AktifInformasiFragment();
        progressDialog                                  = new ProgressDialog(getContext());

        btnVisibleSettingInformasi.setOnClickListener(this);
        btnSimpanSettingInformasi.setOnClickListener(this);
        btnTambahInformasi.setOnClickListener(this);

        TLInformasi.addTab(TLInformasi.newTab().setText(getString(R.string.AKTIF)));
        TLInformasi.addTab(TLInformasi.newTab().setText(getString(R.string.ARSIP)));
        TLInformasi.setTabGravity(TabLayout.GRAVITY_FILL);

        if(getActivity() != null && getContext() != null){
            spinnerAnimasiInformasi.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, Animasi));
            spinnerAnimasiInformasi.setPadding(0, 0, 0, 0);
            txtDurasiGantiInformasi.setPaddings(0, 12, 0, 0);

            VPInformasi.setAdapter(new ViewPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), TLInformasi.getTabCount()));
            VPInformasi.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TLInformasi));
            TLInformasi.setupWithViewPager(VPInformasi, true);

            FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.VPInformasi, aktifInformasiFragment);
            fragmentTransaction.commit();

            sharedPreferences = getContext().getSharedPreferences("VisibleSetting", Context.MODE_PRIVATE);
            if(sharedPreferences.getBoolean("VisibleSettingInformasi", true)){
                VisibleInformasi    = true;
                LLSettingInformasi.setVisibility(View.VISIBLE);
            }
            else{
                VisibleInformasi    = false;
                LLSettingInformasi.setVisibility(View.GONE);
            }
        }

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        settingInformasi();
        PushDownAnim.setPushDownAnimTo(btnVisibleSettingInformasi, btnSimpanSettingInformasi, btnTambahInformasi);

        return view;
    }

    private void settingInformasi() {
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanSettingInformasi")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String Sukses         = jsonObject.getString("Sukses");
                            JSONArray jsonArray   = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if(object.getString("Nama_Setting").equalsIgnoreCase("Durasi_Ganti(Detik)")){
                                        txtDurasiGantiInformasi.requestFocus();
                                        txtDurasiGantiInformasi.setText(object.getString("Isi_Setting"));
                                        txtDurasiGantiInformasi.setSelection(txtDurasiGantiInformasi.length());
                                    }
                                    else if(object.getString("Nama_Setting").equalsIgnoreCase("Animasi")){
                                        for(int j = 0; j < Animate.length; j++){
                                            if(Animate[j].equalsIgnoreCase(object.getString("Isi_Setting"))){
                                                spinnerAnimasiInformasi.setSelection(j);
                                            }
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

    @Override
    public void onClick(View v) {
        if(getContext() != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null){
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

        switch(v.getId()){
            case R.id.btnVisibleSettingInformasi :
                Animation animation = new RotateAnimation(0, 180, btnVisibleSettingInformasi.getWidth()/2, btnVisibleSettingInformasi.getHeight()/2);
                animation.setDuration(250);
                btnVisibleSettingInformasi.startAnimation(animation);
                txtDurasiGantiInformasi.requestFocus();

                if(VisibleInformasi && getContext() != null){
                    btnVisibleSettingInformasi.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.show));
                    YoYo.with(Techniques.SlideInUp).duration(300).playOn(LLSettingInformasi);
                    LLSettingInformasi.setVisibility(View.GONE);
                    VisibleInformasi = false;

                    sharedPreferences.edit().putBoolean("VisibleSettingInformasi", false).apply();
                }
                else{
                    if(getContext() != null){
                        btnVisibleSettingInformasi.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hide));
                        YoYo.with(Techniques.SlideInDown).duration(300).playOn(LLSettingInformasi);
                        LLSettingInformasi.setVisibility(View.VISIBLE);
                        VisibleInformasi = true;

                        sharedPreferences.edit().putBoolean("VisibleSettingInformasi", true).apply();
                    }
                }

                break;
            case R.id.btnSimpanSettingInformasi :
                if(txtDurasiGantiInformasi.getText() != null){
                    if(txtDurasiGantiInformasi.getText().toString().isEmpty()){
                        txtDurasiGantiInformasi.setError(getString(R.string.Jangan_Kosong_));
                        txtDurasiGantiInformasi.requestFocus();
                    }
                    else if(Integer.parseInt(txtDurasiGantiInformasi.getText().toString()) < 5){
                        txtDurasiGantiInformasi.setError(getString(R.string.Durasi_Ganti_Terlalu_Cepat));
                        txtDurasiGantiInformasi.requestFocus();
                    }
                    else if(Integer.parseInt(txtDurasiGantiInformasi.getText().toString()) > 3600){
                        txtDurasiGantiInformasi.setError(getString(R.string.Durasi_Ganti_Terlalu_Lama));
                        txtDurasiGantiInformasi.requestFocus();
                    }
                    else{
                        progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                                .addBodyParameter("Aksi", "SimpanSettingInformasi")
                                .addBodyParameter("DurasiGantiInformasi", txtDurasiGantiInformasi.getText().toString())
                                .addBodyParameter("AnimasiInformasi", Animate[spinnerAnimasiInformasi.getSelectedItemPosition()])
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equalsIgnoreCase("Tersimpan")){
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
            case R.id.btnTambahInformasi :
                tambahInformasi();

                break;
        }
    }

    private void tambahInformasi(){
        if(getContext() != null){
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.tambah_edit_informasi);

            if(dialog.getWindow() != null){
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
            }

            final TextView txtJudulInformasi                = dialog.findViewById(R.id.txtJudulInformasi);
            final MaterialEditText txtTambahEditInformasi   = dialog.findViewById(R.id.txtTambahEditInformasi);
            final RadioGroup rgInformasi                    = dialog.findViewById(R.id.rgInformasi);
            final RadioButton rbInformasiAktif              = dialog.findViewById(R.id.rbInformasiAktif);
            final RadioButton rbInformasiArsip              = dialog.findViewById(R.id.rbInformasiArsip);
            final Button btnSimpanInformasi                 = dialog.findViewById(R.id.btnSimpanInformasi);
            final Button btnBatalSimpanInformasi            = dialog.findViewById(R.id.btnBatalSimpanInformasi);

            txtJudulInformasi.setText(getString(R.string.tambah_informasi));
            txtTambahEditInformasi.requestFocus();
            PushDownAnim.setPushDownAnimTo(btnSimpanInformasi, btnBatalSimpanInformasi);

            btnBatalSimpanInformasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnSimpanInformasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtTambahEditInformasi.getText() != null && getActivity() != null && VPInformasi.getAdapter() != null){
                        if(txtTambahEditInformasi.getText().toString().trim().isEmpty()){
                            txtTambahEditInformasi.setError(getString(R.string.Jangan_Kosong_));
                            txtTambahEditInformasi.requestFocus();
                        }
                        else if(txtTambahEditInformasi.getText().length() <= 5){
                            txtTambahEditInformasi.setError(getString(R.string.Informasi_Terlalu_Singkat_));
                            txtTambahEditInformasi.requestFocus();
                        }
                        else{
                            progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            if(getContext() != null){
                                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                if(inputMethodManager != null){
                                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                }
                            }

                            if(rgInformasi.getCheckedRadioButtonId() == rbInformasiAktif.getId()){
                                Tampil = "Ya";
                            }
                            else if(rgInformasi.getCheckedRadioButtonId() == rbInformasiArsip.getId()){
                                Tampil = "Tidak";
                            }

                            AndroidNetworking.post(getResources().getString(R.string.URL) + "Beranda.php")
                                    .addBodyParameter("Aksi", "TambahInformasi")
                                    .addBodyParameter("Informasi", txtTambahEditInformasi.getText().toString())
                                    .addBodyParameter("Tampil", Tampil)
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equalsIgnoreCase("Tersimpan")){
                                                dialog.dismiss();
                                                progressDialog.dismiss();

                                                VPInformasi.setAdapter(new ViewPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), TLInformasi.getTabCount()));
                                                VPInformasi.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TLInformasi));
                                                VPInformasi.getAdapter().notifyDataSetChanged();

                                                if(Tampil.equalsIgnoreCase("Ya")){
                                                    TabLayout.Tab tab = TLInformasi.getTabAt(0);

                                                    if(tab != null){
                                                        tab.select();
                                                    }
                                                }
                                                else{
                                                    TabLayout.Tab tab = TLInformasi.getTabAt(1);

                                                    if(tab != null){
                                                        tab.select();
                                                    }
                                                }
                                            }
                                            else{
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }
            });
        }
    }
}