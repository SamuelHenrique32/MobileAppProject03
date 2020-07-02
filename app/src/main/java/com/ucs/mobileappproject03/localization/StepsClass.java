package com.ucs.mobileappproject03.localization;

import java.io.Serializable;

public class StepsClass implements Serializable
{
    private int id;
    private String passos;
    private String data;

    public  StepsClass(){}

    public StepsClass(String passos, String data)
    {
        this.passos = passos;
        this.data = data;
    }

    public String getPassos() {
        return passos;
    }

    public void setPassos(String passos) {
        this.passos = passos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
