package com.example.cecenet.Pengaturan.MenuMenujuIqomah;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.cecenet.R;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenujuIqomahCountdownFragment extends Fragment {
    private EditText txtCountdownMenitMenujuIqomahSubuh, txtCountdownMenitMenujuIqomahDzuhur, txtCountdownMenitMenujuIqomahAshar, txtCountdownMenitMenujuIqomahMaghrib,
            txtCountdownMenitMenujuIqomahIsya, txtCountdownDetikMenujuIqomahSubuh, txtCountdownDetikMenujuIqomahDzuhur, txtCountdownDetikMenujuIqomahAshar,
            txtCountdownDetikMenujuIqomahMaghrib, txtCountdownDetikMenujuIqomahIsya;

    private Button btnSimpanMenuCountdown;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                               = inflater.inflate(R.layout.fragment_countdown, container, false);

        txtCountdownMenitMenujuIqomahSubuh      = view.findViewById(R.id.txtCountdownMenitMenujuIqomahSubuh);
        txtCountdownMenitMenujuIqomahDzuhur     = view.findViewById(R.id.txtCountdownMenitMenujuIqomahDzuhur);
        txtCountdownMenitMenujuIqomahAshar      = view.findViewById(R.id.txtCountdownMenitMenujuIqomahAshar);
        txtCountdownMenitMenujuIqomahMaghrib    = view.findViewById(R.id.txtCountdownMenitMenujuIqomahMaghrib);
        txtCountdownMenitMenujuIqomahIsya       = view.findViewById(R.id.txtCountdownMenitMenujuIqomahIsya);
        txtCountdownDetikMenujuIqomahSubuh      = view.findViewById(R.id.txtCountdownDetikMenujuIqomahSubuh);
        txtCountdownDetikMenujuIqomahDzuhur     = view.findViewById(R.id.txtCountdownDetikMenujuIqomahDzuhur);
        txtCountdownDetikMenujuIqomahAshar      = view.findViewById(R.id.txtCountdownDetikMenujuIqomahAshar);
        txtCountdownDetikMenujuIqomahMaghrib    = view.findViewById(R.id.txtCountdownDetikMenujuIqomahMaghrib);
        txtCountdownDetikMenujuIqomahIsya       = view.findViewById(R.id.txtCountdownDetikMenujuIqomahIsya);
        btnSimpanMenuCountdown                  = view.findViewById(R.id.btnSimpanMenuCountdown);
        progressDialog                          = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        settingCountdownMenujuIqomah();
        validasiCountdown();
        simpanMenuCountdown();
        PushDownAnim.setPushDownAnimTo(btnSimpanMenuCountdown);

        return view;
    }

    private void settingCountdownMenujuIqomah(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "DapatkanSettingCountdownMenujuIqomah")
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
                                    String Nama_Salat   = object.getString("Nama_Salat");

                                    if(Nama_Salat.equalsIgnoreCase("Subuh")){
                                        txtCountdownMenitMenujuIqomahSubuh.setText(object.getString("Menuju_Iqomah").substring(0, 2));
                                        txtCountdownDetikMenujuIqomahSubuh.setText(object.getString("Menuju_Iqomah").substring(3, 5));
                                    }
                                    else if(Nama_Salat.equalsIgnoreCase("Dzuhur")){
                                        txtCountdownMenitMenujuIqomahDzuhur.setText(object.getString("Menuju_Iqomah").substring(0, 2));
                                        txtCountdownDetikMenujuIqomahDzuhur.setText(object.getString("Menuju_Iqomah").substring(3, 5));
                                    }
                                    else if(Nama_Salat.equalsIgnoreCase("Ashar")){
                                        txtCountdownMenitMenujuIqomahAshar.setText(object.getString("Menuju_Iqomah").substring(0, 2));
                                        txtCountdownDetikMenujuIqomahAshar.setText(object.getString("Menuju_Iqomah").substring(3, 5));
                                    }
                                    else if(Nama_Salat.equalsIgnoreCase("Maghrib")){
                                        txtCountdownMenitMenujuIqomahMaghrib.setText(object.getString("Menuju_Iqomah").substring(0, 2));
                                        txtCountdownDetikMenujuIqomahMaghrib.setText(object.getString("Menuju_Iqomah").substring(3, 5));
                                    }
                                    else if(Nama_Salat.equalsIgnoreCase("Isya\'")){
                                        txtCountdownMenitMenujuIqomahIsya.setText(object.getString("Menuju_Iqomah").substring(0, 2));
                                        txtCountdownDetikMenujuIqomahIsya.setText(object.getString("Menuju_Iqomah").substring(3, 5));
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

    private void validasiCountdown(){
        txtCountdownMenitMenujuIqomahSubuh.setSelectAllOnFocus(true);
        txtCountdownMenitMenujuIqomahDzuhur.setSelectAllOnFocus(true);
        txtCountdownMenitMenujuIqomahAshar.setSelectAllOnFocus(true);
        txtCountdownMenitMenujuIqomahMaghrib.setSelectAllOnFocus(true);
        txtCountdownMenitMenujuIqomahIsya.setSelectAllOnFocus(true);
        txtCountdownDetikMenujuIqomahSubuh.setSelectAllOnFocus(true);
        txtCountdownDetikMenujuIqomahDzuhur.setSelectAllOnFocus(true);
        txtCountdownDetikMenujuIqomahAshar.setSelectAllOnFocus(true);
        txtCountdownDetikMenujuIqomahMaghrib.setSelectAllOnFocus(true);
        txtCountdownDetikMenujuIqomahIsya.setSelectAllOnFocus(true);

        txtCountdownMenitMenujuIqomahSubuh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtCountdownDetikMenujuIqomahSubuh.requestFocus();
                }
            }
        });

        txtCountdownMenitMenujuIqomahDzuhur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtCountdownDetikMenujuIqomahDzuhur.requestFocus();
                }
            }
        });

        txtCountdownMenitMenujuIqomahAshar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtCountdownDetikMenujuIqomahAshar.requestFocus();
                }
            }
        });

        txtCountdownMenitMenujuIqomahMaghrib.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtCountdownDetikMenujuIqomahMaghrib.requestFocus();
                }
            }
        });

        txtCountdownMenitMenujuIqomahIsya.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtCountdownDetikMenujuIqomahIsya.requestFocus();
                }
            }
        });

        txtCountdownDetikMenujuIqomahIsya.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2){
                    txtCountdownDetikMenujuIqomahIsya.clearFocus();
                }
            }
        });
    }

    private void simpanMenuCountdown(){
        btnSimpanMenuCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager != null){
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }

                progressDialog.setMessage(getString(R.string.Menyimpan_Data));
                progressDialog.setCancelable(false);
                progressDialog.show();

                String MenitSubuh, MenitDzuhur, MenitAshar, MenitMaghrib, MenitIsya, DetikSubuh, DetikDzuhur, DetikAshar, DetikMaghrib, DetikIsya;

                if(txtCountdownMenitMenujuIqomahSubuh.getText().length() == 0){
                    MenitSubuh      = "00";
                }
                else if(txtCountdownMenitMenujuIqomahSubuh.getText().length() == 1){
                    MenitSubuh      = "0" + txtCountdownMenitMenujuIqomahSubuh.getText().toString();
                }
                else{
                    MenitSubuh      = txtCountdownMenitMenujuIqomahSubuh.getText().toString();
                }

                if(txtCountdownMenitMenujuIqomahDzuhur.getText().length() == 0){
                    MenitDzuhur     = "00";
                }
                else if(txtCountdownMenitMenujuIqomahDzuhur.getText().length() == 1){
                    MenitDzuhur     = "0" + txtCountdownMenitMenujuIqomahDzuhur.getText().toString();
                }
                else{
                    MenitDzuhur     = txtCountdownMenitMenujuIqomahDzuhur.getText().toString();
                }

                if(txtCountdownMenitMenujuIqomahAshar.getText().length() == 0){
                    MenitAshar      = "00";
                }
                else if(txtCountdownMenitMenujuIqomahAshar.getText().length() == 1){
                    MenitAshar      = "0" + txtCountdownMenitMenujuIqomahAshar.getText().toString();
                }
                else{
                    MenitAshar      = txtCountdownMenitMenujuIqomahAshar.getText().toString();
                }

                if(txtCountdownMenitMenujuIqomahMaghrib.getText().length() == 0){
                    MenitMaghrib    = "00";
                }
                else if(txtCountdownMenitMenujuIqomahMaghrib.getText().length() == 1){
                    MenitMaghrib    = "0" + txtCountdownMenitMenujuIqomahMaghrib.getText().toString();
                }
                else{
                    MenitMaghrib    = txtCountdownMenitMenujuIqomahMaghrib.getText().toString();
                }

                if(txtCountdownMenitMenujuIqomahIsya.getText().length() == 0){
                    MenitIsya       = "00";
                }
                else if(txtCountdownMenitMenujuIqomahIsya.getText().length() == 1){
                    MenitIsya       = "0" + txtCountdownMenitMenujuIqomahIsya.getText().toString();
                }
                else{
                    MenitIsya       = txtCountdownMenitMenujuIqomahIsya.getText().toString();
                }

                if(txtCountdownDetikMenujuIqomahSubuh.getText().length() == 0){
                    DetikSubuh      = "00";
                }
                else if(txtCountdownDetikMenujuIqomahSubuh.getText().length() == 1){
                    DetikSubuh      = "0" + txtCountdownDetikMenujuIqomahSubuh.getText().toString();
                }
                else{
                    DetikSubuh      = txtCountdownDetikMenujuIqomahSubuh.getText().toString();
                }

                if(txtCountdownDetikMenujuIqomahDzuhur.getText().length() == 0){
                    DetikDzuhur     = "00";
                }
                else if(txtCountdownDetikMenujuIqomahDzuhur.getText().length() == 1){
                    DetikDzuhur     = "0" + txtCountdownDetikMenujuIqomahDzuhur.getText().toString();
                }
                else{
                    DetikDzuhur     = txtCountdownDetikMenujuIqomahDzuhur.getText().toString();
                }

                if(txtCountdownDetikMenujuIqomahAshar.getText().length() == 0){
                    DetikAshar      = "00";
                }
                else if(txtCountdownDetikMenujuIqomahAshar.getText().length() == 1){
                    DetikAshar      = "0" + txtCountdownDetikMenujuIqomahAshar.getText().toString();
                }
                else{
                    DetikAshar      = txtCountdownDetikMenujuIqomahAshar.getText().toString();
                }

                if(txtCountdownDetikMenujuIqomahMaghrib.getText().length() == 0){
                    DetikMaghrib    = "00";
                }
                else if(txtCountdownDetikMenujuIqomahMaghrib.getText().length() == 1){
                    DetikMaghrib    = "0" + txtCountdownDetikMenujuIqomahMaghrib.getText().toString();
                }
                else{
                    DetikMaghrib    = txtCountdownDetikMenujuIqomahMaghrib.getText().toString();
                }

                if(txtCountdownDetikMenujuIqomahIsya.getText().length() == 0){
                    DetikIsya       = "00";
                }
                else if(txtCountdownDetikMenujuIqomahIsya.getText().length() == 1){
                    DetikIsya       = "0" + txtCountdownDetikMenujuIqomahIsya.getText().toString();
                }
                else{
                    DetikIsya       = txtCountdownDetikMenujuIqomahIsya.getText().toString();
                }

                AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                        .addBodyParameter("Aksi", "SimpanSettingCountdownMenujuIqomah")
                        .addBodyParameter("Subuh", MenitSubuh + ":" + DetikSubuh)
                        .addBodyParameter("Dzuhur", MenitDzuhur + ":" + DetikDzuhur)
                        .addBodyParameter("Ashar", MenitAshar + ":" + DetikAshar)
                        .addBodyParameter("Maghrib", MenitMaghrib + ":" + DetikMaghrib)
                        .addBodyParameter("Isya", MenitIsya + ":" + DetikIsya)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("Tersimpan")){
                                    Toast.makeText(getContext(), getString(R.string.Berhasil_Menyimpan_Perubahan), Toast.LENGTH_SHORT).show();
                                    settingCountdownMenujuIqomah();
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
        });
    }
}