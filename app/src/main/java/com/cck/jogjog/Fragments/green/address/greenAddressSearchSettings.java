package com.cck.jogjog.Fragments.green.address;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_SEARCH_CONDITION;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_SELL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_TENANT;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;

public class greenAddressSearchSettings extends Fragment implements View.OnClickListener {
    LinearLayoutCompat linearLayout;
    LinearLayoutCompat spinnerLinearLayout;
    Spinner spinner;
    Spinner spinnerStart;
    Spinner spinnerEnd;
    NestedScrollView scv_content;
    RadioGroup rg;
    TextView title;
    JSONArray formElements = new JSONArray();
    JSONArray searchCriteria = new JSONArray();
    JSONArray savedCriteria = new JSONArray();
    JSONObject criteria;

    //type 3 arraylist
    ArrayList<String> typeThreeSpinnerArray;

    //type 4 arraylist
    ArrayList<String> typeFourStartSpinnerArray;
    ArrayList<String> typeFourEndSpinnerArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_green_search_settings, container, false);

        // Do all the stuff to initialize your custom view
        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_searchsettings = view.findViewById(R.id.btn_apply);
        scv_content = view.findViewById(R.id.scv_content);
        loadSearchCriteria();
        getSearchConditions();

        btn_back.setOnClickListener(this);
        btn_searchsettings.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Fragment selectedFragment = null;
        GlobalVar.previousScreen = "greenAddressSearchSettings";
        switch (v.getId()) {
            case R.id.btn_back:
                GlobalVar.isapplylinesearchsetting = false;
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_apply:
                getFormValues();
                GlobalVar.isapplylinesearchsetting = true;
                selectedFragment = new greenAddressRentListFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
        }

        /*selectedFragment = new greenLocationSearchSettings();
        ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);*/

    }

    public void getSearchConditions() {

        System.out.println("SEARCH");
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_SEARCH_CONDITION + API_PATH_TYPE_SELL;
        System.out.println(url);

        linearLayout = new LinearLayoutCompat(getActivity());
        linearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayoutCompat.VERTICAL);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject returnData, returnSection, returnDataInSection, returnListInData, returnStartInData, returnEndInData;
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONArray sectionInData, dataInSection, listInData, listInStart, listInEnd;
                    String titleInSection, typeInDataSection, nameInList, valueInList, selectedInList, paramInList="";

                    int elementCtr = 0;

                    for (int i = 0; i < data.length(); i++) {
                        returnData = data.getJSONObject(i);
                        sectionInData = returnData.getJSONArray("section");
                        for (int j = 0; j < sectionInData.length(); j++) {
                            returnSection = sectionInData.getJSONObject(j);
                            titleInSection = returnSection.getString("title");

                            LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            tvparams.setMargins(0, 40, 0, 40);

                            title = new TextView(getActivity());
                            title.setTextSize(16);
                            title.setLayoutParams(tvparams);
                            title.setText(titleInSection);
                            linearLayout.addView(title);

                            dataInSection = returnSection.getJSONArray("data");
                            for (int x = 0; x < dataInSection.length(); x++) {
                                returnDataInSection = dataInSection.getJSONObject(x);
                                typeInDataSection = returnDataInSection.getString("type");

                                if (typeInDataSection.equals("1")) {
                                    rg = new RadioGroup(getActivity());
                                    rg.setOrientation(RadioGroup.VERTICAL);

                                    listInData = returnDataInSection.getJSONArray("list");
                                    for (int y = 0; y < listInData.length(); y++) {
                                        returnListInData = listInData.getJSONObject(y);
                                        nameInList = returnListInData.getString("name");
                                        valueInList = returnListInData.getString("value");
                                        elementCtr = elementCtr + 1;

                                        if (y == 0)
                                            addTypeOne(rg, valueInList, nameInList, true, elementCtr, returnDataInSection.getString("pram_name"));
                                        else
                                            addTypeOne(rg, valueInList, nameInList, false, elementCtr, returnDataInSection.getString("pram_name"));
                                    }

                                    linearLayout.addView(rg);

                                } else if (typeInDataSection.equals("2")) {
                                    listInData = returnDataInSection.getJSONArray("list");

                                    for (int y = 0; y < listInData.length(); y++) {
                                        returnListInData = listInData.getJSONObject(y);
                                        nameInList = returnListInData.getString("name");
                                        valueInList = returnListInData.getString("value");
                                        elementCtr = elementCtr + 1;
                                        addTypeTwo(linearLayout, valueInList, nameInList, elementCtr, returnDataInSection.getString("pram_name"));
                                    }

                                } else if (typeInDataSection.equals("3")) {

                                    int isSelected = 0;
                                    elementCtr = elementCtr+1;
                                    spinner = new Spinner(getActivity());
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 130));
                                    typeThreeSpinnerArray = new ArrayList<String>();

                                    listInData = returnDataInSection.getJSONArray("list");
                                    for (int y = 0; y < listInData.length(); y++) {
                                        returnListInData = listInData.getJSONObject(y);
                                        nameInList = returnListInData.getString("name");
                                        valueInList = returnListInData.getString("value");
                                        paramInList = returnDataInSection.getString("pram_name");
                                        if(returnListInData.has("selected")) {
                                            selectedInList = returnListInData.getString("selected");
                                            if(selectedInList.equals("1")) isSelected = y;
                                        }
                                        addTypeThreeOption(typeThreeSpinnerArray, nameInList, valueInList, elementCtr, paramInList);
                                    }
                                    addTypeThree(spinner, typeThreeSpinnerArray, elementCtr, isSelected);
                                    linearLayout.addView(spinner);

                                } else if (typeInDataSection.equals("4")) {
                                    elementCtr = elementCtr + 1;
                                    int width = (linearLayout.getMeasuredWidth() / 2) - 3;
                                    int isSelected = 0;

                                    spinnerLinearLayout = new LinearLayoutCompat(getActivity());
                                    spinnerLinearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    spinnerLinearLayout.setOrientation(LinearLayoutCompat.HORIZONTAL);

                                    spinnerStart = new Spinner(getActivity());
                                    spinnerStart.setLayoutParams(new LinearLayout.LayoutParams(width, 130));

                                    spinnerEnd = new Spinner(getActivity());
                                    spinnerEnd.setLayoutParams(new LinearLayout.LayoutParams(width, 130));

                                    typeFourStartSpinnerArray = new ArrayList<String>();
                                    typeFourEndSpinnerArray = new ArrayList<String>();

                                    returnStartInData = returnDataInSection.getJSONObject("start");
                                    listInStart = returnStartInData.getJSONArray("list");


                                    for (int y = 0; y < listInStart.length(); y++) {
                                        returnListInData = listInStart.getJSONObject(y);
                                        nameInList = returnListInData.getString("name");
                                        valueInList = returnListInData.getString("value");
                                        paramInList = returnStartInData.getString("pram_name");
                                        if(returnListInData.has("selected")) {
                                            selectedInList = returnListInData.getString("selected");
                                            if(selectedInList.equals("1")) isSelected = y;
                                        }
                                        addTypeFourOption(typeFourStartSpinnerArray, nameInList, valueInList, elementCtr, 1, paramInList);
                                    }
                                    addTypeFour(spinnerStart, typeFourStartSpinnerArray, elementCtr, 1, isSelected);
                                    spinnerLinearLayout.addView(spinnerStart);

                                    returnEndInData = returnDataInSection.getJSONObject("end");
                                    listInEnd = returnEndInData.getJSONArray("list");

                                    for (int y = 0; y < listInEnd.length(); y++) {
                                        returnListInData = listInEnd.getJSONObject(y);
                                        nameInList = returnListInData.getString("name");
                                        valueInList = returnListInData.getString("value");
                                        paramInList = returnEndInData.getString("pram_name");
                                        if(returnListInData.has("selected")) {
                                            selectedInList = returnListInData.getString("selected");
                                            if(selectedInList.equals("1")) isSelected = y;
                                        }
                                        addTypeFourOption(typeFourEndSpinnerArray, nameInList, valueInList, elementCtr, 0, paramInList);
                                    }
                                    addTypeFour(spinnerEnd, typeFourEndSpinnerArray, elementCtr, 0, isSelected);
                                    spinnerLinearLayout.addView(spinnerEnd);
                                    linearLayout.addView(spinnerLinearLayout);
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

        scv_content.addView(linearLayout);
    }

    //Radio Button
    public void addTypeOne(RadioGroup rg, String rdVal, String name, boolean isSelected, int id, String param) {

        String strElementId = "10" + id;
        int elementId = Integer.parseInt(strElementId);
        JSONObject element = new JSONObject();

        AppCompatRadioButton rdbtn = new AppCompatRadioButton(getActivity());
        rdbtn.setId(elementId);
        rdbtn.setHighlightColor(Color.parseColor("#ffffff"));
        rdbtn.setText(name);
        rdbtn.setTextColor(Color.parseColor("#000000"));
        rdbtn.setHeight(130);
        JSONObject searchCriteriaObject = new JSONObject();
        try {
            if(savedCriteria.length()>0) {
                for (int i = 0; i < savedCriteria.length(); i++) {
                    searchCriteriaObject = savedCriteria.getJSONObject(i);
                    if (elementId == Integer.parseInt(searchCriteriaObject.getString("id")))
                        rdbtn.setChecked(true);
                }
            }
            else{
                rdbtn.setChecked(isSelected);
            }
            element.put("id", elementId);
            element.put("pram_name", param);
            element.put("name", name);
            element.put("value", rdVal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rg.addView(rdbtn);
        formElements.put(element);
    }

    //Checkboxes
    public void addTypeTwo(LinearLayoutCompat linearLayout, String chkboxVal, String chkboxName, int id, String param) {

        String strElementId = "20" + id;
        int elementId = Integer.parseInt(strElementId);
        final JSONObject element = new JSONObject();
        AppCompatCheckBox chkbox = new AppCompatCheckBox(this.getActivity());
        chkbox.setId(elementId);
        chkbox.setHighlightColor(Color.parseColor("#ffffff"));
        chkbox.setText(chkboxName);
        chkbox.setTextColor(Color.parseColor("#000000"));
        chkbox.setHeight(130);
        JSONObject searchCriteriaObject = new JSONObject();
        try {
            for(int i=0; i<savedCriteria.length();i++){
                searchCriteriaObject = savedCriteria.getJSONObject(i);
                if(elementId==Integer.parseInt(searchCriteriaObject.getString("id")))
                    chkbox.setChecked(true);
            }

            element.put("id", elementId);
            element.put("pram_name", param);
            element.put("name", chkboxName);
            element.put("value", chkboxVal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        linearLayout.addView(chkbox);
        formElements.put(element);
    }

    public void addTypeThreeOption(ArrayList<String> spinnerArray, String name, String val, int id, String param) {

        spinnerArray.add(name);
        String strElementId = "30" + id;
        int elementId = Integer.parseInt(strElementId);
        JSONObject element = new JSONObject();

        try {
            element.put("id", elementId);
            element.put("pram_name", param);
            element.put("name", name);
            element.put("value", val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        formElements.put(element);
    }

    //Single Spinner (Select Option box)
    public void addTypeThree(Spinner spinner, ArrayList<String> spinnerArray, int id, int isSelected) {
        int spinnerIndex=0;
        String spinnerName = "";
        String strElementId = "30" + id;
        int elementId = Integer.parseInt(strElementId);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        spinner.setId(elementId);
        spinner.setAdapter(spinnerArrayAdapter);
        JSONObject searchCriteriaObject = new JSONObject();
        try {
            if(savedCriteria.length()>0) {
                for (int i = 0; i < savedCriteria.length(); i++) {
                    searchCriteriaObject = savedCriteria.getJSONObject(i);
                    if (elementId == Integer.parseInt(searchCriteriaObject.getString("id"))) {
                        spinnerName = searchCriteriaObject.getString("name");
                    }
                }
                for(int j=0;j<spinnerArray.size();j++) {
                    if(spinnerArray.get(j).equals(spinnerName)) {
                        spinnerIndex = j;
                    }
                }
                spinner.setSelection(spinnerIndex);
            }
            else{
                spinner.setSelection(isSelected);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addTypeFourOption(ArrayList<String> spinnerArray, String name, String val, int id, Integer pos, String param) {

        spinnerArray.add(name);
        String strElementId = "4" + pos + id;
        int elementId = Integer.parseInt(strElementId);
        JSONObject element = new JSONObject();

        try {
            element.put("id", elementId);
            element.put("pram_name", param);
            element.put("name", name);
            element.put("value", val);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        formElements.put(element);
    }

    //Range Spinner (2 Select Option box)
    public void addTypeFour(Spinner spinner, ArrayList<String> spinnerArray, int id, int pos, int isSelected) {
        int spinnerIndex=0;
        String spinnerName = "";
        String strElementId = "4" + pos + id;
        int elementId = Integer.parseInt(strElementId);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        spinner.setId(elementId);
        spinner.setAdapter(spinnerArrayAdapter);
        JSONObject searchCriteriaObject = new JSONObject();
        try {
            if(savedCriteria.length()>0) {
                for (int i = 0; i < savedCriteria.length(); i++) {
                    searchCriteriaObject = savedCriteria.getJSONObject(i);
                    if (elementId == Integer.parseInt(searchCriteriaObject.getString("id"))) {
                        spinnerName = searchCriteriaObject.getString("name");
                    }
                }
                for(int j=0;j<spinnerArray.size();j++) {
                    if(spinnerArray.get(j).equals(spinnerName)) {
                        spinnerIndex = j;
                    }
                }
                spinner.setSelection(spinnerIndex);
            }
            else{
                spinner.setSelection(isSelected);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getFormValues() {
        searchCriteria = new JSONArray();

        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = linearLayout.getChildAt(i);

            if(v instanceof LinearLayoutCompat) {
                int llChildCount = ((LinearLayoutCompat) v).getChildCount();
                for (int j = 0; j < llChildCount; j++) {
                    View vl = ((LinearLayoutCompat) v).getChildAt(j);
                    String name = ((Spinner) vl).getSelectedItem().toString();
                    setSearchCriteria(vl, name, null);
                }
            } else if(v instanceof RadioGroup) {
                int rgChildCount = ((RadioGroup) v).getChildCount();
                for (int j = 0; j < rgChildCount; j++) {
                    View vr = ((RadioGroup) v).getChildAt(j);
                    if(((RadioButton) vr).isChecked()) {
                        setSearchCriteria(vr, null, null);
                    } else {
                        removeSearchCriteria(vr);
                    }
                }
            } else if(v instanceof CheckBox) {
                if(((CheckBox) v).isChecked()) {

                    setSearchCriteria(v, null, null);
                } else {
                    removeSearchCriteria(v);
                }
            } else if(v instanceof Spinner) {
                String name = ((Spinner) v).getSelectedItem().toString();
                setSearchCriteria(v, name, null);
            }
        }
        //System.out.println("search criteria: " + searchCriteria);
        addToSearchCriteria();
        generateURL();
    }

    public void setSearchCriteria(View v, String name, String val) {
        JSONObject search = new JSONObject();

        for (int a = 0; a < formElements.length(); a++) {

            try {
                search = formElements.getJSONObject(a);
                if(name != null) {
                    if((search.optInt("id") == v.getId())
                            &&(search.optString("name") == name))
                    {
                        if(val != null)
                            search.put("value", val);
                        searchCriteria.put(search);
                    }
                } else {
                    if(search.optInt("id") == v.getId())
                    {
                        if(val != null)
                            search.put("value", val);

                        searchCriteria.put(search);
                    }
                }

            } catch (JSONException e) {

            }
        }
    }

    public void removeSearchCriteria(View v) {
        JSONObject search = new JSONObject();

        for (int a = 0; a < searchCriteria.length(); a++) {

            try {
                search = searchCriteria.getJSONObject(a);
                if(search.optInt("id") == v.getId())
                {
                    searchCriteria.remove(a);
                }

            } catch (JSONException e) {

            }
        }
    }

    public void generateURL() {
        String searchSettingValues = "";
        String searchSettingURL = "";

        ArrayList<String> paramArray = new ArrayList<String>();

        for(int i=0; i<searchCriteria.length(); i++) {

            try {
                criteria = searchCriteria.getJSONObject(i);

                if (!paramArray.contains(criteria.getString("pram_name")))
                    paramArray.add(criteria.getString("pram_name"));
            } catch (JSONException e) {

            }
        }

        for(int j=0; j<paramArray.size(); j++) {

            searchSettingValues = "";

            for(int i=0; i<searchCriteria.length(); i++) {

                String criteriaValue;
                try {
                    criteria = searchCriteria.getJSONObject(i);

                    if (paramArray.get(j).toString().equals(criteria.getString("pram_name"))){
                        criteriaValue = criteria.getString("value");
                        if(criteriaValue.length() == 0)
                            continue;
                        else {
                            searchSettingValues = searchSettingValues + criteriaValue + ",";
                        }
                    }

                } catch (JSONException e) {

                }
            }

            if(searchSettingValues.length() != 0) {
                searchSettingValues = searchSettingValues.substring(0, searchSettingValues.length() - 1);
                if(searchSettingValues.length() != 0)
                    searchSettingURL = searchSettingURL + paramArray.get(j).toString() + "=" + searchSettingValues + "&";
            }
        }

        searchSettingURL = searchSettingURL.substring(0, searchSettingURL.length() - 1);
        GlobalVar.setsearch_url(searchSettingURL);
        System.out.println("URL SEARCH SETTING GREEN: " + searchSettingURL);
    }

    public void addToSearchCriteria() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("searchpref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("searchCriteria", searchCriteria.toString());
        editor.apply();
        System.out.println("TAMA TO: " + sharedPreferences.getString("searchCriteria", ""));
    }

    public void loadSearchCriteria() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("searchpref", MODE_PRIVATE);
        String searchJSON = sharedPreferences.getString("searchCriteria", null);
        if(searchJSON != null) {

            try {
                JSONArray searchCriteriaArray = new JSONArray(searchJSON);
                JSONObject searchCriteriaObject = new JSONObject();

                for(int i=0; i<searchCriteriaArray.length();i++){
                    savedCriteria.put(searchCriteriaArray.get(i));
                }
                System.out.println("RESULT: "+ savedCriteria.toString());

            } catch (JSONException e) {

            }
            System.out.println("Loaded From shared preference property list: ");

        }
    }

    public void clearSearchPref(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("searchpref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
        editor.commit();
    }

}
