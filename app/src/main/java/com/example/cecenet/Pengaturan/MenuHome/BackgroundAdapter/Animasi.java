package com.example.cecenet.Pengaturan.MenuHome.BackgroundAdapter;

public class Animasi {
    private String ID_Animasi, KategoriAnimasi, NamaAnimasi, Animate;
    private boolean Tampil;

    public Animasi(String ID_Animasi, String KategoriAnimasi, String NamaAnimasi, String Animate, boolean Tampil){
        this.ID_Animasi      = ID_Animasi;
        this.KategoriAnimasi = KategoriAnimasi;
        this.NamaAnimasi     = NamaAnimasi;
        this.Animate         = Animate;
        this.Tampil          = Tampil;
    }

    String getID_Animasi(){
        return ID_Animasi;
    }

    String getKategoriAnimasi(){
        return KategoriAnimasi;
    }

    String getNamaAnimasi(){
        return NamaAnimasi;
    }

    String getAnimate(){
        return Animate;
    }

    boolean isTampil(){
        return Tampil;
    }
}