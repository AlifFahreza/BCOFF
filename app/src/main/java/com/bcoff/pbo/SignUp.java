package com.bcoff.pbo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.bcoff.pbo.AppController;
import com.bcoff.pbo.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUp extends AppCompatActivity {

    public int success;
    public ProgressDialog pDialog;
    public ConnectivityManager conMgr;
    public String tag_json_obj = "json_obj_req";
    public EditText usernametxt, passwordtxt, confirmpasswordtxt, emailtxt;
    public TextView error;
    public Button btn_register;
    public LinearLayout linearLayout, linearkoneksi;
    private static final String TAG = SignUp.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);

        usernametxt = findViewById(R.id.usernametxt);
        passwordtxt = findViewById(R.id.passwordtxt);
        confirmpasswordtxt = findViewById(R.id.confirmpasswordtxt);
        emailtxt = findViewById(R.id.emailtxt);
        btn_register = findViewById(R.id.btn_register);
        linearLayout = findViewById(R.id.linearback);
        linearkoneksi = findViewById(R.id.linearkoneksi);
        error = findViewById(R.id.texterror);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Intro.class);
                startActivity(intent);
                finish();
            }
        });

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

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = usernametxt.getText().toString();
                String Email = emailtxt.getText().toString();
                String Password = passwordtxt.getText().toString();
                String Confirm_password = confirmpasswordtxt.getText().toString();
                String id_user = UUID.randomUUID().toString();

                if (Username.isEmpty()){
                    error.setText("Username Is Empty! Please Fill It In");
                    linearkoneksi.setVisibility(View.VISIBLE);
                }else if (Email.isEmpty()){
                    error.setText("Email Is Empty! Please Fill It In");
                    linearkoneksi.setVisibility(View.VISIBLE);
                }else if (Password.isEmpty()){
                    error.setText("Password Is Empty! Please Fill It In");
                    linearkoneksi.setVisibility(View.VISIBLE);
                }else if (Confirm_password.isEmpty()){
                    error.setText("Confirmation Password Is Empty! Please Fill It In");
                    linearkoneksi.setVisibility(View.VISIBLE);
                }else if (!Confirm_password.matches(Password)){
                    error.setText("Your Password and Confirmation Password are different!");
                    linearkoneksi.setVisibility(View.VISIBLE);
                }else {
                    linearkoneksi.setVisibility(View.INVISIBLE);

                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        linearkoneksi.setVisibility(View.INVISIBLE);
                        checkRegister(id_user, Username, Email, Password, Confirm_password);
                    } else {
                        error.setText("No Internet Connection!");
                        linearkoneksi.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void checkRegister(final String id_user, final String username, final String email, final String password, final String confirm_password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Server.URL_register, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(Server.TAG_SUCCESS);

                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());
                        Toast.makeText(SignUp.this, "SignUp Successfull", Toast.LENGTH_SHORT).show();

                        usernametxt.setText("");
                        emailtxt.setText("");
                        passwordtxt.setText("");
                        confirmpasswordtxt.setText("");

                        Intent intent = new Intent(SignUp.this, Intro.class);
                        startActivity(intent);
                        finish();

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
                Log.e(TAG, "Register Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", id_user);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("confirm_password", confirm_password);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, Intro.class);
        startActivity(intent);
        finish();
    }
}