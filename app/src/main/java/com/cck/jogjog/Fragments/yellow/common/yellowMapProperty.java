package com.cck.jogjog.Fragments.yellow.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class yellowMapProperty extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn_back_map_property;

    public static yellowMapProperty newInstance() {
        yellowMapProperty mapDialog = new yellowMapProperty();
        //Bundle args = new Bundle();
        return mapDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        setShowsDialog(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return view;
        View mapView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_yellow_map_property, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(mapView);
        final AlertDialog dialog = builder.create();

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map_property);
        getLatLong();

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            System.out.println("mapFragment NOT NULL");
            System.out.println(mapFragment);
        } else {
            System.out.println("HELLO???????");
            System.out.println(mapFragment);
        }


        btn_back_map_property = mapView.findViewById(R.id.btn_back_map_property);
        btn_back_map_property.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Fragment selectedFragment = null;
//                selectedFragment = new yellowAddressPropertyDetailFragment();
//                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                dialog.cancel();
//                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assert getFragmentManager() != null;
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map_property));
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    public void getLatLong() {

        String latitude = GlobalVar.getLatitude();
        String longitude = GlobalVar.getLongitude();

        Double x = Double.parseDouble(latitude);
        Double y = Double.parseDouble(longitude);

        System.out.println("getLatLong()");
        System.out.println("LATITUDE: " + latitude);
        System.out.println("LONGITUDE: " + longitude);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        String latitude = GlobalVar.getLatitude();
        String longitude = GlobalVar.getLongitude();

        Double x = Double.parseDouble(latitude);
        Double y = Double.parseDouble(longitude);

        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("onMapReady COORDINATES");
        System.out.println("LATITUDE: " + latitude);
        System.out.println("LONGITUDE: " + longitude);

        mMap = googleMap;
        LatLng japan = new LatLng(x, y);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));

        MarkerOptions client_marker = new MarkerOptions().position(new LatLng(x, y));
        client_marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon_rent));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(japan, 16f));
        Marker marker = mMap.addMarker(client_marker);
        marker.setTag("testing");
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker clickedMarker) {

                return true;
            }
        });
    }
}
