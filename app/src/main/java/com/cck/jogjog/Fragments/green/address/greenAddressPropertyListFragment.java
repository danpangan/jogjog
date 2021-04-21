package com.cck.jogjog.Fragments.green.address;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.maps.GoogleMap;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.Picasso;

import com.cck.jogjog.Fragments.green.common.greenMapProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_LIST;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_SELL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_TENANT;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class greenAddressPropertyListFragment extends Fragment implements View.OnClickListener {
    /*OBJECTS*/
    private GoogleMap mMap;

    private ScrollView sv_list;
    /*VARIABLES*/
    int CheckboxIDMain = 245000000; //CHK000000
    private LinearLayout llcont;
    private Button btn_nextpage, btn_inquire;
    int selectedcount = 0;
    boolean isinitialized = false;
    Random rand = new Random();
    public static JSONObject favoriteDetailsObject = new JSONObject();
    public static JSONArray favoritesDetailsArray = new JSONArray();
    public static ArrayList<String> favoritesArray = new ArrayList<String>();
    int CheckboxIDHeya = 492000000; //HYA000000
    int ButtonID = 280000000; //BT0000000
    int bukkencount;
    int pagecount = 1;
    int currpage = 1;

    D_cDialog CD;
    public static JSONArray historyDetailsArray = new JSONArray();


    /*START*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_green_address_property_list, container, false);

        llcont = view.findViewById(R.id.llcont);
        sv_list = view.findViewById(R.id.sv_list);
        btn_nextpage = view.findViewById(R.id.btn_nextpage);
        btn_nextpage.setOnClickListener(this);

        btn_inquire = view.findViewById(R.id.btn_inquire);
        btn_inquire.setOnClickListener(this);

        if(GlobalVar.selectedtab == 0) {
        if (GlobalVar.previousScreen.equals("greenAddressSearchSettings")) {
            //if search apply button is pressed refrech list
            if(GlobalVar.isapplylinesearchsetting) {
                BukkenList.clearbukken();
                BukkenList.bukken_list.clear();
                BukkenList.bukken_list_setsubi.clear();
                llcont.removeAllViews();
                sv_list.setVisibility(View.INVISIBLE);
                if(GlobalVar.selectedtab == 0) {
                    CD = new D_cDialog(getContext());
                    CD.pbWait.setVisibility(View.VISIBLE);
                    CD.cShow(false);
                }
                getProperties();
                GlobalVar.isapplylinesearchsetting = false;
            }
            //if search back button is pressed reload list
            else{
                llcont.removeAllViews();
                pagecount = BukkenList.bukken_list.size() / 10 + ((BukkenList.bukken_list.size() % 10 == 0) ? 0 : 1);
                for (int i = 0; i < BukkenList.bukken_list.size(); i++) {
                    addProperties(i);
                }
                currpage = pagecount;
            }
        }
        else if(GlobalVar.previousScreen.equals("greenAddressPropertyDetailFragment")) {
            llcont.removeAllViews();
            pagecount = BukkenList.bukken_list.size() / 10 + ((BukkenList.bukken_list.size() % 10 == 0) ? 0 : 1);
            for (int i = 0; i < BukkenList.bukken_list.size(); i++) {
                addProperties(i);
            }
            currpage = pagecount;
        }
        else if(GlobalVar.previousScreen.equals("greenAddressDistrictSelectionFragment")){
            llcont.removeAllViews();
            BukkenList.clearbukken();
            BukkenList.bukken_list.clear();
            BukkenList.bukken_list_setsubi.clear();
            sv_list.setVisibility(View.INVISIBLE);
            if(GlobalVar.selectedtab == 0) {
                CD = new D_cDialog(getContext());
                CD.pbWait.setVisibility(View.VISIBLE);
                CD.cShow(false);
            }
            getProperties();
        }
        else {
            llcont.removeAllViews();
            BukkenList.clearbukken();
            BukkenList.bukken_list.clear();
            BukkenList.bukken_list_setsubi.clear();
            sv_list.setVisibility(View.INVISIBLE);
            if(GlobalVar.selectedtab == 0) {
                CD = new D_cDialog(getContext());
                CD.pbWait.setVisibility(View.VISIBLE);
                CD.cShow(false);
            }
            getProperties();
        }

        loadFavorites();
        loadHistory();
        GlobalVar.previousScreen = "";
        hasnextpage();
        }
        return view;
    }


    /*DATA ACQUISITION*/
    private void getProperties() {
        String prefecture = GlobalVar.getpref_code();
        String municipalities = GlobalVar.getcode_jis();
        String districts = GlobalVar.getcode_chouaza();
        String searchurl = "&" + GlobalVar.getsearch_url();
        String url;
        if (GlobalVar.isFromMap == true) {
            //url = GlobalVar.urlFromMap + searchurl;
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_SELL +
                    "&page=" + currpage +
                    searchurl;
        } else {
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_SELL +
                    "&pref_code=" + prefecture +
                    "&code_jis=" + municipalities +
                    "&code_chouaza=" + districts +
                    "&page=" + currpage +
                    searchurl;
        }
        System.out.println("PROPERTY LIST URL " + url);
        System.out.println(prefecture);
        System.out.println(municipalities);
        System.out.println(districts);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");

                    bukkencount = bukken_list.length();
                    for (int index = 0; index < bukken_list.length(); index++) {
                        JSONObject returnBukken = bukken_list.getJSONObject(index);
                        BukkenList.bukken_list.add(new ArrayList());

                        int i = index + ((currpage-1)*10);

                        if (!returnBukken.isNull("bukken_no")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_no"));    // index:0
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bukken_category_no")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_category_no"));    // index:1
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bukken_type_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_type_name"));    // index:2
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bukken_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_name"));    // index:3
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("gaikan_url")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("gaikan_url"));    // index:4
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bukken_10m_fee")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_10m_fee"));    // index:5
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bukken_10k_fee")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_10k_fee"));    // index:6
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("park_type_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("park_type_name"));    // index:7
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("park_free")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("park_free"));    // index:8
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("park_fee")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("park_fee"));    // index:9
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bukken_kouzou_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_kouzou_name"));    // index:10
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("tochi_m2")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tochi_m2"));    // index:11
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("lot_area_m2")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("lot_area_m2"));    // index:12
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("tochi_tubo")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tochi_tubo"));    // index:13
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("lot_area_tubo")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("lot_area_tubo"));    // index:14
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("heya_madori_room_num")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("heya_madori_room_num"));    // index:15
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("madori_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("madori_name"));    // index:16
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("address1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("address1"));    // index:17
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("address2")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("address2"));    // index:18
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("address3")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("address3"));    // index:19
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("ensen_name1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("ensen_name1"));    // index:20
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("eki_name1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki_name1"));    // index:21
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("eki_min1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki_min1"));    // index:22
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bus_name1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus_name1"));    // index:23
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bus_stop_name1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus_stop_name1"));    // index:24
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bus_min1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus_min1"));    // index:25
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("bus_ride_min1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus_ride_min1"));    // index:26
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("tenpo_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_name"));    // index:27
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("tenpo_tel")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_tel"));    // index:28
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("tenpo_no1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_no"));    // index:29
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }
                        if (!returnBukken.isNull("kakaku_bukken_fee")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("kakaku_bukken_fee"));    // index:30
                        } else {
                            BukkenList.bukken_list.get(i).add("");
                        }

                        if (!returnBukken.isNull("setsubi")) {
                            BukkenList.bukken_list_setsubi.add(new ArrayList());
                            JSONArray setsubilist = returnBukken.getJSONArray("setsubi");
                            for (int j = 0; j < setsubilist.length(); j++) {
                                JSONObject returnsetsubilist = setsubilist.getJSONObject(j);
                                if (!returnsetsubilist.isNull("name")) {
                                    BukkenList.bukken_list_setsubi.get(i).add(new ArrayList());
                                    BukkenList.bukken_list_setsubi.get(i).get(j).add(returnsetsubilist.getString("name"));
                                    BukkenList.bukken_list_setsubi.get(i).get(j).add(returnsetsubilist.getString("setsubi_item_no"));
                                }
                            }
                        }
                        addProperties(index);
                    }

                    if (currpage == 1) {
                        sv_list.setVisibility(View.VISIBLE);
                        if(GlobalVar.selectedtab == 0){
                            CD.cDismiss();
                        }
                    }

                    if(bukken_list.length()==0){
                        btn_nextpage.setVisibility(View.INVISIBLE);
                        alertDialogbukkenisempty();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    CD.cDismiss();
                    alertDialogjsonisempty();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int x = 0;
                // Anything you want
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /*VIEW PLOTTING*/
    @SuppressLint("RestrictedApi")
    private void addProperties(int index) {
        DecimalFormat format = new DecimalFormat("#,###.##");

        int i = index + ((currpage - 1) * 10);
        LinearLayout.LayoutParams contentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        contentparams.setMargins(getDPscale(10), 0, getDPscale(10), getDPscale(5));

        LinearLayout ll_bukkenname = new LinearLayout(getActivity());
        ll_bukkenname.setOrientation(LinearLayout.VERTICAL);
        ll_bukkenname.setBackgroundColor(Color.parseColor("#cbebc4"));

        llcont.addView(ll_bukkenname, contentparams);

        AppCompatCheckBox chkboxpricecheck = new AppCompatCheckBox(getActivity());
        chkboxpricecheck.setId(View.generateViewId());
        chkboxpricecheck.setId(CheckboxIDMain + (i));
        chkboxpricecheck.setHighlightColor(Color.parseColor("#ffffff"));
        chkboxpricecheck.setText("この建物の物件を金チェック");
        chkboxpricecheck.setTag(BukkenList.bukken_list.get(i).get(27));
        chkboxpricecheck.setTextColor(Color.parseColor("#000000"));
        chkboxpricecheck.setHeight(130);
        chkboxpricecheck.setOnClickListener(dynamiccheckboxOnclickListener);
        ll_bukkenname.addView(chkboxpricecheck);

        String bukkentypename;
        if(BukkenList.bukken_list.get(i).get(1).equals("2"))
            bukkentypename = "事業用・収益物件" + BukkenList.bukken_list.get(i).get(2);
        else
            bukkentypename = BukkenList.bukken_list.get(i).get(2);

        TextView tv_bukken_type_name = new TextView(getActivity());
        LinearLayout.LayoutParams bukkentypenameparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bukkentypenameparams.setMargins(getDPscale(5),0,0,0);
        tv_bukken_type_name.setText(bukkentypename);
        tv_bukken_type_name.setTextColor(Color.parseColor("#000000"));
        tv_bukken_type_name.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_wht));
        tv_bukken_type_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_bukkenname.addView(tv_bukken_type_name,bukkentypenameparams);

        TextView tv_bukken_name = new TextView(getActivity());
        LinearLayout.LayoutParams bukkennameparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bukkennameparams.setMargins(getDPscale(5),getDPscale(5),0,getDPscale(5));
        tv_bukken_name.setText(BukkenList.bukken_list.get(i).get(3));
        tv_bukken_name.setTextColor(Color.parseColor("#000000"));
        tv_bukken_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_bukkenname.addView(tv_bukken_name,bukkennameparams);

        LinearLayout ll_bukkenimage = new LinearLayout(getActivity());
        ll_bukkenimage.setOrientation(LinearLayout.VERTICAL);
        ll_bukkenimage.setBackgroundColor(Color.parseColor("#ffffff"));
        llcont.addView(ll_bukkenimage, contentparams);

        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String imggaikanURL = DOMAIN_NAME + BukkenList.bukken_list.get(i).get(4);
        imgparams.setMargins(0,0,0,0);
        ImageView imggaikan = new ImageView(getActivity());
        Picasso.get().load(imggaikanURL).into(imggaikan);
        //imggaikan.setBackgroundResource(R.drawable.pic_001);
        imggaikan.setAdjustViewBounds(true);
        imggaikan.setScaleType(ImageView.ScaleType.FIT_START);

        ll_bukkenimage.addView(imggaikan,imgparams);


        LinearLayout ll_bukkendetails = new LinearLayout(getActivity());
        ll_bukkendetails.setOrientation(LinearLayout.VERTICAL);
        ll_bukkendetails.setBackgroundColor(Color.parseColor("#ffffff"));
        llcont.addView(ll_bukkendetails, contentparams);

        TextView tv_bukken_type = new TextView(getActivity());
        Drawable imgbukken_type = getContext().getResources().getDrawable(R.drawable.ic033x);
        int imgbukkentypeDrawableSize = Math.round(tv_bukken_type.getLineHeight());
        imgbukken_type.setBounds(0, 0, imgbukkentypeDrawableSize, imgbukkentypeDrawableSize);
        tv_bukken_type.setCompoundDrawables(imgbukken_type, null, null, null);
        tv_bukken_type.setGravity(Gravity.TOP);
        tv_bukken_type.setText(BukkenList.bukken_list.get(i).get(2));
        tv_bukken_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_bukken_type.setTextColor(Color.parseColor("#000000"));
        tv_bukken_type.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(tv_bukken_type);

        LinearLayout ll_chinryo = new LinearLayout(getActivity());
        ll_chinryo.setOrientation(LinearLayout.HORIZONTAL);
        ll_chinryo.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_chinryo);

        if((!(BukkenList.bukken_list.get(i).get(5).equals("")))&&(!(BukkenList.bukken_list.get(i).get(5).equals("0")))){
            TextView tv_bukken_10m_fee = new TextView(getActivity());
            tv_bukken_10m_fee.setText(BukkenList.bukken_list.get(i).get(5));
            tv_bukken_10m_fee.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            tv_bukken_10m_fee.setTextColor(Color.parseColor("#ff0000"));
            ll_chinryo.addView(tv_bukken_10m_fee);

            TextView tv_bukken_10m_fee_kanji = new TextView(getActivity());
            String disp_chinryo_tax = " 億 ";
            tv_bukken_10m_fee_kanji.setText(disp_chinryo_tax);
            tv_bukken_10m_fee_kanji.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_bukken_10m_fee_kanji.setTextColor(Color.parseColor("#000000"));
            tv_bukken_10m_fee_kanji.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_chinryo.addView(tv_bukken_10m_fee_kanji);
        }


        TextView bukken_10k_fee = new TextView(getActivity());
        if(!BukkenList.bukken_list.get(i).get(6).equals("0")) {
            bukken_10k_fee.setText(format.format(Double.parseDouble(BukkenList.bukken_list.get(i).get(6))));
        }
        else{
            bukken_10k_fee.setText("");
        }
        bukken_10k_fee.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        bukken_10k_fee.setTextColor(Color.parseColor("#ff0000"));
        ll_chinryo.addView(bukken_10k_fee);

        TextView bukken_10k_fee_kanji = new TextView(getActivity());
        String disp_chinryo_tax;
        if(!BukkenList.bukken_list.get(i).get(6).equals("0")) {
            disp_chinryo_tax = " 万円";
        }
        else{
            disp_chinryo_tax = " 円";
        }

        bukken_10k_fee_kanji.setText(disp_chinryo_tax);
        bukken_10k_fee_kanji.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        bukken_10k_fee_kanji.setTextColor(Color.parseColor("#000000"));
        bukken_10k_fee_kanji.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_chinryo.addView(bukken_10k_fee_kanji);


        if((!(BukkenList.bukken_list.get(i).get(7).equals(""))) ||
                (!(BukkenList.bukken_list.get(i).get(8).equals(""))) ||
                (!(BukkenList.bukken_list.get(i).get(9).equals("")))){

            String parking = "駐車場: ";

            if ((!(BukkenList.bukken_list.get(i).get(7).equals("")))&&(!(BukkenList.bukken_list.get(i).get(7).equals("無し"))))
                parking = parking + BukkenList.bukken_list.get(i).get(7);
            if ((!(BukkenList.bukken_list.get(i).get(8).equals("")))&&(!(BukkenList.bukken_list.get(i).get(8).equals("0"))))
                parking = parking + " (" + BukkenList.bukken_list.get(i).get(8) + "台駐車可能)";
            if ((!(BukkenList.bukken_list.get(i).get(9).equals("")))&&(!(BukkenList.bukken_list.get(i).get(9).equals("0"))))
                parking = parking + BukkenList.bukken_list.get(i).get(9) + "円/月";

            TextView tv_parking = new TextView(getActivity());
            tv_parking.setText(parking);
            tv_parking.setWidth(getDPscale(125));
            tv_parking.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_parking.setTextColor(Color.parseColor("#000000"));
            ll_bukkendetails.addView(tv_parking);
        }

        TextView tv_bukken_kouzou_name = new TextView(getActivity());
        Drawable drw_bukken_kouzou_name = getContext().getResources().getDrawable(R.drawable.ic033x);
        int bukken_kouzou_nameDrawableSize = Math.round(tv_bukken_type.getLineHeight());
        drw_bukken_kouzou_name.setBounds(0, 0, bukken_kouzou_nameDrawableSize, bukken_kouzou_nameDrawableSize);
        tv_bukken_kouzou_name.setCompoundDrawables(imgbukken_type, null, null, null);
        tv_bukken_kouzou_name.setText(BukkenList.bukken_list.get(i).get(10));
        tv_bukken_kouzou_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_bukken_kouzou_name.setTextColor(Color.parseColor("#000000"));
        tv_bukken_kouzou_name.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(tv_bukken_kouzou_name);

        if((!(BukkenList.bukken_list.get(i).get(11).equals(""))) && (!(BukkenList.bukken_list.get(i).get(12).equals("")))) {

            LinearLayout ll_tochi = new LinearLayout(getActivity());
            ll_tochi.setOrientation(LinearLayout.HORIZONTAL);
            ll_tochi.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_bukkendetails.addView(ll_tochi);

            TextView tv_tochi_kanji = new TextView(getActivity());
            tv_tochi_kanji.setText("土地面積 : ");
            tv_tochi_kanji.setWidth(getDPscale(85));
            tv_tochi_kanji.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_tochi_kanji.setTextColor(Color.parseColor("#000000"));
            ll_tochi.addView(tv_tochi_kanji);

            TextView tv_tochi = new TextView(getActivity());
            String tochi = BukkenList.bukken_list.get(i).get(11) + "m\u00B2(" + BukkenList.bukken_list.get(i).get(13) + "坪)";
            tv_tochi.setText(tochi);
            tv_tochi.setMaxLines(5);
            tv_tochi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_tochi.setTextColor(Color.parseColor("#000000"));
            tv_tochi.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_tochi.addView(tv_tochi);

            LinearLayout ll_lot = new LinearLayout(getActivity());
            ll_lot.setOrientation(LinearLayout.HORIZONTAL);
            ll_lot.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_bukkendetails.addView(ll_lot);

            TextView tv_lot_kanji = new TextView(getActivity());
            tv_lot_kanji.setText("建物面積 : ");
            tv_lot_kanji.setWidth(getDPscale(85));
            tv_lot_kanji.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_lot_kanji.setTextColor(Color.parseColor("#000000"));
            ll_lot.addView(tv_lot_kanji);

            TextView tv_lot = new TextView(getActivity());
            String lot = BukkenList.bukken_list.get(i).get(12) + "m\u00B2(" + BukkenList.bukken_list.get(i).get(14) + "坪)";
            tv_lot.setText(lot);
            tv_lot.setMaxLines(5);
            tv_lot.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_lot.setTextColor(Color.parseColor("#000000"));
            tv_lot.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_lot.addView(tv_lot);
        }

        if(!(BukkenList.bukken_list.get(i).get(15).equals(""))) {
            String madori;
            if(!BukkenList.bukken_list.get(i).get(15).equals("0")) {
                madori = BukkenList.bukken_list.get(i).get(15)+BukkenList.bukken_list.get(i).get(16);
            }
            else{
                madori = BukkenList.bukken_list.get(i).get(16);
            }            TextView tv_madori = new TextView(getActivity());
            tv_madori.setText(madori);
            tv_madori.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            tv_madori.setTextColor(Color.parseColor("#ff0000"));
            ll_bukkendetails.addView(tv_madori);
        }

        LinearLayout ll_location_stations_border = new LinearLayout(getActivity());
        ll_location_stations_border.setOrientation(LinearLayout.VERTICAL);
        ll_location_stations_border.setBackgroundColor(Color.parseColor("#dddddd"));
        ll_bukkendetails.addView(ll_location_stations_border);

        LinearLayout.LayoutParams location_stationsparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        location_stationsparams.setMargins(0, getDPscale(1),0,getDPscale(1));

        LinearLayout ll_location_stations = new LinearLayout(getActivity());
        ll_location_stations.setOrientation(LinearLayout.VERTICAL);
        ll_location_stations.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_location_stations.setLayoutParams(location_stationsparams);
        ll_location_stations_border.addView(ll_location_stations);

        LinearLayout ll_address = new LinearLayout(getActivity());
        ll_address.setOrientation(LinearLayout.HORIZONTAL);
        ll_location_stations.addView(ll_address);

        TextView tv_addressicon = new TextView(getActivity());
        Drawable drw_address = getContext().getResources().getDrawable(R.drawable.ic023x);
        int addresspixelDrawableSize = Math.round(tv_addressicon.getLineHeight());
        drw_address.setBounds(0, 0, addresspixelDrawableSize, addresspixelDrawableSize);
        tv_addressicon.setCompoundDrawables(drw_address, null, null, null);
        tv_addressicon.setTextColor(Color.parseColor("#000000"));
        tv_addressicon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_address.addView(tv_addressicon);

        String address = BukkenList.bukken_list.get(i).get(17) + BukkenList.bukken_list.get(i).get(18) + BukkenList.bukken_list.get(i).get(19);
        TextView tv_address = new TextView(getActivity());
        tv_address.setText(address);
        tv_address.setTextColor(Color.parseColor("#000000"));
        tv_address.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_address.addView(tv_address);

        LinearLayout ll_eki = new LinearLayout(getActivity());
        ll_eki.setOrientation(LinearLayout.HORIZONTAL);
        ll_location_stations.addView(ll_eki);

        TextView tv_ek_iconi = new TextView(getActivity());
        Drawable imgeki = getContext().getResources().getDrawable(R.drawable.ic043x);
        int imgekipixelDrawableSize = Math.round(tv_ek_iconi.getLineHeight());
        imgeki.setBounds(0, 0, imgekipixelDrawableSize, imgekipixelDrawableSize);
        tv_ek_iconi.setCompoundDrawables(imgeki, null, null, null);
        tv_ek_iconi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_eki.addView(tv_ek_iconi);

        String eki = BukkenList.bukken_list.get(i).get(20) + BukkenList.bukken_list.get(i).get(21) + "徒歩" + BukkenList.bukken_list.get(i).get(22) + "分" +
                BukkenList.bukken_list.get(i).get(23) + BukkenList.bukken_list.get(i).get(24) + "徒歩" + BukkenList.bukken_list.get(i).get(25) + "分" + BukkenList.bukken_list.get(i).get(26);

        TextView tv_eki = new TextView(getActivity());
        tv_eki.setText(eki);
        tv_eki.setMaxLines(10);
        tv_eki.setTextColor(Color.parseColor("#000000"));
        tv_eki.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_eki.addView(tv_eki);

        LinearLayout.LayoutParams flsetsubiparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        flsetsubiparams.setMargins(0, getDPscale(5),0,getDPscale(5));
        FlowLayout fl_setsubi = new FlowLayout(getActivity());
        fl_setsubi.setChildSpacing(getDPscale(5));
        fl_setsubi.setChildSpacingForLastRow(getDPscale(5));
        fl_setsubi.setRowSpacing(getDPscale(5));
        fl_setsubi.setLayoutParams(flsetsubiparams);
        ll_bukkendetails.addView(fl_setsubi);

        if(BukkenList.bukken_list_setsubi.get(i).size()>0) {
            for (int j = 0; j < BukkenList.bukken_list_setsubi.get(i).size(); j++) {
                LinearLayout.LayoutParams setsubi2params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView tv_setsubi = new TextView(getActivity());
                tv_setsubi.setPadding(5, 0, 5, 0);
                tv_setsubi.setText(BukkenList.bukken_list_setsubi.get(i).get(j).get(0));
                tv_setsubi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                tv_setsubi.setTextColor(Color.parseColor("#ff000000"));
                tv_setsubi.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_grn));
                fl_setsubi.addView(tv_setsubi, setsubi2params);
            }
        }
        LinearLayout ll_tenpo_name = new LinearLayout(getActivity());
        ll_tenpo_name.setOrientation(LinearLayout.HORIZONTAL);
        ll_bukkendetails.addView(ll_tenpo_name);

        TextView tv_tenpo_name_icon = new TextView(getActivity());
        Drawable imgtenpo_name = getContext().getResources().getDrawable(R.drawable.topbar_title_left3x);
        int imgtenpo_namepixelDrawableSizeH = Math.round(tv_bukken_type.getLineHeight());
        int imgtenpo_namepixelDrawableSizeW = (int) Math.round((double) imgtenpo_name.getIntrinsicWidth() * ((double) tv_bukken_type.getLineHeight() / (double) imgtenpo_name.getIntrinsicHeight()));
        imgtenpo_name.setBounds(0, 0, imgtenpo_namepixelDrawableSizeW, imgtenpo_namepixelDrawableSizeH);
        tv_tenpo_name_icon.setCompoundDrawables(imgtenpo_name, null, null, null);
        tv_tenpo_name_icon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_tenpo_name.addView(tv_tenpo_name_icon);

        TextView tv_tenpo_name = new TextView(getActivity());
        tv_tenpo_name.setText(BukkenList.bukken_list.get(i).get(27));
        tv_tenpo_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_tenpo_name.setTextColor(Color.parseColor("#000000"));
        tv_tenpo_name.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_tenpo_name.addView(tv_tenpo_name);

        LinearLayout ll_tenpo_tel = new LinearLayout(getActivity());
        ll_tenpo_tel.setOrientation(LinearLayout.HORIZONTAL);
        ll_tenpo_tel.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_tenpo_tel);

        TextView tv_tel = new TextView(getActivity());
        tv_tel.setText("TEL.");
        tv_tel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_tel.setTextColor(Color.parseColor("#000000"));
        tv_tel.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_tenpo_tel.addView(tv_tel);

        TextView tv_tenpo_tel = new TextView(getActivity());
        tv_tenpo_tel.setText(BukkenList.bukken_list.get(i).get(28));
        tv_tenpo_tel.setTag(BukkenList.bukken_list.get(i).get(28));
        tv_tenpo_tel.setOnClickListener(telephoneOnclickListener);
        tv_tenpo_tel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        tv_tenpo_tel.setTextColor(Color.parseColor("#ff0000"));
        ll_tenpo_tel.addView(tv_tenpo_tel);

        TableLayout tl_buttons = new TableLayout(getActivity());

        TableRow.LayoutParams buttonsuparams = new TableRow.LayoutParams(0,getDPscale(50), 1f);
        buttonsuparams.setMargins(getDPscale(5),getDPscale(5),getDPscale(5),getDPscale(5));

        TableRow tr_buttons = new TableRow(getActivity());

        AppCompatButton btn_bukkenjouhou = new AppCompatButton(getActivity());
        btn_bukkenjouhou.setText("物件詳細");
        btn_bukkenjouhou.setId(ButtonID + 5000000 + ((index + ((currpage-1)*10))* 100));
        btn_bukkenjouhou.setTag(BukkenList.bukken_list.get(i).get(0));
        btn_bukkenjouhou.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        btn_bukkenjouhou.setGravity(Gravity.CENTER);
        btn_bukkenjouhou.setTextColor(Color.parseColor("#ffffff"));
        btn_bukkenjouhou.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blk));
        btn_bukkenjouhou.setOnClickListener(dynamicbuttonOnclickListener);
        tr_buttons.addView(btn_bukkenjouhou,buttonsuparams);

        AppCompatButton btn_okiniiri = new AppCompatButton(getActivity());
        btn_okiniiri.setText("お気に入りに追加");
        btn_bukkenjouhou.setTag(BukkenList.bukken_list.get(i).get(0));
        btn_okiniiri.setId(ButtonID + 3000000 + ((index + ((currpage-1)*10))* 100));
        btn_okiniiri.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        btn_okiniiri.setGravity(Gravity.CENTER);
        btn_okiniiri.setTextColor(Color.parseColor("#ffffff"));
        btn_okiniiri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blk));
        btn_okiniiri.setOnClickListener(dynamicbuttonOnclickListener);
        tr_buttons.addView(btn_okiniiri,buttonsuparams);

        tl_buttons.addView(tr_buttons,0);

        ll_bukkendetails.addView(tl_buttons);

        if(isfavorite(BukkenList.bukken_list.get(i).get(0))){
            btn_okiniiri.setEnabled(false);
            btn_okiniiri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
            btn_okiniiri.setTextColor(Color.parseColor("#aaaaaa"));
            btn_okiniiri.setText("登録済み");
        }

        if (bukkencount - 1 > index) {
            LinearLayout.LayoutParams spacerparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
            spacerparams.setMargins(20, 0, 20, getDPscale(10));
            LinearLayout ll_spacer = new LinearLayout(getActivity());
            ll_spacer.setOrientation(LinearLayout.VERTICAL);
            ll_spacer.setBackgroundColor(Color.parseColor("#dddddd"));
            llcont.addView(ll_spacer, spacerparams);
        }

    }

    /*MEDIA PROCESSING*/
    private int getDPscale(int size) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) ((double) size * scale + 0.5f));
    }

    /*AUXILIARY PROGRAMS*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            GlobalVar.selectedtab = 0;
            if(GlobalVar.previousScreen.equals("")) {
                if (GlobalVar.isFromMap == true) {
                    BukkenList.clearbukken();
                    BukkenList.bukken_list.clear();
                    BukkenList.bukken_list_setsubi.clear();
                    llcont.removeAllViews();
                    sv_list.setVisibility(View.INVISIBLE);
                    if(GlobalVar.selectedtab == 0) {
                        CD = new D_cDialog(getContext());
                        CD.pbWait.setVisibility(View.VISIBLE);
                        CD.cShow(false);
                    }
                    getProperties();
                    hasnextpage();
                }
            }
        }
    }

    private void hasnextpage() {
        String prefecture = GlobalVar.getpref_code();
        String municipalities = GlobalVar.getcode_jis();
        String districts = GlobalVar.getcode_chouaza();
        String searchurl = "&" + GlobalVar.getsearch_url();
        String url;
        if (GlobalVar.isFromMap == true) {
            url = GlobalVar.urlFromMap + searchurl;
        } else {
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_SELL +
                    "&pref_code=" + prefecture +
                    "&code_jis=" + municipalities +
                    "&code_chouaza=" + districts +
                    "&page=" + (currpage + 1) +
                    searchurl;
        }

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");

                    if (bukken_list.length() > 0) {
                        pagecount++;
                        btn_nextpage.setEnabled(true);
                    } else {
                        btn_nextpage.setEnabled(false);
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
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public boolean isfavorite(String heyano) {
        boolean result = false;

        Iterator<String> iterator = favoritesArray.iterator();
        while (iterator.hasNext()) {
            if (heyano.equals(iterator.next())) {
                result = true;
                break;
            }
        }

        return result;
    }

    public void addToFavorites() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedGreenFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favoritesJSON = favoritesDetailsArray.toString();

        editor.putString("favorite list", favoritesJSON);
        editor.apply();
        System.out.println("List of favorites: " + sharedPreferences.getString("favorite list", ""));
    }

    public void loadFavorites() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedGreenFavorites", MODE_PRIVATE);
        String favoriteJSON = sharedPreferences.getString("favorite list", null);

        if (favoriteJSON != null) {

            try {
                favoritesArray = new ArrayList<String>();
                favoritesDetailsArray = new JSONArray(favoriteJSON);
                JSONObject faveDetailsObject = new JSONObject();
                for (int i = 0; i < favoritesDetailsArray.length(); i++) {
                    faveDetailsObject = favoritesDetailsArray.getJSONObject(i);
                    favoritesArray.add(faveDetailsObject.getString("bukken_no"));
                }

            } catch (JSONException e) {

            }
            System.out.println("Loaded From shared preference property list: " + favoritesDetailsArray);
            System.out.println("favorites array list:" + favoritesArray);
        }
    }

    /*LISTENERS*/
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        View view = this.getView();
        Fragment selectedFragment = null;
        AppCompatCheckBox chkbox;
        AppCompatButton btn;

        switch (v.getId()) {
            case R.id.btn_nextpage:
                if (currpage < pagecount) {
                    currpage++;

                    LinearLayout.LayoutParams spacerparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
                    spacerparams.setMargins(20,0,20,getDPscale(10));
                    LinearLayout ll_spacer = new LinearLayout(getActivity());
                    ll_spacer.setOrientation(LinearLayout.VERTICAL);
                    ll_spacer.setBackgroundColor(Color.parseColor("#dddddd"));
                    llcont.addView(ll_spacer,spacerparams);

                    getProperties();
                }
                hasnextpage();
                break;
            default:
                break;
        }
    }

    View.OnClickListener telephoneOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            alertDialogcalltenpotel(((TextView) v).getTag().toString());
        }
    };

    View.OnClickListener dynamicbuttonOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            MainActivity view = ((MainActivity) getActivity());
            Fragment selectedFragment = null;
            AppCompatButton btn;

            if (v.getId() >= ButtonID + 5000000) {
                int id = v.getId() - (ButtonID + 5000000);
                int indexbukken = id / 100;
                addToFavoritesAndHistory(indexbukken, 2);
                GlobalVar.setdetailed_bukken_no(v.getTag().toString());
                selectedFragment = new greenAddressPropertyDetailFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

            } else if (v.getId() >= ButtonID + 3000000) {
                int id = v.getId() - (ButtonID + 3000000);
                int indexbukken = id / 100;

                System.out.println("IDIDID: " + id);
                addToFavoritesAndHistory(indexbukken, 1);
                addToFavorites();
                // message
                alertDialogAddtoFavorites();

                //set button to disabled
                btn = view.findViewById(ButtonID + 3000000 + ((indexbukken + ((currpage - 1) * 10)) * 100));
                btn.setEnabled(false);
                btn.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
                btn.setTextColor(Color.parseColor("#aaaaaa"));
                btn.setText("登録済み");

            }
        }
    };
    View.OnClickListener dynamiccheckboxOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            MainActivity view = ((MainActivity) getActivity());

            if (((CheckBox) v).isChecked()) {
                GlobalVar.addheya_no(v.getTag().toString());
                selectedcount++;
                btn_inquire.setVisibility(View.VISIBLE);
            } else if (!((CheckBox) v).isChecked()) {
                GlobalVar.removeheya_no(v.getTag().toString());
                selectedcount--;
                if (selectedcount == 0) {
                    btn_inquire.setVisibility(View.GONE);
                }
            } else {
            }
        }
    };
    /*DIALOGS*/
    private void alertDialogAddtoFavorites() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("お気に入りしました。");
        builder.setCancelable(true);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public void openMapDialog() {
        DialogFragment mapFragment = greenMapProperty.newInstance();
        mapFragment.show(getFragmentManager(), "dialog");
    }

    private void alertDialogcalltenpotel(final String telephone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "でんわばんご " + telephone + " へお問い合わせをします。";
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri u = Uri.parse("tel:" + telephone);

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try
                {
                    // Launch the Phone app's dialer with a phone
                    // number to dial a call.
                    startActivity(i);
                }
                catch (SecurityException s)
                {
                    // show() method display the toast with
                    // exception message.
                }
            }
        });
        builder.setNegativeButton("いいえ`", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void alertDialogbukkenisempty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "検索条件に一致する物件が存在しませんでした。\n条件を変更して再度検索をお願い致します。";
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void alertDialogjsonisempty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "該当するデータが存在しません。";
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    public void addToHistory() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedGreenHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String historyJSON = historyDetailsArray.toString();

        editor.putString("history list", historyJSON);
        editor.apply();
        System.out.println("List of history: " + sharedPreferences.getString("history list", ""));
    }

    public void loadHistory() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedGreenHistory", MODE_PRIVATE);
        String historyJSON = sharedPreferences.getString("history list", null);

        if(historyJSON != null) {

            try { historyDetailsArray = new JSONArray(historyJSON); }
            catch (JSONException e) { }

        }
    }

    public void addToFavoritesAndHistory(int indexbukken, int destination){

        try {
            favoriteDetailsObject = new JSONObject();
            int randId = rand.nextInt(1000000);

            //System.out.println("index 43: " + BukkenList.bukken_list.get(indexbukken).get(37));

            favoriteDetailsObject.put("tenpo_no1", BukkenList.bukken_list.get(indexbukken).get(29));
            favoriteDetailsObject.put("address1", BukkenList.bukken_list.get(indexbukken).get(17));
            favoriteDetailsObject.put("bukkenName", BukkenList.bukken_list.get(indexbukken).get(3));
            favoriteDetailsObject.put("bukken_type_name", BukkenList.bukken_list.get(indexbukken).get(2));
            favoriteDetailsObject.put("ensen_name", BukkenList.bukken_list.get(indexbukken).get(20));
            favoriteDetailsObject.put("eki_name", BukkenList.bukken_list.get(indexbukken).get(21));
            favoriteDetailsObject.put("eki_min1", BukkenList.bukken_list.get(indexbukken).get(22));
            favoriteDetailsObject.put("kakaku_bukken_fee", BukkenList.bukken_list.get(indexbukken).get(30));
            favoriteDetailsObject.put("bukken_no", BukkenList.bukken_list.get(indexbukken).get(0));
            favoriteDetailsObject.put("random_id", randId);

            favoritesArray.add(BukkenList.bukken_list.get(indexbukken).get(0));

            if(destination == 1) favoritesDetailsArray.put(favoriteDetailsObject);
            if(destination == 2) {
                checkHistory(BukkenList.bukken_list.get(indexbukken).get(0), favoriteDetailsObject);
                historyDetailsArray.put(favoriteDetailsObject);
            }

        } catch (JSONException e) {
            System.out.println("error: " + e);
        }
    }

    private void checkHistory(String bukken_no, JSONObject faveDetailsObject) {

        boolean isExisting = false;
        try {
            JSONObject historyDetailsObject = new JSONObject();

            for(int i=0; i<historyDetailsArray.length(); i++) {
                historyDetailsObject = historyDetailsArray.getJSONObject(i);
                if(historyDetailsObject.getString("bukken_no").equals(bukken_no)) {
                    isExisting = true;
                }
            }

            if(!isExisting) {
                historyDetailsArray.put(faveDetailsObject);
                addToHistory();
            }

        } catch(JSONException e) {
            System.out.println("JSON error: " + e);
        }
    }
}
