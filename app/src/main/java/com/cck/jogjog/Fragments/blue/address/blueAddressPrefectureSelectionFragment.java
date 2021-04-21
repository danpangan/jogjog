package com.cck.jogjog.Fragments.blue.address;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Fragments.home.HomeFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_TODOUFUKEN;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_TENANT;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;

public class blueAddressPrefectureSelectionFragment extends Fragment implements View.OnClickListener {

    RadioGroup rdg_prefecture;
    private D_cDialog CD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_address_prefecture_selection, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_search = view.findViewById(R.id.btn_search);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        rdg_prefecture = view.findViewById(R.id.rdg_prefectures);
        getPrefectures();

        return view;
    }

    private void getPrefectures() {
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_TODOUFUKEN + API_PATH_TYPE_TENANT;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    CD.cDismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject returnData =  data.getJSONObject(i);

                        String dataName = returnData.getString("name");
                        String dataCode = returnData.getString("pref_code");

                        System.out.println(dataName);
                        System.out.println(dataCode);

                        addPrefectures(dataCode,dataName,i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

        CD = new D_cDialog(getContext());
        CD.pbWait.setVisibility(View.VISIBLE);
        CD.cShow(false);
    }

    public void addPrefectures(String prefcode, String name, int index) {
        String saved_prefcode = GlobalVar.getpref_code();

        AppCompatRadioButton rdbtn = new AppCompatRadioButton(getActivity());
        rdbtn.setId(View.generateViewId());
        rdbtn.setTag(prefcode);
        rdbtn.setHighlightColor(Color.parseColor("#ffffff"));
        rdbtn.setText(name);
        rdbtn.setTextSize(20);
        rdbtn.setTextColor(Color.parseColor("#000000"));
        rdbtn.setHeight(130);
        rdbtn.setOnClickListener(radiobuttonOnclickListener);
        rdg_prefecture.addView(rdbtn);

        if ((saved_prefcode == null) || (saved_prefcode.equals(""))) {
            if(index == 0){
                rdbtn.setChecked(true);
            }
        }else if(saved_prefcode.equals(prefcode)){
            rdbtn.setChecked(true);
        }
        else{
            rdbtn.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment=null;

        switch (v.getId()){
            case R.id.btn_back:
                GlobalVar.setpref_code("");
                selectedFragment = new HomeFragment();
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

            if (((RadioButton) v).isChecked() == true) {
                GlobalVar.setpref_code(v.getTag().toString());
                selectedFragment = new blueAddressMunicipalitySelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
            }
        }
    };
}
