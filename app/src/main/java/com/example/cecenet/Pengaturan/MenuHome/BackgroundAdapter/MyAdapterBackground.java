package com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cecenet.R;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;
import java.util.Locale;

public class MyAdapterBackground extends ArrayAdapter<Background> {
    private Context context;
    private List<Background> backgroundList;
    private SharedPreferences SPModeBackground;
    private ProgressDialog progressDialog;

    MyAdapterBackground(@NonNull Context context, List<Background> backgroundList) {
        super(context, R.layout.list_background, backgroundList);

        this.context        = context;
        this.backgroundList = backgroundList;
    }

    @NonNull
    @Override
    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view                               = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_background, null, true);

        final ImageView imgBackground           = view.findViewById(R.id.imgBackground);
        final RelativeLayout RLVideoBackground  = view.findViewById(R.id.RLVideoBackground);
        final VideoView vdBackground            = view.findViewById(R.id.vdBackground);
        final TextView txtDurasiBackground      = view.findViewById(R.id.txtDurasiBackground);
        final TextView txtPlayVideo             = view.findViewById(R.id.txtPlayVideo);
        final ProgressBar pbVideoBackground     = view.findViewById(R.id.pbVideoBackground);
        final RelativeLayout RLVideoAksi        = view.findViewById(R.id.RLVideoAksi);
        final TextView txtHapusVideoBG          = view.findViewById(R.id.txtHapusVideoBG);
        final TextView txtArsipVideoBG          = view.findViewById(R.id.txtArsipVideoBG);

        progressDialog                          = new ProgressDialog(getContext());
        SPModeBackground                        = getContext().getSharedPreferences("ModeBackground", Context.MODE_PRIVATE);

        if(SPModeBackground.getString("Jenis", "Gambar").equalsIgnoreCase("Gambar")){
            RLVideoAksi.setVisibility(View.GONE);
            RLVideoBackground.setVisibility(View.GONE);
            imgBackground.setVisibility(View.VISIBLE);
            Picasso.with(context).load(getContext().getResources().getString(R.string.URL_Background) + backgroundList.get(position).getNama()).into(imgBackground);
        }
        else{
            Uri video = Uri.parse(getContext().getString(R.string.URL_Background) + backgroundList.get(position).getNama());

            if(backgroundList.get(position).getTampil().equalsIgnoreCase("Ya")){
                txtArsipVideoBG.setBackground(ContextCompat.getDrawable(context, R.drawable.arsip));
            }
            else{
                txtArsipVideoBG.setBackground(ContextCompat.getDrawable(context, R.drawable.unarsip));
            }

            txtPlayVideo.setVisibility(View.GONE);
            imgBackground.setVisibility(View.GONE);
            RLVideoAksi.setVisibility(View.VISIBLE);
            RLVideoBackground.setVisibility(View.VISIBLE);
            vdBackground.setBackgroundColor(Color.TRANSPARENT);

            PushDownAnim.setPushDownAnimTo(txtHapusVideoBG, txtArsipVideoBG, txtPlayVideo);
            vdBackground.setZOrderOnTop(false);
            vdBackground.setVideoURI(video);
            vdBackground.requestFocus();

            vdBackground.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });

            vdBackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    try{
                        mp.setLooping(true);
                        mp.setVolume(0, 0);

                        vdBackground.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                switch(what){
                                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START :
                                    case MediaPlayer.MEDIA_INFO_BUFFERING_END :
                                        pbVideoBackground.setVisibility(View.GONE);
                                        return true;
                                    case MediaPlayer.MEDIA_INFO_BUFFERING_START :
                                        pbVideoBackground.setVisibility(View.VISIBLE);
                                        return true;
                                }
                                return false;
                            }
                        });

                        final Handler handler   = new Handler();
                        Runnable runnable       = new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    String format;

                                    int durasi  = (mp.getDuration() / 1000) - (mp.getCurrentPosition() / 1000);
                                    int jam     = durasi / 3600;
                                    int menit   = (durasi / 60) - (jam * 60);
                                    int detik   = durasi - (jam * 3600) - (menit * 60);

                                    if(jam > 0){
                                        format  = String.format(Locale.getDefault(), "%02d:%02d:%02d", jam, menit, detik);
                                    }
                                    else{
                                        format  = String.format(Locale.getDefault(), "%02d:%02d", menit, detik);
                                    }

                                    txtDurasiBackground.setText(format);
                                    pbVideoBackground.setVisibility(View.GONE);
                                    handler.postDelayed(this, 1000);
                                }
                                catch(IllegalStateException e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        handler.postDelayed(runnable, 1000);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    txtPlayVideo.setVisibility(View.VISIBLE);
                }
            });

            vdBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CountDownTimer(2000, 1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            txtPlayVideo.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFinish() {
                            if(SPModeBackground.getBoolean(backgroundList.get(position).getNama(), true)){
                                txtPlayVideo.setVisibility(View.GONE);
                            }
                            else{
                                txtPlayVideo.setVisibility(View.VISIBLE);
                            }
                        }
                    }.start();
                }
            });

            txtPlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SPModeBackground.getBoolean(backgroundList.get(position).getNama(), true)){
                        SPModeBackground.edit().putBoolean(backgroundList.get(position).getNama(), false).apply();
                        txtPlayVideo.setBackground(ContextCompat.getDrawable(context, R.drawable.play));

                        vdBackground.pause();
                        txtPlayVideo.setVisibility(View.VISIBLE);
                    }
                    else{
                        SPModeBackground.edit().putBoolean(backgroundList.get(position).getNama(), true).apply();
                        txtPlayVideo.setBackground(ContextCompat.getDrawable(context, R.drawable.pause));

                        vdBackground.start();
                        vdBackground.isPlaying();
                        txtPlayVideo.setVisibility(View.GONE);
                    }
                }
            });

            txtHapusVideoBG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Tada).duration(2000).playOn(txtHapusVideoBG);
                    KonfirmasiHapus(backgroundList.get(position).getID_Background(), backgroundList.get(position).getNama());
                }
            });

            txtArsipVideoBG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setMessage((backgroundList.get(position).getTampil().equals("Ya")) ? context.getString(R.string.Mengarsipkan) : context.getString(R.string.Mengaktifkan));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    YoYo.with(Techniques.Tada).duration(2000).playOn(txtArsipVideoBG);
                    BackgroundArsip(backgroundList.get(position).getID_Background(), backgroundList.get(position).getNama(), backgroundList.get(position).getTampil());
                }
            });
        }

        return view;
    }

    private void KonfirmasiHapus(final String ID_Background, final String Nama_Background){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle(context.getString(R.string.Konfirmasi))
                .setMessage(context.getString(R.string.Apakah_Yakin_Ingin_Menghapus) + "\n" + Nama_Background + " ?")
                .setIcon(R.drawable.hapus_black)
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.TIDAK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(context.getString(R.string.YA), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage(context.getString(R.string.Menghapus));
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        HapusAction(ID_Background, Nama_Background);
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void HapusAction(String ID_Background, final String Nama_Background){
        AndroidNetworking.post(context.getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", "HapusBackground")
                .addBodyParameter("Jenis", "Video")
                .addBodyParameter("BackgroundAktifAction", ID_Background)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Background Terhapus")){
                                SPModeBackground.edit().putBoolean("Refresh", true).apply();
                                progressDialog.dismiss();

                                Toast.makeText(getContext(), Nama_Background + " " + context.getString(R.string.Terhapus), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
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

    private void BackgroundArsip(String ID_Background, final String Nama_Background, final String Tampil){
        AndroidNetworking.post(context.getString(R.string.URL) + "Beranda.php")
                .addBodyParameter("Aksi", (Tampil.equalsIgnoreCase("Ya")) ? "BackgroundArsip" : "BackgroundAktif")
                .addBodyParameter("Jenis", "Video")
                .addBodyParameter("BackgroundAktifAction", ID_Background)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equalsIgnoreCase("Background di Arsipkan") || response.equalsIgnoreCase("Background di Aktifkan")){
                                SPModeBackground.edit().putBoolean("Refresh", true).apply();
                                progressDialog.dismiss();

                                Toast.makeText(getContext(), Nama_Background + " " + ((Tampil.equalsIgnoreCase("Ya")) ? context.getString(R.string.di_Arsipkan) : context.getString(R.string.di_Aktifkan)), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
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
}