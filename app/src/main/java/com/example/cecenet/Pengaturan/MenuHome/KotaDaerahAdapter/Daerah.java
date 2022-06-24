package com.example.cecenet.Pengaturan.MenuHome.KotaDaerahAdapter;

public class Daerah {
    private String Nama_KotaDaerah, Detail_KotaDaerah;

    public Daerah(String Nama_KotaDaerah, String Detail_KotaDaerah){
        this.Nama_KotaDaerah    = Nama_KotaDaerah;
        this.Detail_KotaDaerah  = Detail_KotaDaerah;
    }

    public String getNama_KotaDaerah(){
        return Nama_KotaDaerah;
    }

    String getDetail_KotaDaerah(){
        return Detail_KotaDaerah;
    }
}