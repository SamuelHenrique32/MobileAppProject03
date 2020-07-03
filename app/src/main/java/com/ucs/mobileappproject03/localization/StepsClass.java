package com.ucs.mobileappproject03.localization;

import java.io.Serializable;

public class StepsClass implements Serializable
{
    private int id;
    private long passos;
    private long data;

    public  StepsClass(){}

    public StepsClass(long passos, long data)
    {
        this.passos = passos;
        this.data = data;
    }

    public long getPassos() {
        return passos;
    }

    public void setPassos(long passos) {
        this.passos = passos;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
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
