package com.bcoff.pbo;

public class ListMenu {
    private String id_menu;
    private String nama_menu;
    private String gambar;
    private String deskripsi;
    private String harga;
    private String jenis_menu;

    public ListMenu(String id_menu, String nama_menu, String gambar, String deskripsi, String harga, String jenis_menu) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.gambar = gambar;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.jenis_menu = jenis_menu;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJenis_menu() {
        return jenis_menu;
    }

    public void setJenis_menu(String jenis_menu) {
        this.jenis_menu = jenis_menu;
    }
}
