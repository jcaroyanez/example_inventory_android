package com.example.dell.retrofirestapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.lang.NumberFormatException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private final String TAG_D = "MainActivity";
    Button btnFechaI,btnFechaF,btnGuardar;
    EditText nombre,cantidad,precio_u;
    TextView total;
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;



    Inventario inventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////declaraciones
        btnFechaI = (Button) findViewById(R.id.btn_fi);
        btnFechaF = (Button) findViewById(R.id.btn_ff);
        nombre = (EditText) findViewById(R.id.nProducto);
        cantidad = (EditText) findViewById(R.id.cantidad);
        precio_u = (EditText) findViewById(R.id.precio);
        total = (TextView) findViewById(R.id.total);
        btnGuardar = (Button)findViewById(R.id.btnguardar);
        inventario = new Inventario();




        ///////////////////////////
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre.getText().equals("") || cantidad.getText().equals("")
                        || precio_u.getText().equals("") || total.getText().equals("0")
                        || inventario.getFecha_i() == null || inventario.getFecha_f() == null  ){
                     Toast.makeText(getApplicationContext(),"Asegúrese de a ver llenado todo los campos y a ver seleccionado" +
                             " las fechas",Toast.LENGTH_SHORT).show();
                }else{
                    inventario.setNombre(nombre.getText().toString());
                    inventario.setCantidad(Integer.parseInt(cantidad.getText().toString()));
                    inventario.setPrecio(Double.parseDouble(precio_u.getText().toString()));
                    inventario.setTotal(Double.parseDouble(total.getText().toString()));
                    saveCarro(inventario);
                }
            }
        });







        /////////////////
        precio_u.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!cantidad.getText().toString().equals("")) {
                    try {
                        total.setText(String.valueOf(Integer.parseInt(cantidad.getText().toString())
                                * (Double.parseDouble(s.toString()))));
                    }catch (NumberFormatException ne){
                        total.setText("0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        btnFechaI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(MainActivity.this, Year, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(Color.parseColor("#009688"));

                datePickerDialog.setTitle("Selecione la fecha inical");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialogI");
            }
        });

        btnFechaF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(MainActivity.this, Year, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(Color.parseColor("#009688"));

                datePickerDialog.setTitle("Selecione la fecha final");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialogF");
            }
        });


    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {
          Log.d(TAG_D,Year+"-"+Month+"-"+Day);
         if(view.getTag().equals("DatePickerDialogI")){
             inventario.setFecha_i(Year+"-"+(Month+1)+"-"+Day);
         }else{
            inventario.setFecha_f(Year+"-"+(Month+1)+"-"+Day);
         }

    }
  ///////////////////// servicios
        public void saveCarro(final Inventario inventario){
        ClientService clientService = ClientService.retrofit.create(ClientService.class);
            final Call<Respuesta> call = clientService.save(inventario);
            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                    if(response.body().getStatus().equals("success")){
                        Toast.makeText(getApplicationContext(),
                                response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                nombre.setText("");
                                cantidad.setText("");
                                precio_u.setText("");
                                total.setText("0");
                                datePickerDialog = DatePickerDialog
                                        .newInstance(MainActivity.this, Year, Month, Day);
                    }else{
                        Toast.makeText(getApplicationContext(),
                                response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG_D,response.body().getMessage());
                }
                @Override
                public void onFailure(Call<Respuesta> call, Throwable t) {
                    Log.d("Response save","save - "+t.toString());
                }
            });

         }
    /*
    public void listAllCars(){
        ClientService clientService = ClientService.retrofit.create(ClientService.class);
        final Call call = clientService.findAllPlaca();

        call.enqueue(new Callback<List<Carro>>() {
            @Override
            public void onResponse(Call<List<Carro>> call, Response<List<Carro>> response) {
                Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();
                Log.d(TAG_D,response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Carro>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG_D,t.getMessage());
            }
        });
    }*/

}
