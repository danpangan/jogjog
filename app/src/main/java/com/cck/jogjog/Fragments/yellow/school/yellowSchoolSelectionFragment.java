package com.cck.jogjog.Fragments.yellow.school;

import android.view.View;

import androidx.fragment.app.Fragment;


public class yellowSchoolSelectionFragment extends Fragment implements View.OnClickListener {


    /*TextView txv_prefectures;
    String code_webtown;
    String code_webarea;
    String school_type;
    int RadioButtonID = 7360000;
    int firstID=0;
    RadioGroup rdg_prefecture;
    private D_cDialog CD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_school_selection, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_search = view.findViewById(R.id.btn_search);
        txv_prefectures = view.findViewById(R.id.txv_prefecture);
        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        rdg_prefecture = view.findViewById(R.id.rdg_prefectures);



        getSchoolTown();

        return view;
    }

    public void addPrefectures(int schoolcode, String name) {
        AppCompatRadioButton rdbtn = new AppCompatRadioButton(getActivity());
        rdbtn.setId(RadioButtonID + schoolcode);
        rdbtn.setHighlightColor(Color.parseColor("#ffffff"));
        rdbtn.setText(name);
        rdbtn.setTextColor(Color.parseColor("#000000"));
        rdbtn.setHeight(130);
        rdbtn.setOnClickListener(this);
        rdg_prefecture.addView(rdbtn);


    }

    }

    public void getSchoolTown() {
        String code_webtown = GlobalVar.getweb_town();
        String code_webarea = GlobalVar.getweb_area();
        System.out.println("SCHOOL SELECTION");
        System.out.println("CODE WEBTOWN: "+code_webtown);
        System.out.println("CODE WEBAREA: "+code_webarea);
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_SCHOOL + "&code_webtown="+code_webtown +"&code_webarea=" +code_webarea;

        System.out.println(url);
//0001 0001
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //CD.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");


                    for (int i = 0; i < data.length(); i++) {
                        JSONObject returnData = data.getJSONObject(i);
                        String dataName = returnData.getString("name");
                        String dataCode = returnData.getString("code");
                        System.out.println("PREFECTURE: " + dataName);
                        System.out.println("School_type: " + dataCode);

                        addPrefectures(Integer.parseInt(dataCode), dataName);

                    }

                    //System.out.println("ARRAY PREFECTURE: " + arrayPrefecture);


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
//        CD = new D_cDialog(getContext());
//        CD.pbWait.setVisibility(View.VISIBLE);
//        CD.cShow(true);
    }
    public void addSchoolList(int schoolcode, String name) {
        AppCompatRadioButton rdbtn = new AppCompatRadioButton(getActivity());
        rdbtn.setId(RadioButtonID + schoolcode);
        rdbtn.setHighlightColor(Color.parseColor("#ffffff"));
        rdbtn.setText(name);
        rdbtn.setTextColor(Color.parseColor("#000000"));
        rdbtn.setHeight(130);
        rdbtn.setOnClickListener(this);
        rdg_prefecture.addView(rdbtn);


    }*/
    @Override
    public void onClick(View v) {
        /*Fragment selectedFragment = null;

        switch (v.getId()) {
            case R.id.btn_back:
                selectedFragment = new blueSchoolTypeSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            default:
                if (((RadioButton) v).isChecked() == true) {
                    int school_type = ((RadioButton) v).getId() - RadioButtonID;
                    if ((school_type < firstID )||(firstID == 0) ){
                        firstID = school_type;
                        System.out.println("BUTTON: "+((RadioButton) v).getText());
                        GlobalVar.setschool_name(((RadioButton) v).getText().toString());
                        System.out.println(GlobalVar.getschool_name().toString());
                    }
                        *//*GlobalVar.addschool_type();*//*
                    //GlobalVar.setschool_type(Integer.toString(school_type));

                    selectedFragment = new blueSchoolRentListFragment();
                    ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                }
                else{

                }
                break;
        */
    }
}
