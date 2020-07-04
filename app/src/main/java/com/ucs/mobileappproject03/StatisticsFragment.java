package com.ucs.mobileappproject03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ucs.mobileappproject03.bd.BDSQLiteHelper;
import com.ucs.mobileappproject03.bd.Store;

public class StatisticsFragment extends Fragment {

    private TextView txtStepsWeek;
    private TextView txtStepsMonth;
    private TextView txtStepsTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics,container,false);

        txtStepsWeek = (TextView) view.findViewById(R.id.txtStepsWeekValue);
        txtStepsMonth = (TextView) view.findViewById(R.id.txtStepsMonthValue);
        txtStepsTotal = (TextView) view.findViewById(R.id.txtStepsTotalValue);

        calculateStatistics();

        return view;
    }

    private void calculateStatistics() {
        BDSQLiteHelper bd;

        if(Store.inTest){
            bd=Store.bdTestSteps;
        } else{
            bd=Store.bd;
        }

        txtStepsWeek.setText(Integer.toString(bd.getWeekSteps()));
        txtStepsMonth.setText(Integer.toString(bd.getMonthSteps()));
        txtStepsTotal.setText(Integer.toString(bd.getAllSteps()));
    }
}
