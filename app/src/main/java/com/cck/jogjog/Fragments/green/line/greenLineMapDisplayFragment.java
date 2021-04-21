package com.cck.jogjog.Fragments.green.line;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Fragments.blue.line.blueLineMapInformation;
import com.cck.jogjog.Fragments.green.address.greenAddressMapInformation;
import com.cck.jogjog.GlobalVar.BukkenList;
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
import com.google.android.gms.maps.model.VisibleRegion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_DETAIL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_LIST;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_SELL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_TENANT;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;


public class greenLineMapDisplayFragment extends Fragment implements
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    String lat, lon, lat2, lon2;
    Double maplat, maplong;
    D_cDialog CD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_map_display, container, false);
        mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        GlobalVar.previousScreen = "";

        //getProperties();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            GlobalVar.isFromMap = true;
            GlobalVar.selectedtab = 1;
            if (GlobalVar.previousScreen.equals("")) {
                if (BukkenList.bukken_list.size() > 0) {
                    String greenCenter = BukkenList.bukken_list.get(0).get(3);

                    double latitude = 0;
                    double longitude = 0;

                    //Set Center point
                    Geocoder geocoder = new Geocoder(getContext());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(greenCenter, 1);

                        if (addresses.size() > 0) {
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();

                            System.out.println("lat lon " + latitude + " " + longitude);
                        }
/*
                        mMap = googleMap;
                        mMap.setOnCameraIdleListener(this);
                        mMap.setOnCameraMoveStartedListener(this);
                        mMap.setOnCameraMoveListener(this);
                        mMap.setOnCameraMoveCanceledListener(this);
*/
                        LatLng japan = new LatLng(latitude, longitude); // Japan default
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
                        MarkerOptions client_marker = new MarkerOptions().position(new LatLng(latitude, longitude));
                        client_marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon_sell));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(japan, 17f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //sapporo coordinates
                    maplat = 43.066666;
                    maplong = 141.350006;
                    LatLng japan = new LatLng(maplat, maplong);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(japan, 17f));
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //initial zoom
        mMap = googleMap;

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        if (!(GlobalVar.lat.equals("0"))) {
            maplat = (Double.parseDouble(GlobalVar.lat) + Double.parseDouble(GlobalVar.lat2)) / 2;
            maplong = (Double.parseDouble(GlobalVar.lon) + Double.parseDouble(GlobalVar.lon2)) / 2;

            LatLng japan = new LatLng(maplat, maplong);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(japan, 17f));
        }
    }


    @Override
    public void onCameraMoveStarted(int reason) {

    }

    @Override
    public void onCameraMove() {
        System.out.println("The camera is moving.");

    }

    @Override
    public void onCameraMoveCanceled() {
        System.out.println("Camera movement canceled.");
    }

    @Override
    public void onCameraIdle() {
        //called after stop of camera movemenr

        System.out.println("The camera has stopped moving");

        VisibleRegion vRegion = mMap.getProjection().getVisibleRegion();
        LatLng upperLeft = vRegion.farLeft; //northwest
        LatLng lowerRight = vRegion.nearRight; //southeast

        String[] test = upperLeft.toString().split(",");
        lat2 = test[0].replace("lat/lng: (", ""); // latfrom
        lon = test[1].replace(")", ""); //latto

        String[] test2 = lowerRight.toString().split(",");
        lat = test2[0].replace("lat/lng: (", ""); //lngfrom
        lon2 = test2[1].replace(")", ""); //lngto

        GlobalVar.lat = lat;
        GlobalVar.lat2 = lat2;
        GlobalVar.lon = lon;
        GlobalVar.lon2 = lon2;

        System.out.println("upperLeft " + upperLeft);
        System.out.println("lowerRight " + lowerRight);

        System.out.println("lat " + lat);
        System.out.println("lon " + lon);

        System.out.println("lat2 " + lat2);
        System.out.println("lon2 " + lon2);

        getLatLong();
    }

    public void getLatLong() {

        final String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_SELL + "&is_map_mode=1" +
                "&latfrom=" + lat +
                "&latto=" + lat2 +
                "&lngfrom=" + lon +
                "&lngto=" + lon2;
        GlobalVar.urlFromMap = url;
        System.out.println("URL getLatLong: " + url);
        GlobalVar.setMap_bukken_url(url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");

                    for (int i = 0; i < bukken_list.length(); i++) {
                        JSONObject returnBukken = bukken_list.getJSONObject(i);
                        String bukken_no = returnBukken.getString("bukken_no");
                        System.out.println("bukken_no " + bukken_no);

                        if (bukken_no == null) {
                            System.out.println("null");
                        }
                        else {
                            getLatLongTwo(bukken_no);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int x = 1;
                // Anything you want
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void getLatLongTwo(String bukken_LatLongOne) {

        if (bukken_LatLongOne == null) {
            System.out.println("null");
        } else {

            String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_DETAIL + API_PATH_TYPE_SELL + "&bukken_no=" + bukken_LatLongOne;

            GlobalVar.urlFromMap = url;
            System.out.println("URL getLatLong2: " + url);

            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");

                        String latitude = data.getString("lat"); //latitude
                        String longitude = data.getString("lon"); //longitude
                        String bukken_no = data.getString("bukken_no"); //bukken_no AS060830001_002

                        if (latitude.isEmpty() && longitude.isEmpty()){
                            System.out.println("wala siya sa earth");
                        }
                        else {

                            Double x = Double.parseDouble(latitude);
                            Double y = Double.parseDouble(longitude);

                            System.out.println("latitude: " + x);
                            System.out.println("longitude: " + y);
                            System.out.println("latitude: " + latitude);
                            System.out.println("longitude: " + longitude);
                            System.out.println("bukken_no: " + bukken_no);

                            MarkerOptions client_marker = new MarkerOptions().position(new LatLng(x, y));
                            client_marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon_sell));
                            Marker marker = mMap.addMarker(client_marker);
                            marker.setTag(bukken_no);
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                                @Override
                                public boolean onMarkerClick(Marker clickedMarker) {
                                    System.out.println("MARKER " + clickedMarker.getTag());
                                    Fragment selectedFragment = null;
                                    GlobalVar.setdetailed_bukken_no((String) clickedMarker.getTag());

                                    selectedFragment = new greenLineMapInformation();
                                    ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

                                    return true;
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    int x = 1;
                    // Anything you want
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);


        }
    }

}