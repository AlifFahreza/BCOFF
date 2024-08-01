package com.bcoff.pbo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bcoff.pbo.Dashboard.TAG_ID;

public class Profile extends getServer {

    CircleImageView userpic;
    String cameraPermission[];
    String storagePermission[];
    String id_user, username, email, password, profilepicture, JSON_STRING;
    EditText usernametx, emailtx, passwordtx;
    SharedPreferences sharedpreferences;
    public LinearLayout linearback;
    Bitmap bitmap, decoded;
    public static final String TAG_ID = "id";
    public static final String my_shared_preferences = "my_shared_preferences";
    public Button btn_logout, btn_save;
    int bitmap_size = 70;
    int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);

        userpic = findViewById(R.id.set_profile_image);
        usernametx = findViewById(R.id.usernametxt);
        emailtx = findViewById(R.id.emailtxt);
        passwordtx = findViewById(R.id.passwordtxt);
        linearback = findViewById(R.id.linearback);
        btn_logout = findViewById(R.id.btn_logout);
        btn_save = findViewById(R.id.btn_save);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        getJSON();

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.commit();

                Intent intent = new Intent(Profile.this, Intro.class);
                finish();
                startActivity(intent);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newusername = usernametx.getText().toString().trim();
                final String newemail = emailtx.getText().toString().trim();
                final String newpassword = passwordtx.getText().toString().trim();

                if (decoded == null){
                    Toast.makeText(Profile.this, "Foto Profil Wajib Ada", Toast.LENGTH_SHORT).show();
                }else {
                    class UpdateProfile extends AsyncTask<Void,Void,String>{
                        ProgressDialog loading;
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(Profile.this,"Updating...","Wait...",false,false);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                            if (s.contains("1")){
                                Toast.makeText(Profile.this, "Data berhasil di update", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Profile.this, "Error update Data", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        protected String doInBackground(Void... params) {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put(Server.TAG_ID_USER,sharedpreferences.getString(Server.TAG_ID_USER, null));
                            hashMap.put(Server.TAG_USERNAME,newusername);
                            hashMap.put(Server.TAG_EMAIL,newemail);
                            hashMap.put(Server.TAG_PASSWORD,newpassword);
                            hashMap.put(Server.TAG_PROFILE,getStringImage(decoded));

                            RequestHandler rh = new RequestHandler();

                            String s = rh.sendPostRequest(Server.URL_UPDATEPROFILE+sharedpreferences.getString(Server.TAG_ID_USER, null),hashMap);

                            return s;
                        }
                    }

                    UpdateProfile gj = new UpdateProfile();
                    gj.execute();
                }
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        userpic.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Server.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                id_user = jo.getString(Server.TAG_ID_USER);
                username = jo.getString(Server.TAG_USERNAME);
                email = jo.getString(Server.TAG_EMAIL);
                password = jo.getString(Server.TAG_PASSWORD);
                profilepicture = jo.getString(Server.TAG_PROFILE);

                HashMap<String, String> data = new HashMap<>();
                data.put(Server.TAG_ID_USER, id_user);
                data.put(Server.TAG_USERNAME, username);
                data.put(Server.TAG_EMAIL, email);
                data.put(Server.TAG_PASSWORD, password);
                data.put(Server.TAG_PROFILE, profilepicture);

                list.add(data);

                usernametx.setText(""+username);
                emailtx.setText(""+email);
                passwordtx.setText(""+password);

                if (profilepicture.contains("null")){

                }else {
                    Picasso.with(this).load(profilepicture).into(userpic);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSON() {
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
                String s = rh.sendGetRequest(Profile.this.getLink()+sharedpreferences.getString(Server.TAG_ID_USER, null));
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    String getLink() {
        return "https://bcoff.000webhostapp.com/profile.php?id_user=";
    }
}