package com.cck.jogjog.Fragments.yellow.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.favorite.FavoriteBluePropertyDetailsFragment;
import com.cck.jogjog.Fragments.favorite.FavoriteGreenPropertyDetailsFragment;
import com.cck.jogjog.Fragments.favorite.FavoriteYellowPropertyDetailsFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_CONTACT_DATA;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_POST_CONTACT_BUKKEN_DETAIL_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_POST_CONTACT_SHOP;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.CONTACT_BASE_URL_DEV;

public class yellowLineInquireProperty extends Fragment implements View.OnClickListener {
    //region VARIABLES
    TextView bukken_nameTV, shozaichiTV, bus1TV, chinryoTV, madoriTV;
    EditText other_reasonsET, seimeiET, furiganaET, emailET, phonenumberET;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10;
    RadioButton rd1, rd2, rd3, rdButton;
    RadioGroup rdGroup;
    Spinner inquire_store_spinner;
    Button confirmBTN, btn_back_store;
    Switch inquireSW;

    //Dynamic elements
    LinearLayoutCompat ll_propertyWrapper, ll_rowWrapper,ll_elementWrapper;
    LinearLayoutCompat.LayoutParams layoutparams;
    TextView textview;

    int screenWidth;
    JSONArray innerBukken;

    //endregion
    //region START
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_inquire_property, container, false);

        ll_propertyWrapper = view.findViewById(R.id.ll_propertyWrapper);
        layoutparams = new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        //==========get the device screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels; //LAGAY MO DIN TO SA GREEN

        //====================================================

        inquire_store_spinner = view.findViewById(R.id.inquire_store_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.inquire_store_spinner, R.layout.spinner_default);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_default);
        inquire_store_spinner.setAdapter(adapter);

        //====================================================

        bukken_nameTV = view.findViewById(R.id.bukken_nameTV); //
        //bus1TV = view.findViewById(R.id.bus1TV); //
        //chinryoTV = view.findViewById(R.id.chinryoTV); //
        shozaichiTV = view.findViewById(R.id.shozaichiTV); //
        // madoriTV = view.findViewById(R.id.madoriTV); //

        //====================================================

        other_reasonsET = view.findViewById(R.id.other_reasonsET); //etc_text
        seimeiET = view.findViewById(R.id.seimeiET); //user_name
        furiganaET = view.findViewById(R.id.furiganaET); //user_furigana
        emailET = view.findViewById(R.id.emailET); //user_mail
        phonenumberET = view.findViewById(R.id.phonenumberET); //user_tel

        //====================================================
        //inquiry_type
        cb1 = view.findViewById(R.id.cb1);
        cb2 = view.findViewById(R.id.cb2);
        cb3 = view.findViewById(R.id.cb3);
        cb4 = view.findViewById(R.id.cb4);
        cb5 = view.findViewById(R.id.cb5);
        cb6 = view.findViewById(R.id.cb6);
        cb7 = view.findViewById(R.id.cb7);
        cb8 = view.findViewById(R.id.cb8);
        cb9 = view.findViewById(R.id.cb9);
        cb10 = view.findViewById(R.id.cb10);

        //====================================================

        //FormContactmethod
        rdGroup = view.findViewById(R.id.rdGroup);
        rd1 = view.findViewById(R.id.rd1);
        rd2 = view.findViewById(R.id.rd2);
        rd3 = view.findViewById(R.id.rd3);
        rd1.setChecked(true);

        int rdID = rdGroup.getCheckedRadioButtonId();
        rdButton = view.findViewById(rdID);

        //====================================================

        confirmBTN = view.findViewById(R.id.confirmBTN);
        confirmBTN.setBackgroundColor(Color.GRAY);
        confirmBTN.setEnabled(false);


        //====================================================

        inquireSW = view.findViewById(R.id.inquireSW);
        inquireSW.setChecked(false);
        inquireSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (inquireSW.isChecked() == true) {
                    confirmBTN.setEnabled(true);
                    confirmBTN.setBackgroundColor(Color.BLACK);
                } else {
                    confirmBTN.setEnabled(false);
                    confirmBTN.setBackgroundColor(Color.GRAY);
                }
            }
        });

        //====================================================

        btn_back_store = view.findViewById(R.id.btn_back_store);
        confirmBTN.setOnClickListener(this);

        btn_back_store.setOnClickListener(this);

        //====================================================

        getStoreDetails();


        return view;
    }

    //endregion
    //region DATA ACQUISITION
    public void getStoreDetails() {
//https://jogjog.goalway.jp/form/bukken/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_contact_data&type=chintai&bukken_no=00159885001
//https://jogjog.com/form/bukken/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_contact_data&type=chintai&bukken_no=00017307004&tenpo_no=051602040000
//https://jogjog.com/form/bukken/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_contact_data&&type=chintai&bukken_no=00159885001 //bella vista //051602040000

        String bukken_noURL = GlobalVar.getContact_bukken_no();


        String url = CONTACT_BASE_URL_DEV + //https://jogjog.com/form/
                API_PATH_POST_CONTACT_BUKKEN_DETAIL_CHINTAI + //bukken/appapi.php
                "?key=" + API_KEY + "&" + //jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX
                API_PATH_GET_CONTACT_DATA + //Processing=get_contact_data&
                API_PATH_TYPE_CHINTAI + //&type=chintai
                "&bukken_no=" + bukken_noURL;

        System.out.println("url " + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String tenpo_no = GlobalVar.getTenpo_no();
                    System.out.println("tenpo_no: " + tenpo_no);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject bukken_list = data.getJSONObject(tenpo_no);

                    innerBukken = bukken_list.getJSONArray("bukken");
                    generatePropertyList();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("RESPONSE ERROR");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void generatePropertyList() {

        try {

            for (int i = 0; i < innerBukken.length(); i++) {
                JSONObject returnBukken = innerBukken.getJSONObject(i);

                String bukken_name = returnBukken.getString("bukken_name"); //bukken_nameTV
                String shozaichi = returnBukken.getString("shozaichi"); //shozaichiT
                String bus1_kanji = returnBukken.getString("bus1_kanji"); //bus1TV
                String bus1_distance = returnBukken.getString("bus1_distance"); //bus1TV
                String chinryo = returnBukken.getString("chinryo"); //chinryo TV
                String chinryoNew = chinryo.replaceAll("0", "");
                String madori = returnBukken.getString("madori"); //madoriTV
                String bukken_type_name = returnBukken.getString("bukken_type_name"); //madoriTV

                addDivider();
                addLinearLayout(1);
                addLinearLayout(2);
                addTextView("物件名", 0);
                addLinearLayout(3);
                addPropertyDetailsLink(bukken_name, i);
                addDivider();
                addLinearLayout(1);
                addLinearLayout(2);
                addTextView("所在地・交通", 0);
                addLinearLayout(3);
                addTextView(shozaichi,1);
                addLinearLayout(1);
                addLinearLayout(2);
                addTextView("", 0);
                addLinearLayout(3);
                if (bus1_kanji.equals("null")) addTextView("(律歩" + bus1_distance + "分)",1);
                else addTextView(bus1_kanji + " " + "(律歩" + bus1_distance + "分)",1);
                addDivider();
                addLinearLayout(1);
                addLinearLayout(2);
                addTextView("賃料", 0);
                addLinearLayout(3);
                addTextView(chinryoNew + "万円",1);
                addDivider();
                addLinearLayout(1);
                addLinearLayout(2);
                addTextView("間取り/種別", 0);
                addLinearLayout(3);
                addTextView(madori + " " + bukken_type_name,1);
                addDivider();

                if(i < (innerBukken.length()-1)) addSpace();
            }

        } catch(JSONException e) {

        }


    }

    //endregion
    //region METHODS
    public void checking() {

        if (seimeiET.length() == 0 && furiganaET.length() == 0 && emailET.length() == 0 && phonenumberET.length() == 0 && other_reasonsET.length() == 0) {
            alertDialogRequiredField();
        } else {
            sendInquiry();
        }
    }

    public void sendInquiry() {

        String etc_text = other_reasonsET.getText().toString();
        String user_name = seimeiET.getText().toString();
        String user_furigana = furiganaET.getText().toString();
        String user_mail = emailET.getText().toString();
        String user_tel = phonenumberET.getText().toString();
        String FormContactmethod = rdButton.getText().toString();
        String FormAllowtime = inquire_store_spinner.getSelectedItem().toString();

        //この部屋で申込みをしたい
        //希望案件に合う物件を紹介してほしい
        //その他

        String inquiry_type = "";
        if (cb1.isChecked()) {
            inquiry_type += "この部屋で申込みをしたい, ";
        }
        if (cb2.isChecked()) {
            inquiry_type += "店舗に直接行きたい, ";
        }
        if (cb3.isChecked()) {
            inquiry_type += "入居に関して相談したい, ";
        }
        if (cb4.isChecked()) {
            inquiry_type += "条件に合う他の物件も紹介してほしい, ";
        }
        if (cb5.isChecked()) {
            inquiry_type += "物件を実際に見てみたい, ";
        }
        if (cb6.isChecked()) {
            inquiry_type += "建物(現地)で待ち合わせしたい, ";
        }
        if (cb7.isChecked()) {
            inquiry_type += "最新の空室情報を知りたい, ";
        }
        if (cb8.isChecked()) {
            inquiry_type += "物件の詳細知りたい, ";
        }
        if (cb9.isChecked()) {
            inquiry_type += "社宅(会社契約)を希望, ";
        }
        if (cb10.isChecked()) {
            inquiry_type += "その他, ";
        }

        String tenpo_no = GlobalVar.getTenpo_no();
        String url = CONTACT_BASE_URL_DEV +
                API_PATH_POST_CONTACT_SHOP +
                "?key=" + API_KEY +
                "&tenpo_no=" + tenpo_no +
                "&inquiry_type=" + inquiry_type +
                "&etc_text=" + etc_text +
                "&user_name=" + user_name +
                "&user_furigana=" + user_furigana +
                "&user_mail=" + user_mail +
                "&user_tel=" + user_tel +
                "&FormContactmethod=" + FormContactmethod +
                "&FormAllowtime=" + FormAllowtime;

        System.out.println("inquiry_type " + inquiry_type);
        System.out.println("etc_text " + etc_text);
        System.out.println("user_name " + user_name);
        System.out.println("user_furigana " + user_furigana);
        System.out.println("user_mail " + user_mail);
        System.out.println("user_tel " + user_tel);
        System.out.println("FormContactmethod " + FormContactmethod);
        System.out.println("FormAllowtime " + FormAllowtime);

        System.out.println("url " + url);

        String newURL = url.replace(" ", "%20");
        System.out.println("newURL " + newURL);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, newURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("OK");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("RESPONSE ERROR");
            }
        });
        queue.add(stringRequest);
    }

    //endregion
    //region LISTENERS
    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {
            case R.id.btn_back_store:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.confirmBTN:
                checking();
                System.out.println("BUTTON CONFIRM");
                break;
        }

    }

    //endregion
    //region DIALOGS
    private void alertDialogRequiredField() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("fill up required fields");
        builder.setCancelable(true);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //endregion

    /* Add dynamic linear layout */
    private void addLinearLayout(int pos)  {

        if(pos == 1) {

            ll_rowWrapper = new LinearLayoutCompat(getActivity());
            ll_rowWrapper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll_rowWrapper.setOrientation(LinearLayoutCompat.HORIZONTAL);

            ll_propertyWrapper.addView(ll_rowWrapper);

        } else if(pos == 2) {

            ll_elementWrapper = new LinearLayoutCompat(getActivity());
            int width = ((screenWidth - 60) / 3) - 10;

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(30,2,0,2);
            ll_elementWrapper.setLayoutParams(params);
            ll_elementWrapper.setOrientation(LinearLayoutCompat.HORIZONTAL);

            ll_rowWrapper.addView(ll_elementWrapper);

        } else if(pos == 3) {

            ll_elementWrapper = new LinearLayoutCompat(getActivity());
            int width = (((screenWidth - 60) / 3) * 2) + 10;

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,2,30,2);
            ll_elementWrapper.setLayoutParams(params);
            ll_elementWrapper.setOrientation(LinearLayoutCompat.HORIZONTAL);

            ll_rowWrapper.addView(ll_elementWrapper);
        }

    }
    /* End of dynamic linear layout */

    /* Add dynamic TextView */
    private void addTextView(String txt, int gravity) {

        textview = new TextView(this.getActivity());
        textview.setLayoutParams(layoutparams);
        textview.setText(txt);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textview.setTextColor(Color.BLACK);
        textview.setPadding(5,0,5,0);

        if(gravity == 0) textview.setGravity(Gravity.CENTER);
        else textview.setGravity(Gravity.LEFT);

        ll_elementWrapper.addView(textview);
    }
    /* End of dynamic TextView */

    /* Add dynamic link to property detials */
    private void addPropertyDetailsLink(String txt, int id){

        final int linkId = Integer.parseInt("8" + id);

        textview = new TextView(this.getActivity());
        textview.setLayoutParams(layoutparams);
        textview.setText(txt);
        textview.setId(linkId);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textview.setPadding(5,0,5,0);

        textview.setTextColor(Color.BLUE);
        textview.setGravity(Gravity.LEFT);

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPropertyDetails(linkId);
            }
        });

        ll_elementWrapper.addView(textview);

    }
    /* End of dynamic link to property detials */

    /* Add dynamic divider */
    private void addDivider(){

        View viewDivider = new View(getActivity());
        viewDivider.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4));
        viewDivider.setBackgroundColor(Color.parseColor("#bbbbbb"));
        ll_propertyWrapper.addView(viewDivider);
    }
    /* End of dynamic divider */

    /* Add dynamic space*/
    private void addSpace(){

        View viewSpace = new View(getActivity());
        viewSpace.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        ll_propertyWrapper.addView(viewSpace);
    }
    /* End of dynamic space */

    private void goToPropertyDetails(int id) {

        Fragment selectedFragment = new Fragment();
        String strId = String.valueOf(id);
        String index = strId.substring(1, strId.length());
        String source = GlobalVar.getFavoritesSrc();

        try {

            JSONObject returnBukken = innerBukken.getJSONObject(Integer.parseInt(index));
            GlobalVar.setdetailed_bukken_no(returnBukken.getString("heya_no"));

            selectedFragment = new FavoriteYellowPropertyDetailsFragment();


            ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

        } catch (JSONException e) {

        }
    }
}
