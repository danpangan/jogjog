package com.cck.jogjog.Fragments.green.common;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_CONTACT_DATA;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_POST_CONTACT_BUKKEN_DETAIL_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_POST_CONTACT_BUKKEN_DETAIL_SELL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_POST_CONTACT_SHOP;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_SELL;
import static com.cck.jogjog.GlobalVar.Common.CONTACT_BASE_URL_DEV;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class greenInquireStore extends Fragment implements View.OnClickListener {

    ImageView shopGaikanURL;
    TextView storenameTV, phonenumberTV, faxnumberTV, shozaichiTV;
    EditText other_reasonsET, seimeiET, furiganaET, emailET, phonenumberET;
    CheckBox cb1, cb2, cb3, cb4;
    RadioButton rd1, rd2, rd3, rdButton;
    RadioGroup rdGroup;
    Spinner inquire_store_spinner;
    Button confirmBTN, btn_back_store;
    Switch inquireSW;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_green_inquire_store, container, false);

        //====================================================

        shopGaikanURL = view.findViewById(R.id.shopGaikanURL);

        //====================================================

        inquire_store_spinner = view.findViewById(R.id.inquire_store_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.inquire_store_spinner, R.layout.spinner_default);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_default);
        inquire_store_spinner.setAdapter(adapter);

        //====================================================

        storenameTV = view.findViewById(R.id.storenameTV); //
        phonenumberTV = view.findViewById(R.id.phonenumberTV); //
        faxnumberTV = view.findViewById(R.id.faxnumberTV); //
        shozaichiTV = view.findViewById(R.id.shozaichiTV); //

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
        btn_back_store.setOnClickListener(this);
        confirmBTN.setOnClickListener(this);

        //====================================================

        getStoreDetails();


        return view;
    }

    public void getStoreDetails() {
//https://jogjog.goalway.jp/form/bukken/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_contact_data&type=chintai&bukken_no=00017307004&tenpo_no=051602040000
//https://jogjog.com/form/bukken/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_contact_data&type=chintai&bukken_no=00017307004&tenpo_no=051602040000
//https://jogjog.com/form/bukken/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_contact_data&&type=chintai&bukken_no=00159885001 //bella vista //051602040000

        String bukken_noURL = GlobalVar.getdetailed_bukken_no();

        String url = CONTACT_BASE_URL_DEV + //https://jogjog.com/form/
                API_PATH_POST_CONTACT_BUKKEN_DETAIL_SELL + //bukken/appapi.php
                "?key=" + API_KEY + "&" + //jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX
                API_PATH_GET_CONTACT_DATA + //Processing=get_contact_data&
                API_PATH_TYPE_SELL + //&type=chintai
                "&bukken_no=" + bukken_noURL;



        System.out.println("url " + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String tenpo_no = GlobalVar.getTenpo_no();
                    System.out.println("tenpo_no: " + tenpo_no);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject returnData = data.getJSONObject(i);

                        JSONObject shopInfo = returnData.getJSONObject("shop_info");

                        String tenpo_name = shopInfo.getString("tenpo_name"); //storenameTV
                        String tenpo_tel = shopInfo.getString("tenpo_tel"); //phonenumberTV
                        String tenpo_fax = shopInfo.getString("tenpo_fax"); //faxnumberTV
                        String tenpo_shozaichi = shopInfo.getString("tenpo_shozaichi"); //shozaichiTV

                        //String shop_gaikan_url = returnBukken.getString("shop_gaikan_url");

                    storenameTV.setText(tenpo_name);
                    phonenumberTV.setText(tenpo_tel);
                    faxnumberTV.setText(tenpo_fax);
                    shozaichiTV.setText(tenpo_shozaichi);
                        //Picasso.get().load(DOMAIN_NAME + shop_gaikan_url).into(shopGaikanURL);
                    }
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

    public void checking() {

//        if (!cb1.isChecked() || !cb2.isChecked() || !cb3.isChecked() || !cb4.isChecked()) {
//            alertDialogRequiredField();
//        }
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

        String inquiry_type = "";
        if (cb1.isChecked()) {
            inquiry_type += "店舗に直接行って相談したい,";
        }
        if (cb2.isChecked()) {
            inquiry_type += "希望案件に合う物件を紹介してほしい,";
        }
        if (cb3.isChecked()) {
            inquiry_type += "入居に関して相談したい,";
        }
        if (cb4.isChecked()) {
            inquiry_type += "その他";
        }

//        String url = "https://jogjog.goalway.jp/form" +
//                "/shopyoyaku/appapi.php?" +
//                "key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX" +
//                "&tenpo_no=161011010000" +
//                "&inquiry_type="店舗に直接行って相談したい," +
//                "&etc_text=what%20if%20may%20spaces" +
//                "&user_name=alxs" +
//                "&user_furigana=alxs" +
//                "&user_mail=test@test.com" +
//                "&user_tel=123456778911" +
//                "&FormContactmethod=メールのみ" +
//                "&FormAllowtime=10:00%20~%2012:00";

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

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {
            case R.id.btn_back_store:
                selectedFragment = GlobalVar.getparent_fragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.confirmBTN:
                checking();
                System.out.println("BUTTON CONFIRM");
                break;
        }

    }

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

}
