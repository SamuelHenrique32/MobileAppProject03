package com.ucs.mobileappproject03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ucs.mobileappproject03.bd.Store;
import com.ucs.mobileappproject03.localization.GPSClass;

import java.util.ArrayList;
import java.util.Calendar;

public class MainFragment extends Fragment {

    private Button testButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main,container,false);

        testButton = (Button) view.findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTestData();
            }
        });

        return view;
    }

    public void createTestData(){
        this.createGPSpoints();

        Store.inTest = true;
    }

    public void createGPSpoints()
    {
        long currentTime = Calendar.getInstance().getTime().getTime();

        // Pontos ao redor do Shopping Iguatemi - Caxias do Sul
        final ArrayList<GPSClass> listaGPS = new ArrayList<>();
        listaGPS.add(new GPSClass("-29.175973","-51.219703",currentTime));
        listaGPS.add(new GPSClass("-29.175876","-51.219902",currentTime));
        listaGPS.add(new GPSClass("-29.175658","-51.220337",currentTime));
        listaGPS.add(new GPSClass("-29.176111","-51.219406",currentTime));
        listaGPS.add(new GPSClass("-29.175831","-51.221119",currentTime));
        listaGPS.add(new GPSClass("-29.176077","-51.221172",currentTime));
        listaGPS.add(new GPSClass("-29.176324","-51.221148",currentTime));
        listaGPS.add(new GPSClass("-29.176649","-51.221092",currentTime));
        listaGPS.add(new GPSClass("-29.176130","-51.219306",currentTime));
        listaGPS.add(new GPSClass("-29.175995","-51.219118",currentTime));
        listaGPS.add(new GPSClass("-29.175831","-51.219027",currentTime));
        listaGPS.add(new GPSClass("-29.176038","-51.219557",currentTime));

        Store.objects = listaGPS;
    }
}