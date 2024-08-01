package com.bcoff.pbo;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MerchandiseAdapter extends RecyclerView.Adapter<MerchandiseAdapter.ProductViewHolder> {

    private Activity mCtx;
    private List<ListMenu> menuList;
    public static final String my_shared_preferences = "my_shared_preferences";
    private static final String TAG = ProductAdapter.class.getSimpleName();

    public MerchandiseAdapter(Activity mCtx, List<ListMenu> menuList) {
        this.mCtx = mCtx;
        this.menuList = menuList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listmerchandise, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final ListMenu menu = menuList.get(position);

        holder.textViewNama.setText(menu.getNama_menu());
        holder.textviewbg.setText(menu.getNama_menu());
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
        TextView textviewbg;
        ImageView img;
        View view;

        public ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            textViewNama = itemView.findViewById(R.id.nama_menu);
            textviewbg = itemView.findViewById(R.id.namabg);
            img = itemView.findViewById(R.id.img);
        }
    }
}