package com.cck.jogjog.Fragments.yellow.school;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_LIST;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;


public class yellowSchoolMapDisplayFragment extends Fragment implements
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback {

    private GoogleMap mMap;

    String lat, lon, lat2, lon2;
    D_cDialog CD;
    int selectedtabonstringrequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_map_display, container, false);
        GlobalVar.isFromMap = true;
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        GlobalVar.previousScreen = "";
        //geoCoder();
        //getLatLong();

        //LatLng japan = new LatLng(35,139);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Pass here "name" from school name
        //String hokkaido = "北海道大学（北大）";
        String hokkaido = GlobalVar.getschool_name();
        double latitude = 0;
        double longitude = 0;

        //Set Center point
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(hokkaido, 1);

            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();

                System.out.println("lat lon " + latitude + " " + longitude);
            }

            mMap = googleMap;
            mMap.setOnCameraIdleListener(this);
            mMap.setOnCameraMoveStartedListener(this);
            mMap.setOnCameraMoveListener(this);
            mMap.setOnCameraMoveCanceledListener(this);

            LatLng japan = new LatLng(latitude, longitude); // Japan default
            mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
            MarkerOptions client_marker = new MarkerOptions().position(new LatLng(latitude, longitude));
            client_marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon_rent));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(japan, 17f));
        } catch (IOException e) {
            e.printStackTrace();
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
        final String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_CHINTAI + "&is_map_mode=1" +
                "&latfrom=" + lat +
                "&latto=" + lat2 +
                "&lngfrom=" + lon +
                "&lngto=" + lon2 +
                searchurl;
        GlobalVar.urlFromMap = url;

        System.out.println("URL getLatLong: " + url);

        System.out.println("lat URL " + lat);
        System.out.println("lon URL " + lon);
        System.out.println("lat2 URL " + lat2);
        System.out.println("lon2 URL " + lon2);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");

                    for (int i = 0; i < bukken_list.length(); i++) {
                        JSONObject returnBukken = bukken_list.getJSONObject(i);

                        String tatemono_no = returnBukken.getString("tatemono_no"); //latitude
                        String latitude = returnBukken.getString("ido_hokui"); //latitude
                        String longitude = returnBukken.getString("keido_tokei"); //longitude

                        Double x = Double.parseDouble(latitude);
                        Double y = Double.parseDouble(longitude);

                        MarkerOptions client_marker = new MarkerOptions().position(new LatLng(x, y));
                        client_marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon_rent));
                        Marker marker = mMap.addMarker(client_marker);
                        marker.setTag(tatemono_no);
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            @Override
                            public boolean onMarkerClick(Marker clickedMarker) {
                                System.out.println("MARKER " + clickedMarker.getTag());
                                Fragment selectedFragment = null;

                                GlobalVar.settatemono_no((String) clickedMarker.getTag());
                                GlobalVar.setMap_bukken_url(url);
                                GlobalVar.setparent_fragment("yellowSchoolRentListFragment");
                                selectedFragment = new yellowSchoolMapInformation();
                                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);


                                return true;
                            }
                        });

                    }
                    if (GlobalVar.selectedtab == 1) {
                        if (selectedtabonstringrequest == 1)
                            CD.cDismiss();
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
        mMap.clear();
        selectedtabonstringrequest = GlobalVar.selectedtab;
        if(GlobalVar.selectedtab == 1) {
            CD = new D_cDialog(getContext());
            CD.pbWait.setVisibility(View.VISIBLE);
            CD.cShow(false);
        }
    }

}