package com.ucs.mobileappproject03.pedometer;

import java.io.Serializable;

public class PedometerClass implements Serializable
{
    private int id;
    private String quantidadePassos;
    private String data;

    public PedometerClass(){}

    public PedometerClass(String quantidadePassos, String data)
    {
        this.quantidadePassos = quantidadePassos;
        this. data = data;
    }

    public String getQuantidadePassos(){return quantidadePassos;}

    public void setQuantidadePassos(String quantidadePassos){this.quantidadePassos = quantidadePassos;}

    public String getData(){return data;}

    public void setData(String data){this.data = data;}

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

}
