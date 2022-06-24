package com.example.cecenet.Pengaturan.MenuHome.PengumumanAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.cecenet.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterPengumuman extends ArrayAdapter<Pengumuman> {
    private Context context;
    private List<Pengumuman> pengumumanList;

    public MyAdapterPengumuman(@NonNull Context context, @NonNull List<Pengumuman> pengumumanList) {
        super(context, R.layout.list_pengumuman, pengumumanList);

        this.context        = context;
        this.pengumumanList = pengumumanList;
    }

    @NonNull
    @Override
    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view                   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengumuman, null, true);

        CardView CVListPengumuman   = view.findViewById(R.id.CVListPengumuman);
        ImageView imgPengumuman     = view.findViewById(R.id.imgPengumuman);
        TextView txtJudulPengumuman = view.findViewById(R.id.txtJudulPengumuman);
        TextView txtIsiPengumuman   = view.findViewById(R.id.txtIsiPengumuman);

        if(pengumumanList.get(position).isTampil()){
            CVListPengumuman.setCardBackgroundColor(ContextCompat.getColor(context, R.color.background));
        }
        else{
            CVListPengumuman.setCardBackgroundColor(ContextCompat.getColor(context, R.color.invisible));
        }

        if(!pengumumanList.get(position).getGambar_Pengumuman().isEmpty()){
            Picasso.with(context).load(getContext().getString(R.string.URL_Pengumuman) + pengumumanList.get(position).getGambar_Pengumuman()).into(imgPengumuman);
        }
        else{
            imgPengumuman.setVisibility(View.GONE);
        }

        txtJudulPengumuman.setText(pengumumanList.get(position).getJenis_Pengumuman());
        txtIsiPengumuman.setText(pengumumanList.get(position).getIsi_Pengumuman());

        return view;
    }
}