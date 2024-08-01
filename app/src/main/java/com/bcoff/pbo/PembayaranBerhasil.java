package com.bcoff.pbo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class PembayaranBerhasil extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    EditText namatxt, mejatxt, alamattxt, notxt, pembayarantxt, harga, nominall, kembalian;
    Button selesai;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PembayaranBerhasil.this);
        builder.setCancelable(true);
        builder.setMessage("Klik YES Jika Pesanan Anda Sudah Datang. Apakah Anda Yakin?");
        builder.setTitle("SELESAI");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PembayaranBerhasil.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pembayaran_berhasil);

        namatxt = findViewById(R.id.namatxt);
        mejatxt = findViewById(R.id.nomeja);
        alamattxt = findViewById(R.id.alamattxt);
        notxt = findViewById(R.id.notxt);
        harga = findViewById(R.id.harga);
        kembalian = findViewById(R.id.kembalian);
        selesai = findViewById(R.id.selesai);
        nominall = findViewById(R.id.nominall);
        pembayarantxt = findViewById(R.id.pembayarantxt);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        String nama = extras.getString("nama");
        String meja = extras.getString("meja");
        String alamat = extras.getString("alamat");
        String no = extras.getString("no");
        String pembayaran = extras.getString("pembayaran");
        String harganya = extras.getString("harga");
        String nominalnya = extras.getString("nominal");

        namatxt.setText(nama);
        mejatxt.setText(meja);
        alamattxt.setText(alamat);
        notxt.setText(no);
        pembayarantxt.setText(pembayaran);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int nominal = Integer.parseInt(harganya);
        harga.setText(formatRupiah.format((double)nominal));

        int nominale = Integer.parseInt(nominalnya);
        nominall.setText(formatRupiah.format((double)nominale));

        if (nominale > nominal){
            int jumlah = nominale - nominal;
            kembalian.setText(formatRupiah.format((double)jumlah));
        }else {
            kembalian.setText("-");
        }

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PembayaranBerhasil.this);
                builder.setCancelable(true);
                builder.setMessage("Klik YES Jika Pesanan Anda Sudah Datang. Apakah Anda Yakin?");
                builder.setTitle("SELESAI");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PembayaranBerhasil.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                );
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}