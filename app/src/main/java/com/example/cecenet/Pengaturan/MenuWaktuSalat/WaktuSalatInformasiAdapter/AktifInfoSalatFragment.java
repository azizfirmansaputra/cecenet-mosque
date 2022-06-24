package com.example.cecenet.Pengaturan.MenuWaktuSalat.WaktuSalatInformasiAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.cecenet.Pengaturan.MenuHome.InformasiAdapter.Informasi;
import com.example.cecenet.Pengaturan.MenuHome.InformasiAdapter.MyAdapterInformasi;
import com.example.cecenet.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AktifInfoSalatFragment extends Fragment {
    private SwipeRefreshLayout SRLInformasi;
    private ListView LVInformasi;

    private Informasi informasi;
    private MyAdapterInformasi myAdapterInformasi;
    private static ArrayList<Informasi> InformasiArrayList  = new ArrayList<>();
    private ArrayList<String> InformasiAktifAction          = new ArrayList<>();

    private String ID_InfoSalat, Info;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_tab_informasi, container, false);

        SRLInformasi    = view.findViewById(R.id.SRLInformasi);
        LVInformasi     = view.findViewById(R.id.LVInformasi);
        progressDialog  = new ProgressDialog(getContext());

        if(getContext() != null){
            myAdapterInformasi = new MyAdapterInformasi(getContext(), InformasiArrayList);
            LVInformasi.setAdapter(myAdapterInformasi);
        }

        LVInformasi.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        LVInformasi.setMultiChoiceModeListener(modeListener);
        LVInformasi.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

        SRLInformasi.setRefreshing(true);
        tampilInformasi();

        SRLInformasi.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tampilInformasi();
            }
        });

        return view;
    }

    private void tampilInformasi(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanInfoSalatAktif")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        InformasiArrayList.clear();
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object   = jsonArray.getJSONObject(i);

                                    String ID_InfoSalat = object.getString("ID_InfoSalat");
                                    String Info         = object.getString("Info");

                                    informasi           = new Informasi(ID_InfoSalat, Info);
                                    InformasiArrayList.add(informasi);
                                    LVInformasi.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                    myAdapterInformasi.notifyDataSetChanged();
                                }
                                SRLInformasi.setRefreshing(false);
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        SRLInformasi.setRefreshing(false);
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            switch(LVInformasi.getCheckedItemCount()){
                case 0  :
                    mode.setTitle(null);
                    break;
                case 1  :
                    mode.setTitle(R.string.SATU_INFORMASI);
                    break;
                default :
                    mode.setTitle(LVInformasi.getCheckedItemCount() + " " + getString(R.string.INFORMASI));
                    break;
            }

            if(checked){
                ID_InfoSalat    = InformasiArrayList.get(position).getID_INFO();
                Info            = InformasiArrayList.get(position).getInfo();

                InformasiAktifAction.add(InformasiArrayList.get(position).getID_INFO());
            }
            else{
                InformasiAktifAction.remove(InformasiArrayList.get(position).getID_INFO());
            }

            mode.invalidate();
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_informasi, menu);
            InformasiAktifAction.clear();

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.action_aktif).setVisible(false);
            LVInformasi.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

            if(LVInformasi.getCheckedItemCount() == 1){
                menu.findItem(R.id.action_edit).setVisible(true);
            }
            else{
                menu.findItem(R.id.action_edit).setVisible(false);
            }

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
                case R.id.action_edit :
                    EditInformasi(mode);
                    break;
                case R.id.action_arsip :
                    InformasiArsip(mode);
                    break;
                case R.id.action_hapus :
                    KonfirmasiHapus(mode);
                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) { }
    };

    private void EditInformasi(final ActionMode mode){
        if(getContext() != null){
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.tambah_edit_informasi);

            if(dialog.getWindow() != null) {
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
            }

            final TextView txtJudulInformasi                = dialog.findViewById(R.id.txtJudulInformasi);
            final MaterialEditText txtTambahEditInformasi   = dialog.findViewById(R.id.txtTambahEditInformasi);
            final RadioGroup rgInformasi                    = dialog.findViewById(R.id.rgInformasi);
            final Button btnSimpanInformasi                 = dialog.findViewById(R.id.btnSimpanInformasi);
            final Button btnBatalSimpanInformasi            = dialog.findViewById(R.id.btnBatalSimpanInformasi);

            txtJudulInformasi.setText(R.string.EDIT_INFO_SALAT);
            txtTambahEditInformasi.setText(Info);
            txtTambahEditInformasi.requestFocus();
            rgInformasi.setVisibility(View.GONE);
            PushDownAnim.setPushDownAnimTo(btnSimpanInformasi, btnBatalSimpanInformasi);

            btnBatalSimpanInformasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mode.finish();
                    dialog.dismiss();
                }
            });

            btnSimpanInformasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtTambahEditInformasi.getText() != null && getActivity() != null){
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

                            if(txtTambahEditInformasi.getText().toString().equals(Info)){
                                mode.finish();
                                dialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), getString(R.string.Tidak_Ada_Perubahan_yang_Disimpan), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                                        .addBodyParameter("Aksi", "EditInfoSalat")
                                        .addBodyParameter("ID_InfoSalat", ID_InfoSalat)
                                        .addBodyParameter("InfoSalat", txtTambahEditInformasi.getText().toString())
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsString(new StringRequestListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                if(response.equalsIgnoreCase("Tersimpan")){
                                                    mode.finish();
                                                    dialog.dismiss();
                                                    tampilInformasi();
                                                    Toast.makeText(getContext(), getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                                }
                                                progressDialog.dismiss();
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                mode.finish();
                                                dialog.dismiss();
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                }
            });
        }
    }

    private void InformasiArsip(final ActionMode mode){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "InfoSalatArsip")
                .addBodyParameter("InformasiAktifAction", InformasiAktifAction.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Informasi di Arsipkan")){
                                mode.finish();
                                tampilInformasi();
                                Toast.makeText(getContext(), InformasiAktifAction.size() + " " + getString(R.string.Informasi_di_Arsipkan), Toast.LENGTH_SHORT).show();
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

    private void KonfirmasiHapus(final ActionMode mode){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle(getString(R.string.Konfirmasi))
                .setMessage(getString(R.string.Apakah_Yakin_Ingin_Menghapus) + " " + InformasiAktifAction.size() + " " + getString(R.string.Informasi_ini_))
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
                .addBodyParameter("Aksi", "HapusInfoSalat")
                .addBodyParameter("InformasiAktifAction", InformasiAktifAction.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Informasi Terhapus")){
                                mode.finish();
                                tampilInformasi();
                                Toast.makeText(getContext(), InformasiAktifAction.size() + " " + getString(R.string.Informasi_Terhapus), Toast.LENGTH_SHORT).show();
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
}