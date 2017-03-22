package com.example.dell.retrofirestapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ListInventarioActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    private final String TAG_D = "ListInventarioActivity";
    IventarioAdapter iventarioAdapter;
    RecyclerView inveRecicler;
    FloatingActionButton agregar;
    Inventario inventario;
    List<Inventario> inventariosList;

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
              inventario = iventarioAdapter.getItem(inveRecicler.getChildAdapterPosition(v));
             Intent intent = new Intent().setClass(ListInventarioActivity.this,UpdateActivity.class);
                intent.putExtra("inventario",inventario);
                startActivity(intent);
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

        Intent intent = new Intent(getApplicationContext(),ServiceVencimiento.class);
        startService(intent);
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
                inventariosList = response.body();
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
                Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                listInventario();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buscar_productos,menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Inventario> inventarios = filter(inventariosList,newText);
        iventarioAdapter.set((ArrayList<Inventario>) inventarios);
        return false;
    }

    private List<Inventario> filter (List<Inventario> inventarios,String clave){
        clave = clave.toLowerCase();
        final List<Inventario> filterInventario = new ArrayList<>();
        for(Inventario i:inventarios){
            final String text = i.getNombre().toLowerCase();
            if(text.contains(clave)){
                filterInventario.add(i);
            }
        }
        return filterInventario;
    }
}
