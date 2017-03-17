package com.example.dell.retrofirestapp;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * Created by DELL on 17/03/2017.
 */

public class Vencimiento implements Serializable{
    String respuesta;
    TimerTask timerTask;

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public String toString() {
        return "Vencimiento{" +
                "respuesta='" + respuesta + '\'' +
                '}';
    }
}
