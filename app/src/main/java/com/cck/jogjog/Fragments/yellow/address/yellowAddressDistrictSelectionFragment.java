package com.cck.jogjog.Fragments.yellow.address;


import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_JOUCHOUME;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;

public class yellowAddressDistrictSelectionFragment extends Fragment implements View.OnClickListener {

    private int CheckboxID = 245000000; //CHK000000
    private int selectedcount = 0 ;
    private Boolean isreloaded = false;
    private int district_count = 0;
    private int selected_district_count = 0;
    private  D_cDialog CD;
    private List<String> selectedcodechouaza;
    private String previouscodejis = "";
    private LinearLayoutCompat ll_code_jis_cont;
    private LinearLayoutCompat cont_districts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_district_selection, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_searchsettings = view.findViewById(R.id.btn_searchsettings);
        Button btn_next = view.findViewById(R.id.btn_next);

        btn_back.setOnClickListener(this);
        btn_searchsettings.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        GlobalVar.isFromMap = false;
        GlobalVar.previousScreen = "yellowAddressDistrictSelectionFragment";
        cont_districts = view.findViewById(R.id.cont_districts);

        selectedcodechouaza = GlobalVar.getselectedcode_chouaza();

        if (BukkenList.jouchomei.size() > 0) {
            reloadMunicipalities();
        } else {
            getDistricts();
        }

