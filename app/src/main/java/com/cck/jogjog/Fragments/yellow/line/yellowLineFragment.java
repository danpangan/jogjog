package com.cck.jogjog.Fragments.yellow.line;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.yellow.location.yellowLocationRentListFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_SEARCH_CONDITION;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;

public class yellowLineFragment extends Fragment implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_line, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_search = view.findViewById(R.id.btn_search);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_yellow_line_container,
                    new yellowLocationRentListFragment()).commit();
        }

        String code_station = GlobalVar.getcode_station();
        System.out.println("CODE STATION FOR LINEFRAGMENT: " +code_station);
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {
            case R.id.btn_back:
                selectedFragment = new yellowLineStationSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_search:
                getSearchConditions();
                break;
        }
    }

    public void getSearchConditions() {
        System.out.println("CODE STATION: " +GlobalVar.getcode_station());
        System.out.println("SEARCH");

        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_SEARCH_CONDITION + "type=chintai";
        //

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject returnData, returnSection,returnDataInSection,returnListInData;
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONArray sectionInData, dataInSection,listInData;
                    String titleInData, titleInSection, typeInDataSection, nameInList,selectedInList,valueInList;
                    for (int i = 0; i < data.length(); i++) {
                        returnData = data.getJSONObject(i);
                        titleInData = returnData.getString("title");
                        System.out.println("TITLE IN DATA: " + i + " " + titleInData);

                        sectionInData = returnData.getJSONArray("section");
                        for (int j = 0; j < sectionInData.length(); j++) {
                            returnSection = sectionInData.getJSONObject(j);
                            titleInSection = returnSection.getString("title");
                            System.out.println("TITLE IN SECTION: " + i + " " + j + " " + titleInSection);

                            dataInSection = returnSection.getJSONArray("data");
                            for (int x = 0; x < dataInSection.length(); x++) {
                                returnDataInSection = dataInSection.getJSONObject(x);
                                typeInDataSection = returnDataInSection.getString("type");
                                System.out.println("Type in Data Section " + titleInData + " " +
                                        titleInSection + " " + x + "  TYPE" + typeInDataSection);

                                listInData = returnDataInSection.getJSONArray("list");
                                for (int y = 0; y < listInData.length(); y++) {
                                    returnListInData = listInData.getJSONObject(y);
                                    nameInList = returnListInData.getString("name");
                                    selectedInList = returnListInData.getString("selected");
                                    valueInList = returnListInData.getString("value");
                                    System.out.println("LIST IN DATA SECTION " + x +  " " + titleInData + " " +
                                            titleInSection + "TYPE " + typeInDataSection + " " + nameInList +
                                            " " + selectedInList + " VALUE  " + valueInList);

                                }


                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);


   }
}