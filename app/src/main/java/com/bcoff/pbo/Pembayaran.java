package com.bcoff.pbo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class Pembayaran extends AppCompatActivity {

    RadioGroupPlus radioGroupPlus;
    Button bayar;
    String harga;
    String metode;
    LinearLayout linearback;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Pembayaran.this, Keranjang.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pembayaran);

        radioGroupPlus = findViewById(R.id.radio_group);
        bayar = findViewById(R.id.bayar);
        linearback = findViewById(R.id.linearback);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        harga = extras.getString("harga");

        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pembayaran.this, Keranjang.class);
                startActivity(intent);
                finish();
            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = radioGroupPlus.getCheckedRadioButtonId();

                switch (id){
                    case R.id.radio_button1:
                        metode = "PayPal";
                        pindah(metode);
                        break;
                    case R.id.radio_button2:
                        metode = "Visa";
                        pindah(metode);
                        break;
                    case R.id.radio_button3:
                        metode = "MasterCard";
                        pindah(metode);
                    break;
                    case R.id.radio_button4:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Pembayaran.this);
                        builder.setCancelable(true);
                        builder.setMessage("Apakah anda Yakin?");
                        builder.setTitle("Konfirmasi Pembayaran Ke Kasir Dengan Menunjukkan Username Anda");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Pembayaran.this, Dashboard.class);
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
                        break;
                }
            }
        });
    }

    private void pindah(final String metode) {
        Intent intent = new Intent(Pembayaran.this, Data.class);
        intent.putExtra("pembayaran", metode);
        intent.putExtra("harga", harga);
        finish();
        startActivity(intent);
    }
}