package com.cck.jogjog.Fragments.green.school;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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
import com.cck.jogjog.Fragments.blue.school.blueSchoolListDisplay;
import com.cck.jogjog.Fragments.blue.school.blueSchoolTownSelectionFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_SCHOOL_TYPE;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_WEBAREA;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;


public class greenSchoolTypeSelectionFragment extends Fragment implements View.OnClickListener{

        int selectedcount = 0 ;
        // int CheckboxID = 0000000;
        final int SubeteID = 0;
        int firstID=0;
        int px;
        JSONObject returnData;
        JSONArray data;
        LinearLayoutCompat cont_municipalities, ll_cont;
        LinearLayoutCompat.LayoutParams params;
        TextView txv_municipality;
        String savedWebArea = "";
        String savedSchoolType = "";

        private D_cDialog CD;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_yellow_school_type_selection, container, false);

            Button btn_back = view.findViewById(R.id.btn_back);
            Button btn_next = view.findViewById(R.id.btn_next);

            btn_back.setOnClickListener(this);
            btn_next.setOnClickListener(this);

            cont_municipalities = view.findViewById(R.id.cont_municipalities);
            ll_cont = view.findViewById(R.id.ll_cont);
            txv_municipality = view.findViewById(R.id.txv_municipality);

            txv_municipality.setText(GlobalVar.getweb_town_name());
            loadSchoolTypeSelectionData();

            GlobalVar.clearschooltype();
            getSchoolType();

            GlobalVar.clearwebarea();
            return view;
        }

        public void getSchoolType() {
            String code_webtown = GlobalVar.getweb_town();
            System.out.println("SCHOOL TYPE");
            System.out.println("code_webtown " + code_webtown);

            String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_WEBAREA + "code_webtown="+code_webtown;
            System.out.println(url);

            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //CD.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length() > 0) {
                            addAreas_Subete();
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject returnData = data.getJSONObject(i);
                            //JSONArray data = jsonObject.getJSONArray("name");

                            String dataName = returnData.getString("name");
                            String dataCode = returnData.getString("code_webarea");

                            GlobalVar.data_webarea.add(new ArrayList());
                            GlobalVar.data_webarea.get(i).add(returnData.getString("name"));
                            GlobalVar.data_webarea.get(i).add(returnData.getString("code_webarea"));
                            GlobalVar.data_webarea.get(i).add(returnData.getString("code_webtown"));

                            System.out.println("NAME: "+dataName);
                            System.out.println("WEB AREA: "+dataCode);

                            GlobalVar.addallweb_area(dataCode);
                            addAreaList(i,dataName);

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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);

            String url2 = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_SCHOOL_TYPE;
            System.out.println("School type list: " + url2);

            StringRequest stringRequest2 = new StringRequest(url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray data = jsonObject.getJSONArray("data");
                        String msg = jsonObject.getString("msg");
                        addTextView(msg);
                        System.out.println("MSG: " + msg);
                        for (int j = 0; j < data.length(); j++) {
                            JSONObject returnData = data.getJSONObject(j);
                            String dataName = returnData.getString("name");
                            String dataCode = returnData.getString("code");

                            GlobalVar.data_schooltype.add(new ArrayList());
                            GlobalVar.data_schooltype.get(j).add(returnData.getString("code"));
                            System.out.println("NAME: " + dataName);
                            System.out.println("SCHOOL TYPE: " + dataCode);

                            addSchoolTypeList(j, dataName);

                        }
                        String msg2 = "タウン内の小・中学校・高等学校・高等専門学校、大学・短大、専門学校を検索できます。";
                        addTextView2(msg2);
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue2.add(stringRequest2);
    //        CD = new D_cDialog(getContext());
    //        CD.pbWait.setVisibility(View.VISIBLE);
    //        CD.cShow(true);
        }
        private void addTextView(String header) {
            params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            px = getDPmeasurement(10);

            TextView tv_school_type = new TextView(getActivity());
            params.setMargins(px, px, px, px);
            tv_school_type.setLayoutParams(params);
            tv_school_type.setText(header);
            tv_school_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            cont_municipalities.addView(tv_school_type);
        }

        private void addTextView2(String text) {
            TextView tv_school_type = new TextView(getActivity());
            tv_school_type.setText(text);
            tv_school_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_school_type.setBackgroundColor(Color.parseColor("#ffffff"));
            cont_municipalities.addView(tv_school_type);
        }
        private void addAreas_Subete() {
            params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            px = getDPmeasurement(30);

            AppCompatCheckBox chkbox = new AppCompatCheckBox(getActivity());
            params.setMargins(px,0,0,0);
            chkbox.setLayoutParams(params);
            chkbox.setId(SubeteID);
            chkbox.setHighlightColor(Color.parseColor("#ffffff"));
            chkbox.setText("全て");
            chkbox.setTextSize(20);
            chkbox.setTextColor(Color.parseColor("#000000"));
            chkbox.setHeight(130);
            chkbox.setOnClickListener(this);
            ll_cont.addView(chkbox);
        }

        private void CheckAllAreas() {
            int childCount = ll_cont.getChildCount();
            int checkboxID = 0;
            for (int i = 1; i < childCount; i++) {
                View vl = ll_cont.getChildAt(i);
                if(vl instanceof CheckBox) {
                    checkboxID = vl.getId();
                    ((CheckBox) vl).setChecked(true);
                    selectedcount++;
                    GlobalVar.addweb_area(GlobalVar.data_webarea.get(checkboxID).get(1));
                    //GlobalVar.addschool_type(GlobalVar.data_schooltype.get(checkboxID).get(0));
                }
            }
        }
        private void UncheckAllAreas() {
            int childCount = ll_cont.getChildCount();
            int checkboxID = 0;
            for (int i = 1; i < childCount; i++) {
                View vl = ll_cont.getChildAt(i);

                if(vl instanceof CheckBox) {
                    checkboxID = vl.getId();
                    ((CheckBox) vl).setChecked(false);
                    selectedcount++;
                    GlobalVar.removeweb_area(GlobalVar.data_webarea.get(checkboxID).get(1));
                    System.out.println("DATA WEBAREA: " + GlobalVar.getweb_area());
                }
            }
        }

        public void checkForm() { //for the checkbox
            int childCount = ll_cont.getChildCount();
            int checkboxID = 0;
            int selectedcount = 0;
            for (int i = 1; i < childCount; i++) {
                View vl = ll_cont.getChildAt(i);

                if (vl instanceof CheckBox) {
                    checkboxID = vl.getId();
                    if (((CheckBox) vl).isChecked()) {
                        GlobalVar.addweb_area(GlobalVar.data_webarea.get(checkboxID).get(1));
                        selectedcount = selectedcount + 1;
                    } else {
                        GlobalVar.removeweb_area(GlobalVar.data_webarea.get(checkboxID).get(1));
                    }
                }
            }
            if (selectedcount == 0) { CheckAllAreas(); }

        }

        public void checkSchoolTypeForm() { //for the checkbox
            int childCount = cont_municipalities.getChildCount();
            int checkboxID = 0;
            int selectedcount=0;
            for (int i = 1; i < childCount; i++) {
                View vl = cont_municipalities.getChildAt(i);

                if(vl instanceof CheckBox) {
                    checkboxID = vl.getId();
                    if(((CheckBox) vl).isChecked()){
                        GlobalVar.addschool_type(GlobalVar.data_schooltype.get(checkboxID).get(0));
                        selectedcount=selectedcount+1;
                    }
                    else{
                        GlobalVar.removeschool_type(GlobalVar.data_schooltype.get(checkboxID).get(0));
                    }
                }
            }

        }

        public void addAreaList(int webarea, String name) {
            params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            px = getDPmeasurement(30);

            AppCompatCheckBox chkbox = new AppCompatCheckBox(getActivity());
            params.setMargins(px,0, px,0);
            chkbox.setLayoutParams(params);
            chkbox.setId(webarea);
            chkbox.setHighlightColor(Color.parseColor("#ffffff"));
            chkbox.setText(name);
            chkbox.setTextSize(20);
            chkbox.setTextColor(Color.parseColor("#000000"));
            chkbox.setHeight(130);
            chkbox.setChecked(isWebareaChecked(webarea));

            ll_cont.addView(chkbox);
        }

        public void addSchoolTypeList(int schooltype, String name) {
            params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            px = getDPmeasurement(30);

            AppCompatCheckBox chkbox = new AppCompatCheckBox(getActivity());
            params.setMargins(px,0, px,0);
            chkbox.setLayoutParams(params);
            chkbox.setId(schooltype);
            chkbox.setHighlightColor(Color.parseColor("#ffffff"));
            chkbox.setText(name);
            chkbox.setTextColor(Color.parseColor("#000000"));
            chkbox.setHeight(130);
            chkbox.setChecked(isSchoolTypeChecked(schooltype));

            cont_municipalities.addView(chkbox);
        }

        public boolean isWebareaChecked(int id) {

            if(savedWebArea.length() > 0) {
                String[] webArea = savedWebArea.split(",");
                if(webArea.length > 0) {
                    for(int i=0; i<webArea.length; i++) {
                        if(webArea[i].equals(GlobalVar.data_webarea.get(id).get(1))) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public boolean isSchoolTypeChecked(int id) {

            if(savedSchoolType.length() > 0) {
                String[] schoolType = savedSchoolType.split(",");
                if(schoolType.length > 0) {
                    for(int i=0; i<schoolType.length; i++) {
                        if(schoolType[i].equals(GlobalVar.data_schooltype.get(id).get(0))) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public void saveSchoolTypeSelectionData() {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolTypeSelectionData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("webarea", GlobalVar.getweb_area());
            editor.putString("schooltype", GlobalVar.getschool_type());
            editor.apply();
        }

        public void loadSchoolTypeSelectionData() {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolTypeSelectionData", MODE_PRIVATE);
            savedWebArea = sharedPreferences.getString("webarea", "");
            savedSchoolType = sharedPreferences.getString("schooltype", "");
        }

        public void clearSchoolTypeSelectionData() {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolTypeSelectionData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }

        public Integer getDPmeasurement(int val) {

            Resources r = this.getResources();
            px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, r.getDisplayMetrics());
            return px;
        }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment=null;

        switch (v.getId()){
            case R.id.btn_back:
                clearSchoolTypeSelectionData();
                selectedFragment = new greenSchoolTownSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_next:
                checkForm();
                checkSchoolTypeForm();
                saveSchoolTypeSelectionData();
                selectedFragment = new greenSchoolListDisplay();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case SubeteID: {
                if (((CheckBox) v).isChecked() == true) {
                    CheckAllAreas();
                } else if (((CheckBox) v).isChecked() == false) {
                    UncheckAllAreas();
                }
                break;
            }
            default:

                break;
        }
    }

}

