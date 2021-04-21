package com.cck.jogjog.Fragments.yellow.address;

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

import com.android.volley.DefaultRetryPolicy;
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

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class yellowAddressMapInformation extends Fragment implements View.OnClickListener {

    TextView bukken_nameTV, shozaichiTV, eki1_kantiTV, bukken_type_nameTV, building_infoTV, kanseibeiTV, chikunenTV, chinryoTV, madoriTV, heya_kaisuTV;
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
        View view = inflater.inflate(R.layout.fragment_yellow_map_information, container, false);

        // Do all the stuff to initialize your custom view
        btn_back_map_information = view.findViewById(R.id.btn_back_map_information);
        btn_bukken_details = view.findViewById(R.id.btn_bukken_details);
        gaikanURLImageView = view.findViewById(R.id.gaikanURLImageView);

        bukken_nameTV = view.findViewById(R.id.bukken_nameTV);
        shozaichiTV = view.findViewById(R.id.shozaichiTV);
        eki1_kantiTV = view.findViewById(R.id.eki1_kantiTV);
        bukken_type_nameTV = view.findViewById(R.id.bukken_type_nameTV);
        building_infoTV = view.findViewById(R.id.building_infoTV);
        kanseibeiTV = view.findViewById(R.id.kanseibeiTV);
        chikunenTV = view.findViewById(R.id.chikunenTV);
        /*chinryoTV = view.findViewById(R.id.chinryoTV);
        madoriTV = view.findViewById(R.id.madoriTV);*/
        //heya_kaisuTV = view.findViewById(R.id.heya_kaisuTV);

        btn_back_map_information.setOnClickListener(this);

        ll_haf = view.findViewById(R.id.ll_heyaandfriends);

        getBukkenDetail();

        addDetails();

        return view;
    }

    public void getBukkenDetail() {
        String MapBukkenURL = GlobalVar.getMap_bukken_url();
        System.out.println("URL getBukkenDetail: " + MapBukkenURL);

        StringRequest stringRequest = new StringRequest(MapBukkenURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DecimalFormat format = new DecimalFormat("#,###.##");

                try {
                    String tatemono_noMap = GlobalVar.gettatemono_no();

                    String bukken_no = null;

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");
                    for (int i = 0; i < bukken_list.length(); i++) {
                        JSONObject returnBukken = bukken_list.getJSONObject(i);
                        //Loop for Heya Array
                        JSONArray heyaList = returnBukken.getJSONArray("heya");
                        for (int j = 0; j < heyaList.length(); j++) {
                            JSONObject returnHeya = heyaList.getJSONObject(j);
                            bukken_no = returnHeya.getString("bukken_no");
                        }

                        if (bukken_no.equals(tatemono_noMap)) {
                            System.out.println("TRUTH");
                            GlobalVar.setdetailed_bukken_no(tatemono_noMap);

                            JSONObject heyaandfriends = bukken_list.getJSONObject(i);
                            System.out.println("FREINDS " + returnBukken);

                            String friend_tatemono_no = heyaandfriends.getString("tatemono_no");
                            String friend_bukken_name = heyaandfriends.getString("bukken_name");
                            String friend_shozaichi = heyaandfriends.getString("shozaichi");
                            String friend_eki1_kanji = heyaandfriends.getString("eki1_kanji");
                            String friend_bukken_type_name = heyaandfriends.getString("bukken_type_name");
                            String friend_building_info = heyaandfriends.getString("building_info");
                            String friend_kanseibi = heyaandfriends.getString("kanseibi");
                            String friend_chikunen = heyaandfriends.getString("chikunen");
                            String friend_gaikan_url = heyaandfriends.getString("gaikan_url");

                            System.out.println("FRIENDS BUKKEN LIST");
                            System.out.println("tatemono_no: " + friend_tatemono_no);
                            System.out.println("bukken_name: " + friend_bukken_name);
                            System.out.println("shozaichi: " + friend_shozaichi);
                            System.out.println("eki1_kanji: " + friend_eki1_kanji);
                            System.out.println("bukken_type_name: " + friend_bukken_type_name);
                            System.out.println("building_info: " + friend_building_info);
                            System.out.println("kanseibi: " + friend_kanseibi);
                            System.out.println("chikunen: " + friend_chikunen);
                            System.out.println("gaikan_url: " + friend_gaikan_url);

                            JSONArray heyafriend = heyaandfriends.getJSONArray("heya");

                            //Counts how many rooms are there
                            for (int j = 0; j < heyafriend.length(); j++) {
                                JSONObject returnFriends = heyafriend.getJSONObject(j);
                                String chinryo = returnFriends.getString("chinryo");
                                String madori = returnFriends.getString("madori");
                                String heya_kaisu = returnFriends.getString("heya_kaisu");
                                bukken_no = returnFriends.getString("bukken_no");
                                String friend_heya = returnFriends.getString("heya_no");

                                System.out.println("FRIENDS RETURN HEYA " + j);
                                System.out.println("how many heya_no " + j + " " + friend_heya);

                                System.out.println("chinryo: " + j + " " + chinryo);
                                System.out.println("madori: " + j + " " + madori);
                                System.out.println("heya_kaisu: " + j + " " + heya_kaisu);
                                System.out.println("bukken_no: " + j + " " + bukken_no);

                                // Set in User interface
                                bukken_nameTV.setText(friend_bukken_name);
                                shozaichiTV.setText(friend_shozaichi);
                                eki1_kantiTV.setText(friend_eki1_kanji);
                                bukken_type_nameTV.setText(friend_bukken_type_name);
                                building_infoTV.setText(friend_building_info + "階建");
                                kanseibeiTV.setText(friend_kanseibi);
                                chikunenTV.setText(friend_chikunen);

                                Picasso.get().load(DOMAIN_NAME + friend_gaikan_url).into(gaikanURLImageView);

                                //Add linear layout for each room
                                addLinearLayout(1);

                                //Add linear layout for the room details and button
                                addLinearLayout(2);

                                //add first line of text - chinryo
                                addTextView("賃料" + format.format(Double.parseDouble(chinryo)) + "万円");

                                //add second line of text - madori/heya_kaisu
                                addTextView("間取" + madori + "/" + heya_kaisu + "階");

                                //add button
                                addButton("詳細", friend_heya);

                                //add divider
                                addDivider();

                            }
                        } else {
                            System.out.println("NANI");
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

    public void addDetails() {


    }

    @Override
    public void onClick(View v) {

        Fragment selectedFragment = null;
        GlobalVar.previousScreen = "yellowAddressMapInformation";
        switch (v.getId()) {
            case R.id.btn_back_map_information:
                System.out.println("BACK TO RENT");
                GlobalVar.isFromMap = false;
                selectedFragment = new yellowAddressRentListFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
        }
    }

    /* Add dynamic linear layout */
    private void addLinearLayout(int pos) {

        if (pos == 1) {

            ll_heyaWrapper = new LinearLayout(getActivity());
            ll_heyaWrapper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll_heyaWrapper.setOrientation(LinearLayout.VERTICAL);

            ll_haf.addView(ll_heyaWrapper);

        } else if (pos == 2) {

            ll_heyaDetailsWrapper = new LinearLayout(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            ll_heyaDetailsWrapper.setLayoutParams(params);
            ll_heyaDetailsWrapper.setOrientation(LinearLayout.VERTICAL);

            ll_heyaWrapper.addView(ll_heyaDetailsWrapper);
        }

    }
    /* End of dynamic linear layout */

    /* Add dynamic TextView */
    private void addTextView(String txt) {

        textview = new TextView(this.getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 10);
        textview.setLayoutParams(layoutParams);
        textview.setText(txt);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textview.setTextColor(Color.BLACK);
        textview.setPadding(5, 5, 5, 5);
        textview.setTextColor(Color.BLACK);
        textview.setGravity(Gravity.LEFT);

        ll_heyaDetailsWrapper.addView(textview);
    }
    /* End of dynamic TextView */

    /* Add dynamic button */
    private void addButton(String lbl, final String heya_no) {

        btn = new TextView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        btn.setLayoutParams(params);
        btn.setPadding(0, 20, 0, 20);
        btn.setText(lbl);
        btn.setTextSize(24);
        btn.setBackgroundColor(Color.BLACK);
        btn.setTextColor(Color.WHITE);
        btn.setGravity(Gravity.CENTER);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                GlobalVar.setdetailed_bukken_no(heya_no);
                System.out.println(heya_no);
                Fragment selectedFragment = null;
                GlobalVar.previousScreen = "yellowAddressMapInformation";
                selectedFragment = new yellowAddressPropertyDetailFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

            }
        });

        //add button to the layout //onclick listener
        ll_heyaDetailsWrapper.addView(btn);
    }
    /* End of dynamic button */

    /* Add dynamic divider */
    public void addDivider() {

        View viewDivider = new View(getActivity());
        viewDivider.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        viewDivider.setBackgroundColor(Color.parseColor("#bbbbbb"));
        ll_heyaWrapper.addView(viewDivider);

    }
    /* End of dynamic divider */


}
