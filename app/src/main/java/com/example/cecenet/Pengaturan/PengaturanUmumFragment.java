package com.example.cecenet.Pengaturan;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.Pengaturan.Album.AlbumActivity;
import com.example.cecenet.MainActivity;
import com.example.cecenet.R;
import com.hotmail.or_dvir.easysettings.events.SwitchSettingsClickEvent;
import com.hotmail.or_dvir.easysettings.pojos.BasicSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.hotmail.or_dvir.easysettings.pojos.HeaderSettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject;
import com.hotmail.or_dvir.easysettings.pojos.SwitchSettingsObject;
import com.hotmail.or_dvir.easysettings_dialogs.events.ListSettingsValueChangedEvent;
import com.hotmail.or_dvir.easysettings_dialogs.pojos.ListSettingsObject;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class PengaturanUmumFragment extends Fragment {
    private ArrayList<String> arrayListLayar    = new ArrayList<>();
    private ArrayList<String> arrayListBahasa   = new ArrayList<>();
    private ArrayList<String> arrayListTema     = new ArrayList<>();

    private TextView txtPreloader, txtTentang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                       = inflater.inflate(R.layout.fragment_pengaturan_umum, container, false);

        LinearLayout LLPengaturanUmum   = view.findViewById(R.id.LLPengaturanUmum);

        arrayListLayar.clear();
        arrayListLayar.add(getString(R.string.Deteksi_Otomatis));
        arrayListLayar.add("1360 x 768 (720p)");
        arrayListLayar.add("1920 x 1080 (1080p)");

        arrayListBahasa.clear();
        arrayListBahasa.add(getString(R.string.Indonesia));
        arrayListBahasa.add(getString(R.string.Inggris));

        arrayListTema.clear();
        arrayListTema.add(getString(R.string.Default_Ponsel));
        arrayListTema.add(getString(R.string.Terang));
        arrayListTema.add(getString(R.string.Gelap));

        boolean Preloader = EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).getBoolean("PRELOADER", true);

        ArrayList<SettingsObject> settingsObjectArrayList = EasySettings.createSettingsArray(
                new HeaderSettingsObject.Builder(getString(R.string.Pengaturan_Web)).build(),
                new SwitchSettingsObject.Builder("PRELOADER", getString(R.string.Preloader_Saat_Memuat_Halaman), true)
                        .setSummary((Preloader) ? getString(R.string.Ya) : getString(R.string.Tidak))
                        .addDivider()
                        .build(),
                new ListSettingsObject.Builder("RESOLUSI_LAYAR", getString(R.string.Resolusi_Layar_Raspberry), getString(R.string.Deteksi_Otomatis), arrayListLayar, getString(R.string.PILIH))
                        .setUseValueAsSummary()
                        .setDialogTitle(getString(R.string.Resolusi_Layar))
                        .setNegativeBtnText(getString(R.string.batal))
                        .setNeutralBtnText("")
                        .addDivider()
                        .build(),
                new BasicSettingsObject.Builder("ATUR_JAM", getString(R.string.Atur_Jam_Raspberry))
                        .setSummary(getString(R.string.Otomatis_samakan_dengan_Ponsel_atau_Manual))
                        .addDivider()
                        .build(),
                new HeaderSettingsObject.Builder(getString(R.string.Pengaturan_Aplikasi)).build(),
                new BasicSettingsObject.Builder("ALBUM", getString(R.string.Album))
                        .setSummary(getString(R.string.Kumpulan_Gambar_Cecenet))
                        .addDivider()
                        .build(),
                new ListSettingsObject.Builder("BAHASA", getString(R.string.Bahasa), "Indonesia", arrayListBahasa, getString(R.string.ATUR))
                        .setUseValueAsSummary()
                        .setDialogTitle(getString(R.string.Pilih_Bahasa))
                        .setNegativeBtnText(getString(R.string.batal))
                        .setNeutralBtnText("")
                        .addDivider()
                        .build(),
                new ListSettingsObject.Builder("TEMA", getString(R.string.Tema), "Default Ponsel", arrayListTema, getString(R.string.ATUR))
                        .setUseValueAsSummary()
                        .setDialogTitle(getString(R.string.Pilih_Tema))
                        .setNegativeBtnText(getString(R.string.batal))
                        .setNeutralBtnText("")
                        .addDivider()
                        .build(),
                new BasicSettingsObject.Builder("TENTANG", getString(R.string.Tentang))
                        .setSummary(getString(R.string.Cecenet_Versi___by_Firman))
                        .build()
        );

        EasySettings.initializeSettings(getContext(), settingsObjectArrayList);
        EasySettings.inflateSettingsLayout(getContext(), LLPengaturanUmum, settingsObjectArrayList);

        settingPreloader(view, settingsObjectArrayList);
        settingWaktu(view, settingsObjectArrayList);
        settingAlbum(view, settingsObjectArrayList);
        settingTentang(view, settingsObjectArrayList);

        return view;
    }

    @Subscribe
    public void onSwitchSettingsClicked(SwitchSettingsClickEvent event) {
        boolean Preloader   = EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).getBoolean(
                                event.getClickedSettingsObj().getKey(), event.getClickedSettingsObj().getDefaultValue());

        if(Preloader){
            Preloader(getString(R.string.ya), true);
        }
        else{
            Preloader(getString(R.string.tidak), false);
        }
    }

    private void settingPreloader(View view, ArrayList<SettingsObject> settingsObjectArrayList){
        SettingsObject Preloader    = EasySettings.findSettingsObject("PRELOADER", settingsObjectArrayList);
        View viewPreloader          = view.findViewById(Objects.requireNonNull(Preloader).getRootId());
        txtPreloader                = viewPreloader.findViewById(Objects.requireNonNull(Preloader.getTextViewSummaryId()));
    }

    private void Preloader(final String Aktif, final boolean Status){
        AndroidNetworking.post(getString(R.string.URL) + "Menu.php")
                .addBodyParameter("Aksi", "Preloader")
                .addBodyParameter("Status", Aktif)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Tersimpan")){
                            EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).edit().putBoolean("PRELOADER", Status).apply();
                            txtPreloader.setText(Aktif);
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

    @Subscribe
    public void onListSettingsValueChanged(ListSettingsValueChangedEvent event) {
        if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListLayar.get(0))){
            resolusiLayar("1", "31");
        }
        else if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListLayar.get(1))){
            resolusiLayar("2", "39");
        }
        else if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListLayar.get(2))){
            resolusiLayar("1", "31");
        }

        if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListBahasa.get(0))){
            setLocale("idn");
        }
        else if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListBahasa.get(1))){
            setLocale("en");
        }

        if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListTema.get(0))){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListTema.get(1))){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(event.getNewValueAsSaved().equalsIgnoreCase(arrayListTema.get(2))){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void resolusiLayar(String Mode, String Resolusi){
        AndroidNetworking.post(getString(R.string.URL) + "Menu.php")
                .addBodyParameter("Aksi", "ResolusiLayar")
                .addBodyParameter("Mode", Mode)
                .addBodyParameter("Resolusi", Resolusi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Sukses")){
                            Toast.makeText(getContext(), getString(R.string.Mulai_Ulang_Raspberry_Sekarang), Toast.LENGTH_SHORT).show();
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

    private void settingWaktu(View view, ArrayList<SettingsObject> settingsObjectArrayList){
        SettingsObject Waktu    = EasySettings.findSettingsObject("ATUR_JAM", settingsObjectArrayList);
        View viewWaktu          = view.findViewById(Objects.requireNonNull(Waktu).getRootId());

        viewWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Waktu();
            }
        });
    }

    private void Waktu(){
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.waktu_layout);

        if(dialog.getWindow() != null){
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        final LinearLayout LLWaktu          = dialog.findViewById(R.id.LLWaktu);
        final RadioGroup RGAturWaktu        = dialog.findViewById(R.id.RGAturWaktu);
        final RadioButton RBPonsel          = dialog.findViewById(R.id.RBPonsel);
        final RadioButton RBManual          = dialog.findViewById(R.id.RBManual);
        final DatePicker DPWaktuRaspberry   = dialog.findViewById(R.id.DPWaktuRaspberry);
        final TimePicker TPWaktuRaspberry   = dialog.findViewById(R.id.TPWaktuRaspberry);
        final Button btnBatalAturWaktu      = dialog.findViewById(R.id.btnBatalAturWaktu);
        final Button btnAturWaktu           = dialog.findViewById(R.id.btnAturWaktu);

        RBPonsel.setChecked(true);
        LLWaktu.setVisibility(View.GONE);
        TPWaktuRaspberry.setIs24HourView(true);
        PushDownAnim.setPushDownAnimTo(btnBatalAturWaktu, btnAturWaktu);

        RGAturWaktu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(RBPonsel.isChecked()){
                    YoYo.with(Techniques.SlideInUp).duration(500).playOn(DPWaktuRaspberry);
                    YoYo.with(Techniques.SlideInUp).duration(500).playOn(TPWaktuRaspberry);

                    LLWaktu.setVisibility(View.GONE);
                    DPWaktuRaspberry.setVisibility(View.GONE);
                    TPWaktuRaspberry.setVisibility(View.GONE);
                }
                else{
                    YoYo.with(Techniques.SlideInDown).duration(500).playOn(DPWaktuRaspberry);
                    YoYo.with(Techniques.SlideInDown).duration(500).playOn(TPWaktuRaspberry);

                    LLWaktu.setVisibility(View.VISIBLE);
                    DPWaktuRaspberry.setVisibility(View.VISIBLE);
                    TPWaktuRaspberry.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBatalAturWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAturWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Perintah = "";

                if(RBPonsel.isChecked()){
                    String DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    Perintah        = "sudo date -s \'" + DateTime + "\'";
                }
                else if(RBManual.isChecked()){
                    String Detik        = new SimpleDateFormat("ss", Locale.getDefault()).format(new Date());
                    Calendar calendar   = new GregorianCalendar(DPWaktuRaspberry.getYear(), DPWaktuRaspberry.getMonth(), DPWaktuRaspberry.getDayOfMonth(),
                                                                TPWaktuRaspberry.getHour(), TPWaktuRaspberry.getMinute(), Integer.parseInt(Detik));
                    String DateTime     = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.getTime());
                    Perintah            = "sudo date -s \'" + DateTime + "\'";
                }

                AndroidNetworking.post(getString(R.string.URL) + "Menu.php")
                        .addBodyParameter("Aksi", "AturWaktu")
                        .addBodyParameter("Perintah", Perintah)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("Sukses")){
                                    Toast.makeText(getContext(), getString(R.string.Mulai_Ulang_Raspberry_Sekarang), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void settingAlbum(View view, ArrayList<SettingsObject> settingsObjectArrayList){
        SettingsObject Album    = EasySettings.findSettingsObject("ALBUM", settingsObjectArrayList);
        View viewAlbum          = view.findViewById(Objects.requireNonNull(Album).getRootId());

        viewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AlbumActivity.class));
            }
        });
    }

    private void setLocale(String localeName){
        String Bahasa   = EasySettings.retrieveSettingsSharedPrefs(Objects.requireNonNull(getContext())).getString("SET_BAHASA", "idn");

        if(!localeName.equalsIgnoreCase(Bahasa)){
            Locale locale       = new Locale(localeName);
            Resources resources = getResources();
            DisplayMetrics DM   = resources.getDisplayMetrics();
            Configuration conf  = resources.getConfiguration();
            conf.locale         = locale;

            resources.updateConfiguration(conf, DM);
            EasySettings.retrieveSettingsSharedPrefs(getContext()).edit().putString("SET_BAHASA", localeName).apply();
            startActivity(new Intent(getContext(), MainActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    private void settingTentang(View view, ArrayList<SettingsObject> settingsObjectArrayList){
        SettingsObject Tentang  = EasySettings.findSettingsObject("TENTANG", settingsObjectArrayList);
        View viewTentang        = view.findViewById(Objects.requireNonNull(Tentang).getRootId());
        txtTentang              = viewTentang.findViewById(Objects.requireNonNull(Tentang.getTextViewSummaryId()));

        viewTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), txtTentang.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}