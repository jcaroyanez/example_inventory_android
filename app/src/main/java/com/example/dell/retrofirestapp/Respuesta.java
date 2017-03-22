package com.example.dell.retrofirestapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

/**
 * Created by DELL on 8/03/2017.*/

public class Respuesta {
    String status;
    String  message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Respuesta{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
