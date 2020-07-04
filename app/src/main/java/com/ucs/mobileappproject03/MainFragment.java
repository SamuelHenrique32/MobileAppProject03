package com.ucs.mobileappproject03;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ucs.mobileappproject03.bd.BDSQLiteHelper;
import com.ucs.mobileappproject03.bd.Store;
import com.ucs.mobileappproject03.localization.GPSClass;
import com.ucs.mobileappproject03.localization.StepsClass;

import java.util.ArrayList;
import java.util.Calendar;

public class MainFragment extends Fragment {

    private Button testButton;
    private TextView txtStepsToday;
    private TextView txtStepsRemainingToGoal;
    private final int STEPS_DAILY_GOAL = 4500;
    private final long MILLISECONDSBYDAY = 86400000;

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

        txtStepsToday = (TextView) view.findViewById(R.id.txtStepsQuantity);

        txtStepsRemainingToGoal = (TextView) view.findViewById(R.id.txtStepsGoalQuantity);

        calculateTodaySteps();

        if(!Store.inTest){
            testButton.setBackgroundColor(Color.WHITE);
            testButton.setTextColor(Color.BLACK);
        } else{
            testButton.setBackgroundColor(Color.GREEN);
            testButton.setTextColor(Color.WHITE);
        }

        return view;
    }

    private void createTestData(){
        this.createGPSpoints();

        this.createTestSteps();

        Store.inTest = !Store.inTest;

        if(!Store.inTest){
            testButton.setBackgroundColor(Color.WHITE);
            testButton.setTextColor(Color.BLACK);
        } else{
            testButton.setBackgroundColor(Color.GREEN);
            testButton.setTextColor(Color.WHITE);
        }

        calculateTodaySteps();
    }

    private void createGPSpoints()
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

    public void createTestSteps()
    {
        long currentTime = Calendar.getInstance().getTime().getTime();

        Store.bdTestSteps.deleteAllStepsRegisters();

        Store.bdTestSteps.addSteps(new StepsClass(3276,currentTime));
        Store.bdTestSteps.addSteps(new StepsClass(3200,currentTime - MILLISECONDSBYDAY));
        Store.bdTestSteps.addSteps(new StepsClass(2132,currentTime - MILLISECONDSBYDAY*2));
        Store.bdTestSteps.addSteps(new StepsClass(4533,currentTime - MILLISECONDSBYDAY*3));
        Store.bdTestSteps.addSteps(new StepsClass(1233,currentTime - MILLISECONDSBYDAY*4));
        Store.bdTestSteps.addSteps(new StepsClass(8783,currentTime - MILLISECONDSBYDAY*5));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*6));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*7));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*8));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*9));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*10));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*11));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*12));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*13));
        Store.bdTestSteps.addSteps(new StepsClass(1000,currentTime - MILLISECONDSBYDAY*14));
    }

    public void calculateTodaySteps(){
        BDSQLiteHelper bd;

        if(Store.inTest){
            bd=Store.bdTestSteps;
        } else{
            bd=Store.bd;
        }

        int todaySteps = bd.getTodaySteps();

        txtStepsToday.setText(Integer.toString(todaySteps));

        txtStepsRemainingToGoal.setText(Integer.toString(STEPS_DAILY_GOAL-todaySteps));
    }
}