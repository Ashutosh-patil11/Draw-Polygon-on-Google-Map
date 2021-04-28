package com.example.workingwithmap;

import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, SeekBar.OnSeekBarChangeListener {
    //Initialize map first
    GoogleMap gMap;
   // Marker marker;

    CheckBox checkBox;
    SeekBar seekRed, seekGreen, seekBlue;
    Button btnDraw, btnClear;

    Polygon polygon = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    int red=0, green=0, blue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        checkBox = findViewById(R.id.cb);
        seekRed = findViewById(R.id.sb1);
        seekGreen = findViewById(R.id.sb2);
        seekBlue = findViewById(R.id.sb3);
        btnDraw = findViewById(R.id.btn);
        btnClear = findViewById(R.id.btn2);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set check box state
                if(isChecked)
                {
                    if(polygon == null)
                    {
                        polygon.setFillColor(Color.rgb(red,green,blue));
                    }
                }
                else
                {
                    // Unfill color if check box not checked
                    polygon.setFillColor(Color.TRANSPARENT);
                }
            }
        });

        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finally draw polygon on map
                if(polygon != null)
                {
                    polygon.remove();
                }
                //create polygon Options
                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).clickable(true);
                polygon = gMap.addPolygon(polygonOptions);
                //set Polygon Stroke color
                polygon.setStrokeColor(Color.rgb(red,green,blue));
                if(checkBox.isChecked())
                {
                    //fill colors
                    polygon.setFillColor(Color.rgb(red,green,blue));
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the polygon
                if(polygon != null)
                {
                    polygon.remove();
                }
                for(Marker marker: markerList) marker.remove();
                latLngList.clear();
                checkBox.setChecked(false);
                seekRed.setProgress(0);
                seekGreen.setProgress(0);
                seekBlue.setProgress(0);
            }
        });

        seekRed.setOnSeekBarChangeListener(this);
        seekGreen.setOnSeekBarChangeListener(this);
        seekBlue.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Creating marker
                MarkerOptions markerOptions = new MarkerOptions();
                //set marker position by passing latitude
                markerOptions.position(latLng);
                //set latitude and longitude on marker position
                markerOptions.title(latLng.latitude+ " : " + latLng.longitude);
                //and clear previous click position
                gMap.clear();
                //for zooming on marker
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                //Add marker on map
                Marker marker = gMap.addMarker(markerOptions);

                //add laang and lati for polygon
                latLngList.add(latLng);
                markerList.add(marker);
            }
        });

        gMap.setOnMapLongClickListener(this);


    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        
        gMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId())
        {
            case R.id.sb1:
                red=progress;
                break;
            case R.id.sb2:
                green = progress;
                break;
            case R.id.sb3:
                blue = progress;
                break;
        }
        if(polygon != null)
        {
            polygon.setStrokeColor(Color.rgb(red,green,blue));
            if(checkBox.isChecked())
            {
                //fill colors
                polygon.setFillColor(Color.rgb(red,green,blue));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}