        return view;
    }

    private void getDistricts() {
        isreloaded = false;

        String prefecture = GlobalVar.getpref_code();
        String municipalities =GlobalVar.getcode_jis();

        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_JOUCHOUME +"pref_code=" + prefecture + "&code_jis=" + municipalities + "&" + API_PATH_TYPE_CHINTAI + "&mode=cnt";
        System.out.println("URL district: " + url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    CD.cDismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    //mali
                    //if (data.length()>0){
                    //    addDistricts_Subete();
                    //}
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject returnData =  data.getJSONObject(i);

                        String dataName = returnData.getString("name");
                        String dataCode = returnData.getString("code");
                        String dataCnt = returnData.getString("cnt");
                        String dataCode_jis = returnData.getString("code_jis");
                        String dataJis_kanji = returnData.getString("jis_kanji");

                        BukkenList.jouchomei.add(new ArrayList());
                        BukkenList.jouchomei.get(i).add(returnData.getString("code"));
                        BukkenList.jouchomei.get(i).add(returnData.getString("name"));
                        BukkenList.jouchomei.get(i).add(returnData.getString("cnt"));
                        BukkenList.jouchomei.get(i).add(returnData.getString("code_jis"));
                        BukkenList.jouchomei.get(i).add(returnData.getString("jis_kanji"));

                        System.out.println(dataName);
                        System.out.println(dataCode);

                        GlobalVar.addallcode_chouaza(dataCode);
                        addDistricts(i, dataCode, dataName, dataCnt, dataCode_jis, dataJis_kanji);

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
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
        CD = new D_cDialog(getContext());
        CD.pbWait.setVisibility(View.VISIBLE);
        CD.cShow(true);
    }

    public void reloadMunicipalities() {
        isreloaded=true;
        for (int i = 0; i < BukkenList.jouchomei.size(); i++) {
            addDistricts(i, BukkenList.jouchomei.get(i).get(0), BukkenList.jouchomei.get(i).get(1), BukkenList.jouchomei.get(i).get(2), BukkenList.jouchomei.get(i).get(3), BukkenList.jouchomei.get(i).get(4));
        }
    }

    private void addDistricts(int index, String code, String name, String cnt, String codejis, String jiskanji) {

        LinearLayoutCompat.LayoutParams chkparams = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chkparams.setMargins(getDPscale(30), getDPscale(0), getDPscale(30), getDPscale(0));

        if (codejis.equals(previouscodejis) == false) {
            previouscodejis = codejis;

            LinearLayoutCompat.LayoutParams llparams = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll_code_jis_cont = new LinearLayoutCompat(getActivity());
            ll_code_jis_cont.setOrientation(LinearLayoutCompat.VERTICAL);
            ll_code_jis_cont.setLayoutParams(llparams);
            cont_districts.addView(ll_code_jis_cont);

            LinearLayoutCompat.LayoutParams tvparams = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvparams.setMargins(getDPscale(10), getDPscale(10), getDPscale(10), getDPscale(10));
            TextView tv_codejis = new TextView(getActivity());
            tv_codejis.setText(jiskanji);
            tv_codejis.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            //tv_codejis.setLayoutParams(tvparams);
            ll_code_jis_cont.addView(tv_codejis, tvparams);

            AppCompatCheckBox chkbox = new AppCompatCheckBox(getActivity());
            chkbox.setId(View.generateViewId());
            chkbox.setHighlightColor(Color.parseColor("#ffffff"));
            chkbox.setText("全て");
            chkbox.setTextSize(20);
            chkbox.setTag("All");
            chkbox.setTextColor(Color.parseColor("#000000"));
            chkbox.setHeight(getDPscale(40));
            chkbox.setOnClickListener(checkboxOnclickListener);
            ll_code_jis_cont.addView(chkbox, chkparams);

            if (isreloaded) {
                for (int i = 0; i < BukkenList.jouchomei.size(); i++) {
                    if (BukkenList.jouchomei.get(i).get(4).equals(jiskanji)) {
                        district_count++;
                    }
                }
            }
        }

        AppCompatCheckBox chkbox = new AppCompatCheckBox(getActivity());
        chkbox.setId(CheckboxID + index);
        chkbox.setHighlightColor(Color.parseColor("#ffffff"));
        chkbox.setText(name +" ("+ cnt + ")");
        chkbox.setTextSize(20);
        chkbox.setTag(code);
        chkbox.setTextColor(Color.parseColor("#000000"));
        chkbox.setHeight(getDPscale(40));
        //chkbox.setLayoutParams(chkparams);
        chkbox.setOnClickListener(checkboxOnclickListener);
        ll_code_jis_cont.addView(chkbox, chkparams);

        if (isreloaded){
            // check if selected
            if(checkboxselected(BukkenList.jouchomei.get(index).get(0))){
                chkbox.setChecked(true);
                selected_district_count++;

                if (selected_district_count == district_count) {
                    View v = (View) chkbox.getParent();
                    if (v instanceof LinearLayoutCompat) {
                        View vl = ((LinearLayoutCompat) v).getChildAt(1);
                        ((CheckBox) vl).setChecked(true);
                    }
                }
            }
            else{
                chkbox.setChecked(false);
            }
        }
    }

    public boolean checkboxselected(String distcode) {
        boolean result = false;

        Iterator<String> iterator = selectedcodechouaza.iterator();
        while (iterator.hasNext()) {
            if (distcode.equals(iterator.next())) {
                selectedcount++;
                result = true;
                break;
            }
        }

        return result;
    }

    private void CheckAllDistrict(CheckBox all) {
        View v = (View) all.getParent();
        if (v instanceof LinearLayoutCompat) {
            int llChildCount = ((LinearLayoutCompat) v).getChildCount();
            for (int j = 2; j < llChildCount; j++) {
                View vl = ((LinearLayoutCompat) v).getChildAt(j);
                ((CheckBox) vl).setChecked(true);
                selectedcount++;
                GlobalVar.addcode_chouaza(vl.getTag().toString());
            }
        }
    }

    private void UncheckAllDistrict(CheckBox all) {
        View v = (View) all.getParent();
        if (v instanceof LinearLayoutCompat) {
            int llChildCount = ((LinearLayoutCompat) v).getChildCount();
            for (int j = 2; j < llChildCount; j++) {
                View vl = ((LinearLayoutCompat) v).getChildAt(j);
                ((CheckBox) vl).setChecked(false);
                selectedcount--;
                GlobalVar.removecode_chouaza(vl.getTag().toString());
            }
        }
    }

    private int getDPscale(int size) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) ((double) size * scale + 0.5f));
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment=null;

        switch (v.getId()){
            case R.id.btn_back:
                GlobalVar.clearchouazacode();
                BukkenList.jouchomei.clear();
                clearSearchPref();
                selectedFragment = new yellowAddressMunicipalitySelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_next:
                if(selectedcount==0){
                    alertDialogSelectStation();
                }
                GlobalVar.isFromMap = false;
                GlobalVar.clearpreviousscreen();
                selectedFragment = new yellowAddressRentListFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_searchsettings:
                GlobalVar.setparent_fragment("yellowAddressDistrictSelectionFragment");
                selectedFragment = new yellowAddressSearchSettings();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            default:
                break;
        }

    }

    View.OnClickListener checkboxOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {

            if (((CheckBox) v).isChecked() == true) {
                if (v.getTag().equals("All")) {
                    CheckAllDistrict((CheckBox) v);
                } else {
                    selectedcount++;
                    GlobalVar.addcode_chouaza(v.getTag().toString());
                }
            } else if (((CheckBox) v).isChecked() == false) {
                if (v.getTag().equals("All")) {
                    UncheckAllDistrict((CheckBox) v);
                } else {
                    selectedcount--;
                    GlobalVar.removecode_chouaza(v.getTag().toString());
                }
                if (selectedcount <= 0) {
                    selectedcount = 0;
                }
            } else {
            }
        }
    };

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