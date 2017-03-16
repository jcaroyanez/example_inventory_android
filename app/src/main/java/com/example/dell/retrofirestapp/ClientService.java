package com.example.dell.retrofirestapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by DELL on 8/03/2017.
 */

public interface ClientService {

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    @GET("inventarios")
    Call<List<Inventario>> listarCarros();

    @Headers({"Content-Type:application/json"})
    @POST("inventario")
     Call<Respuesta> save(@Body Inventario inventario);

    @GET("delete/{id}")
    Call<Respuesta> delete(@Path("id") int id);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.111/practicas/api-rest-inventario/carro-api.php/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
