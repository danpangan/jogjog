package com.cck.jogjog.Fragments.green.school;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Fragments.blue.school.blueSchoolRentListFragment;
import com.cck.jogjog.Fragments.blue.school.blueSchoolTypeSelectionFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_SCHOOL;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;


public class greenSchoolListDisplay extends Fragment implements View.OnClickListener {
    ArrayList<String> schooltype = new ArrayList<String>();
    List<List<String>> data2 = new ArrayList<>();

    LinearLayout llcont;
    private D_cDialog CD;

    String savedSchool = "";

    public greenSchoolListDisplay() {
        // Required empty public constructor
    }

    public static greenSchoolListDisplay newInstance(String param1, String param2) {
        greenSchoolListDisplay fragment = new greenSchoolListDisplay();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_green_school_list_display, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_search = view.findViewById(R.id.btn_search);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        GlobalVar.previousScreen = "greenSchoolListDisplay";
        llcont = view.findViewById(R.id.ll_cont);

        loadSchoolData();
        getSchoolList();

        return view;
    }

    public void getSchoolList() {
        String code_webtown = GlobalVar.getweb_town();
        String code_webarea = GlobalVar.getweb_area();
        //String school_name = GlobalVar.getschool_name();
        String school_type = GlobalVar.getschool_type();
        System.out.println("SCHOOL LIST");
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_SCHOOL + "code_webtown=" + code_webtown + "&code_webarea=" + code_webarea + "&school_type=" + school_type;
        //String url = "https://www.jogjog.com/api/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_school_list&code_webtown=0001&code_webarea=0001&school_type=2";
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //CD.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject returnData = data.getJSONObject(i);

                        data2.add(new ArrayList());
                        //data2.get(i).add(returnData.getString("code_webarea"));
                        data2.get(i).add(returnData.getString("school_type"));
                        data2.get(i).add(returnData.getString("name"));
                        if (!schooltype.contains(returnData.getString("school_type")))
                            schooltype.add(returnData.getString("school_type"));

                        System.out.println("School  Type: " + schooltype);
                        /*
                        String dataName = returnData.getString("name");
                        String dataCode = returnData.getString("school_type");
                        System.out.println("PREFECTURE: " + dataName);
                        System.out.println("School_TYPE: " + dataCode);*/

                    }

                    System.out.println("TRAFFIC KANJI 2 : " + schooltype);

                    for (int j = 0; j < schooltype.size(); j++) {
                        String name = schooltype.get(j);
                        String typename = "";
                        int type = Integer.parseInt(name);
                        switch (type) {
                            case 2:
                                typename = "小学校";
                                break;
                            case 3:
                                typename = "中学校";
                                break;
                            case 4:
                                typename = "高等学校・高等専門学校";
                                break;
                            case 5:
                                typename = "短大・大学";
                                break;
                            case 6:
                                typename = "専門学校";
                                break;
                        }
                        addTextView(typename);
                        for (int k = 0; k < data2.size(); k++) {
                            String text = data2.get(k).get(1);
                            String name2 = data2.get(k).get(0);
                            if (name.equals(name2))
                                addLineRadioButton(text, k);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
//        CD = new D_cDialog(getContext());
//        CD.pbWait.setVisibility(View.VISIBLE);
//        CD.cShow(true);
    }

    private void addTextView(String header) {
        TextView tv_school_type = new TextView(getActivity());
        tv_school_type.setText(header);
        tv_school_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv_school_type.setBackgroundColor(Color.parseColor("#ffffff"));
        llcont.addView(tv_school_type);
    }

    public void addLineRadioButton(String text, int index) {
        LinearLayout.LayoutParams rdbtnparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        AppCompatRadioButton rdbtn = new AppCompatRadioButton(getActivity());
        rdbtn.setId(index);
        rdbtn.setText(text);
        rdbtn.setTextSize(20);
        rdbtn.setTextColor(Color.parseColor("#000000"));
        rdbtn.setMinHeight(getDPscale(40));
        rdbtn.setOnClickListener(this);
        rdbtn.setLayoutParams(rdbtnparams);
        rdbtn.setChecked(isSchoolListChecked(index));
        llcont.addView(rdbtn);
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
                clearSchoolData();
                selectedFragment = new greenSchoolTypeSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            default:
                int radioID = v.getId();
                saveSchoolData();
                System.out.println("RADIO ID: " + radioID);
                GlobalVar.setschool_name(data2.get(radioID).get(1));
                selectedFragment = new greenSchoolRentListFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

                break;
        }
    }

    private void alertDialogSchoolList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("学校が見つかりませんでした");
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

    public boolean isSchoolListChecked(int id) {

        if (savedSchool.length() > 0) {
            String[] school = savedSchool.split(",");
            if (school.length > 0) {
                for (int i = 0; i < school.length; i++) {
                    if (school[i].equals(GlobalVar.data_schooltype.get(id).get(1))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void saveSchoolData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("schoolname", GlobalVar.getschool_name());
        editor.apply();
    }

    public void loadSchoolData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolData", MODE_PRIVATE);
        savedSchool = sharedPreferences.getString("schoolname", "");
    }

    public void clearSchoolData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}