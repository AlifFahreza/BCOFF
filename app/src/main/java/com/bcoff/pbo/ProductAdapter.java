package com.bcoff.pbo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Activity mCtx;
    private List<ListMenu> menuList;
    public static final String my_shared_preferences = "my_shared_preferences";
    private static final String TAG = ProductAdapter.class.getSimpleName();

    public ProductAdapter(Activity mCtx, List<ListMenu> menuList) {
        this.mCtx = mCtx;
        this.menuList = menuList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listmenu, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final ListMenu menu = menuList.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int nominal = Integer.parseInt(menu.getHarga());

        holder.textViewNama.setText(menu.getNama_menu());
        holder.textViewHarga.setText(""+ formatRupiah.format((double)nominal));
        Picasso.with(mCtx)
                .load("https://bcoff.000webhostapp.com/menu/" + menu.getGambar())
                .into(holder.img);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, DeskripsiMenu.class);
                intent.putExtra("id_menu", menu.getId_menu());
                intent.putExtra("nama_menu", menu.getNama_menu());
                intent.putExtra("gambar", menu.getGambar());
                intent.putExtra("deskripsi", menu.getDeskripsi());
                intent.putExtra("harga", menu.getHarga());
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNama;
        TextView textViewHarga;
        ImageView img;
        View view;

        public ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            textViewNama = itemView.findViewById(R.id.nama_menu);
            textViewHarga = itemView.findViewById(R.id.harga);
            img = itemView.findViewById(R.id.img);
        }
    }
}