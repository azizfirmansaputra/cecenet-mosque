package com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.cecenet.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterAnimasi extends ArrayAdapter<Animasi> {
    private Context context;
    private List<Animasi> animasiList;

    public ArrayList<String> AnimasiAktifAction         = new ArrayList<>();
    public ArrayList<String> AnimasiTidakAktifAction    = new ArrayList<>();

    public MyAdapterAnimasi(@NonNull Context context, List<Animasi> animasiList) {
        super(context, R.layout.spinner_animasi, animasiList);

        this.context        = context;
        this.animasiList    = animasiList;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view                           = LayoutInflater.from(context).inflate(R.layout.spinner_animasi, null, true);

        final CheckBox checkBoxAnimasi      = view.findViewById(R.id.checkBoxAnimasi);
        final TextView txtAnimasiBackground = view.findViewById(R.id.txtAnimasiBackground);

        checkBoxAnimasi.setChecked(animasiList.get(position).isTampil());
        txtAnimasiBackground.setText(animasiList.get(position).getNamaAnimasi() + " (" + animasiList.get(position).getAnimate() + ")");

        if(position == 0){
            checkBoxAnimasi.setVisibility(View.GONE);
            txtAnimasiBackground.setText(R.string.Atur_Animasi);
            txtAnimasiBackground.setPadding(10, 10, 0, 0);
        }
        else{
            checkBoxAnimasi.setVisibility(View.VISIBLE);
        }

        txtAnimasiBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("SetJavaScriptEnabled")
            public void onClick(View v) {
                String kategori_animasi = animasiList.get(position).getKategoriAnimasi();
                String nama_animasi     = animasiList.get(position).getNamaAnimasi();
                String animate          = animasiList.get(position).getAnimate();

                if(position != 0){
                    txtAnimasiBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    Toast.makeText(getContext(), kategori_animasi + " - " + nama_animasi, Toast.LENGTH_LONG).show();

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.preview_animasi);

                    if(dialog.getWindow() != null){
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    final WebView webViewPreviewAnimasi = dialog.findViewById(R.id.webViewPreviewAnimasi);

                    webViewPreviewAnimasi.getSettings().setJavaScriptEnabled(true);
                    webViewPreviewAnimasi.getSettings().setAppCacheEnabled(false);
                    webViewPreviewAnimasi.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    webViewPreviewAnimasi.setBackgroundColor(Color.TRANSPARENT);
                    webViewPreviewAnimasi.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

                    String preview  =   "<html>" +
                                            "<head>" +
                                                "<link rel=\"stylesheet\" href=\"raw/animate_min.css\">" +
                                                "<style>" +
                                                    "body{background-color: transparent;}" +
                                                    "div{height: 100%; width: 100%; left: 0; top: 0; position: fixed; z-index: -1;}" +
                                                    "div img{height: 100%; width: 100%;}" +
                                                "</style>" +
                                            "</head>" +
                                            "<body>" +
                                                "<div class=\"animate__animated animate__" + animate + " animate__repeat-3\">" +
                                                    "<img src=\"drawable/preview.png\" width=\"100%\" height=\"100%\">" +
                                                "</div>" +
                                            "</body>" +
                                        "</html>";

                    webViewPreviewAnimasi.loadDataWithBaseURL("file:///android_res/", preview, "text/html", "UTF-8", null);

                    new CountDownTimer(5000, 1000){
                        @Override
                        public void onTick(long millisUntilFinished) { }

                        @Override
                        public void onFinish() {
                            txtAnimasiBackground.setBackgroundColor(Color.TRANSPARENT);
                            dialog.dismiss();
                        }
                    }.start();
                }
            }
        });

        AnimasiAktifAction.clear();
        AnimasiTidakAktifAction.clear();
        checkBoxAnimasi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AnimasiAktifAction.add(animasiList.get(position).getID_Animasi());
                    AnimasiTidakAktifAction.remove(animasiList.get(position).getID_Animasi());
                }
                else{
                    AnimasiTidakAktifAction.add(animasiList.get(position).getID_Animasi());
                    AnimasiAktifAction.remove(animasiList.get(position).getID_Animasi());
                }
            }
        });

        return view;
    }
}