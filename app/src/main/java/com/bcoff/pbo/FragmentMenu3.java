package com.bcoff.pbo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentMenu3 extends Fragment {

    private RecyclerView recyclerView;
    public String jenis_menu = "3";
    List<ListMenu> listmenu;

    public FragmentMenu3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu3, container, false);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        listmenu = new ArrayList<>();
        loadList();

        return view;
    }

    private void loadList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.URL_MENU+jenis_menu,
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

                            ProductAdapter adapter = new ProductAdapter(getActivity(), listmenu);
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

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}