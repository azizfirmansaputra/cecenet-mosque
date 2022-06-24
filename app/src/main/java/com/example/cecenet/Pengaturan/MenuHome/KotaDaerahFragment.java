package com.example.cecenet.Pengaturan.MenuHome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.Pengaturan.MenuHome.KotaDaerahAdapter.Daerah;
import com.example.cecenet.Pengaturan.MenuHome.KotaDaerahAdapter.MyAdapterDaerah;
import com.example.cecenet.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class KotaDaerahFragment extends Fragment implements View.OnClickListener {
    private TextView txtKota_Daerah, txtDetailKota_Daerah;
    private AppCompatAutoCompleteTextView txtEditKota_Daerah;
    private EditText txtLongitude, txtLatitude, txtZonaWaktu;
    private Button btnUbahKota_Daerah, btnTambahKota_Daerah;
    private CardView CVTambahKota_Daerah;
    private MaterialEditText txtTambahNamaKota_Daerah, txtTambahDetailKota_Daerah, txtTambahLongitudeKota_Daerah, txtTambahZonaWaktuKota_Daerah,txtTambahLatitudeKota_Daerah;

    private Daerah daerah;
    private MyAdapterDaerah myAdapterDaerah;
    private ArrayList<Daerah> daerahArrayList = new ArrayList<>();

    private ProgressDialog progressDialog;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                           = inflater.inflate(R.layout.fragment_kota_daerah, container, false);

        txtKota_Daerah                      = view.findViewById(R.id.txtKota_Daerah);
        txtDetailKota_Daerah                = view.findViewById(R.id.txtDetailKota_Daerah);
        txtLongitude                        = view.findViewById(R.id.txtLongitude);
        txtLatitude                         = view.findViewById(R.id.txtLatitude);
        txtZonaWaktu                        = view.findViewById(R.id.txtZona_Waktu);
        txtEditKota_Daerah                  = view.findViewById(R.id.txtEditKota_Daerah);
        btnUbahKota_Daerah                  = view.findViewById(R.id.btnUbahKota_Daerah);
        btnTambahKota_Daerah                = view.findViewById(R.id.btnTambahKota_Daerah);

        CVTambahKota_Daerah                 = view.findViewById(R.id.CVTambahKota_Daerah);
        txtTambahNamaKota_Daerah            = view.findViewById(R.id.txtTambahNamaKota_Daerah);
        txtTambahDetailKota_Daerah          = view.findViewById(R.id.txtTambahDetailKota_Daerah);
        txtTambahLongitudeKota_Daerah       = view.findViewById(R.id.txtTambahLongitudeKota_Daerah);
        txtTambahLatitudeKota_Daerah        = view.findViewById(R.id.txtTambahLatitudeKota_Daerah);
        txtTambahZonaWaktuKota_Daerah       = view.findViewById(R.id.txtTambahZonaWaktuKota_Daerah);
        Button btnBatalTambahKota_Daerah    = view.findViewById(R.id.btnBatalTambahKota_Daerah);
        Button btnLokasiTambahKota_Daerah   = view.findViewById(R.id.btnLokasiTambahKota_Daerah);
        Button btnSimpanTambahKota_Daerah   = view.findViewById(R.id.btnSimpanTambahKota_Daerah);
        progressDialog                      = new ProgressDialog(getContext());

        btnUbahKota_Daerah.setOnClickListener(this);
        btnTambahKota_Daerah.setOnClickListener(this);
        btnBatalTambahKota_Daerah.setOnClickListener(this);
        btnLokasiTambahKota_Daerah.setOnClickListener(this);
        btnSimpanTambahKota_Daerah.setOnClickListener(this);

        progressDialog.setMessage(getString(R.string.Memuat_Data));
        progressDialog.setCancelable(false);
        progressDialog.show();

        cekKotaDaerah();
        listKotaDaerah();
        PushDownAnim.setPushDownAnimTo(btnUbahKota_Daerah, btnTambahKota_Daerah, btnBatalTambahKota_Daerah, btnLokasiTambahKota_Daerah, btnSimpanTambahKota_Daerah);

        if(getContext() != null){
            myAdapterDaerah = new MyAdapterDaerah(getContext(), R.layout.list_daerah, daerahArrayList);
            txtEditKota_Daerah.setThreshold(1);
            txtEditKota_Daerah.setAdapter(myAdapterDaerah);
        }

        txtEditKota_Daerah.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                daerah = (Daerah)parent.getItemAtPosition(position);
                txtEditKota_Daerah.setText(daerah.getNama_KotaDaerah());
                txtEditKota_Daerah.setSelection(txtEditKota_Daerah.getText().length());
            }
        });

        txtEditKota_Daerah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                listKotaDaerah();
            }
        });

        return view;
    }

    private void cekKotaDaerah(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "CekKotaDaerah")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    @SuppressLint("SetTextI18n")
                    public void onResponse(JSONArray response) {
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);

                                txtKota_Daerah.setText(jsonObject.getString("Nama_Kota"));
                                txtEditKota_Daerah.setText(jsonObject.getString("Nama_Kota"));
                                txtDetailKota_Daerah.setText(jsonObject.getString("Detail_Kota"));
                                txtLongitude.setText(getString(R.string.Long_) + " " + jsonObject.getString("Longitude_Kota"));
                                txtLatitude.setText(getString(R.string.Lat_) + " " + jsonObject.getString("Latitude_Kota"));
                                txtZonaWaktu.setText(jsonObject.getString("ZonaWaktu_Kota"));
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

    private void listKotaDaerah(){
        AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "ListKotaDaerah")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        daerahArrayList.clear();
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            String Sukses           = jsonObject.getString("Sukses");
                            JSONArray jsonArray     = jsonObject.getJSONArray("Data");

                            if(Sukses.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object   = jsonArray.getJSONObject(i);

                                    String Nama_Kota    = object.getString("Nama_Kota");
                                    String Detail_Kota  = object.getString("Detail_Kota");

                                    if(Nama_Kota.toLowerCase().startsWith(txtEditKota_Daerah.getText().toString().toLowerCase())){
                                        daerahArrayList.add(new Daerah(Nama_Kota, Detail_Kota));
                                        myAdapterDaerah.notifyDataSetChanged();
                                    }
                                }

                                if(daerahArrayList.size() == 0){
                                    daerahArrayList.add(new Daerah("", getString(R.string.Nama_Kota_atau_Daerah_tidak_Tersimpan)));
                                    myAdapterDaerah.notifyDataSetChanged();
                                }
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

    @Override
    public void onClick(View v) {
        if(getContext() != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null){
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

        switch(v.getId()){
            case R.id.btnUbahKota_Daerah :
                if(btnUbahKota_Daerah.getText().toString().equalsIgnoreCase(getString(R.string.ubah))){
                    btnUbahKota_Daerah.setText(R.string.ATUR);
                    txtKota_Daerah.setVisibility(View.GONE);
                    txtEditKota_Daerah.setVisibility(View.VISIBLE);
                    btnTambahKota_Daerah.setVisibility(View.VISIBLE);
                    CVTambahKota_Daerah.setVisibility(View.GONE);
                    txtEditKota_Daerah.requestFocus();
                    txtEditKota_Daerah.setSelection(txtEditKota_Daerah.getText().length());

                    YoYo.with(Techniques.FlipInX).duration(300).playOn(txtKota_Daerah);
                    YoYo.with(Techniques.FlipInX).duration(300).playOn(txtEditKota_Daerah);
                    YoYo.with(Techniques.SlideInLeft).duration(300).playOn(btnUbahKota_Daerah);
                    YoYo.with(Techniques.SlideInUp).duration(300).playOn(CVTambahKota_Daerah);
                }
                else if(btnUbahKota_Daerah.getText().toString().equalsIgnoreCase(getString(R.string.ATUR))){
                    ubahKotaDaerah();
                }

                break;
            case R.id.btnTambahKota_Daerah :
                txtEditKota_Daerah.setVisibility(View.GONE);
                txtKota_Daerah.setVisibility(View.VISIBLE);
                btnTambahKota_Daerah.setVisibility(View.GONE);
                CVTambahKota_Daerah.setVisibility(View.VISIBLE);
                btnUbahKota_Daerah.setText(getString(R.string.ubah));

                YoYo.with(Techniques.FlipInX).duration(300).playOn(txtEditKota_Daerah);
                YoYo.with(Techniques.SlideInDown).duration(300).playOn(btnTambahKota_Daerah);
                YoYo.with(Techniques.FadeInDown).duration(300).playOn(CVTambahKota_Daerah);

                txtTambahNamaKota_Daerah.setText("");
                txtTambahDetailKota_Daerah.setText("");
                txtTambahLongitudeKota_Daerah.setText("");
                txtTambahLatitudeKota_Daerah.setText("");
                txtTambahZonaWaktuKota_Daerah.setText("");

                break;
            case R.id.btnBatalTambahKota_Daerah :
                btnTambahKota_Daerah.setVisibility(View.VISIBLE);
                CVTambahKota_Daerah.setVisibility(View.GONE);

                YoYo.with(Techniques.SlideInUp).duration(300).playOn(btnTambahKota_Daerah);
                YoYo.with(Techniques.FadeInUp).duration(300).playOn(CVTambahKota_Daerah);

                break;
            case R.id.btnLokasiTambahKota_Daerah :
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    if(getActivity() != null){
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                }
                else{
                    progressDialog.setMessage(getString(R.string.Memuat_Data));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    if(locationManager != null){
                        location        = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location == null){
                            location    = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        lokasiTerakhir();
                    }
                }

                break;
            case R.id.btnSimpanTambahKota_Daerah :
                tambahKotaDaerah();

                break;
        }
    }

    private void ubahKotaDaerah(){
        if(txtEditKota_Daerah.getText().toString().trim().isEmpty()){
            YoYo.with(Techniques.Shake).duration(100).playOn(txtEditKota_Daerah);
            txtEditKota_Daerah.setError(getString(R.string.Jangan_Kosong_));
        }
        else{
            AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                    .addBodyParameter("Aksi", "UbahKotaDaerah")
                    .addBodyParameter("Kota_Daerah", txtEditKota_Daerah.getText().toString())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Tersimpan")){
                                cekKotaDaerah();
                                btnUbahKota_Daerah.setText(getString(R.string.ubah));
                                txtKota_Daerah.setVisibility(View.VISIBLE);
                                txtEditKota_Daerah.setVisibility(View.GONE);

                                YoYo.with(Techniques.FlipInX).duration(300).playOn(txtKota_Daerah);
                                YoYo.with(Techniques.SlideInLeft).duration(300).playOn(btnUbahKota_Daerah);
                            }
                            else if(response.equalsIgnoreCase("Tidak Ada")){
                                YoYo.with(Techniques.Shake).duration(100).playOn(txtEditKota_Daerah);
                                txtEditKota_Daerah.setError(getString(R.string.Kota_atau_Daerah_Tidak_ditemukan));
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
    }

    @SuppressLint("SimpleDateFormat")
    private void lokasiTerakhir(){
        if(location != null){
            try{
                List<Address> addressList;
                Geocoder geocoder   = new Geocoder(getContext(), Locale.getDefault());
                addressList         = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                Calendar calendar   = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
                Date calendarTime   = calendar.getTime();
                DateFormat format   = new SimpleDateFormat("Z");

                String NamaKota     = addressList.get(0).getLocality();
                String DetailKota   = addressList.get(0).getAddressLine(0);
                String Longitude    = String.valueOf(location.getLongitude());
                String Latitude     = String.valueOf(location.getLatitude());
                String ZonaWaktu    = format.format(calendarTime).replace("0", "");

                txtTambahNamaKota_Daerah.setText(NamaKota);
                txtTambahDetailKota_Daerah.setText(DetailKota);
                txtTambahLongitudeKota_Daerah.setText(Longitude);
                txtTambahLatitudeKota_Daerah.setText(Latitude);
                txtTambahZonaWaktuKota_Daerah.setText(ZonaWaktu);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        progressDialog.dismiss();
    }

    private void tambahKotaDaerah(){
        if(Objects.requireNonNull(txtTambahNamaKota_Daerah.getText()).toString().trim().isEmpty()){
            txtTambahNamaKota_Daerah.setError(getText(R.string.Jangan_Kosong_));
            txtTambahNamaKota_Daerah.requestFocus();
        }
        else if(txtTambahNamaKota_Daerah.getText().length() <= 2){
            txtTambahNamaKota_Daerah.setError(getString(R.string.Terlalu_Pendek_));
            txtTambahNamaKota_Daerah.requestFocus();
        }
        else if(Objects.requireNonNull(txtTambahDetailKota_Daerah.getText()).toString().trim().isEmpty()){
            txtTambahDetailKota_Daerah.setError(getText(R.string.Jangan_Kosong_));
            txtTambahDetailKota_Daerah.requestFocus();
        }
        else if(txtTambahDetailKota_Daerah.getText().length() <= 20){
            txtTambahDetailKota_Daerah.setError(getString(R.string.Terlalu_Pendek_));
            txtTambahDetailKota_Daerah.requestFocus();
        }
        else if(Objects.requireNonNull(txtTambahLongitudeKota_Daerah.getText()).toString().trim().isEmpty()){
            txtTambahLongitudeKota_Daerah.setError(getText(R.string.Jangan_Kosong_));
            txtTambahLongitudeKota_Daerah.requestFocus();
        }
        else if(txtTambahLongitudeKota_Daerah.getText().length() < 10){
            txtTambahLongitudeKota_Daerah.setError(getString(R.string.Terlalu_Pendek_));
            txtTambahLongitudeKota_Daerah.requestFocus();
        }
        else if(Objects.requireNonNull(txtTambahZonaWaktuKota_Daerah.getText()).toString().trim().isEmpty()){
            txtTambahZonaWaktuKota_Daerah.setError(getText(R.string.Jangan_Kosong_));
            txtTambahZonaWaktuKota_Daerah.requestFocus();
        }
        else if(txtTambahZonaWaktuKota_Daerah.getText().length() < 2){
            txtTambahZonaWaktuKota_Daerah.setError(getString(R.string.Terlalu_Pendek_));
            txtTambahZonaWaktuKota_Daerah.requestFocus();
        }
        else if(Objects.requireNonNull(txtTambahLatitudeKota_Daerah.getText()).toString().trim().isEmpty()){
            txtTambahLatitudeKota_Daerah.setError(getText(R.string.Jangan_Kosong_));
            txtTambahLatitudeKota_Daerah.requestFocus();
        }
        else if(txtTambahLatitudeKota_Daerah.getText().length() < 10){
            txtTambahLatitudeKota_Daerah.setError(getString(R.string.Terlalu_Pendek_));
            txtTambahLatitudeKota_Daerah.requestFocus();
        }
        else{
            progressDialog.setMessage(getString(R.string.Menyimpan_Data));
            progressDialog.setCancelable(false);
            progressDialog.show();

            AndroidNetworking.post(getString(R.string.URL) + "Beranda.php")
                    .addBodyParameter("Aksi", "TambakKotaDaerah")
                    .addBodyParameter("Nama_Kota", txtTambahNamaKota_Daerah.getText().toString())
                    .addBodyParameter("Detail_Kota", txtTambahDetailKota_Daerah.getText().toString())
                    .addBodyParameter("Longitude_Kota", txtTambahLongitudeKota_Daerah.getText().toString())
                    .addBodyParameter("Latitude_Kota", txtTambahLatitudeKota_Daerah.getText().toString())
                    .addBodyParameter("ZonaWaktu_Kota", txtTambahZonaWaktuKota_Daerah.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Tersimpan")){
                                btnTambahKota_Daerah.setVisibility(View.VISIBLE);
                                CVTambahKota_Daerah.setVisibility(View.GONE);

                                YoYo.with(Techniques.SlideInUp).duration(300).playOn(btnTambahKota_Daerah);
                                YoYo.with(Techniques.FadeInUp).duration(300).playOn(CVTambahKota_Daerah);

                                Toast.makeText(getContext(), R.string.Kota_Daerah_Tersimpan, Toast.LENGTH_SHORT).show();
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