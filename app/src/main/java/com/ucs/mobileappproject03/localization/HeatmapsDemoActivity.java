package com.ucs.mobileappproject03.localization;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.Gradient;
import com.ucs.mobileappproject03.MainActivity;
import com.ucs.mobileappproject03.R;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;

public class HeatmapsDemoActivity extends BaseDemoActivity {

    private ArrayList<GPSClass> latLgnRegisters;
    private final int DEFAULT_ZOOM = 15;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    /**
     * Maps name of data set to data (list of LatLngs)
     * Also maps to the URL of the data set for attribution
     */
    private HashMap<String, DataSet> mLists = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.heatmaps_demo;
    }

    @Override
    protected void startDemo(boolean isRestore) {

        Bundle extra = getIntent().getBundleExtra("extra");
        this.latLgnRegisters = (ArrayList<GPSClass>) extra.getSerializable("objects");

        if (!isRestore) {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-25, 143), 4));
        }

        try {
            mLists.put(getString(R.string.location_registers), new DataSet(readItems(R.raw.police),
                    getString(R.string.location_registers_url)));
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }

        mProvider = new HeatmapTileProvider.Builder().data(
                mLists.get(getString(R.string.location_registers)).getData()).build();
        mOverlay = getMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<>();

        for (int i = 0; i < this.latLgnRegisters.size(); i++) {
            double lat = Double.parseDouble(String.valueOf(this.latLgnRegisters.get(i).getLatitude()));
            double lng = Double.parseDouble(String.valueOf(this.latLgnRegisters.get(i).getLongitude()));
            list.add(new LatLng(lat, lng));
        }

        GoogleMap map = getMap();

        LatLng coordinate = new LatLng(Double.parseDouble(String.valueOf(this.latLgnRegisters.get(0).getLatitude())),
                Double.parseDouble(String.valueOf(this.latLgnRegisters.get(this.latLgnRegisters.size()-1).getLongitude())));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, DEFAULT_ZOOM));

        return list;
    }

    /**
     * Helper class - stores data sets and sources.
     */
    private class DataSet {
        private ArrayList<LatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<LatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<LatLng> getData() {
            return mDataset;
        }

        public String getUrl() {
            return mUrl;
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
    }
}