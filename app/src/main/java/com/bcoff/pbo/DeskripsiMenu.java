package com.bcoff.pbo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class DeskripsiMenu extends AppCompatActivity {

    ImageView img;
    TextView namamenu, harga, deskripsi;
    LinearLayout linearback;
    Button btn_tambah;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_deskripsi_menu);

        img = findViewById(R.id.img);
        namamenu = findViewById(R.id.nama_menu);
        harga = findViewById(R.id.harga);
        deskripsi = findViewById(R.id.deskripsi);
        linearback = findViewById(R.id.linearback);
        btn_tambah = findViewById(R.id.btn_tambah);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeskripsiMenu.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        String string_img = extras.getString("gambar");
        String string_desc = extras.getString("deskripsi");
        String string_harga = extras.getString("harga");
        String string_nama = extras.getString("nama_menu");
        String id_menu = extras.getString("id_menu");

        Glide.with(DeskripsiMenu.this)
                .load("https://bcoff.000webhostapp.com/menu/" + string_img)
                .into(img);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        int nominal = Integer.parseInt(string_harga);
        namamenu.setText(string_nama);
        harga.setText(formatRupiah.format((double)nominal));
        deskripsi.setText(string_desc);

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string_id_keranjang = extras.getString("id_menu");

                if(!string_id_keranjang.equals("")){
                    class TambahKeranjang extends AsyncTask<Void,Void,String> {
                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(DeskripsiMenu.this,"Tambah Keranjang...","Please Wait...",false,false);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                            if (s.contains("1")){
                                Toast.makeText(DeskripsiMenu.this, "Barang Berhasil Ditambahkan Di Keranjang", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(DeskripsiMenu.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        protected String doInBackground(Void... params) {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id_menu",id_menu);
                            hashMap.put(Server.TAG_ID_USER,sharedpreferences.getString(Server.TAG_ID_USER, null));
                            hashMap.put("nama_menu",string_nama);
                            hashMap.put("harga",string_harga);
                            hashMap.put("gambar",string_img);

                            RequestHandler rh = new RequestHandler();

                            String s = rh.sendPostRequest(Server.URL_KERANJANG,hashMap);

                            return s;
                        }
                    }

                    TambahKeranjang gj = new TambahKeranjang();
                    gj.execute();

                }else{
                    Toast.makeText(DeskripsiMenu.this, "Mohon Maaf terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}