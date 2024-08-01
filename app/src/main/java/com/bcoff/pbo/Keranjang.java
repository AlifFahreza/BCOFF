package com.bcoff.pbo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Keranjang extends getServer implements TotalHarga{

    public LinearLayout linearback;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    List<ListKeranjang> keranjangList;
    public RecyclerView recyclerView;
    TextView total;
    String JSON_STRING, harga, id_user;
    public Button bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_keranjang);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(Keranjang.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.shoppingcart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        keranjangList = new ArrayList<>();

        linearback = findViewById(R.id.linearback);
        bayar = findViewById(R.id.bayar);
        total = findViewById(R.id.totalharga);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Keranjang.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Keranjang.this, Pembayaran.class);
                intent.putExtra("harga", harga);
                startActivity(intent);
                finish();
            }
        });

        Keranjang.this.total();
        loadList();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Keranjang.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void showData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Server.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                harga = jo.getString("harga");

                HashMap<String, String> data = new HashMap<>();
                data.put("harga", harga);

                list.add(data);

                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                int nominal = Integer.parseInt(harga);

                total.setText(""+formatRupiah.format((double)nominal));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Keranjang.this.getLink()+sharedpreferences.getString(Server.TAG_ID_USER, null),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);

                                keranjangList.add(new ListKeranjang(
                                        product.getString("id_keranjang"),
                                        product.getString("id_menu"),
                                        product.getString("id_user"),
                                        product.getString("nama_menu"),
                                        product.getString("harga"),
                                        product.getString("gambar")
                                ));
                            }

                            KeranjangAdapter adapter = new KeranjangAdapter(Keranjang.this, keranjangList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(Keranjang.this).add(stringRequest);
    }

    @Override
    String getLink() {
        return "https://bcoff.000webhostapp.com/tampilkeranjang.php?id_user=";
    }

    @Override
    public void total() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Server.URL_HARGA+sharedpreferences.getString(Server.TAG_ID_USER, null));
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}