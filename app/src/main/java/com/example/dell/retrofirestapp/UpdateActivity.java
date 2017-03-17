package com.example.dell.retrofirestapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private final String TAG_D = "UpdateActivity";
    Button btnFechaI,btnFechaF,btnGuardar;
    EditText nombre,cantidad,precio_u;
    TextView total;
    Calendar calendar ;
    DatePickerDialog datePickerDialogI ;
    DatePickerDialog datePickerDialogF ;
    int Year, Month, Day ;
    Inventario inventario;
    String[] fechai;
    String[] fechaf;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Bundle bundle = getIntent().getExtras();
        inventario = (Inventario)bundle.get("inventario");

        fechai = inventario.getFecha_i().split("-");
        fechaf = inventario.getFecha_f().split("-");

        datePickerDialogI = DatePickerDialog.newInstance
                (UpdateActivity.this,
                        Integer.parseInt(fechai[0]),Integer.parseInt(fechai[1])-1,Integer.parseInt(fechai[2]));

        datePickerDialogF = DatePickerDialog.newInstance
                (UpdateActivity.this,
                        Integer.parseInt(fechaf[0]),Integer.parseInt(fechaf[1])-1,Integer.parseInt(fechaf[2]));

        btnFechaI = (Button) findViewById(R.id.btn_fi);
        btnFechaF = (Button) findViewById(R.id.btn_ff);
        nombre = (EditText) findViewById(R.id.nProducto);
        cantidad = (EditText) findViewById(R.id.cantidad);
        precio_u = (EditText) findViewById(R.id.precio);
        total = (TextView) findViewById(R.id.total);
        btnGuardar = (Button)findViewById(R.id.btnguardar);

        nombre.setText(inventario.getNombre());
        cantidad.setText(inventario.getCantidad()+"");
        precio_u.setText(inventario.getPrecio()+"");
        total.setText(inventario.getTotal()+"");

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        btnFechaI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                datePickerDialogI.setThemeDark(false);

                datePickerDialogI.showYearPickerFirst(false);

                datePickerDialogI.setAccentColor(Color.parseColor("#009688"));

                datePickerDialogI.setTitle("Selecione la fecha inical");

                datePickerDialogI.show(getFragmentManager(), "DatePickerDialogI");
            }
        });

        btnFechaF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                datePickerDialogF.setThemeDark(false);

                datePickerDialogF.showYearPickerFirst(false);

                datePickerDialogF.setAccentColor(Color.parseColor("#009688"));

                datePickerDialogF.setTitle("Selecione la fecha final");

                datePickerDialogF.show(getFragmentManager(), "DatePickerDialogF");
            }
        });

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

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nombre.getText().toString().trim().equals("") || cantidad.getText().toString().trim().equals("")
                        || precio_u.getText().toString().trim().equals("") || total.getText().toString().trim().equals("0")
                        || inventario.getFecha_i() == null || inventario.getFecha_f() == null  ){
                    Toast.makeText(getApplicationContext(),"Asegúrese de a ver llenado todo los campos y a ver seleccionado" +
                            " las fechas",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG_D,"ok");
                    inventario.setNombre(nombre.getText().toString());
                    inventario.setCantidad(Integer.parseInt(cantidad.getText().toString()));
                    inventario.setPrecio(Double.parseDouble(precio_u.getText().toString()));
                    inventario.setTotal(Double.parseDouble(total.getText().toString()));
                    updateProducto(inventario);
                }
            }
        });


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(view.getTag().equals("DatePickerDialogI")){

            inventario.setFecha_i(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            view.initialize(UpdateActivity.this,year,monthOfYear,dayOfMonth);

        }else if(view.getTag().equals("DatePickerDialogF")){

            inventario.setFecha_f(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            view.initialize(UpdateActivity.this,year,monthOfYear,dayOfMonth);

        }
    }

    public void updateProducto(final Inventario inventario){
        Log.d(TAG_D,inventario.toString());
        ClientService clientService = ClientService.retrofit.create(ClientService.class);
        final Call<Respuesta> call = clientService.update(inventario);
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
                    Intent intent = new Intent()
                            .setClass(UpdateActivity.this,ListInventarioActivity.class);
                    startActivity(intent);
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
}
