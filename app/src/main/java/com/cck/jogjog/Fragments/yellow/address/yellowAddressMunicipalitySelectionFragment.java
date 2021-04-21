package com.cck.jogjog.Fragments.yellow.address;


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
import android.widget.Toast;

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
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_SHIKUCHOUSON;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;

public class yellowAddressMunicipalitySelectionFragment extends Fragment implements View.OnClickListener {

    int selectedcount = 0;

    String firstID = "0";
    Boolean isreloaded = false;

    private static D_cDialog CD;
    private List<String> selectedcodejis;

    LinearLayoutCompat cont_municipalities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yellow_municipality_selection, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_next = view.findViewById(R.id.btn_next);

        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        cont_municipalities = view.findViewById(R.id.cont_municipalities);

        selectedcodejis = GlobalVar.getselectedcode_jis();

        if (BukkenList.shikuchouson.size() > 0) {
            reloadMunicipalities();
        } else {
            getMunicipalities();
        }
        return view;
    }

    public void getMunicipalities() {
        isreloaded = false;

        String prefecture = GlobalVar.getpref_code();
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_SHIKUCHOUSON +"pref_code=" + prefecture + API_PATH_TYPE_CHINTAI + "&mode=cnt";
        System.out.println("PREFECTURE TO MUNICIPALITY: " + prefecture);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    CD.cDismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject returnData =  data.getJSONObject(i);
                        String dataName = returnData.getString("name");
                        String dataCode = returnData.getString("code");

                        BukkenList.shikuchouson.add(new ArrayList());
                        BukkenList.shikuchouson.get(i).add(dataCode);
                        BukkenList.shikuchouson.get(i).add(dataName);

                        System.out.println(dataName);
                        System.out.println(dataCode);

                        addMunicipalities(dataCode,dataName);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
        CD = new D_cDialog(getContext());
        CD.pbWait.setVisibility(View.VISIBLE);
        CD.cShow(false);
    }

    public void reloadMunicipalities() {
        isreloaded=true;
        for (int i = 0; i < BukkenList.shikuchouson.size(); i++) {
            addMunicipalities(BukkenList.shikuchouson.get(i).get(0),BukkenList.shikuchouson.get(i).get(1));
        }
    }

    public void addMunicipalities(String municode, String name) {
        AppCompatCheckBox chkbox = new AppCompatCheckBox(getActivity());
        chkbox.setId(View.generateViewId());
        chkbox.setHighlightColor(Color.parseColor("#ffffff"));
        chkbox.setText(name);
        chkbox.setTextSize(20);
        chkbox.setTag(municode);
        chkbox.setTextColor(Color.parseColor("#000000"));
        chkbox.setHeight(130);
        chkbox.setOnClickListener(checkboxOnclickListener);
        cont_municipalities.addView(chkbox);
        if (isreloaded){
            // check if selected
            if(checkboxselected(municode)){
                chkbox.setChecked(true);
            }
            else{
                chkbox.setChecked(false);
            }
        }
    }

    public boolean checkboxselected(String municode){
        boolean result = false;

        Iterator<String> iterator = selectedcodejis.iterator();
        while (iterator.hasNext()) {
            if(municode.equals(iterator.next())){
                if((firstID.equals("0"))||(Integer.valueOf(firstID) > Integer.valueOf(municode))){
                    firstID = municode;
                }
                selectedcount++;
                result=true;
                break;
            }
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment=null;

        switch (v.getId()){
            case R.id.btn_back:
                GlobalVar.clearcodejis();
                BukkenList.shikuchouson.clear();
                selectedFragment = new yellowAddressPrefectureSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_next:
                if (selectedcount == 0){
                    alertDialogSelectMunicipality();
                }
                else {
                    selectedFragment = new yellowAddressDistrictSelectionFragment();
                    ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                }
                break;
            default:
                break;
        }

    }

    View.OnClickListener checkboxOnclickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if (((CheckBox) v).isChecked() == true) {
                selectedcount++;
                if ((Integer.valueOf(v.getTag().toString()) < Integer.valueOf(firstID)) || (firstID.equals("0"))) {
                    firstID = v.getTag().toString();
                    GlobalVar.setmunicipality_name("" + ((CheckBox) v).getText());
                }
                GlobalVar.addcode_jis(v.getTag().toString());
            } else if (((CheckBox) v).isChecked() == false) {
                selectedcount--;
                GlobalVar.removecode_jis(v.getTag().toString());

                if (selectedcount <= 0) {
                    selectedcount = 0;
                }
                if (firstID.equals(v.getTag().toString())) {
                    firstID = GlobalVar.getlowestcode_jis();
                    if (firstID.equals("")) {
                        firstID = "0";
                    }
                    for (List<String> list : BukkenList.shikuchouson) {
                        if (list.contains(firstID)) {
                            int index = BukkenList.shikuchouson.indexOf(list);
                            GlobalVar.setmunicipality_name(BukkenList.shikuchouson.get(index).get(1));
                            break;
                        }
                    }
                }
            } else {

            }
        }
    };

    private void alertDialogSelectMunicipality() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("市区町村を選択してください。");
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

}