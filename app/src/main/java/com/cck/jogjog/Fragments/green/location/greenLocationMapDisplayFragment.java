package com.cck.jogjog.Fragments.green.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Fragments.blue.location.blueLocationMapInformation;
import com.cck.jogjog.Fragments.green.address.greenAddressMapInformation;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_DETAIL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_LIST;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_SELL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_TENANT;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;


public class greenLocationMapDisplayFragment extends Fragment implements
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    String lat, lon, lat2, lon2;
    Double maplat, maplong;
    D_cDialog CD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_map_display, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        GlobalVar.previousScreen = "";

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            GlobalVar.selectedtab = 1;
            GlobalVar.isFromMap = true;
            GlobalVar.previousScreen = "";
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            fetchLocation();
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    maplat = currentLocation.getLatitude();
                    maplong = currentLocation.getLongitude();

//                    maplat = 43.0779575;
//                    maplong = 141.3378243;
                    LatLng japan = new LatLng(maplat, maplong);
                    MarkerOptions client_marker = new MarkerOptions().position(new LatLng(maplat, maplong));
                    Marker marker = mMap.addMarker(client_marker);
                    //mMap.addMarker(new MarkerOptions().position(japan).title("Marker in Japan"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(japan, 18f));

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //initial zoom
        mMap = googleMap;

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
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

        System.out.println("upperLeft " + upperLeft);
        System.out.println("lowerRight " + lowerRight);

        System.out.println("lat " + lat);
        System.out.println("lon " + lon);

        System.out.println("lat2 " + lat2);
        System.out.println("lon2 " + lon2);

        getLatLong();
    }

    public void getLatLong() {

        String searchurl = "&" + GlobalVar.getsearch_url();

        final String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_SELL + "&is_map_mode=1" +
                "&latfrom=" + lat +
                "&latto=" + lat2 +
                "&lngfrom=" + lon +
                "&lngto=" + lon2 +
                searchurl;

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

                                    selectedFragment = new greenLocationMapInformation();
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