package com.bcoff.pbo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends getServer {

    public String id;
    public SharedPreferences sharedpreferences;
    public static final String TAG_ID = "id_user";
    public TextView usernametext;
    public ImageView profile, keranjang;
    public CardView menu1, menu2, menu3, menu4, menu5;
    public FrameLayout frameLayout;
    public RecyclerView recyclerView;
    public String link;
    List<ListMenu> listmenu;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slider1jpg, R.drawable.slider2, R.drawable.slider3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);
        menu5 = findViewById(R.id.menu5);
        frameLayout = findViewById(R.id.framelayout);
        usernametext = findViewById(R.id.usernameteks);
        carouselView = findViewById(R.id.carouselView);
        profile = findViewById(R.id.profile);
        keranjang = findViewById(R.id.keranjang);
        carouselView.setPageCount(sampleImages.length);
        usernametext.setText(sharedpreferences.getString(Server.TAG_USERNAME, null));

        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new FragmentMenu1()).commit();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(Dashboard.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.merchandise);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        listmenu = new ArrayList<>();
        loadList();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Profile.class);
                intent.putExtra("EXTRA_SESSION_ID", id);
                finish();
                startActivity(intent);
            }
        });

        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Keranjang.class);
                finish();
                startActivity(intent);
            }
        });

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };

        carouselView.setImageListener(imageListener);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new FragmentMenu1()).commit();
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new FragmentMenu2()).commit();
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new FragmentMenu3()).commit();
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new FragmentMenu4()).commit();
            }
        });

        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new FragmentMenu5()).commit();
            }
        });
    }

    private void loadList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Dashboard.this.getLink(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);

                                listmenu.add(new ListMenu(
                                        product.getString("id_menu"),
                                        product.getString("nama_menu"),
                                        product.getString("gambar"),
                                        product.getString("deskripsi"),
                                        product.getString("harga"),
                                        product.getString("jenis_menu")
                                ));
                            }

                            MerchandiseAdapter adapter = new MerchandiseAdapter(Dashboard.this, listmenu);
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

        Volley.newRequestQueue(Dashboard.this).add(stringRequest);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    String getLink() {
        return "https://bcoff.000webhostapp.com/merchandise.php";
    }
}