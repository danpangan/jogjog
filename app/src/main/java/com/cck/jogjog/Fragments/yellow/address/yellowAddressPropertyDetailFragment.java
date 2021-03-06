package com.cck.jogjog.Fragments.yellow.address;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Dialog.D_panoramaDialog;
import com.cck.jogjog.Dialog.D_videoDialog;
import com.cck.jogjog.Fragments.yellow.common.yellowInquireProperty;
import com.cck.jogjog.Fragments.yellow.common.yellowInquireReservation;
import com.cck.jogjog.Fragments.yellow.common.yellowInquireStore;
import com.cck.jogjog.Fragments.yellow.common.yellowMapProperty;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_DETAIL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class yellowAddressPropertyDetailFragment extends Fragment implements View.OnClickListener {
    /* OBJECTS */
    private LinearLayout ll_panorama, ll_panoramaimg, scroll_MM, ll_video, ll_images, ll_videolist;
    private ConstraintLayout cl_eki1, cl_eki2, cl_eki3;
    private FlowLayout fl_setsubi2;
    private Button btn_ookiniri, btn_showareamap, btn_inquireproperty, btn_inquirestore, btn_visitreserve, btn_storeareamap, btn_back, btn_lastimg, btn_nextimg, btn_movieplay;
    private TextView tv_bukken_name, tv_heya_update_date, tv_next_heya_update_date, tv_heya_no, tv_chinryo, tv_kanrihi,
            tv_shikikin_reikin, tv_madori_menseki, tv_shozaichi, tv_eki1, tv_eki2, tv_eki3, tv_kouzou_type_name,
            tv_kanseibi_chikunen, tv_gas_type, txtView_indicators, tv_heya_kaisu_building_info, tv_sotosu, tv_madori_detail,
            tv_chushajodai, tv_has_bike_parking, tv_has_jutaku_hoken, tv_torihiki_type, tv_has_teiki_shakuyaken,
            tv_nyukyo_yoteibi, tv_nyuukyo_keitai, tv_shikihiki_shokyakukin, tv_petreikin, tv_kanri24kin,
            tv_hoshokin_daikou, tv_tenpo_name_yobi2, tv_licence, tv_tenpo_tel, tv_tenpo_fax, tv_tenpo_shozaichi,
            tv_tenpo_eigyo_jikan, tv_tenpo_teikyubi, tv_kenrikin_hoshokin, tv_cmttitle;
    private ImageView img_shop_gaikan, img_video;
    private HorizontalScrollView mainHSCV;
    private NestedScrollView scv_radgroup;

    /* ARRAYS */
    List<String> bukken_details = new ArrayList<>();
    List<String> setsubi2 = new ArrayList<>();
    List<String> panoramacomment = new ArrayList<>();
    List<String> panoramaimgfiles = new ArrayList<>();
    List<Bitmap> panoramaimg = new ArrayList<>();
    List<String> sliderimgfiles = new ArrayList<>();
    List<Bitmap> sliderimg = new ArrayList<>();
    List<String> moviefiles = new ArrayList<>();
    List<Bitmap> movie = new ArrayList<>();
    public static ArrayList<String> favoritesArray = new ArrayList<String>();


    /* VARIABLES */
    private int ImageID = 46400000;
    private int VideoID = 84300000;
    private int ButtonID = 28600000;
    private int currentImg = 0;
    Random rand = new Random();
    float tOX = 0;
    float tOY = 0;
    boolean isbgtaskended = false;
    D_cDialog CD;
    public static JSONObject favoriteDetailsObject = new JSONObject();
    public static JSONArray favoritesDetailsArray = new JSONArray();
    private String parentfragment = "";

    //favorites
    private static JSONObject returnData = new JSONObject();
    private boolean isFavorite = false;

    /*START*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yellow_property_detailed, container, false);
        clearlists();
        initializelayout(view);
        getProperties();
        GlobalVar.previousScreen = "yellowAddressPropertyDetailFragment";
        GlobalVar.isFromMap = false;
        scv_radgroup.setVisibility(View.INVISIBLE);
        CD = new D_cDialog(getContext());
        CD.pbWait.setVisibility(View.VISIBLE);
        CD.cShow(false);
        return view;
    }

    /*VIEW INITIALIZATION*/
    private void initializelayout(View view) {
        scv_radgroup = view.findViewById(R.id.scv_radgroup);
        tv_bukken_name = view.findViewById(R.id.tv_bukken_name);
        tv_heya_update_date = view.findViewById(R.id.tv_heya_update_date);
        tv_next_heya_update_date = view.findViewById(R.id.tv_next_heya_update_date);
        tv_heya_no = view.findViewById(R.id.tv_heya_no);
        tv_chinryo = view.findViewById(R.id.tv_chinryo);
        tv_kanrihi = view.findViewById(R.id.tv_kanrihi);
        tv_shikikin_reikin = view.findViewById(R.id.tv_shikikin_reikin);
        tv_madori_menseki = view.findViewById(R.id.tv_madori_menseki);
        tv_shozaichi = view.findViewById(R.id.tv_shozaichi);
        tv_eki1 = view.findViewById(R.id.tv_eki1);
        tv_eki2 = view.findViewById(R.id.tv_eki2);
        tv_eki3 = view.findViewById(R.id.tv_eki3);
        tv_kouzou_type_name = view.findViewById(R.id.tv_kouzou_type_name);
        tv_kanseibi_chikunen = view.findViewById(R.id.tv_kanseibi_chikunen);
        tv_gas_type = view.findViewById(R.id.tv_gas_type);
        tv_heya_kaisu_building_info = view.findViewById(R.id.tv_heya_kaisu_building_info);
        tv_sotosu = view.findViewById(R.id.tv_sotosu);
        tv_madori_detail = view.findViewById(R.id.tv_madori_detail);
        tv_chushajodai = view.findViewById(R.id.tv_chushajodai);
        tv_has_bike_parking = view.findViewById(R.id.tv_has_bike_parking);
        tv_has_jutaku_hoken = view.findViewById(R.id.tv_has_jutaku_hoken);
        tv_torihiki_type = view.findViewById(R.id.tv_torihiki_type);
        tv_has_teiki_shakuyaken = view.findViewById(R.id.tv_has_teiki_shakuyaken);
        tv_nyukyo_yoteibi = view.findViewById(R.id.tv_nyukyo_yoteibi);
        tv_nyuukyo_keitai = view.findViewById(R.id.tv_nyuukyo_keitai);
        tv_shikihiki_shokyakukin = view.findViewById(R.id.tv_shikihiki_shokyakukin);
        tv_petreikin = view.findViewById(R.id.tv_petreikin);
        tv_kanri24kin = view.findViewById(R.id.tv_kanri24kin);
        tv_hoshokin_daikou = view.findViewById(R.id.tv_hoshokin_daikou);
        tv_tenpo_name_yobi2 = view.findViewById(R.id.tv_tenpo_name_yobi2);
        tv_licence = view.findViewById(R.id.tv_licence);
        tv_tenpo_tel = view.findViewById(R.id.tv_tenpo_tel);
        tv_tenpo_fax = view.findViewById(R.id.tv_tenpo_fax);
        tv_tenpo_shozaichi = view.findViewById(R.id.tv_tenpo_shozaichi);
        tv_tenpo_eigyo_jikan = view.findViewById(R.id.tv_tenpo_eigyo_jikan);
        tv_tenpo_teikyubi = view.findViewById(R.id.tv_tenpo_teikyubi);
        tv_kenrikin_hoshokin = view.findViewById(R.id.tv_kenrikin_hoshokin);
        tv_cmttitle = view.findViewById(R.id.tv_cmttitle);

        cl_eki1 = view.findViewById(R.id.cl_eki1);
        cl_eki2 = view.findViewById(R.id.cl_eki2);
        cl_eki3 = view.findViewById(R.id.cl_eki3);

        img_shop_gaikan = view.findViewById(R.id.img_shop_gaikan);

        mainHSCV = view.findViewById(R.id.scroll_main);
        scroll_MM = view.findViewById(R.id.scroll_MM);
        txtView_indicators = view.findViewById(R.id.txtView_indicators);

        fl_setsubi2 = view.findViewById(R.id.fl_setsubi2);

        ll_panoramaimg = view.findViewById(R.id.ll_panoramaimg);
        ll_panorama = view.findViewById(R.id.ll_panorama);
        ll_video = view.findViewById(R.id.ll_video);
        ll_images = view.findViewById(R.id.ll_images);
        ll_videolist = view.findViewById(R.id.ll_videolist);

        btn_ookiniri = view.findViewById(R.id.btn_ookiniri);
        btn_showareamap = view.findViewById(R.id.btn_showareamap);
        btn_inquireproperty = view.findViewById(R.id.btn_inquireproperty);
        btn_inquirestore = view.findViewById(R.id.btn_inquirestore);
        btn_visitreserve = view.findViewById(R.id.btn_visitreserve);
        btn_storeareamap = view.findViewById(R.id.btn_storeareamap);
        btn_back = view.findViewById(R.id.btn_back);
        btn_lastimg = view.findViewById(R.id.btn_lastimg);
        btn_nextimg = view.findViewById(R.id.btn_nextimg);

        mainHSCV.setOnTouchListener(sliderOnTouchListener);
        btn_ookiniri.setOnClickListener(this);
        btn_showareamap.setOnClickListener(this);
        btn_inquireproperty.setOnClickListener(this);
        btn_inquirestore.setOnClickListener(this);
        btn_visitreserve.setOnClickListener(this);
        btn_storeareamap.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_lastimg.setOnClickListener(imgbuttonOnclickListener);
        btn_nextimg.setOnClickListener(imgbuttonOnclickListener);
        tv_tenpo_tel.setOnClickListener(this);

    }

    private void clearlists(){
        bukken_details.clear();
        setsubi2.clear();
        panoramacomment.clear();
        panoramaimgfiles.clear();
        panoramaimg.clear();
        sliderimgfiles.clear();
        sliderimg.clear();
        moviefiles.clear();
        movie.clear();
    }

    /*DATA ACQUISITION*/
    private void getProperties() {
        String bukken_no = GlobalVar.getdetailed_bukken_no();

        //String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_CHINTAI + "&pref_code=" + prefecture + "&code_jis=" + municipalities + "&code_chouaza=" + districts + "&page=" + currpage;
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_DETAIL + API_PATH_TYPE_CHINTAI + "&bukken_no=" + bukken_no;
        //String url = "https://jogjog.goalway.jp/api/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_bukken_detail&type=chintai&bukken_no=00017307004";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject returnData = jsonObject.getJSONObject("data");
                    if (!returnData.isNull("bukken_name")) {
                        bukken_details.add(returnData.getString("bukken_name"));    // index:0
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("heya_update_date")) {
                        bukken_details.add(returnData.getString("heya_update_date"));    // index:1
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("next_heya_update_date")) {
                        bukken_details.add(returnData.getString("next_heya_update_date"));    // index:2
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("heya_no")) {
                        bukken_details.add(returnData.getString("heya_no"));    // index:3
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("chinryo")) {
                        bukken_details.add(returnData.getString("chinryo"));    // index:4
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("kanrihi")) {
                        bukken_details.add(returnData.getString("kanrihi"));    // index:5
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("shikikin")) {
                        bukken_details.add(returnData.getString("shikikin"));    // index:6
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("reikin")) {
                        bukken_details.add(returnData.getString("reikin"));    // index:7
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("madori")) {
                        bukken_details.add(returnData.getString("madori"));    // index:8
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("menseki")) {
                        bukken_details.add(returnData.getString("menseki"));    // index:9
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("shozaichi")) {
                        bukken_details.add(returnData.getString("shozaichi"));    // index:10
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("eki1_kanji")) {
                        bukken_details.add(returnData.getString("eki1_kanji"));    // index:11
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("eki2_kanji")) {
                        bukken_details.add(returnData.getString("eki2_kanji"));    // index:12
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("eki3_kanji")) {
                        bukken_details.add(returnData.getString("eki3_kanji"));    // index:13
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("kouzou")) {
                        bukken_details.add(returnData.getString("kouzou"));    // index:14
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("type_name")) {
                        bukken_details.add(returnData.getString("type_name"));    // index:15
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("kanseibi")) {
                        bukken_details.add(returnData.getString("kanseibi"));    // index:16
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("chikunen")) {
                        bukken_details.add(returnData.getString("chikunen"));    // index:17
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("gas_type")) {
                        bukken_details.add(returnData.getString("gas_type"));    // index:18
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("heya_kaisu")) {
                        bukken_details.add(returnData.getString("heya_kaisu"));    // index:19
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("building_info")) {
                        bukken_details.add(returnData.getString("building_info"));    // index:20
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("sotosu")) {
                        bukken_details.add(returnData.getString("sotosu"));    // index:21
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("madori_detail")) {
                        bukken_details.add(returnData.getString("madori_detail"));    // index:22
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("chushajodai")) {
                        if(returnData.getString("chushajodai").equals("?????? ??????")){
                            bukken_details.add("??????");    // index:23
                        }
                        else {
                            bukken_details.add(returnData.getString("chushajodai"));    // index:23
                        }
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("has_bike_parking")) {
                        bukken_details.add(returnData.getString("has_bike_parking"));    // index:24
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("has_jutaku_hoken")) {
                        bukken_details.add(returnData.getString("has_jutaku_hoken"));    // index:25
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("torihiki_type")) {
                        bukken_details.add(returnData.getString("torihiki_type"));    // index:26
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("has_teiki_shakuyaken")) {
                        bukken_details.add(returnData.getString("has_teiki_shakuyaken"));    // index:27
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("nyukyo_yoteibi")) {
                        bukken_details.add(returnData.getString("nyukyo_yoteibi"));    // index:28
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("nyuukyo_keitai")) {
                        bukken_details.add(returnData.getString("nyuukyo_keitai"));    // index:29
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("shikihiki")) {
                        bukken_details.add(returnData.getString("shikihiki"));    // index:30
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("shokyakukin")) {
                        bukken_details.add(returnData.getString("shokyakukin"));    // index:31
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("petreikin")) {
                        bukken_details.add(returnData.getString("petreikin"));    // index:32
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("kanri24kin")) {
                        bukken_details.add(returnData.getString("kanri24kin"));    // index:33
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("hoshokin_daikou")) {
                        bukken_details.add(returnData.getString("hoshokin_daikou"));    // index:34
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_name")) {
                        bukken_details.add(returnData.getString("tenpo_name"));    // index:35
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("yobi2")) {
                        bukken_details.add(returnData.getString("yobi2"));    // index:36
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("licence")) {
                        bukken_details.add(returnData.getString("licence"));    // index:37
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_tel")) {
                        bukken_details.add(returnData.getString("tenpo_tel"));    // index:38
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_fax")) {
                        bukken_details.add(returnData.getString("tenpo_fax"));    // index:39
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_shozaichi")) {
                        bukken_details.add(returnData.getString("tenpo_shozaichi"));    // index:40
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_eigyo_jikan")) {
                        bukken_details.add(returnData.getString("tenpo_eigyo_jikan"));    // index:41
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_teikyubi")) {
                        bukken_details.add(returnData.getString("tenpo_teikyubi"));    // index:42
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("kenrikin")) {
                        bukken_details.add(returnData.getString("kenrikin"));    // index:43
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("hoshokin")) {
                        bukken_details.add(returnData.getString("hoshokin"));    // index:44
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("shop_gaikan_url")) {
                        bukken_details.add(returnData.getString("shop_gaikan_url")); //index:45
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("ido_hokui")) {
                        bukken_details.add(returnData.getString("ido_hokui")); //index:46 lat
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("keido_tokei")) {
                        bukken_details.add(returnData.getString("keido_tokei")); //index:47 long
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("eki1_distance")) {
                        bukken_details.add(returnData.getString("eki1_distance")); //index:48
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("eki2_distance")) {
                        bukken_details.add(returnData.getString("eki2_distance")); //index:49
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("eki3_distance")) {
                        bukken_details.add(returnData.getString("eki3_distance")); //index:50
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("title")) {
                        bukken_details.add(returnData.getString("title")); //index:51
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_ido_hokui")) {
                        bukken_details.add(returnData.getString("tenpo_ido_hokui")); //index:52 shop lat
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_keido_tokei")) {
                        bukken_details.add(returnData.getString("tenpo_keido_tokei")); //index:53 shop long
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("tenpo_no1")) {
                        bukken_details.add(returnData.getString("tenpo_no1")); //index:54
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("parking_type_name")) {
                        bukken_details.add(returnData.getString("parking_type_name"));    // index:55
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("bus1_kanji")) {
                        bukken_details.add(returnData.getString("bus1_kanji")); //index:56
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("bus2_kanji")) {
                        bukken_details.add(returnData.getString("bus2_kanji")); //index:57
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("bus3_kanji")) {
                        bukken_details.add(returnData.getString("bus3_kanji")); //index:58
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("bus1_distance")) {
                        bukken_details.add(returnData.getString("bus1_distance")); //index:59
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("bus2_distance")) {
                        bukken_details.add(returnData.getString("bus2_distance")); //index:60
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("bus3_distance")) {
                        bukken_details.add(returnData.getString("bus3_distance")); //index:61
                    } else {
                        bukken_details.add("null");
                    }
                    if (!returnData.isNull("bukken_type_name")) {
                        bukken_details.add(returnData.getString("bukken_type_name")); //index:62
                    } else {
                        bukken_details.add("null");
                    }

                    if (!returnData.isNull("setsubi2")) {
                        JSONArray heyaList = returnData.getJSONArray("setsubi2");
                        for (int j = 0; j < heyaList.length(); j++) {
                            setsubi2.add(heyaList.getString(j));
                            String test = setsubi2.get(j);
                        }
                    }
                    //panorama
                    if (!returnData.isNull("heya_panoramna")) {
                        JSONObject heyapanorama = returnData.getJSONObject("heya_panoramna");
                        if (!heyapanorama.isNull("files")) {
                            JSONArray heyapanoramafiles = heyapanorama.getJSONArray("files");
                            for (int k = 0; k < heyapanoramafiles.length(); k++) {
                                JSONObject returnFiles = heyapanoramafiles.getJSONObject(k);
                                panoramacomment.add(returnFiles.getString("comment"));
                                panoramaimgfiles.add(returnFiles.getString("url"));
                            }
                        }
                    }

                    //movies
                    if (!returnData.isNull("heya_movies")) {
                        JSONObject heyapmovies = returnData.getJSONObject("heya_movies");
                        if (!heyapmovies.isNull("files")) {
                            JSONArray heyapmoviesfiles = heyapmovies.getJSONArray("files");
                            for (int k = 0; k < heyapmoviesfiles.length(); k++) {
                                JSONObject returnFiles = heyapmoviesfiles.getJSONObject(k);
                                moviefiles.add(returnFiles.getString("url"));
                            }
                        }
                    }
                    if (moviefiles.size() <= 0) {
                        ll_video.setVisibility(View.GONE);
                    }
                    //slider
                    if (!returnData.isNull("gaikan_url")) {
                        sliderimgfiles.add(returnData.getString("gaikan_url"));
                    }
                    if (!returnData.isNull("madori_url")) {
                        sliderimgfiles.add(returnData.getString("madori_url"));
                    }
                    for (int l = 1; l <= 30; l++) {
                        String interiorfilename = "interior" + l + "_file_name";
                        if (!returnData.isNull(interiorfilename)) {
                            String filename = returnData.getString(interiorfilename);
                            if (!filename.isEmpty() && (filename != "null")) {
                                String naikanurl = "naikan" + l + "_url";
                                sliderimgfiles.add(returnData.getString(naikanurl));
                            }
                        }
                    }
                    if (panoramaimgfiles.size() <= 0) {
                        ll_panorama.setVisibility(View.GONE);
                        isbgtaskended = true;
                    }
                    else {
                        AsyncTask<Void, Integer, Integer> task = new AsyncTask<Void, Integer, Integer>() {
                            @Override
                            protected Integer doInBackground(Void... params) {    //Other thread--------------------------------------------------------------
                                //publishProgress(25);			//If progress status is needed...
                                Integer retVal = bgTaskloadimages();
                                return retVal;
                            }

                            @Override
                            protected void onProgressUpdate(Integer... progress) {    //UI Thread-----------------------------------------------------------------
                                //int n = progress[0];		//This is how you use the paramet
                            }

                            @Override
                            protected void onPostExecute(Integer result) {    //UI Thread-----------------------------------------------------------------
                                postBgTask(result);
                            }
                        };
                        task.execute();        //Start task (thread)
                    }
                    loadFavorites();
                    addPropertydetails();
                    if (isbgtaskended) {
                        scv_radgroup.setVisibility(View.VISIBLE);
                        CD.cDismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int x = 0;
                // Anything you want
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /*VIEW PLOTTING*/
    private void addPropertydetails() {
        DecimalFormat format = new DecimalFormat("#,###.##");

        if(isFavorite){
            btn_ookiniri.setEnabled(false);
            btn_ookiniri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
            btn_ookiniri.setTextColor(Color.parseColor("#aaaaaa"));
            btn_ookiniri.setText("????????????");
        }

        if (bukken_details.get(51) != "null") {
            tv_cmttitle.setText(bukken_details.get(51));
        } else {
            tv_cmttitle.setVisibility(View.GONE);
        }
        tv_bukken_name.setText(bukken_details.get(0));
        tv_heya_update_date.setText("??????????????? ???" + bukken_details.get(1));
        tv_next_heya_update_date.setText("????????????????????? ???" + bukken_details.get(2));
        tv_heya_no.setText("?????????????????? ???" + bukken_details.get(3));
/*
        String chinryo;
        if (Double.parseDouble(bukken_details.get(4)) % 1 == 0) {
            chinryo = String.format("%.0f", Double.parseDouble(bukken_details.get(4)));
        } else {
            chinryo = String.format("%.1f", Double.parseDouble(bukken_details.get(4)));
        }
        tv_chinryo.setText(chinryo);
*/
        tv_chinryo.setText(format.format(Double.parseDouble(bukken_details.get(4))));
        tv_kanrihi.setText(bukken_details.get(5));
        String shikikin_reikin = bukken_details.get(6) + "/" + bukken_details.get(7);
        tv_shikikin_reikin.setText(shikikin_reikin);
        String madori_menseki = bukken_details.get(8) + "/" + bukken_details.get(9) + "m\u00B2";
        tv_madori_menseki.setText(madori_menseki);
        tv_shozaichi.setText("???" + bukken_details.get(10));


        String eki1;
        if ((!bukken_details.get(11).equals("null"))&&(!bukken_details.get(11).equals(""))) {
            eki1 = "???" + bukken_details.get(11);
            if ((!bukken_details.get(48).equals("null"))&&(!bukken_details.get(48).equals(""))&& (Integer.parseInt(bukken_details.get(48))>0)) {
                eki1 = eki1  + "???(??????" + bukken_details.get(48) + "???)";
            }
            tv_eki1.setText(eki1);
        }
        else if((!bukken_details.get(56).equals("null"))&&(!bukken_details.get(56).equals(""))){
            eki1 = "???" + bukken_details.get(56);
            if ((!bukken_details.get(59).equals("null"))&&(!bukken_details.get(59).equals(""))&& (Integer.parseInt(bukken_details.get(59))>0)) {
                eki1 = eki1  + "???(??????" + bukken_details.get(59) + "???)";
            }
            tv_eki1.setText(eki1);        }
        else {
            cl_eki1.setVisibility(View.GONE);
        }

        String eki2;
        if  ((!bukken_details.get(12).equals("null"))&&(!bukken_details.get(12).equals(""))) {
            eki2 = "???" + bukken_details.get(12);
            if ((!bukken_details.get(49).equals("null"))&&(!bukken_details.get(49).equals(""))&& (Integer.parseInt(bukken_details.get(49))>0)) {
                eki2 = eki2  + "???(??????" + bukken_details.get(49) + "???)";
            }
            tv_eki2.setText(eki2);
        }
        else if((!bukken_details.get(57).equals("null"))&&(!bukken_details.get(57).equals(""))){
            eki2 = "???" + bukken_details.get(57);
            if ((!bukken_details.get(60).equals("null"))&&(!bukken_details.get(60).equals(""))&& (Integer.parseInt(bukken_details.get(60))>0)) {
                eki2 = eki2  + "???(??????" + bukken_details.get(60) + "???)";
            }
            tv_eki2.setText(eki2);
        }
        else {
            cl_eki2.setVisibility(View.GONE);
        }

        String eki3;
        if  ((!bukken_details.get(13).equals("null"))&&(!bukken_details.get(13).equals(""))) {
            eki3 = "???" + bukken_details.get(13);
            if ((!bukken_details.get(50).equals("null"))&&(!bukken_details.get(50).equals(""))&& (Integer.parseInt(bukken_details.get(50))>0)) {
                eki3 = eki3  + "???(??????" + bukken_details.get(50) + "???)";
            }
            tv_eki3.setText(eki3);
        }
        else if((!bukken_details.get(58).equals("null"))&&(!bukken_details.get(58).equals(""))){
            eki3 = "???" + bukken_details.get(58);
            if ((!bukken_details.get(61).equals("null"))&&(!bukken_details.get(61).equals(""))&& (Integer.parseInt(bukken_details.get(61))>0)) {
                eki3 = eki3  + "???(??????" + bukken_details.get(61) + "???)";
            }
            tv_eki3.setText(eki3);
        }
        else {
            cl_eki3.setVisibility(View.GONE);
        }

        String kouzou_type_name = bukken_details.get(14) + "/" + bukken_details.get(15);
        tv_kouzou_type_name.setText(kouzou_type_name);
        String kanseibi_chikunen = bukken_details.get(16) + "/" + bukken_details.get(17);
        tv_kanseibi_chikunen.setText(kanseibi_chikunen);
        tv_gas_type.setText(bukken_details.get(18));
        String heya_kaisu_building_info = bukken_details.get(19) + "???/" + bukken_details.get(20) + "??????";
        tv_heya_kaisu_building_info.setText(heya_kaisu_building_info);
        tv_sotosu.setText(bukken_details.get(21));
        tv_madori_detail.setText(bukken_details.get(22));

        String chushajodai = bukken_details.get(23) + bukken_details.get(55);
        tv_chushajodai.setText(chushajodai);

        tv_has_bike_parking.setText(bukken_details.get(24));
        tv_has_jutaku_hoken.setText(bukken_details.get(25));
        tv_torihiki_type.setText(bukken_details.get(26));
        tv_has_teiki_shakuyaken.setText(bukken_details.get(27));
        tv_nyukyo_yoteibi.setText(bukken_details.get(28));
        tv_nyuukyo_keitai.setText(bukken_details.get(29));
        String shikihiki_shokyakukin = bukken_details.get(30) + "/" + bukken_details.get(31);
        tv_shikihiki_shokyakukin.setText(shikihiki_shokyakukin);
        tv_petreikin.setText(bukken_details.get(32));
        tv_kanri24kin.setText(bukken_details.get(33));
        tv_hoshokin_daikou.setText(bukken_details.get(34));

        String tenpo_name_yobi2 = bukken_details.get(35) + " " + bukken_details.get(36);
        Drawable imgtenpo_name = getContext().getResources().getDrawable(R.drawable.topbar_title_left3x);
        int imgtenpo_namepixelDrawableSizeH = Math.round(tv_tenpo_tel.getLineHeight());
        int imgtenpo_namepixelDrawableSizeW = (int) Math.round((double) imgtenpo_name.getIntrinsicWidth() * ((double) tv_tenpo_tel.getLineHeight() / (double) imgtenpo_name.getIntrinsicHeight()));
        imgtenpo_name.setBounds(0, 0, imgtenpo_namepixelDrawableSizeW, imgtenpo_namepixelDrawableSizeH);
        tv_tenpo_name_yobi2.setCompoundDrawables(imgtenpo_name, null, null, null);
        tv_tenpo_name_yobi2.setText(tenpo_name_yobi2);

        tv_licence.setText(bukken_details.get(37));
        tv_tenpo_tel.setText(bukken_details.get(38));
        tv_tenpo_tel.setTextColor(Color.parseColor("#ff0000ff"));
        tv_tenpo_fax.setText(bukken_details.get(39));
        tv_tenpo_shozaichi.setText(bukken_details.get(40));
        tv_tenpo_eigyo_jikan.setText(bukken_details.get(41));
        tv_tenpo_teikyubi.setText(bukken_details.get(42));
        String kenrikin_hoshokin = bukken_details.get(43) + "/" + bukken_details.get(44);
        tv_kenrikin_hoshokin.setText(kenrikin_hoshokin);

        for (int i = 0; i < setsubi2.size(); i++) {
            LinearLayout.LayoutParams setsubi2params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv_setsubi = new TextView(getActivity());
            tv_setsubi.setPadding(5, 0, 5, 0);
            tv_setsubi.setText(setsubi2.get(i));
            tv_setsubi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv_setsubi.setTextColor(Color.parseColor("#ff000000"));
            tv_setsubi.setBackground(getContext().getResources().getDrawable(R.drawable.button_bordered_background_blk));
            fl_setsubi2.addView(tv_setsubi, setsubi2params);
        }

        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String img_shop_gaikanURL = DOMAIN_NAME + bukken_details.get(45);
        imgparams.setMargins(0, 0, 0, 0);
        Picasso.get().load(img_shop_gaikanURL).into(img_shop_gaikan);
        //imggaikan.setBackgroundResource(R.drawable.pic_001);
        img_shop_gaikan.setAdjustViewBounds(true);
        img_shop_gaikan.setScaleType(ImageView.ScaleType.FIT_START);


        LinearLayout MM = scroll_MM; //Linear Layout ID in XML File.
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView[] mainImgs = new ImageView[sliderimgfiles.size()];
        for (int mm = 0; mm < sliderimgfiles.size(); mm++) {
            mainImgs[mm] = new ImageView(getActivity());
            String url = DOMAIN_NAME + sliderimgfiles.get(mm);
            Picasso.get().load(url).into(mainImgs[mm]);
            //mainImgs[mm].setImageBitmap(sliderimg.get(mm));
            int imgmmpixelDrawableSizeW = Math.round(txtView_indicators.getMeasuredWidth());
            int imgmmpixelDrawableSizeH = (int) Math.round((double) 3 * ((double) txtView_indicators.getMeasuredWidth() / (double) 4));
            mParams = new LinearLayout.LayoutParams(imgmmpixelDrawableSizeW, imgmmpixelDrawableSizeH);
            mainImgs[mm].setLayoutParams(mParams);
            mainImgs[mm].setAdjustViewBounds(true);
            mainImgs[mm].setScaleType(ImageView.ScaleType.FIT_XY);
            MM.addView(mainImgs[mm]);
        }
        SetPageGauge();

        if (moviefiles.size()>0) {
            try {
                for (int i = 0; i < moviefiles.size(); i++) {

                    ConstraintLayout cl_thumbnail_cont = new ConstraintLayout(getActivity());
                    cl_thumbnail_cont.setId(getView().generateViewId());

                    ConstraintLayout.LayoutParams imgdougaparams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);


                    ImageView img_douga = new ImageView(getActivity());
                    img_douga.setId(VideoID + i);
                    Bitmap bmpThumbnail = retriveVideoFrameFromVideo(DOMAIN_NAME + moviefiles.get(0));
                    if (bmpThumbnail != null) {

                        int imgmmpixelDrawableSizeW = Math.round(ll_panoramaimg.getMeasuredWidth());
                        int imgmmpixelDrawableSizeH = (int) Math.round((double) bmpThumbnail.getHeight() * ((double) ll_panoramaimg.getMeasuredWidth() / (double) bmpThumbnail.getWidth()));

                        bmpThumbnail = Bitmap.createScaledBitmap(bmpThumbnail, imgmmpixelDrawableSizeW, imgmmpixelDrawableSizeH, false);
                        img_douga.setImageBitmap(bmpThumbnail);
                    }
                    img_douga.setLayoutParams(imgdougaparams);
                    img_douga.setOnClickListener(videoOnclickListener);
                    cl_thumbnail_cont.addView(img_douga);

                    ConstraintSet cs_img_douga = new ConstraintSet();
                    cs_img_douga.clone(cl_thumbnail_cont);
                    cs_img_douga.connect(img_douga.getId(), ConstraintSet.TOP, cl_thumbnail_cont.getId(), ConstraintSet.TOP, 0);
                    cs_img_douga.connect(img_douga.getId(), ConstraintSet.LEFT, cl_thumbnail_cont.getId(), ConstraintSet.LEFT, 0);
                    cs_img_douga.connect(img_douga.getId(), ConstraintSet.BOTTOM, cl_thumbnail_cont.getId(), ConstraintSet.BOTTOM, 0);
                    cs_img_douga.connect(img_douga.getId(), ConstraintSet.RIGHT, cl_thumbnail_cont.getId(), ConstraintSet.RIGHT, 0);
                    cs_img_douga.applyTo(cl_thumbnail_cont);

                    ConstraintLayout.LayoutParams frameparams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);

                    LinearLayout ll_blackframe = new LinearLayout(getActivity());
                    ll_blackframe.setId(getView().generateViewId());
                    ll_blackframe.setBackgroundColor(Color.parseColor("#ff000000"));
                    ll_blackframe.setAlpha(0.5f);
                    ll_blackframe.setLayoutParams(frameparams);
                    cl_thumbnail_cont.addView(ll_blackframe);


                    ConstraintSet cs_blackframe = new ConstraintSet();
                    cs_blackframe.clone(cl_thumbnail_cont);
                    cs_blackframe.connect(ll_blackframe.getId(), ConstraintSet.TOP, cl_thumbnail_cont.getId(), ConstraintSet.TOP, 0);
                    cs_blackframe.connect(ll_blackframe.getId(), ConstraintSet.LEFT, cl_thumbnail_cont.getId(), ConstraintSet.LEFT, 0);
                    cs_blackframe.connect(ll_blackframe.getId(), ConstraintSet.BOTTOM, cl_thumbnail_cont.getId(), ConstraintSet.BOTTOM, 0);
                    cs_blackframe.connect(ll_blackframe.getId(), ConstraintSet.RIGHT, cl_thumbnail_cont.getId(), ConstraintSet.RIGHT, 0);
                    cs_blackframe.applyTo(cl_thumbnail_cont);

                    ConstraintLayout.LayoutParams buttonparams = new ConstraintLayout.LayoutParams(
                            getDPscale(100), getDPscale(100));

                    Button btn_play = new Button(getActivity());
                    btn_play.setId(ButtonID + i);
                    btn_play.setBackground(getContext().getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
                    btn_play.setAlpha(0.75f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btn_play.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.button_light_tint));
                    }
                    btn_play.setLayoutParams(buttonparams);
                    btn_play.setOnClickListener(videoOnclickListener);
                    cl_thumbnail_cont.addView(btn_play);

                    ConstraintSet cs_btn_play = new ConstraintSet();
                    cs_btn_play.clone(cl_thumbnail_cont);
                    cs_btn_play.connect(btn_play.getId(), ConstraintSet.TOP, cl_thumbnail_cont.getId(), ConstraintSet.TOP, 0);
                    cs_btn_play.connect(btn_play.getId(), ConstraintSet.LEFT, cl_thumbnail_cont.getId(), ConstraintSet.LEFT, 0);
                    cs_btn_play.connect(btn_play.getId(), ConstraintSet.BOTTOM, cl_thumbnail_cont.getId(), ConstraintSet.BOTTOM, 0);
                    cs_btn_play.connect(btn_play.getId(), ConstraintSet.RIGHT, cl_thumbnail_cont.getId(), ConstraintSet.RIGHT, 0);
                    cs_btn_play.applyTo(cl_thumbnail_cont);

                    ll_videolist.addView(cl_thumbnail_cont);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    private void SetPageGauge() {
        if (sliderimgfiles.size() <= 1) {
            txtView_indicators.setVisibility(View.GONE);
        } else {
            txtView_indicators.setVisibility(View.VISIBLE);
            String S = "";
            for (int n = 0; n < sliderimgfiles.size(); n++) {
                if (n == currentImg) {
                    S += "???";
                } else {
                    S += "???";
                }
            }
            txtView_indicators.setText(S);
        }
    }

    /*MEDIA PROCESSING*/
    private int bgTaskloadimages(){
        try
        {
            if(panoramaimgfiles.size()>0) {
                for (int n = 0; n < panoramaimgfiles.size(); n++) {
                    String url = DOMAIN_NAME + panoramaimgfiles.get(n);
                    panoramaimg.add(httpBitmapGET(url));
                }
            }
        }
        catch (Exception ex)
        {
            return -1;
        }
        return 1;
    }

    private void postBgTask(int result){
        if (result == 1) {
            for(int i =0; i<panoramaimg.size();i++){
                TextView tv_comment = new TextView(getActivity());
                tv_comment.setText("???" + panoramacomment.get(i));
                tv_comment.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                tv_comment.setBackgroundColor(Color.parseColor("#ffffff"));
                ll_panoramaimg.addView(tv_comment);

                int imgpanoramapixelDrawableSizeW = Math.round(ll_panoramaimg.getMeasuredWidth());
                int imgpanoramapixelDrawableSizeH = (int) Math.round((double) panoramaimg.get(i).getHeight() * ((double) ll_panoramaimg.getMeasuredWidth() / (double) panoramaimg.get(i).getWidth()));

                ConstraintLayout cl_panoramaimg = new ConstraintLayout(getActivity());
                cl_panoramaimg.setId(getView().generateViewId());

                ConstraintLayout.LayoutParams clpcontactUs = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

                ImageView imgpanorama = new ImageView(getActivity());
                imgpanorama.setId( ImageID + i);
                LinearLayout.LayoutParams imgpanoramaparams = new LinearLayout.LayoutParams(imgpanoramapixelDrawableSizeW, imgpanoramapixelDrawableSizeH);
                imgpanorama.setLayoutParams(imgpanoramaparams);
                imgpanorama.setImageBitmap(panoramaimg.get(i));
                imgpanorama.setAdjustViewBounds(true);
                imgpanorama.setScaleType(ImageView.ScaleType.FIT_XY);
                imgpanorama.setOnClickListener(dynamicimageOnclickListener);
                cl_panoramaimg.addView(imgpanorama);

                ConstraintSet cs_imgpanorama = new ConstraintSet();
                cs_imgpanorama.clone(cl_panoramaimg);
                cs_imgpanorama.connect(imgpanorama.getId(), ConstraintSet.TOP, cl_panoramaimg.getId(), ConstraintSet.TOP, 0);
                cs_imgpanorama.connect(imgpanorama.getId(), ConstraintSet.LEFT, cl_panoramaimg.getId(), ConstraintSet.LEFT, 0);
                cs_imgpanorama.connect(imgpanorama.getId(), ConstraintSet.BOTTOM, cl_panoramaimg.getId(), ConstraintSet.BOTTOM, 0);
                cs_imgpanorama.connect(imgpanorama.getId(), ConstraintSet.RIGHT, cl_panoramaimg.getId(), ConstraintSet.RIGHT, 0);
                cs_imgpanorama.applyTo(cl_panoramaimg);

                TextView tv_disppanorama = new TextView(getActivity());
                tv_disppanorama.setText("???????????????????????????");
                tv_disppanorama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_disppanorama.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                tv_disppanorama.setBackgroundColor(Color.parseColor("#77dddddd"));
                tv_disppanorama.setId(getView().generateViewId());
                tv_disppanorama.setLayoutParams(clpcontactUs);
                cl_panoramaimg.addView(tv_disppanorama);

                ConstraintSet cs_tvdippanorama = new ConstraintSet();
                cs_tvdippanorama.clone(cl_panoramaimg);
                cs_tvdippanorama.connect(tv_disppanorama.getId(), ConstraintSet.TOP, cl_panoramaimg.getId(), ConstraintSet.TOP, 0);
                cs_tvdippanorama.connect(tv_disppanorama.getId(), ConstraintSet.LEFT, cl_panoramaimg.getId(), ConstraintSet.LEFT, 0);
                cs_tvdippanorama.connect(tv_disppanorama.getId(), ConstraintSet.BOTTOM, cl_panoramaimg.getId(), ConstraintSet.BOTTOM, 0);
                cs_tvdippanorama.connect(tv_disppanorama.getId(), ConstraintSet.RIGHT, cl_panoramaimg.getId(), ConstraintSet.RIGHT, 0);
                cs_tvdippanorama.applyTo(cl_panoramaimg);

                ll_panoramaimg.addView(cl_panoramaimg);
            }
        }
        scv_radgroup.setVisibility(View.VISIBLE);
        CD.cDismiss();
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static Bitmap httpBitmapGET(String _url) {
        try {
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    private int getDPscale(int size) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) ((double) size * scale + 0.5f));
    }

    /*AUXILIARY PROGRAMS*/
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
                    if(faveDetailsObject.getString("heya_no").equals(bukken_details.get(3)))
                        isFavorite = true;

                }
                System.out.println("is Favorite: " + isFavorite);
            }
            catch (JSONException e) {
                System.out.println("error: " + e);
            }
            System.out.println("Loaded From shared preference property list: " + favoritesDetailsArray);
            System.out.println("favorites array list:" + favoritesArray);
        }
    }

    /*public boolean isfavorite(String heyano) {
        boolean result = false;

        Iterator<String> iterator = favoritesArray.iterator();
        while (iterator.hasNext()) {
            if (heyano.equals(iterator.next())) {
                result = true;
                break;
            }
        }

        return result;
    }*/

    public void addToFavorites() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedYellowFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favoritesJSON = favoritesDetailsArray.toString();

        editor.putString("favorite list", favoritesJSON);
        editor.apply();
        System.out.println("List of favorites: " + sharedPreferences.getString("favorite list", ""));
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
            case R.id.btn_back:
                if(parentfragment.equals("")) {
                selectedFragment = new yellowAddressRentListFragment();
                }
                else if (parentfragment.equals("yellowAddressMapInformation")){
                    selectedFragment = new yellowAddressMapInformation();
                }
                else {}
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_inquirestore:
                GlobalVar.setparent_fragment("yellowAddressPropertyDetailFragment");
                GlobalVar.setTenpo_no(bukken_details.get(54)); //tenpo_no
                selectedFragment = new yellowInquireStore();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_storeareamap:
                GlobalVar.setLatitude(bukken_details.get(52));
                GlobalVar.setLongitude(bukken_details.get(53));
                openMapDialog();
                break;
            case R.id.btn_showareamap:
                GlobalVar.setLatitude(bukken_details.get(46));
                GlobalVar.setLongitude(bukken_details.get(47));
                openMapDialog();
                break;
            case R.id.btn_inquireproperty:
                GlobalVar.setparent_fragment("yellowAddressPropertyDetailFragment");
                GlobalVar.setContact_bukken_no(GlobalVar.getdetailed_bukken_no());
                GlobalVar.setTenpo_no(bukken_details.get(54)); //tenpo_no
                selectedFragment = new yellowInquireProperty();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_visitreserve:
                GlobalVar.setparent_fragment("yellowAddressPropertyDetailFragment");
                GlobalVar.setTenpo_no(bukken_details.get(54)); //tenpo_no
                selectedFragment = new yellowInquireReservation();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.tv_tenpo_tel:
                alertDialogcalltenpotel();
                break;
            case R.id.btn_ookiniri:
                createFavoritesObject();
                alertDialogAddtoFavorites();
                //disabling
                btn_ookiniri.setEnabled(false);
                btn_ookiniri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
                btn_ookiniri.setTextColor(Color.parseColor("#aaaaaa"));
                btn_ookiniri.setText("????????????");

                break;
            default:
                break;
        }
    }

    View.OnClickListener dynamicimageOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            int imageid = v.getId() - ImageID;
            boolean withimage = false;

            switch (imageid) {
                case 0:
                    GlobalVar.setpanoramabitmap(panoramaimg.get(0));
                    withimage = true;
                    break;
                case 1:
                    GlobalVar.setpanoramabitmap(panoramaimg.get(1));
                    withimage = true;
                    break;
                case 2:
                    GlobalVar.setpanoramabitmap(panoramaimg.get(2));
                    withimage = true;
                    break;
                case 3:
                    GlobalVar.setpanoramabitmap(panoramaimg.get(3));
                    withimage = true;
                    break;
                case 4:
                    GlobalVar.setpanoramabitmap(panoramaimg.get(4));
                    withimage = true;
                    break;
                case 5:
                    GlobalVar.setpanoramabitmap(panoramaimg.get(5));
                    withimage = true;
                    break;
                default:
                    break;
            }
            if (withimage) {
                D_panoramaDialog CD = new D_panoramaDialog(getContext());
                CD.cShow(false);
            }
        }
    };

    View.OnTouchListener sliderOnTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            MainActivity view = ((MainActivity) getActivity());
            //mainImgCounter = movementInterval;

            float x1 = event.getX();
            float y1 = event.getY();
            int eventAction = event.getActionMasked();

            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    tOX = x1;
                    tOY = y1;
                    return true;

                case MotionEvent.ACTION_UP:
                    if ((Math.abs(x1 - tOX) < 5) && (Math.abs(y1 - tOY) < 5)) {
                        return true;
                    } else if ((x1 - tOX) < -100) {   //Swiped to the left
                        currentImg++;
                        if (currentImg >= sliderimgfiles.size()) {
                            currentImg = sliderimgfiles.size() - 1;
                            //currentImg = 0; //go to start
                        }
                    } else if ((x1 - tOX) > 100) {   //Swiped to the right
                        currentImg--;
                        if (currentImg < 0) {
                            currentImg = 0;
                            //currentImg = sliderimgfiles.size() - 1; // go to end
                        }
                    }
                    //txtView_indicators
                    mainHSCV.smoothScrollTo(currentImg * Math.round(mainHSCV.getWidth()), 0);
                    SetPageGauge();

                    return true;
            }

            return false;
        }
    };

    View.OnClickListener imgbuttonOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            MainActivity view = ((MainActivity) getActivity());
            Fragment selectedFragment = null;

            switch (v.getId()) {
                case R.id.btn_lastimg:
                    currentImg--;
                    if (currentImg < 0) {
                        currentImg = 0;
                        //currentImg = sliderimgfiles.size() - 1;
                    }
                    mainHSCV.smoothScrollTo(currentImg * Math.round(mainHSCV.getWidth()), 0);
                    SetPageGauge();
                    break;
                case R.id.btn_nextimg:
                    currentImg++;
                    if (currentImg >= sliderimgfiles.size()) {
                        currentImg = sliderimgfiles.size() - 1;
                        //currentImg = 0;
                    }
                    mainHSCV.smoothScrollTo(currentImg * Math.round(mainHSCV.getWidth()), 0);
                    SetPageGauge();
                    break;
                default:
                    break;
            }
        }
    };
    View.OnClickListener videoOnclickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            MainActivity view = ((MainActivity) getActivity());

            for (int i = 0; i < moviefiles.size(); i++) {
                if (((v.getId() - ImageID) == i) || ((v.getId() - ButtonID) == i)) {
                    if (moviefiles.size() > 0) {
                        GlobalVar.setmovie_files(moviefiles.get(i));
                        D_videoDialog CD = new D_videoDialog(getActivity());
                        CD.cShow(false);
                    }
                    break;
                }
            }
        }
    };

    /*DIALOGS*/
    private void alertDialogcalltenpotel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "?????????????????? " + bukken_details.get(38) + " ????????????????????????????????????";
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri u = Uri.parse("tel:" + bukken_details.get(38));

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
        builder.setNegativeButton("?????????`", new DialogInterface.OnClickListener() {
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
        DialogFragment mapFragment = yellowMapProperty.newInstance();
        mapFragment.show(getFragmentManager(), "dialog");
    }

    private void alertDialogAddtoFavorites() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("??????????????????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
    private void createFavoritesObject() {

        try {
            JSONObject favoriteDetailsObject = new JSONObject();
            Random rand = new Random();
            int randId = rand.nextInt(1000000);

            favoriteDetailsObject.put("tenpo_no1", bukken_details.get(54));
            favoriteDetailsObject.put("shozaichi", bukken_details.get(10));
            favoriteDetailsObject.put("bukkenName", bukken_details.get(0));
            favoriteDetailsObject.put("madori", bukken_details.get(8));
            favoriteDetailsObject.put("bukken_type_name", bukken_details.get(62));
            favoriteDetailsObject.put("shikikin", bukken_details.get(6));
            favoriteDetailsObject.put("heya_no", bukken_details.get(3));
            favoriteDetailsObject.put("random_id", randId);

            favoritesDetailsArray.put(favoriteDetailsObject);
            addToFavorites();

        } catch (JSONException e) {

        }
    }
}

