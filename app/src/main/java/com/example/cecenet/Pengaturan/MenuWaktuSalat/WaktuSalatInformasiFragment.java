package com.example.cecenet.Pengaturan.MenuWaktuSalat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.cecenet.Pengaturan.MenuWaktuSalat.WaktuSalatInformasiAdapter.AktifInfoSalatFragment;
import com.example.cecenet.Pengaturan.MenuWaktuSalat.WaktuSalatInformasiAdapter.ViewPagerAdapter;
import com.example.cecenet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class WaktuSalatInformasiFragment extends Fragment implements View.OnClickListener {
    private TabLayout TLInfoSalat;
    private ViewPager VPInfoSalat;

    private String Tampil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                               = inflater.inflate(R.layout.fragment_info_salat, container, false);

        TLInfoSalat                             = view.findViewById(R.id.TLInfoSalat);
        VPInfoSalat                             = view.findViewById(R.id.VPInfoSalat);
        FloatingActionButton btnTambahInfoSalat = view.findViewById(R.id.btnTambahInfoSalat);

        btnTambahInfoSalat.setOnClickListener(this);

        TLInfoSalat.addTab(TLInfoSalat.newTab().setText(getString(R.string.AKTIF)));
        TLInfoSalat.addTab(TLInfoSalat.newTab().setText(getString(R.string.ARSIP)));
        TLInfoSalat.setTabGravity(TabLayout.GRAVITY_FILL);

        if(getActivity() != null && getContext() != null){
            VPInfoSalat.setAdapter(new ViewPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), TLInfoSalat.getTabCount()));
            VPInfoSalat.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TLInfoSalat));
            TLInfoSalat.setupWithViewPager(VPInfoSalat, true);

            FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.VPInfoSalat, new AktifInfoSalatFragment());
            fragmentTransaction.commit();
        }

        PushDownAnim.setPushDownAnimTo(btnTambahInfoSalat);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnTambahInfoSalat){
            tambahInfoSalat();
        }
    }

    private void tambahInfoSalat(){
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
            final ProgressDialog progressDialog             = new ProgressDialog(getContext());

            txtJudulInformasi.setText(getString(R.string.TAMBAH_INFO_SALAT));
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
                    if(txtTambahEditInformasi.getText() != null && getActivity() != null && VPInfoSalat.getAdapter() != null){
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
                                    .addBodyParameter("Aksi", "TambahInfoSalat")
                                    .addBodyParameter("InfoSalat", txtTambahEditInformasi.getText().toString())
                                    .addBodyParameter("Tampil", Tampil)
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equalsIgnoreCase("Tersimpan")){
                                                dialog.dismiss();
                                                progressDialog.dismiss();

                                                VPInfoSalat.setAdapter(new ViewPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), TLInfoSalat.getTabCount()));
                                                VPInfoSalat.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TLInfoSalat));
                                                VPInfoSalat.getAdapter().notifyDataSetChanged();

                                                if(Tampil.equalsIgnoreCase("Ya")){
                                                    TabLayout.Tab tab = TLInfoSalat.getTabAt(0);

                                                    if(tab != null){
                                                        tab.select();
                                                    }
                                                }
                                                else{
                                                    TabLayout.Tab tab = TLInfoSalat.getTabAt(1);

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