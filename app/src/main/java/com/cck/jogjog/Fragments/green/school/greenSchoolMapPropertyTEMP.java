package com.cck.jogjog.Fragments.green.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.blue.school.blueSchoolRentListFragment;
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

public class greenSchoolMapPropertyTEMP extends Fragment implements OnMapReadyCallback {

    Button btn_back_map_property;
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_map_property, container, false);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_property));
        mapFragment.getMapAsync(this);

        btn_back_map_property = view.findViewById(R.id.btn_back_map_property);
        btn_back_map_property.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment selectedFragment = null;

                selectedFragment = new greenSchoolRentListFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

            }
        });

        getLatLong();


        return view;
    }


    public void getLatLong() {

        String latitude = GlobalVar.getLatitude();
        String longitude = GlobalVar.getLongitude();

        Double x = Double.parseDouble(latitude);
        Double y = Double.parseDouble(longitude);
        System.out.println("LATITUDE: " + latitude);
        System.out.println("LONGITUDE: " + longitude);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        String latitude = GlobalVar.getLatitude();
        String longitude = GlobalVar.getLongitude();

        Double x = Double.parseDouble(latitude);
        Double y = Double.parseDouble(longitude);

        mMap = googleMap;
        LatLng japan = new LatLng(x, y);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));

        MarkerOptions client_marker = new MarkerOptions().position(new LatLng(x, y));
        client_marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon_sell));
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
