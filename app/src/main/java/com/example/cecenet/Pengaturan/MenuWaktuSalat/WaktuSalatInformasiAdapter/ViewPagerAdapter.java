package com.example.cecenet.Pengaturan.MenuWaktuSalat.WaktuSalatInformasiAdapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cecenet.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTAB;

    @SuppressWarnings("deprecation")
    public ViewPagerAdapter(Context context, FragmentManager fragmentManager, int totalTAB){
        super(fragmentManager);

        this.context    = context;
        this.totalTAB   = totalTAB;
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0 : return new AktifInfoSalatFragment();
            case 1 : return new ArsipInfoSalatFragment();
        }

        return new AktifInfoSalatFragment();
    }

    @Override
    public int getCount(){
        return  totalTAB;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0 : return context.getString(R.string.AKTIF);
            case 1 : return context.getString(R.string.ARSIP);
            default: return null;
        }
    }
}