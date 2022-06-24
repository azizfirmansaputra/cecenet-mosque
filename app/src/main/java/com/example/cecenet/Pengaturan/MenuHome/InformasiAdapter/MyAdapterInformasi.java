package com.example.cecenet.Pengaturan.MenuHome.InformasiAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cecenet.R;

import java.util.List;

public class MyAdapterInformasi extends ArrayAdapter<Informasi> {
    private List<Informasi> informasiList;

    public MyAdapterInformasi(@NonNull Context context, List<Informasi> informasiList) {
        super(context, R.layout.list_informasi, informasiList);

        this.informasiList  = informasiList;
    }

    @NonNull
    @Override
    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view               = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_informasi, null, true);

        TextView txtInformasi   = view.findViewById(R.id.txtInformasi);
        txtInformasi.setText(informasiList.get(position).getInfo());

        return view;
    }
}