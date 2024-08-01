package com.bcoff.pbo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.ProductViewHolder> {

    private Activity mCtx;
    private List<ListKeranjang> menuList;

    public KeranjangAdapter(Activity mCtx, List<ListKeranjang> menuList) {
        this.mCtx = mCtx;
        this.menuList = menuList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listcart, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final ListKeranjang menu = menuList.get(position);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setCancelable(true);
                builder.setMessage("Are you sure want to delete this menu from your cart?");
                builder.setTitle("Delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapus(menu.getId_keranjang());
                                menuList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
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

    private void hapus(final String id_keranjang) {
        class HapusKeranjang extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(mCtx,"Delete Menu...","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.contains("1")){
                    Toast.makeText(mCtx, "Menu berhasil di Hapus dari Keranjang", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mCtx, Keranjang.class);
                    mCtx.finish();
                    mCtx.startActivity(intent);
                }else {
                    Toast.makeText(mCtx, "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id_keranjang",id_keranjang);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Server.URL_DELETE+id_keranjang,hashMap);

                return s;
            }
        }

        HapusKeranjang gj = new HapusKeranjang();
        gj.execute();
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
