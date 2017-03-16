package com.example.dell.retrofirestapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

public class ListInventarioActivity extends AppCompatActivity {
    private final String TAG_D = "ListInventarioActivity";
    IventarioAdapter iventarioAdapter;
    RecyclerView inveRecicler;
    FloatingActionButton agregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_inventario);
        inveRecicler = (RecyclerView)findViewById(R.id.inven_recyvler);
        inveRecicler.setHasFixedSize(true);
        iventarioAdapter = new IventarioAdapter();
        iventarioAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iventarioAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createSimpleDialog(
                     iventarioAdapter.getItem(inveRecicler.getChildAdapterPosition(v)).getId()).show();
                return true;
            }
        });
        inveRecicler.setAdapter(iventarioAdapter);
        inveRecicler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        inveRecicler.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        inveRecicler.setItemAnimator(new DefaultItemAnimator());

        agregar = (FloatingActionButton) findViewById(R.id.agregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent   mainIntent = new Intent().setClass(
                        ListInventarioActivity.this,MainActivity.class);
                startActivity(mainIntent);
            }
        });
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

    public void eliminar(int id){
        ClientService clientService = ClientService.retrofit.create(ClientService.class);
        final Call call = clientService.delete(id);
        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Log.d(TAG_D, response.body().toString());
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.d(TAG_D, t.getMessage());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listInventario();
    }

    public AlertDialog createSimpleDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListInventarioActivity.this);
        builder.setTitle("Inventario")
                .setMessage("Desea eliminar este producto?")
                .setNeutralButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminar(id);
                                listInventario();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }


}
