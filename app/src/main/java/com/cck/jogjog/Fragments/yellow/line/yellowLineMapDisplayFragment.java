package com.cck.jogjog.Fragments.yellow.line;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_LIST;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;


public class yellowLineMapDisplayFragment extends Fragment implements
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
        //getLatLong();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            GlobalVar.selectedtab = 1;
            GlobalVar.isFromMap = true;
            if(GlobalVar.previousScreen.equals("")) {
                if (BukkenList.ido_hokui.size() > 0) {
                    maplat = 0.0;
                    maplong = 0.0;

                    maplat = Double.parseDouble(BukkenList.ido_hokui.get(0));
                    maplong = Double.parseDouble(BukkenList.keido_tokei.get(0));

                    LatLng japan = new LatLng(maplat, maplong);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(japan, 17f));
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

        // plots markers in map
        String searchurl = "&" + GlobalVar.getsearch_url();

        final String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_CHINTAI + "&is_map_mode=1" +
                "&latfrom=" + lat +
                "&latto=" + lat2 +
                "&lngfrom=" + lon +
                "&lngto=" + lon2 +
                searchurl;
        GlobalVar.urlFromMap = url;
        System.out.println("URL getLatLong: " + url);
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
                                selectedFragment = new yellowLineMapInformation();
                                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

                                return true;
                            }
                        });

                    }
                    if(GlobalVar.selectedtab == 1) {
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
        if(GlobalVar.selectedtab == 1) {
            CD = new D_cDialog(getContext());
            CD.pbWait.setVisibility(View.VISIBLE);
            CD.cShow(false);
        }
    }

    public void getBukkenDetail(String getLatLongURL) {

        System.out.println("URL getBukkenDetail: " + getLatLongURL);

        StringRequest stringRequest = new StringRequest(getLatLongURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String tatemono_noMap = GlobalVar.gettatemono_no();

                    String bukken_no = null;

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");
                    for (int i = 0; i < bukken_list.length(); i++) {
                        JSONObject returnBukken = bukken_list.getJSONObject(i);
                        //Loop for Heya Array
                        JSONArray heyaList = returnBukken.getJSONArray("heya");
                        for (int j = 0; j < heyaList.length(); j++) {
                            JSONObject returnHeya = heyaList.getJSONObject(j);
                            bukken_no = returnHeya.getString("bukken_no");
                        }

                        if (bukken_no.equals(tatemono_noMap)) {
                            System.out.println("TRUTH");
                            GlobalVar.setdetailed_bukken_no(tatemono_noMap);

                            JSONObject heyaandfriends = bukken_list.getJSONObject(i);
                            System.out.println("FREINDS " + returnBukken);

                            String friend_tatemono_no = heyaandfriends.getString("tatemono_no");
                            String friend_bukken_name = heyaandfriends.getString("bukken_name");
                            String friend_shozaichi = heyaandfriends.getString("shozaichi");
                            String friend_eki1_kanji = heyaandfriends.getString("eki1_kanji");
                            String friend_bukken_type_name = heyaandfriends.getString("bukken_type_name");
                            String friend_building_info = heyaandfriends.getString("building_info");
                            String friend_kanseibi = heyaandfriends.getString("kanseibi");
                            String friend_chikunen = heyaandfriends.getString("chikunen");
                            String friend_gaikan_url = heyaandfriends.getString("gaikan_url");

                            System.out.println("FRIENDS BUKKEN LIST");
                            System.out.println("tatemono_no: " + friend_tatemono_no);
                            System.out.println("bukken_name: " + friend_bukken_name);
                            System.out.println("shozaichi: " + friend_shozaichi);
                            System.out.println("eki1_kanji: " + friend_eki1_kanji);
                            System.out.println("bukken_type_name: " + friend_bukken_type_name);
                            System.out.println("building_info: " + friend_building_info);
                            System.out.println("kanseibi: " + friend_kanseibi);
                            System.out.println("chikunen: " + friend_chikunen);
                            System.out.println("gaikan_url: " + friend_gaikan_url);

                            JSONArray heyafriend = heyaandfriends.getJSONArray("heya");

                            //Counts how many rooms are there
                            for (int j = 0; j < heyafriend.length(); j++) {
                                JSONObject returnFriends = heyafriend.getJSONObject(j);
                                String chinryo = returnFriends.getString("chinryo");
                                String madori = returnFriends.getString("madori");
                                String heya_kaisu = returnFriends.getString("heya_kaisu");
                                bukken_no = returnFriends.getString("bukken_no");
                                String friend_heya = returnFriends.getString("heya_no");

                                System.out.println("FRIENDS RETURN HEYA " + j);
                                System.out.println("how many heya_no " + j + " " + friend_heya);

                                System.out.println("chinryo: " + j + " " + chinryo);
                                System.out.println("madori: " + j + " " + madori);
                                System.out.println("heya_kaisu: " + j + " " + heya_kaisu);
                                System.out.println("bukken_no: " + j + " " + bukken_no);

                            }
                        } else {
                            System.out.println("NANI");
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
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }
}