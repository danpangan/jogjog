package com.cck.jogjog.Fragments.blue.line;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Fragments.yellow.line.yellowLinePrefectureSelectionFragment;
import com.cck.jogjog.Fragments.yellow.line.yellowLineStationSelectionFragment;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_ENSEN;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;

public class blueLineLineSelectionFragment extends Fragment implements View.OnClickListener {

    LinearLayout llcont;

    Boolean isreloaded = false;
    private D_cDialog CD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private String previoustraffic = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_line_line_selection, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_search = view.findViewById(R.id.btn_search);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        llcont = view.findViewById(R.id.ll_cont);

        if (BukkenList.lineline.size() > 0) {
            reloadLineLine();
        } else {
            getLineLine();
        }

        return view;
    }

    public void getLineLine() {

        System.out.println("LINE LINE");
        String prefecture = GlobalVar.getpref_code();

        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_ENSEN + "pref_code=" + prefecture;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    CD.cDismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject returnData = data.getJSONObject(i);


                        String datatraffickanji = returnData.getString("traffic_kanji");
                        String dataroutekanji = returnData.getString("route_kanji");
                        String datacoderoute = returnData.getString("code_route");
                        String datacodetraffic = returnData.getString("code_traffic");
                        String datacodetraffic_company = returnData.getString("code_traffic_company");

                        BukkenList.lineline.add(new ArrayList());
                        BukkenList.lineline.get(i).add(returnData.getString("traffic_kanji")); // index 0
                        BukkenList.lineline.get(i).add(returnData.getString("route_kanji")); // index 1
                        BukkenList.lineline.get(i).add(returnData.getString("code_route")); // index 2
                        BukkenList.lineline.get(i).add(returnData.getString("code_traffic")); // index 3
                        BukkenList.lineline.get(i).add(returnData.getString("code_traffic_company")); // index 4

                        addLines(i, datatraffickanji, dataroutekanji, datacoderoute);

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
        CD = new D_cDialog(getContext());
        CD.pbWait.setVisibility(View.VISIBLE);
        CD.cShow(true);
    }

    public void reloadLineLine() {
        isreloaded = true;
        for (int i = 0; i < BukkenList.lineline.size(); i++) {
            addLines(i, BukkenList.lineline.get(i).get(0), BukkenList.lineline.get(i).get(1), BukkenList.lineline.get(i).get(2));
        }
    }

    private void addLines(int index, String traffic, String route, String code) {
        if (traffic.equals(previoustraffic) == false) {
            previoustraffic = traffic;
            LinearLayoutCompat.LayoutParams tvparams = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvparams.setMargins(getDPscale(10), getDPscale(10), getDPscale(10), getDPscale(10));
            TextView tv_traffic = new TextView(getActivity());
            tv_traffic.setText(traffic);
            tv_traffic.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv_traffic.setLayoutParams(tvparams);
            llcont.addView(tv_traffic);
        }

        LinearLayoutCompat.LayoutParams radparams = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        radparams.setMargins(getDPscale(30), getDPscale(0), getDPscale(30), getDPscale(0));

        AppCompatRadioButton rdbtn = new AppCompatRadioButton(getActivity());
        rdbtn.setId(index);
        rdbtn.setText(route);
        rdbtn.setTextSize(20);
        rdbtn.setTag(code);
        rdbtn.setLayoutParams(radparams);
        rdbtn.setTextColor(Color.parseColor("#000000"));
        rdbtn.setHeight(getDPscale(40));
        rdbtn.setOnClickListener(radiobuttonOnclickListener);
        llcont.addView(rdbtn);

        if (isreloaded) {
            // check if selected
            if (BukkenList.lineline.get(index).get(2).equals(GlobalVar.getcode_route()) &&
                    BukkenList.lineline.get(index).get(3).equals(GlobalVar.getcode_traffic()) &&
                    BukkenList.lineline.get(index).get(4).equals(GlobalVar.getcode_traffic_company())) {
                rdbtn.setChecked(true);
            } else {
                rdbtn.setChecked(false);
            }
        }
    }

    private int getDPscale(int size) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) ((double) size * scale + 0.5f));
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;

        switch (v.getId()) {
            case R.id.btn_back:
                GlobalVar.setcode_route("");
                GlobalVar.setcode_traffic("");
                GlobalVar.setcode_traffic_company("");
                BukkenList.lineline.clear();
                selectedFragment = new blueLinePrefectureSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            default:

                break;
        }
    }

    View.OnClickListener radiobuttonOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {

            Fragment selectedFragment = null;

            int radioID = v.getId();
            System.out.println("RADIO ID: " + radioID);
            GlobalVar.setcode_route(BukkenList.lineline.get(radioID).get(2)); //code_route
            GlobalVar.setcode_traffic(BukkenList.lineline.get(radioID).get(3)); //code_traffic
            GlobalVar.setcode_traffic_company(BukkenList.lineline.get(radioID).get(4)); //code_traffic_company

            System.out.println("SELECTED CODE ROUTE: " + GlobalVar.getcode_route());
            System.out.println("SELECTED CODE TRAFFIC: " + GlobalVar.getcode_traffic());
            System.out.println("SELECTED PREF CODE: " + GlobalVar.getpref_code());
            System.out.println("SELECTED TRAFFIC COMPANY: " + GlobalVar.getcode_traffic_company());

            selectedFragment = new blueLineStationSelectionFragment();
            ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
        }
    };
}