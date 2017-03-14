package com.example.dell.retrofirestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListInventarioActivity extends AppCompatActivity {
    private final String TAG_D = "ListInventarioActivity";
    IventarioAdapter iventarioAdapter;
    RecyclerView inveRecicler;
    ArrayList<Inventario> inventarioArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_inventario);
        inveRecicler = (RecyclerView)findViewById(R.id.inven_recyvler);
        inveRecicler.setHasFixedSize(true);
        iventarioAdapter = new IventarioAdapter();
        inveRecicler.setAdapter(iventarioAdapter);
        inveRecicler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        inveRecicler.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        inveRecicler.setItemAnimator(new DefaultItemAnimator());
        listInventario();
    }

    public void refrescar(List<Inventario> inventarios){
        iventarioAdapter.set((ArrayList<Inventario>) inventarios);
        iventarioAdapter.notifyDataSetChanged();
    }
    public void listInventario(){
        ClientService clientService = ClientService.retrofit.create(ClientService.class);
        final Call call = clientService.listarCarros();

        call.enqueue(new Callback<List<Inventario>>() {
            @Override
            public void onResponse(Call<List<Inventario>> call, Response<List<Inventario>> response) {
                refrescar(response.body());
                Log.d(TAG_D,response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Inventario>> call, Throwable t) {
                Log.d(TAG_D,t.getMessage());
            }
        });
    }
}
