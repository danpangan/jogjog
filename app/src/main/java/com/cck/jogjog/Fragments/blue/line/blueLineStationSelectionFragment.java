package com.cck.jogjog.Fragments.blue.line;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
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
import com.cck.jogjog.Fragments.yellow.line.yellowLineLineSelectionFragment;
import com.cck.jogjog.Fragments.yellow.line.yellowLineRentListFragment;
import com.cck.jogjog.Fragments.yellow.line.yellowLineSearchSettings;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_EKI;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;


public class blueLineStationSelectionFragment extends Fragment implements View.OnClickListener {

    private LinearLayoutCompat cont_municipalities;

    private int selectedcount = 0;
    private int CheckboxID = 77770000;
    private JSONObject returnData;
    private Boolean isreloaded = false;
    private D_cDialog CD;
    private List<String> selectedcodestation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_line_station_selection, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_next = view.findViewById(R.id.btn_next);
        Button btn_searchsettings = view.findViewById(R.id.btn_searchsettings);

        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_searchsettings.setOnClickListener(this);

        selectedcodestation = GlobalVar.getselectedcode_station();
        cont_municipalities = view.findViewById(R.id.cont_municipalities);

        GlobalVar.isFromMap = false;
        GlobalVar.previousScreen = "blueLineStationSelectionFragment";

        if (GlobalVar.data_codestation.size() > 0) {
            reloadMunicipalities();
        } else {
            getLineStation();
        }
        return view;
    }

    public void getLineStation() {

        System.out.println("LINE STATION");
        String prefecture = GlobalVar.getpref_code();
        String code_traffic = GlobalVar.getcode_traffic();
        String code_traffic_company = GlobalVar.getcode_traffic_company();
        String code_route = GlobalVar.getcode_route();

        System.out.println("CODE_TRAFFIC: "+code_traffic);

        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_EKI + "pref_code=" + prefecture + "&code_traffic=" + code_traffic + "&code_traffic_company=" + code_traffic_company
                + "&code_route=" + code_route;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        returnData = data.getJSONObject(i);

                        String name = returnData.getString("name");
                        String bukkencnt = returnData.getString("bukken_count");
                        String dataCode = returnData.getString("code_station");

                        GlobalVar.data_codestation.add(new ArrayList());
                        GlobalVar.data_codestation.get(i).add(returnData.getString("name"));
                        GlobalVar.data_codestation.get(i).add(returnData.getString("code_station"));
                        GlobalVar.data_codestation.get(i).add(returnData.getString("bukken_count"));

                        GlobalVar.addallcode_station(dataCode);
                        addLineStation(i, name, dataCode, bukkencnt);
                        System.out.println("BUKKEN COUNT:" + bukkencnt);
                    }
                    CD.cDismiss();
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

    public void reloadMunicipalities() {
        isreloaded = true;
        for (int i = 0; i < GlobalVar.data_codestation.size(); i++) {
            addLineStation(i, GlobalVar.data_codestation.get(i).get(0), GlobalVar.data_codestation.get(i).get(1), GlobalVar.data_codestation.get(i).get(2));
        }
    }

    public void addLineStation(int index, String name, String codestation, String cnt) {

        AppCompatCheckBox chkbx = new AppCompatCheckBox(getActivity());
        chkbx.setId(CheckboxID + index);
        chkbx.setHighlightColor(Color.parseColor("#ffffff"));
        chkbx.setText(name + "(" + cnt + ")");
        chkbx.setTextSize(20);
        chkbx.setTextColor(Color.parseColor("#000000"));
        chkbx.setHeight(130);
        chkbx.setOnClickListener(checkboxnOnclickListener);
        cont_municipalities.addView(chkbx);
        if(Integer.parseInt(cnt) == 0) {
            chkbx.setEnabled(false);
        }

        if (isreloaded) {
            // check if selected
            if (checkboxselected(codestation)) {
                chkbx.setChecked(true);
                selectedcount++;
            } else {
                chkbx.setChecked(false);
            }
        }
    }

    public boolean checkboxselected(String codestation) {
        boolean result = false;

        Iterator<String> iterator = selectedcodestation.iterator();
        while (iterator.hasNext()) {
            if (codestation.equals(iterator.next())) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;

        switch (v.getId()) {
            case R.id.btn_back:
                GlobalVar.clearcodestation();
                GlobalVar.data_codestation.clear();
                clearSearchPref();
                selectedFragment = new blueLineLineSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_next:
                if (selectedcount == 0) {
                    alertDialogSelectStation();
                } else {
                    GlobalVar.isFromMap = false;
                    selectedFragment = new blueLineRentListFragment();
                    ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                }
                GlobalVar.clearpreviousscreen();
                break;
            case R.id.btn_searchsettings:
                GlobalVar.setparent_fragment("blueLineStationSelectionFragment");
                selectedFragment = new blueLineSearchSettings();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

                break;
            default:
                break;
        }

    }

    View.OnClickListener checkboxnOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {

            if (((CheckBox) v).isChecked() == true) {
                selectedcount++;
                int index = v.getId() - CheckboxID;
                GlobalVar.addcode_station(GlobalVar.data_codestation.get(index).get(1));
                    /*GlobalVar.setcode_route(GlobalVar.data_codestation.get(index).get(3));
                    GlobalVar.setcode_traffic(GlobalVar.data_codestation.get(index).get(4));
                    GlobalVar.setcode_traffic_company(GlobalVar.data_codestation.get(index).get(5));*/

            } else if (((CheckBox) v).isChecked() == false) {
                selectedcount--;
                int index = v.getId() - CheckboxID;
                GlobalVar.removecode_station(GlobalVar.data_codestation.get(index).get(1));
                if (selectedcount <= 0) {
                    selectedcount = 0;
                }
            } else {
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if(GlobalVar.isapplylinesearchsetting){
            alertDialogSelectStation();
            GlobalVar.isapplylinesearchsetting = false;
        }
    }

    private void alertDialogSelectStation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("駅を選択してください。");
        builder.setCancelable(true);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getActivity(), "confirmed", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public void clearSearchPref(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("searchpref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
        editor.commit();
    }
}

