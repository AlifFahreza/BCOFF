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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.Locale;

public class Data extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    TextView payment, totalharga;
    Button bayar;
    String harga;
    LinearLayout linearback;
    TextInputLayout inputpembayaran;
    EditText namatxt, nomeja, alamattxt, notxt, pembayarantxt, nominaltxt;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Data.this, Keranjang.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_data);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        payment = findViewById(R.id.payment);
        totalharga = findViewById(R.id.totalharga);
        nominaltxt = findViewById(R.id.nominaltxt);
        inputpembayaran = findViewById(R.id.inputpembayaran);
        linearback = findViewById(R.id.linearback);
        bayar = findViewById(R.id.bayar);

        namatxt = findViewById(R.id.namatxt);
        nomeja = findViewById(R.id.nomeja);
        alamattxt = findViewById(R.id.alamattxt);
        notxt = findViewById(R.id.notxt);
        pembayarantxt = findViewById(R.id.pembayarantxt);

        String method = extras.getString("pembayaran");
        harga = extras.getString("harga");
        payment.setText("Paymnet "+method);
        inputpembayaran.setHint("No "+method);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int nominal = Integer.parseInt(harga);
        totalharga.setText(formatRupiah.format((double)nominal));

        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Data.this, Keranjang.class);
                startActivity(intent);
                finish();
            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Data.this);
                builder.setCancelable(true);
                builder.setMessage("Apakah anda Yakin?");
                builder.setTitle("Konfirmasi Pembayaran");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nama, meja, alamat, no, pembayaran, nominal;

                                nama = namatxt.getText().toString();
                                meja = nomeja.getText().toString();
                                alamat = alamattxt.getText().toString();
                                no = notxt.getText().toString();
                                pembayaran = pembayarantxt.getText().toString();
                                nominal = nominaltxt.getText().toString();

                                int diabayar, totalbayar;
                                diabayar = Integer.parseInt(nominal);
                                totalbayar = Integer.parseInt(harga);

                                if (diabayar < totalbayar){
                                    Toast.makeText(Data.this, "Mohon Maaf Uang Anda Kurang", Toast.LENGTH_SHORT).show();
                                }else{
                                    Intent intent = new Intent(Data.this, PembayaranBerhasil.class);
                                    intent.putExtra("nama", nama);
                                    intent.putExtra("meja", meja);
                                    intent.putExtra("alamat", alamat);
                                    intent.putExtra("no", no);
                                    intent.putExtra("pembayaran", pembayaran);
                                    intent.putExtra("nominal", nominal);
                                    intent.putExtra("harga", harga);
                                    startActivity(intent);
                                    finish();
                                }
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