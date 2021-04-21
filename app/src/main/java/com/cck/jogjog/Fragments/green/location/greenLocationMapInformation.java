package com.cck.jogjog.Fragments.green.location;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.blue.location.blueLocationFragment;
import com.cck.jogjog.Fragments.blue.location.blueLocationPropertyDetailFragment;
import com.cck.jogjog.Fragments.green.address.greenAddressPropertyDetailFragment;
import com.cck.jogjog.Fragments.green.address.greenAddressRentListFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_DETAIL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_SELL;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class greenLocationMapInformation extends Fragment implements View.OnClickListener {

    TextView bukken_nameTV, address1TV, ensen_name1TV, bukken_type_nameTV, kansei_year_monthTV, bukken_10m_feeTV;
    ImageView gaikanURLImageView;
    Button btn_back_map_information, btn_bukken_details;
    RadioGroup rg;

    /*Dynamic Elements*/
    LinearLayout ll_haf, ll_heyaWrapper, ll_heyaDetailsWrapper;
    TextView textview, btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_green_map_information, container, false);

        // Do all the stuff to initialize your custom view
        btn_back_map_information = view.findViewById(R.id.btn_back_map_information);
        btn_bukken_details = view.findViewById(R.id.btn_bukken_details);
        gaikanURLImageView = view.findViewById(R.id.gaikanURLImageView);

        bukken_nameTV = view.findViewById(R.id.bukken_nameTV);
        address1TV = view.findViewById(R.id.address1TV);
        ensen_name1TV = view.findViewById(R.id.ensen_name1TV);
        bukken_type_nameTV = view.findViewById(R.id.bukken_type_nameTV);
        kansei_year_monthTV = view.findViewById(R.id.kansei_year_monthTV);
        bukken_10m_feeTV = view.findViewById(R.id.bukken_10m_feeTV);

        btn_back_map_information.setOnClickListener(this);
        btn_bukken_details.setOnClickListener(this);

        ll_haf = view.findViewById(R.id.ll_heyaandfriends);

        getBukkenDetail();

        return view;
    }

    public void getBukkenDetail() {

        String MapBukkenURL = GlobalVar.getMap_bukken_url();
        final String bukken_noMap = GlobalVar.getdetailed_bukken_no();

        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_DETAIL + API_PATH_TYPE_SELL + "&bukken_no=" + bukken_noMap;
        System.out.println("URL getBukkenDetail: " + MapBukkenURL);

        final StringRequest stringRequest = new StringRequest(MapBukkenURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DecimalFormat format = new DecimalFormat("#,###.##");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");

                    for (int i = 0; i < bukken_list.length(); i++) {
                        JSONObject returnBukken = bukken_list.getJSONObject(i);
                        String bukken_no = returnBukken.getString("bukken_no");
                        System.out.println("bukken_no " + bukken_no);

                        if (bukken_no == null) {
                            System.out.println("null");
                        } else {
                            if (bukken_no.equals(bukken_noMap)) {

                                String bukken_name = returnBukken.getString("bukken_name");

                                String address1 = returnBukken.getString("address1");
                                String address2 = returnBukken.getString("address2");
                                String address3 = returnBukken.getString("address3");

                                String ensen_name1 = returnBukken.getString("ensen_name1");
                                String eki_name1 = returnBukken.getString("eki_name1");

                                String bus_stop_name1 = returnBukken.getString("bus_stop_name1");
                                String bus_name1 = returnBukken.getString("bus_name1");

                                String bukken_type_name = returnBukken.getString("bukken_type_name");

                                String tatemono_kansei_year = returnBukken.getString("tatemono_kansei_year"); //tatemono_kansei_year - tatemono_kansei_month
                                String tatemono_kansei_month = returnBukken.getString("tatemono_kansei_month");

                                String bukken_10m_fee = returnBukken.getString("bukken_10m_fee");
                                String bukken_10k_fee = returnBukken.getString("bukken_10k_fee");

                                String gaikan_url = returnBukken.getString("gaikan_url");

                                // Set in User interface
                                bukken_nameTV.setText(bukken_name);

                                if (address2.equals("null") && address3.equals("null")){
                                    address1TV.setText(address1);
                                }
                                else{
                                    address1TV.setText(address1 + address2 + address3);
                                }

                                if (ensen_name1.equals("null") && eki_name1.equals("null")) {
                                    ensen_name1TV.setText(bus_stop_name1 + bus_name1);
                                } else {
                                    ensen_name1TV.setText(ensen_name1 + eki_name1);
                                }

                                bukken_type_nameTV.setText(bukken_type_name);

                                kansei_year_monthTV.setText(tatemono_kansei_year + "年" + tatemono_kansei_month + "月");

                                String price ="";
                                if ((!bukken_10m_fee.equals("0")) && (!bukken_10m_fee.equals(""))) {
                                    price = price + bukken_10m_fee + "億";
                                }
                                if ((!bukken_10k_fee.equals("0")) && (!bukken_10k_fee.equals(""))) {
                                    price = price + format.format(Double.parseDouble(bukken_10k_fee)) + "万";
                                }

                                bukken_10m_feeTV.setText(price);
                                //bukken_10m_feeTV.setText(bukken_10m_fee + "億" + bukken_10k_fee + "万");


                                Picasso.get().load(DOMAIN_NAME + gaikan_url).into(gaikanURLImageView);

                                btn_bukken_details.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        Fragment selectedFragment = null;
                                        GlobalVar.previousScreen = "greenLocationMapInformation";
                                        selectedFragment = new greenLocationPropertyDetailFragment();
                                        ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

                                    }
                                });

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
                int x = 1;
                // Anything you want
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {

        Fragment selectedFragment = null;
        GlobalVar.previousScreen = "greenLocationMapInformation";
        switch (v.getId()) {
            case R.id.btn_back_map_information:
                System.out.println("BACK TO RENT");
                GlobalVar.isFromMap = false;
                selectedFragment = new greenLocationFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
        }
    }



}
