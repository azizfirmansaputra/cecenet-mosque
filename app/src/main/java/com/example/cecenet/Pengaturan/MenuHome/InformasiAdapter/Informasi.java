package com.example.cecenet.Pengaturan.MenuHome.InformasiAdapter;

public class Informasi {
    private String ID_INFO, Info;

    public Informasi(String ID_INFO, String Info){
        this.ID_INFO    = ID_INFO;
        this.Info       = Info;
    }

    public String getID_INFO(){
        return ID_INFO;
    }

    public String getInfo(){
        return Info;
    }
}