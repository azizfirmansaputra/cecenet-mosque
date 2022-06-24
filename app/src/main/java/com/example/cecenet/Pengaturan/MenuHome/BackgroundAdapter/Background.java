package com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter;

public class Background {
    private String ID_Background, Nama, Tampil;

    public Background(String ID_Background, String Nama, String Tampil){
        this.ID_Background  = ID_Background;
        this.Nama           = Nama;
        this.Tampil         = Tampil;
    }

    String getID_Background(){
        return ID_Background;
    }

    String getNama(){
        return Nama;
    }

    String getTampil(){
        return Tampil;
    }
}