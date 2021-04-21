package com.cck.jogjog.Fragments.blue.line;

import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;

import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_DETAIL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_TENANT;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class blueLineMapInformation extends Fragment implements View.OnClickListener {

    TextView bukken_nameTV, shozaichiTV, eki1_kanjiTV, bukken_type_nameTV, heya_tuboTV, disp_shikikinTV;
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
        View view = inflater.inflate(R.layout.fragment_blue_map_information, container, false);

        // Do all the stuff to initialize your custom view
        btn_back_map_information = view.findViewById(R.id.btn_back_map_information);
        btn_bukken_details = view.findViewById(R.id.btn_bukken_details);
        gaikanURLImageView = view.findViewById(R.id.gaikanURLImageView);

        bukken_nameTV = view.findViewById(R.id.bukken_nameTV);
        shozaichiTV = view.findViewById(R.id.shozaichiTV);
        eki1_kanjiTV = view.findViewById(R.id.eki1_kanjiTV);
        bukken_type_nameTV = view.findViewById(R.id.bukken_type_nameTV);
        heya_tuboTV = view.findViewById(R.id.heya_tuboTV);
        disp_shikikinTV = view.findViewById(R.id.disp_shikikinTV);

        btn_back_map_information.setOnClickListener(this);
        btn_bukken_details.setOnClickListener(this);

        ll_haf = view.findViewById(R.id.ll_heyaandfriends);

        getBukkenDetail();

        return view;
    }

    public void getBukkenDetail() {

        String bukken_noMap = GlobalVar.getdetailed_bukken_no();
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_DETAIL + API_PATH_TYPE_TENANT + "&bukken_no=" + bukken_noMap;
        System.out.println("URL getBukkenDetail: " + url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String bukken_name = data.getString("bukken_name");
                    String shozaichi = data.getString("shozaichi");
                    String eki1_kanji = data.getString("eki1_kanji");
                    String bukken_type_name = data.getString("bukken_type_name");
                    String heya_tubo = data.getString("heya_tubo");
                    String heya_m2 = data.getString("heya_m2");
                    String disp_shikikin = data.getString("disp_shikikin");
                    String floor_info = data.getString("floor");
                    String gaikan_url = data.getString("gaikan_url1");

                    // Set in User interface
                    bukken_nameTV.setText(bukken_name);
                    shozaichiTV.setText(shozaichi);
                    eki1_kanjiTV.setText(eki1_kanji);
                    bukken_type_nameTV.setText(bukken_type_name);

                    heya_tuboTV.setText(heya_tubo + "坪" + "(" + heya_m2 + "m\u00B2" + ")");
                    disp_shikikinTV.setText(disp_shikikin + "/" + floor_info + "階");

                    Picasso.get().load(DOMAIN_NAME + gaikan_url).into(gaikanURLImageView);

                    btn_bukken_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {

                            Fragment selectedFragment = null;
                            GlobalVar.previousScreen = "blueLineMapInformation";
                            selectedFragment = new blueLinePropertyDetailFragment();
                            ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

                        }
                    });


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
        GlobalVar.previousScreen = "blueLineMapInformation";
        switch (v.getId()) {
            case R.id.btn_back_map_information:
                System.out.println("BACK TO RENT");
                GlobalVar.isFromMap = false;
                selectedFragment = new blueLineRentListFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
        }
    }


}
