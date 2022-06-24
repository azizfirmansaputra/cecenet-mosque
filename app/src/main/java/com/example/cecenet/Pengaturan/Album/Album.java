package com.example.cecenet.Pengaturan.Album;

public class Album {
    private String ID_Background, Nama;
    private boolean Tampil;

    public Album(String ID_Background, String Nama, boolean Tampil){
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

    boolean isTampil(){
        return Tampil;
    }
}