package com.bcoff.pbo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class Intro extends AppCompatActivity {

    public Button btnSignUp, btnLogin;
    public String textwhite, textorange;
    public LinearLayout linearkoneksi;
    public SharedPreferences sharedpreferences;
    public Boolean session = false;
    public String id_user, username;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro);

        btnSignUp = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.login);
        linearkoneksi = findViewById(R.id.linearkoneksi);
        textwhite = "<font color='#FFFFFF'>No account yet? </font>";
        textorange = "<font color='#EE6A1F'>Signup</font>";
        btnSignUp.setText(Html.fromHtml(textwhite + textorange));

        if(checkInternet()){
            linearkoneksi.setVisibility(View.INVISIBLE);
        }else{
            linearkoneksi.setVisibility(View.VISIBLE);
        }

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_user = sharedpreferences.getString(Server.TAG_ID_USER, null);
        username = sharedpreferences.getString(Server.TAG_USERNAME, null);

        if (session) {
            Intent intent = new Intent(Intro.this, Dashboard.class);
            intent.putExtra(Server.TAG_ID_USER, id_user);
            intent.putExtra(Server.TAG_USERNAME, username);
            finish();
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
}