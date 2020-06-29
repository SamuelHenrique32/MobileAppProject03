package com.ucs.mobileappproject03;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ucs.mobileappproject03.bd.Store;
import com.ucs.mobileappproject03.localization.GPSClass;
import com.ucs.mobileappproject03.localization.HeatmapsDemoActivity;

import java.util.ArrayList;

public class FragmentSecond extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArrayList<GPSClass> objects = Store.objects;
        Bundle extra = new Bundle();
        extra.putSerializable("objects",objects);

        Intent intent = new Intent(getActivity(), HeatmapsDemoActivity.class);
        intent.putExtra("extra", extra);
        startActivity(intent);

        View view = inflater.inflate(R.layout.fragment_second,container,false);

        return view;
    }
}
