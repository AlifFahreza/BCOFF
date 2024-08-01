package com.bcoff.pbo;

public class ListKeranjang {
    private String id_keranjang;
    private String id_menu;
    private String id_user;
    private String nama_menu;
    private String harga;
    private String gambar;

    public ListKeranjang(String id_keranjang, String id_menu, String id_user, String nama_menu, String harga, String gambar) {
        this.id_keranjang = id_keranjang;
        this.id_menu = id_menu;
        this.id_user = id_user;
        this.nama_menu = nama_menu;
        this.harga = harga;
        this.gambar = gambar;
    }

    public String getId_keranjang() {
        return id_keranjang;
    }

    public void setId_keranjang(String id_keranjang) {
        this.id_keranjang = id_keranjang;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
