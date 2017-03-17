package com.example.dell.retrofirestapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 17/03/2017.
 */

public class ServiceVencimiento extends Service {
    private final String TAG_D = "ServiceVencimiento";
    TimerTask timerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG_D, "Servicio creado");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG_D, "Servicio destruido");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG_D, "Servicio iniciado");
        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                vencimiento();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 12000);
        return START_NOT_STICKY;
    }


    public void vencimiento() {
        ClientService clientService = ClientService.retrofit.create(ClientService.class);
        final Call call = clientService.vencimiento();

        call.enqueue(new Callback<List<Vencimiento>>() {
            @Override
            public void onResponse(Call<List<Vencimiento>> call, Response<List<Vencimiento>> response) {
                verificar(response.body());

            }

            @Override
            public void onFailure(Call<List<Vencimiento>> call, Throwable t) {
                Log.d(TAG_D, t.getMessage());
            }
        });
    }

    public void verificar(List<Vencimiento> vencimientos) {
        for (Vencimiento v : vencimientos) {
            if (v.getRespuesta().equals("a")) {
                Log.d(TAG_D, "productos prosimos a vencer");
                showNotification();
                break;
            } else {

            }
        }
    }

    public void showNotification() {
        Intent intent = new Intent("com.rj.notitfications.SECACTIVITY");

        PendingIntent pendingIntent = PendingIntent.getActivity(ServiceVencimiento.this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(ServiceVencimiento.this);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_warning_black_24dp);
        builder.setAutoCancel(false);
        builder.setTicker("Productor proximos por vencer");
        builder.setContentTitle("Advertencia");
        builder.setContentText("Productos proximos por vencer");
        builder.setSmallIcon(android.R.drawable.stat_sys_warning);
        builder.setLargeIcon(icon);
        builder.setSmallIcon(R.drawable.ic_warning_black_24dp);
        //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setNumber(100);
        builder.build();

        Notification myNotication = builder.getNotification();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(11, myNotication);
    }
}
