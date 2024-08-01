package com.bcoff.pbo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public LinearLayout linearLayout;
    public ConnectivityManager conMgr;
    public EditText emailtxt, passwordtxt;
    public String tag_json_obj = "json_obj_req", id_user, username;
    public Button btn_login;
    public LinearLayout linearkoneksi;
    public int success;
    public TextView errortext;
    public ProgressDialog pDialog;
    public SharedPreferences sharedpreferences;
    public Boolean session = false;
    private static final String TAG = Login.class.getSimpleName();
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        linearLayout = findViewById(R.id.linearback);
        emailtxt = findViewById(R.id.emailtxt);
        passwordtxt = findViewById(R.id.passwordtxt);
        btn_login = findViewById(R.id.btn_login);
        linearkoneksi = findViewById(R.id.linearkoneksi);
        errortext = findViewById(R.id.errortext);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                linearkoneksi.setVisibility(View.INVISIBLE);
            } else {
                linearkoneksi.setVisibility(View.VISIBLE);
            }
        }

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_user = sharedpreferences.getString(Server.TAG_ID_USER, null);
        username = sharedpreferences.getString(Server.TAG_USERNAME, null);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailtxt.getText().toString();
                String password = passwordtxt.getText().toString();

                if (email.isEmpty()){
                    errortext.setText("Email Is Empty! Please Fill It In");
                    linearkoneksi.setVisibility(View.VISIBLE);
                }else if (password.isEmpty()){
                    errortext.setText("Password Is Empty! Please Fill It In");
                    linearkoneksi.setVisibility(View.VISIBLE);
                }else {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        linearkoneksi.setVisibility(View.INVISIBLE);
                        checkLogin(email, password);
                    } else {
                        errortext.setText("No Internet Connection!");
                        linearkoneksi.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Intro.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, Intro.class);
        startActivity(intent);
        finish();
    }

    private void checkLogin(final String email, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Server.URL_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(Server.TAG_SUCCESS);

                    if (success == 1) {
                        String username = jObj.getString(Server.TAG_USERNAME);
                        String id_user = jObj.getString(Server.TAG_ID_USER);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(Server.TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(Server.TAG_ID_USER, id_user);
                        editor.putString(Server.TAG_USERNAME, username);
                        editor.commit();

                        Intent intent = new Intent(Login.this, Dashboard.class);
                        intent.putExtra(Server.TAG_ID_USER, id_user);
                        intent.putExtra(Server.TAG_USERNAME, username);
                        finish();
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(Server.TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}