package com.example.cecenet.Pengaturan.MenuHome.PengumumanAdapter;

public class Pengumuman {
    private String ID_Pengumuman, Jenis_Pengumuman, Gambar_Pengumuman, Isi_Pengumuman;
    private boolean Tampil;

    public Pengumuman(String ID_Pengumuman, String Jenis_Pengumuman, String Gambar_Pengumuman, String Isi_Pengumuman, boolean Tampil){
        this.ID_Pengumuman      = ID_Pengumuman;
        this.Jenis_Pengumuman   = Jenis_Pengumuman;
        this.Gambar_Pengumuman  = Gambar_Pengumuman;
        this.Isi_Pengumuman     = Isi_Pengumuman;
        this.Tampil             = Tampil;
    }

    public String getID_Pengumuman(){
        return ID_Pengumuman;
    }

    public String getJenis_Pengumuman(){
        return Jenis_Pengumuman;
    }

    public String getGambar_Pengumuman(){
        return Gambar_Pengumuman;
    }

    public String getIsi_Pengumuman(){
        return Isi_Pengumuman;
    }

    public boolean isTampil(){
        return Tampil;
    }
}