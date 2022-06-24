package com.example.cecenet.Pengaturan.MenuHome;

import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.cecenet.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IdentitasFragment extends Fragment {
    private MaterialEditText txtNamaMasjid, txtAlamatMasjid;
    private ProgressDialog progressDialog;
    private String ID_USER, NamaMasjid, AlamatMasjid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                   = inflater.inflate(R.layout.fragment_identitas, container, false);

        txtNamaMasjid               = view.findViewById(R.id.txtNamaMasjid);
        txtAlamatMasjid             = view.findViewById(R.id.txtAlamatMasjid);
        Button btnSimpanIdentitas   = view.findViewById(R.id.btnSimpanIdentitas);
        progressDialog              = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        DapatkanIdentitas();
        PushDownAnim.setPushDownAnimTo(btnSimpanIdentitas);
        btnSimpanIdentitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpanIdentitas(v);
            }
        });

        return view;
    }

    private void DapatkanIdentitas(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanIdentitas")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject   = response.getJSONObject(i);

                                ID_USER                 = jsonObject.getString("ID_USER");
                                NamaMasjid              = jsonObject.getString("Nama_Masjid");
                                AlamatMasjid            = jsonObject.getString("Alamat_Masjid");

                                txtNamaMasjid.setText(jsonObject.getString("Nama_Masjid"));
                                txtAlamatMasjid.setText(jsonObject.getString("Alamat_Masjid"));
                            }
                            progressDialog.dismiss();
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

    private void SimpanIdentitas(View view){
        if(getContext() != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if(txtNamaMasjid.getText() != null && txtAlamatMasjid.getText() != null && inputMethodManager != null){
                if(txtNamaMasjid.getText().toString().trim().isEmpty()){
                    txtNamaMasjid.setError(getString(R.string.Jangan_Kosong_));
                    txtNamaMasjid.requestFocus();
                }
                else if(txtAlamatMasjid.getText().toString().trim().isEmpty()){
                    txtAlamatMasjid.setError(getString(R.string.Jangan_Kosong_));
                    txtAlamatMasjid.requestFocus();
                }
                else{
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if(txtNamaMasjid.getText().toString().equals(NamaMasjid) && txtAlamatMasjid.getText().toString().equals(AlamatMasjid)){
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.Tidak_Ada_Perubahan_yang_Disimpan), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                                .addBodyParameter("Aksi", "SimpanIdentitas")
                                .addBodyParameter("ID_USER", ID_USER)
                                .addBodyParameter("Nama_Masjid", txtNamaMasjid.getText().toString())
                                .addBodyParameter("Alamat_Masjid", txtAlamatMasjid.getText().toString())
                                .setPriority(Priority.LOW)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equalsIgnoreCase("Tersimpan")){
                                            NamaMasjid      = txtNamaMasjid.getText().toString();
                                            AlamatMasjid    = txtAlamatMasjid.getText().toString();

                                            progressDialog.dismiss();
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
                }
            }
        }
    }
}