package com.cck.jogjog.Fragments.yellow.line;

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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Fragments.yellow.common.yellowMapPropertyListTEMP;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_LIST;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class yellowLinePropertyListFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    /*OBJECTS*/
    private GoogleMap mMap;
    private ScrollView sv_list;

    /*VARIABLES*/
    int CheckboxIDMain = 245000000; //CHK000000
    LinearLayout llcont;
    Button btn_nextpage, btn_inquire, btn_back_map_property;
    int selectedcount = 0;

    Random rand = new Random();
    public static JSONObject favoriteDetailsObject = new JSONObject();
    public static JSONArray favoritesDetailsArray = new JSONArray();
    public static ArrayList<String> favoritesArray = new ArrayList<String>();
    int CheckboxIDHeya = 492000000; //HYA000000
    int ButtonID = 280000000; //BT0000000
    int bukkencount;
    int pagecount = 0;
    int currpage = 1;
    D_cDialog CD;
    public static JSONArray historyDetailsArray = new JSONArray();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_line_property_list, container, false);
        llcont = view.findViewById(R.id.llcont);
        sv_list = view.findViewById(R.id.sv_list);
        btn_nextpage = view.findViewById(R.id.btn_nextpage);
        btn_nextpage.setOnClickListener(this);
        btn_inquire = view.findViewById(R.id.btn_inquire);
        btn_inquire.setOnClickListener(this);
        if(GlobalVar.selectedtab == 0) {
            if (GlobalVar.previousScreen.equals("yellowLineSearchSettings")) {
                //if search apply button is pressed refrech list
                if (GlobalVar.isapplylinesearchsetting) {
                    BukkenList.clearbukken();
                    llcont.removeAllViews();
                    sv_list.setVisibility(View.INVISIBLE);
                    if (GlobalVar.selectedtab == 0) {
                        CD = new D_cDialog(getContext());
                        CD.pbWait.setVisibility(View.VISIBLE);
                        CD.cShow(false);
                    }
                    getProperties();
                    GlobalVar.isapplylinesearchsetting = false;
                }
                //if search back button is pressed reload list
                else {
                    llcont.removeAllViews();
                    pagecount = BukkenList.tatemono_no.size() / 10 + ((BukkenList.tatemono_no.size() % 10 == 0) ? 0 : 1);
                    for (int i = 0; i < BukkenList.tatemono_no.size(); i++) {
                        addProperties(i);
                    }
                    currpage = pagecount;
                }
            } else if (GlobalVar.previousScreen.equals("yellowLinePropertyDetailFragment")) {
                llcont.removeAllViews();
                pagecount = BukkenList.tatemono_no.size() / 10 + ((BukkenList.tatemono_no.size() % 10 == 0) ? 0 : 1);
                for (int i = 0; i < BukkenList.tatemono_no.size(); i++) {
                    addProperties(i);
                }
                currpage = pagecount;
            } else if (GlobalVar.previousScreen.equals("yellowLineStationSelectionFragment")) {
                llcont.removeAllViews();
                BukkenList.clearbukken();
                llcont.removeAllViews();
                sv_list.setVisibility(View.INVISIBLE);
                if (GlobalVar.selectedtab == 0) {
                    CD = new D_cDialog(getContext());
                    CD.pbWait.setVisibility(View.VISIBLE);
                    CD.cShow(false);
                }
                getProperties();
            } else {
                llcont.removeAllViews();
                BukkenList.clearbukken();
                llcont.removeAllViews();
                sv_list.setVisibility(View.INVISIBLE);
                if (GlobalVar.selectedtab == 0) {
                    CD = new D_cDialog(getContext());
                    CD.pbWait.setVisibility(View.VISIBLE);
                    CD.cShow(false);
                }
                getProperties();
            }

            loadHistory();
            GlobalVar.previousScreen = "";
            hasnextpage();
        }
        return view;
    }

    private void getProperties() {
        String code_traffic = GlobalVar.getcode_traffic();
        String code_traffic_company = GlobalVar.getcode_traffic_company();
        String code_route = GlobalVar.getcode_route();
        String code_station = GlobalVar.getcode_station();
        String searchurl = "&" + GlobalVar.getsearch_url() + "&";
        String url;

        System.out.println("SEARCH URL + " + searchurl);
        String searchChinryo1 = searchurl;
        String searchChinryo2 = searchurl;
        String searchMenseki1 = searchurl;
        String searchMenseki2 = searchurl;

        //type 4
        Pattern pattern_chinryo1 = Pattern.compile("chinryo1=(.*?)&");
        Pattern pattern_chinryo2 = Pattern.compile("chinryo2=(.*?)&");
        Pattern pattern_menseki1 = Pattern.compile("menseki1=(.*?)&");
        Pattern pattern_menseki2 = Pattern.compile("menseki2=(.*?)&");

        Matcher matcher_chinryo1 = pattern_chinryo1.matcher(searchChinryo1);
        Matcher matcher_chinryo2 = pattern_chinryo2.matcher(searchChinryo2);
        Matcher matcher_menseki1 = pattern_menseki1.matcher(searchMenseki1);
        Matcher matcher_menseki2 = pattern_menseki2.matcher(searchMenseki2);

        if (matcher_chinryo1.find()) {
            System.out.println("matcher_chinryo1 " + matcher_chinryo1.group(1));
            GlobalVar.setchinryo1_search(matcher_chinryo1.group(1));
        }

        if (matcher_chinryo2.find()) {
            System.out.println("matcher_chinryo2 " + matcher_chinryo2.group(1));
            GlobalVar.setchinryo2_search(matcher_chinryo2.group(1));
        }

        if (matcher_menseki1.find()) {
            System.out.println("matcher_menseki1 " + matcher_menseki1.group(1));
            GlobalVar.setmenseki1_search(matcher_menseki1.group(1));
        }

        if (matcher_menseki2.find()) {
            System.out.println("matcher_menseki2 " + matcher_menseki2.group(1));
            GlobalVar.setmenseki2_search(matcher_menseki2.group(1));
        }

        if (GlobalVar.isFromMap == true) {
            //url = GlobalVar.urlFromMap + searchurl;
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_CHINTAI +
                    "&page=" + currpage +
                    searchurl;
        } else {
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_CHINTAI +
                    "&code_traffic=" + code_traffic +
                    "&code_traffic_company=" + code_traffic_company +
                    "&code_route=" + code_route +
                    "&code_station=" + code_station +
                    "&page=" + currpage +
                    searchurl;
        }


        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray bukken_list = data.getJSONArray("bukken_list");
                    bukkencount = bukken_list.length();
                    for (int i = 0; i < bukken_list.length(); i++) {
                        JSONObject returnBukken = bukken_list.getJSONObject(i);

                        BukkenList.tatemono_no.add(returnBukken.getString("tatemono_no"));
                        BukkenList.bukken_name.add(returnBukken.getString("bukken_name"));
                        BukkenList.is_disp_bukken_name.add(returnBukken.getString("is_disp_bukken_name"));
                        BukkenList.shubetsu.add(returnBukken.getString("shubetsu"));
                        BukkenList.eki1_kanji.add(returnBukken.getString("eki1_kanji"));
                        BukkenList.eki1_distance.add(returnBukken.getString("eki1_distance"));
                        BukkenList.bus1_kanji.add(returnBukken.getString("bus1_kanji"));
                        BukkenList.bus1_distance.add(returnBukken.getString("bus1_distance"));
                        BukkenList.eki2_kanji.add(returnBukken.getString("eki2_kanji"));
                        BukkenList.eki2_distance.add(returnBukken.getString("eki2_distance"));
                        BukkenList.bus2_kanji.add(returnBukken.getString("bus2_kanji"));
                        BukkenList.bus2_distance.add(returnBukken.getString("bus2_distance"));
                        BukkenList.eki3_kanji.add(returnBukken.getString("eki3_kanji"));
                        BukkenList.eki3_distance.add(returnBukken.getString("eki3_distance"));
                        BukkenList.bus3_kanji.add(returnBukken.getString("bus3_kanji"));
                        BukkenList.bus3_distance.add(returnBukken.getString("bus3_distance"));
                        BukkenList.shozaichi.add(returnBukken.getString("shozaichi"));
                        BukkenList.zipcode.add(returnBukken.getString("zipcode"));
                        BukkenList.kouzou.add(returnBukken.getString("kouzou"));
                        BukkenList.building_info.add(returnBukken.getString("building_info"));
                        BukkenList.kanseibi.add(returnBukken.getString("kanseibi"));
                        BukkenList.chikunen.add(returnBukken.getString("chikunen"));
                        BukkenList.ido_hokui.add(returnBukken.getString("ido_hokui")); //latitude
                        BukkenList.keido_tokei.add(returnBukken.getString("keido_tokei")); //longitude
                        BukkenList.tenpo_no1.add(returnBukken.getString("tenpo_no1"));
                        BukkenList.tenpo_no2.add(returnBukken.getString("tenpo_no2"));
                        BukkenList.tenpo_name.add(returnBukken.getString("tenpo_name"));
                        BukkenList.tenpo_tel.add(returnBukken.getString("tenpo_tel"));
                        BukkenList.line_id.add(returnBukken.getString("line_id"));
                        BukkenList.tenpo_name2.add(returnBukken.getString("tenpo_name2"));
                        BukkenList.tenpo_tel2.add(returnBukken.getString("tenpo_tel2"));
                        BukkenList.gaikan_url.add(returnBukken.getString("gaikan_url"));
                        BukkenList.bukken_type_name.add(returnBukken.getString("bukken_type_name"));
                        BukkenList.heya.add(new ArrayList());

                        JSONArray heyaList = returnBukken.getJSONArray("heya");
                        for (int j = 0; j < heyaList.length(); j++) {
                            BukkenList.heya.get((i + ((currpage - 1) * 10))).add(new ArrayList());
                            JSONObject returnHeya = heyaList.getJSONObject(j);
                            BukkenList.heya.get((i + ((currpage - 1) * 10))).get(j).add(returnHeya.getString("heya_no"));
                            BukkenList.heya.get((i + ((currpage - 1) * 10))).get(j).add(returnHeya.getString("heya_kaisu"));
                            BukkenList.heya.get((i + ((currpage - 1) * 10))).get(j).add(returnHeya.getString("chinryo"));
                            BukkenList.heya.get((i + ((currpage - 1) * 10))).get(j).add(returnHeya.getString("madori"));
                        }
                        loadFavorites();
                        addProperties(i);
                    }

                    if (currpage == 1) {
                        sv_list.setVisibility(View.VISIBLE);
                        if(GlobalVar.selectedtab == 0) {
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

    @SuppressLint("RestrictedApi")
    private void addProperties(int index) {
        DecimalFormat format = new DecimalFormat("#,###.##");

        LinearLayout.LayoutParams contentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        contentparams.setMargins(20,0,20,0);

        LinearLayout ll_bukkenimage = new LinearLayout(getActivity());
        ll_bukkenimage.setOrientation(LinearLayout.VERTICAL);
        ll_bukkenimage.setBackgroundColor(Color.parseColor("#fff191"));

        llcont.addView(ll_bukkenimage,contentparams);

        AppCompatCheckBox chkboxpricecheck = new AppCompatCheckBox(getActivity());
        chkboxpricecheck.setId(View.generateViewId());
        chkboxpricecheck.setId(CheckboxIDMain + (index + ((currpage-1)*10)));
        chkboxpricecheck.setHighlightColor(Color.parseColor("#ffffff"));
        chkboxpricecheck.setText("この建物の物件を金チェック");
        chkboxpricecheck.setTextColor(Color.parseColor("#000000"));
        chkboxpricecheck.setHeight(130);
        chkboxpricecheck.setOnClickListener(dynamiccheckboxOnclickListener);
        ll_bukkenimage.addView(chkboxpricecheck);

        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String imggaikanURL = DOMAIN_NAME + BukkenList.gaikan_url.get((index + ((currpage-1)*10)));
        imgparams.setMargins(0,0,0,0);
        ImageView imggaikan = new ImageView(getActivity());
        Picasso.get().load(imggaikanURL).into(imggaikan);
        //imggaikan.setBackgroundResource(R.drawable.pic_001);
        imggaikan.setAdjustViewBounds(true);
        imggaikan.setScaleType(ImageView.ScaleType.FIT_START);

        ll_bukkenimage.addView(imggaikan,imgparams);

        TextView tv_bukkenname = new TextView(getActivity());
        LinearLayout.LayoutParams bukkennamesparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bukkennamesparams.setMargins(0,0,0,5);
        tv_bukkenname.setText(BukkenList.bukken_name.get((index + ((currpage-1)*10))));
        tv_bukkenname.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_bukkenname.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkenimage.addView(tv_bukkenname,bukkennamesparams);


        LinearLayout ll_bukkendetails = new LinearLayout(getActivity());
        ll_bukkendetails.setOrientation(LinearLayout.VERTICAL);
        ll_bukkendetails.setBackgroundColor(Color.parseColor("#ffffff"));
        llcont.addView(ll_bukkendetails,contentparams);

        TextView tv_shozaichi = new TextView(getActivity());
        Drawable imgshozaichi = getContext().getResources().getDrawable(R.drawable.ic023x);
        int imgshozaichipixelDrawableSize = Math.round(tv_shozaichi.getLineHeight());
        imgshozaichi.setBounds(0, 0, imgshozaichipixelDrawableSize, imgshozaichipixelDrawableSize);
        tv_shozaichi.setCompoundDrawables(imgshozaichi, null, null, null);
        tv_shozaichi.setText(BukkenList.shozaichi.get((index + ((currpage-1)*10))));
        tv_shozaichi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_shozaichi.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(tv_shozaichi);

        String eki_kanji = null;
        if((!BukkenList.eki1_kanji.get(index).equals("")) && (!BukkenList.eki1_kanji.get(index).equals("null"))){
            eki_kanji = BukkenList.eki1_kanji.get((index + ((currpage-1)*10)));
            if((!BukkenList.eki1_distance.get(index).equals("")) && (!BukkenList.eki1_distance.get(index).equals("null"))&& (Integer.parseInt(BukkenList.eki1_distance.get(index))>0)){
                eki_kanji = eki_kanji + "　(徒歩" + BukkenList.eki1_distance.get(index) + "分)";
            }

        } else if ((!BukkenList.bus1_kanji.get(index).equals("")) && (!BukkenList.bus1_kanji.get(index).equals("null"))) {
            eki_kanji = BukkenList.bus1_kanji.get((index + ((currpage-1)*10)));
            if((!BukkenList.bus1_distance.get(index).equals("")) && (!BukkenList.bus1_distance.get(index).equals("null"))&& (Integer.parseInt(BukkenList.bus1_distance.get(index))>0)){
                eki_kanji = eki_kanji + "　(徒歩" + BukkenList.bus1_distance.get(index) + "分)";
            }

        } else {
            eki_kanji = "";
        }

        if((!eki_kanji.equals("")) && (!eki_kanji.equals("null"))){
            TextView tv_eki_kanji = new TextView(getActivity());
            Drawable imgeki = getContext().getResources().getDrawable(R.drawable.ic043x);
            int imgekipixelDrawableSize = Math.round(tv_eki_kanji.getLineHeight());
            imgeki.setBounds(0, 0, imgekipixelDrawableSize, imgekipixelDrawableSize);
            tv_eki_kanji.setCompoundDrawables(imgeki, null, null, null);
            tv_eki_kanji.setText(eki_kanji);
            tv_eki_kanji.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_eki_kanji.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_bukkendetails.addView(tv_eki_kanji);
        }

        String bukken_name = BukkenList.bukken_type_name.get((index + ((currpage-1)*10))) + "/"+ BukkenList.building_info.get((index + ((currpage-1)*10))) + "階建";
        TextView tv_bukken_type = new TextView(getActivity());
        Drawable imgbukken = getContext().getResources().getDrawable(R.drawable.ic033x);
        int pixelDrawableSize = Math.round(tv_bukken_type.getLineHeight());
        imgbukken.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize);
        tv_bukken_type.setCompoundDrawables(imgbukken, null, null, null);
        tv_bukken_type.setText(bukken_name);
        tv_bukken_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_bukken_type.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(tv_bukken_type);

        String kanseibei = BukkenList.kanseibi.get(index) + " "+ BukkenList.chikunen.get(index) ;
        TextView tv_kanseibi = new TextView(getActivity());
        Drawable imgkanseibei = getContext().getResources().getDrawable(R.drawable.ic003x);
        int imgkanseibeipixelDrawableSize = Math.round(tv_kanseibi.getLineHeight());
        imgkanseibei.setBounds(0, 0, imgkanseibeipixelDrawableSize, imgkanseibeipixelDrawableSize);
        tv_kanseibi.setCompoundDrawables(imgkanseibei, null, null, null);
        tv_kanseibi.setText(kanseibei);
        tv_kanseibi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_kanseibi.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(tv_kanseibi);

        String ido_hokui = BukkenList.ido_hokui.get(index);
        String keido_tokei = BukkenList.keido_tokei.get(index);
        System.out.println("LATITUDE " + ido_hokui + " " + index);
        System.out.println("LONGITUDE " + keido_tokei + " " + index);

        String tenpo_name=null;
        String tenpo_tel=null;
        if((BukkenList.tenpo_name.get(index)!="") && (BukkenList.tenpo_name.get(index) != null)){
            tenpo_name = BukkenList.tenpo_name.get((index + ((currpage-1)*10)));
            tenpo_tel = BukkenList.tenpo_tel.get((index + ((currpage-1)*10)));
        }
        else if((BukkenList.tenpo_name2.get(index)!="") && (BukkenList.tenpo_name2.get(index) != null)){
            tenpo_name = BukkenList.tenpo_name2.get((index + ((currpage-1)*10)));
            tenpo_tel = BukkenList.tenpo_tel2.get((index + ((currpage-1)*10)));
        }
        else{
            tenpo_name="";
        }
        if((tenpo_name != "")&&(tenpo_name != "null")){
            TextView tv_tenpo_name = new TextView(getActivity());
            Drawable imgtenpo_name = getContext().getResources().getDrawable(R.drawable.topbar_title_left3x);
            int imgtenpo_namepixelDrawableSizeH = Math.round(tv_bukken_type.getLineHeight());
            int imgtenpo_namepixelDrawableSizeW = (int)Math.round((double)imgtenpo_name.getIntrinsicWidth() * ((double)tv_bukken_type.getLineHeight()/(double)imgtenpo_name.getIntrinsicHeight()));
            imgtenpo_name.setBounds(0, 0, imgtenpo_namepixelDrawableSizeW, imgtenpo_namepixelDrawableSizeH);
            tv_tenpo_name.setCompoundDrawables(imgtenpo_name, null, null, null);
            tv_tenpo_name.setText(" " + tenpo_name);
            tv_tenpo_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_tenpo_name.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_bukkendetails.addView(tv_tenpo_name);

            LinearLayout ll_tenpo_tel = new LinearLayout(getActivity());
            ll_tenpo_tel.setOrientation(LinearLayout.HORIZONTAL);
            ll_tenpo_tel.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_bukkendetails.addView(ll_tenpo_tel);

            TextView tv_tel = new TextView(getActivity());
            tv_tel.setText("TEL.");

            tv_tel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv_tel.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_tenpo_tel.addView(tv_tel);

            TextView tv_tenpo_tel = new TextView(getActivity());
            tv_tenpo_tel.setText(" " + tenpo_tel);
            tv_tenpo_tel.setTag(tenpo_tel);
            tv_tenpo_tel.setOnClickListener(telephoneOnclickListener);
            tv_tenpo_tel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
            tv_tenpo_tel.setTextColor(Color.parseColor("#ff0000"));
            ll_tenpo_tel.addView(tv_tenpo_tel);
        }

        TableLayout tl_heya = new TableLayout(getActivity());
        TableRow tr_heyadetheader = new TableRow(getActivity());
        TableRow.LayoutParams heyadetheaderparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        heyadetheaderparams.setMargins(10,10,10,10);
        //tr_heyadetheader.setLayoutParams(heyadetheaderparams);

        TextView tv_sentakuheader = new TextView(getActivity());
        tv_sentakuheader.setText("選択");
        tv_sentakuheader.setGravity(Gravity.CENTER);
        tv_sentakuheader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv_sentakuheader.setBackgroundColor(Color.parseColor("#ffffff"));
        tr_heyadetheader.addView(tv_sentakuheader,heyadetheaderparams);

        TextView tv_heyakaisuheader = new TextView(getActivity());
        tv_heyakaisuheader.setText("階");
        tv_heyakaisuheader.setGravity(Gravity.CENTER);
        tv_heyakaisuheader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv_heyakaisuheader.setBackgroundColor(Color.parseColor("#ffffff"));
        tr_heyadetheader.addView(tv_heyakaisuheader,heyadetheaderparams);

        TextView tv_chinryoheader = new TextView(getActivity());
        tv_chinryoheader.setText("賃料");
        tv_chinryoheader.setGravity(Gravity.CENTER);
        tv_chinryoheader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv_chinryoheader.setBackgroundColor(Color.parseColor("#ffffff"));
        tr_heyadetheader.addView(tv_chinryoheader,heyadetheaderparams);

        TextView tv_madoriheader = new TextView(getActivity());
        tv_madoriheader.setText("間取り");
        tv_madoriheader.setGravity(Gravity.CENTER);
        tv_madoriheader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv_madoriheader.setBackgroundColor(Color.parseColor("#ffffff"));
        tr_heyadetheader.addView(tv_madoriheader,heyadetheaderparams);

        TableRow.LayoutParams heyaheaderbuttonparams = new TableRow.LayoutParams(getDPscale(70),getDPscale(40));
        heyaheaderbuttonparams.setMargins(10,5,10,5);

        TextView tv_okiniiriheader = new TextView(getActivity());
        tv_okiniiriheader.setText("お気に入り");
        tv_okiniiriheader.setGravity(Gravity.CENTER);
        tv_okiniiriheader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv_okiniiriheader.setBackgroundColor(Color.parseColor("#ffffff"));
        tr_heyadetheader.addView(tv_okiniiriheader,heyaheaderbuttonparams);

        TextView tv_shosaiheader = new TextView(getActivity());
        tv_shosaiheader.setText("詳細");
        tv_shosaiheader.setGravity(Gravity.CENTER);
        tv_shosaiheader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv_shosaiheader.setBackgroundColor(Color.parseColor("#ffffff"));
        tr_heyadetheader.addView(tv_shosaiheader,heyaheaderbuttonparams);

        tl_heya.addView(tr_heyadetheader,0);

        for (int i=0; i < BukkenList.heya.get((index + ((currpage-1)*10))).size(); i++){

            //{heya_no,heya_kaisu,chinryo,marodi}

            TableRow tr_heyadet = new TableRow(getActivity());
            TableRow.LayoutParams heyadetparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            heyadetparams.setMargins(10,10,10,10);
            //tr_heyadet.setLayoutParams(heyadetparams);

            AppCompatCheckBox chkboxsentaku = new AppCompatCheckBox(getActivity());
            chkboxsentaku.setId(View.generateViewId());
            chkboxsentaku.setId(CheckboxIDHeya +((index + ((currpage-1)*10))* 100)+ i);
            chkboxsentaku.setTextColor(Color.parseColor("#000000"));
            chkboxsentaku.setHeight(130);
            chkboxsentaku.setOnClickListener(dynamiccheckboxOnclickListener);
            tr_heyadet.addView(chkboxsentaku,heyadetparams);

            TextView tv_heyakaisu = new TextView(getActivity());
            tv_heyakaisu.setText(BukkenList.heya.get((index + ((currpage-1)*10))).get(i).get(1) + "階");
            tv_heyakaisu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tv_heyakaisu.setBackgroundColor(Color.parseColor("#ffffff"));
            tr_heyadet.addView(tv_heyakaisu,heyadetparams);

            TextView tv_chinryo = new TextView(getActivity());
            tv_chinryo.setText(format.format(Double.parseDouble(BukkenList.heya.get((index + ((currpage-1)*10))).get(i).get(2)))  + "万円");
            //tv_chinryo.setText(String.format("%.1f", Double.parseDouble(BukkenList.heya.get((index + ((currpage-1)*10))).get(i).get(2))) + "万円");
            tv_chinryo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tv_chinryo.setBackgroundColor(Color.parseColor("#ffffff"));
            tr_heyadet.addView(tv_chinryo,heyadetparams);

            TextView tv_madori = new TextView(getActivity());
            tv_madori.setText(BukkenList.heya.get((index + ((currpage-1)*10))).get(i).get(3));
            tv_madori.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tv_madori.setBackgroundColor(Color.parseColor("#ffffff"));
            tr_heyadet.addView(tv_madori,heyadetparams);

            TableRow.LayoutParams heyabuttonparams = new TableRow.LayoutParams(getDPscale(60),getDPscale(40));
            heyabuttonparams.setMargins(10,5,10,5);

            AppCompatButton btn_okiniiri = new AppCompatButton(getActivity());
            btn_okiniiri.setText("+追加");

            btn_okiniiri.setId(ButtonID + 3000000 + ((index + ((currpage-1)*10))* 100)+ i);
            btn_okiniiri.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            btn_okiniiri.setTextColor(Color.parseColor("#ffffff"));
            btn_okiniiri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blk));
            btn_okiniiri.setOnClickListener(dynamicbuttonOnclickListener);
            tr_heyadet.addView(btn_okiniiri,heyabuttonparams);

            if(isfavorite(BukkenList.heya.get((index + ((currpage-1)*10))).get(i).get(0))){
                btn_okiniiri.setEnabled(false);
                btn_okiniiri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
                btn_okiniiri.setTextColor(Color.parseColor("#aaaaaa"));
                btn_okiniiri.setText("登録済み");

            }

            AppCompatButton btn_shosai = new AppCompatButton(getActivity());
            btn_shosai.setText("詳細");
            btn_shosai.setId(ButtonID + 5000000 + ((index + ((currpage-1)*10))* 100)+ i);
            btn_shosai.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            btn_shosai.setTextColor(Color.parseColor("#ffffff"));
            btn_shosai.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blk));
            btn_shosai.setOnClickListener(dynamicbuttonOnclickListener);
            tr_heyadet.addView(btn_shosai,heyabuttonparams);

            tl_heya.addView(tr_heyadet,i+1);
            //}

        }
        ll_bukkendetails.addView(tl_heya);

        RelativeLayout rl_showareamap = new RelativeLayout(getActivity());
        rl_showareamap.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(rl_showareamap);
        RelativeLayout.LayoutParams btnshowareaparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getDPscale(40));
        btnshowareaparams.setMargins(20,10,20,getDPscale(10));
        btnshowareaparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        AppCompatButton btn_showareamap = new AppCompatButton(getActivity());
        btn_showareamap.setText("周辺地図を表示");
        btn_showareamap.setId(ButtonID + 7000000 + (index + ((currpage-1)*10)));
        btn_showareamap.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        btn_showareamap.setTextColor(Color.parseColor("#000000"));
        btn_showareamap.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_jogjogylw));
        btn_showareamap.setPadding(getDPscale(20),0,getDPscale(20),0);
        btn_showareamap.setOnClickListener(dynamicbuttonOnclickListener);
        rl_showareamap.addView(btn_showareamap,btnshowareaparams);

        if(bukkencount-1 > index){
            LinearLayout.LayoutParams spacerparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
            spacerparams.setMargins(20,0,20,getDPscale(10));
            LinearLayout ll_spacer = new LinearLayout(getActivity());
            ll_spacer.setOrientation(LinearLayout.VERTICAL);
            ll_spacer.setBackgroundColor(Color.parseColor("#dddddd"));
            llcont.addView(ll_spacer,spacerparams);
        }

    }

    private int getDPscale(int size) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) ((double) size * scale + 0.5f));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            GlobalVar.selectedtab = 0;
            if(GlobalVar.previousScreen.equals("")) {
                if (GlobalVar.isFromMap == true) {
                    BukkenList.clearbukken();
                    llcont.removeAllViews();
                    sv_list.setVisibility(View.INVISIBLE);
                    if(GlobalVar.selectedtab == 0) {
                        CD = new D_cDialog(getContext());
                        CD.pbWait.setVisibility(View.VISIBLE);
                        CD.cShow(false);
                    }
                    getProperties();
                }
            }
        }
    }

    private void hasnextpage() {
        String code_traffic = GlobalVar.getcode_traffic();
        String code_traffic_company = GlobalVar.getcode_traffic_company();
        String code_route = GlobalVar.getcode_route();
        String code_station = GlobalVar.getcode_station();
        String searchurl = "&" + GlobalVar.getsearch_url();
        String url;
        if (GlobalVar.isFromMap == true) {
            //url = GlobalVar.urlFromMap + searchurl;
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_CHINTAI +
                    "&page=" + (currpage + 1) +
                    searchurl;
        } else {
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_CHINTAI +
                "&code_traffic=" + code_traffic +
                "&code_traffic_company=" + code_traffic_company +
                "&code_route=" + code_route +
                "&code_station=" + code_station +
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
                    //bukkencount = bukken_list.length();

                    if (bukken_list.length() > 0) {
                        pagecount++;
                        btn_nextpage.setEnabled(true);
                    } else {
                        btn_nextpage.setEnabled(false);
                    }
                    btn_nextpage.setVisibility(View.VISIBLE);
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
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedYellowFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favoritesJSON = favoritesDetailsArray.toString();

        editor.putString("favorite list", favoritesJSON);
        editor.apply();
        System.out.println("List of favorites: " + sharedPreferences.getString("favorite list", ""));
    }

    public void loadFavorites() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedYellowFavorites", MODE_PRIVATE);
        String favoriteJSON = sharedPreferences.getString("favorite list", null);

        if(favoriteJSON != null) {

            try {
                favoritesArray = new ArrayList<String>();
                favoritesDetailsArray = new JSONArray(favoriteJSON);
                JSONObject faveDetailsObject = new JSONObject();
                for(int i=0; i<favoritesDetailsArray.length(); i++) {
                    faveDetailsObject = favoritesDetailsArray.getJSONObject(i);
                    favoritesArray.add(faveDetailsObject.getString("heya_no"));
                }

            } catch (JSONException e) {

            }
            System.out.println("Loaded From shared preference property list: " + favoritesDetailsArray);
            System.out.println("favorites array list:" + favoritesArray);
        }
    }

    public void clearSavedYellowFavorites() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedYellowFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
    }
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
            alertDialogcalltenpotel(v.getTag().toString());
        }
    };

    View.OnClickListener dynamicbuttonOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            MainActivity view = ((MainActivity) getActivity());
            Fragment selectedFragment = null;
            AppCompatButton btn;

            if (v.getId() >= ButtonID + 7000000) {
                int index = v.getId() - (ButtonID + 7000000);
                // goto property solo map view
                GlobalVar.setparent_fragment("yellowLineRentListFragment");
                selectedFragment = new yellowMapPropertyListTEMP();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

                String ido_hokui = BukkenList.ido_hokui.get(index);
                String keido_tokei = BukkenList.keido_tokei.get(index);
                System.out.println("LATITUDE " + ido_hokui + " " + index);
                System.out.println("LONGITUDE " + keido_tokei + " " + index);

                GlobalVar.setLatitude(ido_hokui);
                GlobalVar.setLongitude(keido_tokei);
            }
            else if (v.getId() >= ButtonID + 5000000) {
                int id = v.getId() - (ButtonID + 5000000);
                int indexbukken = id / 100;
                int indexheya = id - (indexbukken * 100);
                // save selected heya_no for detailed view
                String selectedheyano = BukkenList.heya.get(indexbukken).get(indexheya).get(0);
                GlobalVar.setdetailed_bukken_no(selectedheyano);
                addToFavoritesAndHistory(indexbukken, indexheya, 2);
                selectedFragment = new yellowLinePropertyDetailFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
            }
            else if (v.getId() >= ButtonID + 3000000) {
                int id = v.getId() - (ButtonID + 3000000);
                int indexbukken = id / 100;
                int indexheya = id - (indexbukken * 100);

                addToFavoritesAndHistory(indexbukken,indexheya, 1);
                addToFavorites();

                // message
                alertDialogAddtoFavorites();

                //set button to disabled
                btn = view.findViewById(ButtonID + 3000000 + ((indexbukken + ((currpage - 1) * 10)) * 100) + indexheya);
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
            Fragment selectedFragment = null;
            AppCompatCheckBox chkbox;

            if (((CheckBox) v).isChecked()) {
                if (v.getId() >= CheckboxIDHeya) {
                    int id = v.getId() - CheckboxIDHeya;
                    int indexbukken = id / 100;
                    int indexheya = id - (indexbukken * 100);
                    // add to selected heya list
                    GlobalVar.addheya_no(BukkenList.heya.get(indexbukken).get(indexheya).get(0));
                    selectedcount++;

                    int checkedchildheya = 0;
                    for (int i = 0; i < BukkenList.heya.get(indexbukken).size(); i++) {
                        chkbox = view.findViewById(CheckboxIDHeya + ((indexbukken * 100) + i));
                        if (chkbox.isChecked()) {
                            checkedchildheya++;
                        }
                    }
                    if (checkedchildheya > 0) {
                        chkbox = view.findViewById(CheckboxIDMain + (indexbukken));
                        chkbox.setChecked(true);
                    }
                } else if (v.getId() >= CheckboxIDMain) {
                    int index = v.getId() - CheckboxIDMain;

                    for (int i = 0; i < BukkenList.heya.get(index).size(); i++) {
                        chkbox = view.findViewById(CheckboxIDHeya + ((index * 100) + i));
                        chkbox.setChecked(true);
                        //add to selected heya list
                        GlobalVar.addheya_no(BukkenList.heya.get(index).get(i).get(0));
                        selectedcount++;
                    }
                }
                btn_inquire.setVisibility(View.VISIBLE);
            } else if (!((CheckBox) v).isChecked()) {

                if (v.getId() >= CheckboxIDHeya) {
                    int id = v.getId() - CheckboxIDHeya;
                    int indexbukken = id / 100;
                    int indexheya = id - (indexbukken * 100);
                    // add to selected heya list
                    GlobalVar.removeheya_no(BukkenList.heya.get(indexbukken).get(indexheya).get(0));
                    selectedcount--;

                    int checkedchildheya = 0;
                    for (int i = 0; i < BukkenList.heya.get(indexbukken).size(); i++) {
                        chkbox = view.findViewById(CheckboxIDHeya + ((indexbukken * 100) + i));
                        if (chkbox.isChecked()) {
                            checkedchildheya++;
                        }
                    }
                    if (checkedchildheya == 0) {
                        chkbox = view.findViewById(CheckboxIDMain + (indexbukken));
                        chkbox.setChecked(false);
                    }
                } else if (v.getId() >= CheckboxIDMain) {
                    int index = v.getId() - CheckboxIDMain;

                    for (int i = 0; i < BukkenList.heya.get(index).size(); i++) {
                        chkbox = view.findViewById(CheckboxIDHeya + ((index * 100) + i));
                        chkbox.setChecked(false);
                        //add to selected heya list
                        GlobalVar.removeheya_no(BukkenList.heya.get(index).get(i).get(0));
                        selectedcount--;
                    }
                }

                if (selectedcount == 0) {
                    btn_inquire.setVisibility(View.GONE);
                }
            } else {
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Japan and move the camera
        LatLng japan = new LatLng(35, 139);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(japan));
    }

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

    public void addToFavoritesAndHistory(int indexbukken, int indexheya, int destination){

        try {
            favoriteDetailsObject = new JSONObject();
            int randId = rand.nextInt(1000000);

            //favoriteDetailsObject.put("tatemono_no", BukkenList.tatemono_no.get(indexbukken));
            favoriteDetailsObject.put("tenpo_no1", BukkenList.tenpo_no1.get(indexbukken));
            favoriteDetailsObject.put("shozaichi", BukkenList.shozaichi.get(indexbukken));
            favoriteDetailsObject.put("bukkenName", BukkenList.bukken_name.get(indexbukken));
            favoriteDetailsObject.put("madori", BukkenList.heya.get(indexbukken).get(indexheya).get(2));
            favoriteDetailsObject.put("bukken_type_name", BukkenList.bukken_type_name.get(indexbukken));
            favoriteDetailsObject.put("shikikin", BukkenList.heya.get(indexbukken).get(indexheya).get(3));
            favoriteDetailsObject.put("heya_no", BukkenList.heya.get(indexbukken).get(indexheya).get(0));
            favoriteDetailsObject.put("random_id", randId);

            favoritesArray.add(BukkenList.heya.get(indexbukken).get(indexheya).get(0));

            if(destination == 1) favoritesDetailsArray.put(favoriteDetailsObject);
            if(destination == 2) {
                checkHistory(BukkenList.heya.get(indexbukken).get(indexheya).get(0), favoriteDetailsObject);
                //historyDetailsArray.put(favoriteDetailsObject);
            }

        } catch (JSONException e) {

        }
    }
    public void addToHistory() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedYellowHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String historyJSON = historyDetailsArray.toString();

        editor.putString("history list", historyJSON);
        editor.apply();
        System.out.println("List of history: " + sharedPreferences.getString("history list", ""));
    }
    public void loadHistory() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedYellowHistory", MODE_PRIVATE);
        String historyJSON = sharedPreferences.getString("history list", null);

        if(historyJSON != null) {

            try { historyDetailsArray = new JSONArray(historyJSON); }
            catch (JSONException e) { }

        }
    }
    private void checkHistory(String bukken_no, JSONObject faveDetailsObject) {

        boolean isExisting = false;
        try {
            JSONObject historyDetailsObject = new JSONObject();

            for(int i=0; i<historyDetailsArray.length(); i++) {
                historyDetailsObject = historyDetailsArray.getJSONObject(i);
                if(historyDetailsObject.getString("heya_no").equals(bukken_no)) {
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
