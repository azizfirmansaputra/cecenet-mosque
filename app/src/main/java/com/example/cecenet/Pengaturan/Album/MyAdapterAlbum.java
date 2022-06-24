package com.example.cecenet.Pengaturan.Album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cecenet.R;
import com.hotmail.or_dvir.easysettings.pojos.EasySettings;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterAlbum extends ArrayAdapter<Album> {
    private Context context;
    private List<Album> albumList;

    MyAdapterAlbum(@NonNull Context context, List<Album> albumList) {
        super(context, R.layout.list_background, albumList);

        this.context    = context;
        this.albumList  = albumList;
    }

    @NonNull
    @Override
    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view                           = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_background, null, true);

        ImageView imgBackground             = view.findViewById(R.id.imgBackground);
        RelativeLayout RLVideoBackground    = view.findViewById(R.id.RLVideoBackground);
        RelativeLayout RLVideoAksi          = view.findViewById(R.id.RLVideoAksi);

        boolean Background  = EasySettings.retrieveSettingsSharedPrefs(context).getBoolean("PILIH_GAMBAR_BACKGROUND", false);
        boolean Pengumuman  = EasySettings.retrieveSettingsSharedPrefs(context).getBoolean("PILIH_GAMBAR_PENGUMUMAN", false);

        RLVideoBackground.setVisibility(View.GONE);
        RLVideoAksi.setVisibility(View.GONE);

        if(Background){
            Picasso.with(context).load(context.getString(R.string.URL_Background) + albumList.get(position).getNama()).into(imgBackground);
        }
        else if(Pengumuman){
            Picasso.with(context).load(context.getString(R.string.URL_Pengumuman) + albumList.get(position).getNama()).into(imgBackground);
        }

        return view;
    }
}