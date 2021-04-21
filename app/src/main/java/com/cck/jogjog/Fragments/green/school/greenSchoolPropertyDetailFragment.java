package com.cck.jogjog.Fragments.green.school;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Dialog.D_cDialog;
import com.cck.jogjog.Dialog.D_panoramaDialog;
import com.cck.jogjog.Dialog.D_videoDialog;
import com.cck.jogjog.Fragments.green.common.greenInquireProperty;
import com.cck.jogjog.Fragments.green.common.greenInquireReservation;
import com.cck.jogjog.Fragments.green.common.greenInquireStore;
import com.cck.jogjog.Fragments.green.common.greenMapProperty;
import com.cck.jogjog.Fragments.green.location.greenLocationRentListFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
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

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_DETAIL;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_SELL;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class greenSchoolPropertyDetailFragment extends Fragment implements View.OnClickListener {
    /* OBJECTS */
    //COMMON//
    private LinearLayout scroll_MM, ll_images, ll_bukken_type_bukken_name, ll_bukken_10m_fee, ll_schools,
            ll_shuhen_shisetsu, ll_shuhen_shisetsu_list, ll_setsubi, ll_public_memo, ll_panorama, ll_panoramaimg,
            ll_video, ll_videolist;
    private ConstraintLayout cl_eki1, cl_eki2, cl_eki3;
    private Button btn_ookiniri, btn_showareamap, btn_loan , btn_inquireproperty, btn_inquirestore, btn_visitreserve, btn_storeareamap, btn_back, btn_lastimg, btn_nextimg;
    private TextView tv_pr_memo, tv_bukken_type, tv_bukken_name, tv_heya_update_date, tv_next_heya_update_date, tv_bukken_10m_fee, tv_bukken_10k_fee, tv_shozaichi,
            tv_eki1, tv_eki2, tv_eki3, tv_bukkentypename, tv_genkyou_name, tv_hikiwatashi_name, tv_torihiki_name, tv_bukken_kouzou_name, tv_park_type_name_park_free_park_fee,
            tv_primary_school_district, tv_junior_high_school_district, tv_setsubi, tv_public_memo, tv_tenpo_name_yobi2, tv_licence, tv_tenpo_tel, tv_tenpo_fax,tv_tenpo_shozaichi,
            tv_tenpo_eigyo_jikan, tv_tenpo_teikyubi, txtView_indicators;
    private ImageView img_shop_gaikan;
    private HorizontalScrollView mainHSCV;
    private NestedScrollView scv_radgroup;

    // CATEGORY 1 //
    private LinearLayout ll_madori, ll_senyuu, ll_balcony, ll_kai, ll_tatamono_kansei_cat1, ll_bukken_room_num_cat1, ll_kaidate, ll_sekou_company_name, ll_kanri_keitai_name,
            ll_kanri_fee, ll_shuzen_fee;
    private TextView tv_madori_text, tv_senyuu_m2_senyuu_m2_tsubo, tv_balcony_m2_balcony_m2_tsubo, tv_kai, tv_tatemono_kansei_year_tatemono_kansei_month_cat1,
            tv_bukken_room_num_cat1, tv_kaidate_upper_kaidate_under, tv_sekou_company_name, tv_kanri_keitai_name, tv_kanri_fee, tv_shuzen_fee;

    // CATEGORY 2//
    private LinearLayout ll_max_income_yield, ll_lot_area, ll_tochi, ll_private_road, ll_tatamono_kansei_cat2, ll_bukken_room_num_cat2, ll_kaidate_upper,
            ll_kaidate_under, ll_toshi_keikaku_name, ll_youto_chiiki, ll_toshi_keikaku_memo, ll_chimoku, ll_tochi_kenri_name, ll_setsudo_name;
    private TextView tv_max_income_max_yield, tv_lot_area_m2_lot_area_m2_tsubo, tv_tochi_m2_tochi_m2_tsubo, tv_include_private_road,
            tv_tatemono_kansei_year_tatemono_kansei_month_cat2, tv_bukken_room_num_cat2, tv_kaidate_upper, tv_kaidate_under, tv_toshi_keikaku_name,
            tv_youto_chiiki, tv_toshi_keikaku_memo, tv_chimoku_name, tv_tochi_kenri_name, tv_setsudo_name;

    /* ARRAYS */
    List<String> bukken_details = new ArrayList<>();
    List<String> setsubi = new ArrayList<>();
    List<String> panoramacomment = new ArrayList<>();
    List<String> panoramaimgfiles = new ArrayList<>();
    List<Bitmap> panoramaimg = new ArrayList<>();
    List<List<String>> sliderimgfiles = new ArrayList<>();
    List<List<String>> shunenshisetsu = new ArrayList<>();
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
    private boolean isFavorite = false;

    /*START*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_green_property_detailed, container, false);
        clearlists();
        initializelayout(view);
        getProperties();
        parentfragment = GlobalVar.previousScreen;
        GlobalVar.previousScreen = "greenSchoolPropertyDetailFragment";
        GlobalVar.isFromMap = false;
        scv_radgroup.setVisibility(View.INVISIBLE);
        CD = new D_cDialog(getContext());
        CD.pbWait.setVisibility(View.VISIBLE);
        CD.cShow(false);
        return view;
    }

    /*VIEW INITIALIZATION*/
    private void initializelayout(View view) {
        //COMMON//
        scv_radgroup = view.findViewById(R.id.scv_radgroup);

        scroll_MM = view.findViewById(R.id.scroll_MM);
        ll_images = view.findViewById(R.id.ll_images);
        ll_bukken_type_bukken_name = view.findViewById(R.id.ll_bukken_type_bukken_name);
        ll_bukken_10m_fee = view.findViewById(R.id.ll_bukken_10m_fee);
        ll_schools = view.findViewById(R.id.ll_schools);
        ll_shuhen_shisetsu = view.findViewById(R.id.ll_shuhen_shisetsu);
        ll_shuhen_shisetsu_list = view.findViewById(R.id.ll_shuhen_shisetsu_list);
        ll_setsubi = view.findViewById(R.id.ll_setsubi);
        ll_public_memo = view.findViewById(R.id.ll_public_memo);
        ll_panoramaimg = view.findViewById(R.id.ll_panoramaimg);
        ll_panorama = view.findViewById(R.id.ll_panorama);
        ll_video = view.findViewById(R.id.ll_video);
        ll_videolist = view.findViewById(R.id.ll_videolist);

        cl_eki1 = view.findViewById(R.id.cl_eki1);
        cl_eki2 = view.findViewById(R.id.cl_eki2);
        cl_eki3 = view.findViewById(R.id.cl_eki3);

        tv_pr_memo = view.findViewById(R.id.tv_pr_memo);
        tv_bukken_type = view.findViewById(R.id.tv_bukken_type);
        tv_bukken_name = view.findViewById(R.id.tv_bukken_name);
        tv_heya_update_date = view.findViewById(R.id.tv_heya_update_date);
        tv_next_heya_update_date = view.findViewById(R.id.tv_next_heya_update_date);
        tv_bukken_10m_fee = view.findViewById(R.id.tv_bukken_10m_fee);
        tv_bukken_10k_fee = view.findViewById(R.id.tv_bukken_10k_fee);
        tv_shozaichi = view.findViewById(R.id.tv_shozaichi);
        tv_eki1 = view.findViewById(R.id.tv_eki1);
        tv_eki2 = view.findViewById(R.id.tv_eki2);
        tv_eki3 = view.findViewById(R.id.tv_eki3);
        tv_bukkentypename = view.findViewById(R.id.tv_bukkentypename);
        tv_genkyou_name = view.findViewById(R.id.tv_genkyou_name);
        tv_hikiwatashi_name = view.findViewById(R.id.tv_hikiwatashi_name);
        tv_torihiki_name = view.findViewById(R.id.tv_torihiki_name);
        tv_bukken_kouzou_name = view.findViewById(R.id.tv_bukken_kouzou_name);
        tv_park_type_name_park_free_park_fee = view.findViewById(R.id.tv_park_type_name_park_free_park_fee);
        tv_primary_school_district = view.findViewById(R.id.tv_primary_school_district);
        tv_junior_high_school_district = view.findViewById(R.id.tv_junior_high_school_district);
        tv_setsubi = view.findViewById(R.id.tv_setsubi);
        tv_public_memo = view.findViewById(R.id.tv_public_memo);
        tv_tenpo_name_yobi2 = view.findViewById(R.id.tv_tenpo_name_yobi2);
        tv_licence = view.findViewById(R.id.tv_licence);
        tv_tenpo_tel = view.findViewById(R.id.tv_tenpo_tel);
        tv_tenpo_fax = view.findViewById(R.id.tv_tenpo_fax);
        tv_tenpo_shozaichi = view.findViewById(R.id.tv_tenpo_shozaichi);
        tv_tenpo_eigyo_jikan = view.findViewById(R.id.tv_tenpo_eigyo_jikan);
        tv_tenpo_teikyubi = view.findViewById(R.id.tv_tenpo_teikyubi);
        txtView_indicators = view.findViewById(R.id.txtView_indicators);

        btn_ookiniri = view.findViewById(R.id.btn_ookiniri);
        btn_showareamap = view.findViewById(R.id.btn_showareamap);
        btn_loan = view.findViewById(R.id.btn_loan);
        btn_inquireproperty = view.findViewById(R.id.btn_inquireproperty);
        btn_inquirestore = view.findViewById(R.id.btn_inquirestore);
        btn_visitreserve = view.findViewById(R.id.btn_visitreserve);
        btn_storeareamap = view.findViewById(R.id.btn_storeareamap);
        btn_back = view.findViewById(R.id.btn_back);
        btn_lastimg = view.findViewById(R.id.btn_lastimg);
        btn_nextimg = view.findViewById(R.id.btn_nextimg);

        mainHSCV = view.findViewById(R.id.scroll_main);

        img_shop_gaikan = view.findViewById(R.id.img_shop_gaikan);

        // CATEGORY 1 //
        ll_madori = view.findViewById(R.id.ll_madori);
        ll_senyuu = view.findViewById(R.id.ll_senyuu);
        ll_balcony = view.findViewById(R.id.ll_balcony);
        ll_kai = view.findViewById(R.id.ll_kai);
        ll_tatamono_kansei_cat1 = view.findViewById(R.id.ll_tatamono_kansei_cat1);
        ll_bukken_room_num_cat1 = view.findViewById(R.id.ll_bukken_room_num_cat1);
        ll_kaidate = view.findViewById(R.id.ll_kaidate);
        ll_sekou_company_name = view.findViewById(R.id.ll_sekou_company_name);
        ll_kanri_keitai_name = view.findViewById(R.id.ll_kanri_keitai_name);
        ll_kanri_fee = view.findViewById(R.id.ll_kanri_fee);
        ll_shuzen_fee = view.findViewById(R.id.ll_shuzen_fee);

        tv_madori_text = view.findViewById(R.id.tv_madori_text);
        tv_senyuu_m2_senyuu_m2_tsubo = view.findViewById(R.id.tv_senyuu_m2_senyuu_m2_tsubo);
        tv_balcony_m2_balcony_m2_tsubo = view.findViewById(R.id.tv_balcony_m2_balcony_m2_tsubo);
        tv_kai = view.findViewById(R.id.tv_kai);
        tv_tatemono_kansei_year_tatemono_kansei_month_cat1 = view.findViewById(R.id.tv_tatemono_kansei_year_tatemono_kansei_month_cat1);
        tv_bukken_room_num_cat1 = view.findViewById(R.id.tv_bukken_room_num_cat1);
        tv_kaidate_upper_kaidate_under = view.findViewById(R.id.tv_kaidate_upper_kaidate_under);
        tv_sekou_company_name = view.findViewById(R.id.tv_sekou_company_name);
        tv_kanri_keitai_name = view.findViewById(R.id.tv_kanri_keitai_name);
        tv_kanri_fee = view.findViewById(R.id.tv_kanri_fee);
        tv_shuzen_fee = view.findViewById(R.id.tv_shuzen_fee);

        // CATEGORY 2 //
        ll_max_income_yield = view.findViewById(R.id.ll_max_income_yield);
        ll_lot_area = view.findViewById(R.id.ll_lot_area);
        ll_tochi = view.findViewById(R.id.ll_tochi);
        ll_private_road = view.findViewById(R.id.ll_private_road);
        ll_tatamono_kansei_cat2 = view.findViewById(R.id.ll_tatamono_kansei_cat2);
        ll_bukken_room_num_cat2 = view.findViewById(R.id.ll_bukken_room_num_cat2);
        ll_kaidate_upper = view.findViewById(R.id.ll_kaidate_upper);
        ll_kaidate_under = view.findViewById(R.id.ll_kaidate_under);
        ll_toshi_keikaku_name = view.findViewById(R.id.ll_toshi_keikaku_name);
        ll_youto_chiiki = view.findViewById(R.id.ll_youto_chiiki);
        ll_toshi_keikaku_memo = view.findViewById(R.id.ll_toshi_keikaku_memo);
        ll_chimoku = view.findViewById(R.id.ll_chimoku);
        ll_tochi_kenri_name = view.findViewById(R.id.ll_tochi_kenri_name);
        ll_setsudo_name = view.findViewById(R.id.ll_setsudo_name);

        tv_max_income_max_yield = view.findViewById(R.id.tv_max_income_max_yield);
        tv_lot_area_m2_lot_area_m2_tsubo = view.findViewById(R.id.tv_lot_area_m2_lot_area_m2_tsubo);
        tv_tochi_m2_tochi_m2_tsubo = view.findViewById(R.id.tv_tochi_m2_tochi_m2_tsubo);
        tv_include_private_road = view.findViewById(R.id.tv_include_private_road);
        tv_tatemono_kansei_year_tatemono_kansei_month_cat2 = view.findViewById(R.id.tv_tatemono_kansei_year_tatemono_kansei_month_cat2);
        tv_bukken_room_num_cat2 = view.findViewById(R.id.tv_bukken_room_num_cat2);
        tv_kaidate_upper = view.findViewById(R.id.tv_kaidate_upper);
        tv_kaidate_under = view.findViewById(R.id.tv_kaidate_under);
        tv_toshi_keikaku_name = view.findViewById(R.id.tv_toshi_keikaku_name);
        tv_youto_chiiki = view.findViewById(R.id.tv_youto_chiiki);
        tv_toshi_keikaku_memo = view.findViewById(R.id.tv_toshi_keikaku_memo);
        tv_chimoku_name = view.findViewById(R.id.tv_chimoku_name);
        tv_tochi_kenri_name = view.findViewById(R.id.tv_tochi_kenri_name);
        tv_setsudo_name = view.findViewById(R.id.tv_setsudo_name);

        mainHSCV.setOnTouchListener(sliderOnTouchListener);
        btn_ookiniri.setOnClickListener(this);
        btn_showareamap.setOnClickListener(this);
        btn_loan.setOnClickListener(this);
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
        setsubi.clear();
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
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_DETAIL + API_PATH_TYPE_SELL + "&bukken_no=" + bukken_no;
        //String url = "https://jogjog.goalway.jp/api/appapi.php?key=jFB0eP4pncSEe3SVYcrOIcpoAWZcw2OX&Processing=get_bukken_detail&type=chintai&bukken_no=00017307004";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject returnData = jsonObject.getJSONObject("data");

                    if (!returnData.isNull("bukken_no")) {
                        bukken_details.add(returnData.getString("bukken_no"));    // index:0
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bukken_category_no")) {
                        bukken_details.add(returnData.getString("bukken_category_no"));    // index:1
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("pr_memo")) {
                        bukken_details.add(returnData.getString("pr_memo"));    // index:2
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bukken_type_name")) {
                        bukken_details.add(returnData.getString("bukken_type_name"));    // index:3
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bukken_name")) {
                        bukken_details.add(returnData.getString("bukken_name"));    // index:4
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("heya_update_date")) {
                        bukken_details.add(returnData.getString("heya_update_date"));    // index:5
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("next_heya_update_date")) {
                        bukken_details.add(returnData.getString("next_heya_update_date"));    // index:6
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bukken_10m_fee")) {
                        bukken_details.add(returnData.getString("bukken_10m_fee"));    // index:7
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bukken_10k_fee")) {
                        bukken_details.add(returnData.getString("bukken_10k_fee"));    // index:8
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("max_income")) {
                        bukken_details.add(returnData.getString("max_income"));    // index:9
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("max_yield")) {
                        bukken_details.add(returnData.getString("max_yield"));    // index:10
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("madori_text")) {
                        bukken_details.add(returnData.getString("madori_text"));    // index:11
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("senyuu_m2")) {
                        bukken_details.add(returnData.getString("senyuu_m2"));    // index:12
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("senyuu_m2_tsubo")) {
                        bukken_details.add(returnData.getString("senyuu_m2_tsubo"));    // index:13
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("balcony_m2")) {
                        bukken_details.add(returnData.getString("balcony_m2"));    // index:14
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("balcony_m2_tsubo")) {
                        bukken_details.add(returnData.getString("balcony_m2_tsubo"));    // index:15
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("lot_area_m2")) {
                        bukken_details.add(returnData.getString("lot_area_m2"));    // index:16
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("lot_area_m2_tsubo")) {
                        bukken_details.add(returnData.getString("lot_area_m2_tsubo"));    // index:17
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tochi_m2")) {
                        bukken_details.add(returnData.getString("tochi_m2"));    // index:18
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tochi_m2_tsubo")) {
                        bukken_details.add(returnData.getString("tochi_m2_tsubo"));    // index:19
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("include_private_road")) {
                        bukken_details.add(returnData.getString("include_private_road"));    // index:20
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("address1")) {
                        bukken_details.add(returnData.getString("address1"));    // index:21
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("address2")) {
                        bukken_details.add(returnData.getString("address2"));    // index:22
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("address3")) {
                        bukken_details.add(returnData.getString("address3"));    // index:23
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("line_name1")) {
                        bukken_details.add(returnData.getString("line_name1"));    // index:24
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("station_name1")) {
                        bukken_details.add(returnData.getString("station_name1"));    // index:25
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("eki_min1")) {
                        bukken_details.add(returnData.getString("eki_min1"));    // index:26
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("line_name2")) {
                        bukken_details.add(returnData.getString("line_name2"));    // index:27
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("station_name2")) {
                        bukken_details.add(returnData.getString("station_name2"));    // index:28
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("eki_min2")) {
                        bukken_details.add(returnData.getString("eki_min2"));    // index:29
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("line_name3")) {
                        bukken_details.add(returnData.getString("line_name3"));    // index:30
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("station_name3")) {
                        bukken_details.add(returnData.getString("station_name3"));    // index:31
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("eki_min3")) {
                        bukken_details.add(returnData.getString("eki_min3"));    // index:32
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_name1")) {
                        bukken_details.add(returnData.getString("bus_name1"));    // index:33
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_stop_name1")) {
                        bukken_details.add(returnData.getString("bus_stop_name1"));    // index:34
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_min1")) {
                        bukken_details.add(returnData.getString("bus_min1"));    // index:35
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_ride_min1")) {
                        bukken_details.add(returnData.getString("bus_ride_min1"));    // index:36
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_name2")) {
                        bukken_details.add(returnData.getString("bus_name2"));    // index:37
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_stop_name2")) {
                        bukken_details.add(returnData.getString("bus_stop_name2"));    // index:38
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_min2")) {
                        bukken_details.add(returnData.getString("bus_min2"));    // index:39
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_ride_min2")) {
                        bukken_details.add(returnData.getString("bus_ride_min2"));    // index:40
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_name3")) {
                        bukken_details.add(returnData.getString("bus_name3"));    // index:41
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_stop_name3")) {
                        bukken_details.add(returnData.getString("bus_stop_name3")); //index:42
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_min3")) {
                        bukken_details.add(returnData.getString("bus_min3")); //index:43
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bus_ride_min3")) {
                        bukken_details.add(returnData.getString("bus_ride_min3")); //index:44
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("genkyou_name")) {
                        bukken_details.add(returnData.getString("genkyou_name")); //index:45
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("hikiwatashi_name")) {
                        bukken_details.add(returnData.getString("hikiwatashi_name")); //index:46
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("torihiki_name")) {
                        bukken_details.add(returnData.getString("torihiki_name")); //index:47
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tatemono_kansei_year")) {
                        bukken_details.add(returnData.getString("tatemono_kansei_year")); //index:48
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tatemono_kansei_month")) {
                        bukken_details.add(returnData.getString("tatemono_kansei_month")); //index:49
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bukken_room_num")) {
                        bukken_details.add(returnData.getString("bukken_room_num")); //index:50
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("kaidate_upper")) {
                        bukken_details.add(returnData.getString("kaidate_upper")); //index:51
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("kaidate_under")) {
                        bukken_details.add(returnData.getString("kaidate_under")); //index:52
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("kai")) {
                        bukken_details.add(returnData.getString("kai")); //index:53
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("bukken_kouzou_name")) {
                        bukken_details.add(returnData.getString("bukken_kouzou_name")); //index:54
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("sekou_company_name")) {
                        bukken_details.add(returnData.getString("sekou_company_name")); //index:55
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("kanri_keitai_name")) {
                        bukken_details.add(returnData.getString("kanri_keitai_name")); //index:56
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("kanri_fee")) {
                        bukken_details.add(returnData.getString("kanri_fee")); //index:57
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("shuzen_fee")) {
                        bukken_details.add(returnData.getString("shuzen_fee")); //index:58
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("park_type_name")) {
                        bukken_details.add(returnData.getString("park_type_name")); //index:59
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("park_free")) {
                        bukken_details.add(returnData.getString("park_free")); //index:60
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("park_fee")) {
                        bukken_details.add(returnData.getString("park_fee")); //index:61
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("toshi_keikaku_name")) {
                        bukken_details.add(returnData.getString("toshi_keikaku_name")); //index:62
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("youto_chiiki")) {
                        bukken_details.add(returnData.getString("youto_chiiki")); //index:63
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("toshi_keikaku_memo")) {
                        bukken_details.add(returnData.getString("toshi_keikaku_memo")); //index:64
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("chimoku_name")) {
                        bukken_details.add(returnData.getString("chimoku_name")); //index:65
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tochi_kenri_name")) {
                        bukken_details.add(returnData.getString("tochi_kenri_name")); //index:66
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("setsudo_name")) {
                        bukken_details.add(returnData.getString("setsudo_name")); //index:67
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("primary_school_district")) {
                        bukken_details.add(returnData.getString("primary_school_district")); //index:68
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("primary_school_distance")) {
                        bukken_details.add(returnData.getString("primary_school_distance")); //index:69
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("primary_school_min")) {
                        bukken_details.add(returnData.getString("primary_school_min")); //index:70
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("junior_high_school_district")) {
                        bukken_details.add(returnData.getString("junior_high_school_district")); //index:71
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("junior_high_school_distance")) {
                        bukken_details.add(returnData.getString("junior_high_school_distance")); //index:72
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("junior_high_school_min")) {
                        bukken_details.add(returnData.getString("junior_high_school_min")); //index:73
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("public_memo")) {
                        bukken_details.add(returnData.getString("public_memo")); //index:74
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_name")) {
                        bukken_details.add(returnData.getString("tenpo_name"));    // index:75
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("yobi2")) {
                        bukken_details.add(returnData.getString("yobi2"));    // index:76
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("licence")) {
                        bukken_details.add(returnData.getString("licence"));    // index:77
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("shop_gaikan_url")) {
                        bukken_details.add(returnData.getString("shop_gaikan_url")); //index:78
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_tel")) {
                        bukken_details.add(returnData.getString("tenpo_tel")); //index:79
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_fax")) {
                        bukken_details.add(returnData.getString("tenpo_fax")); //index:80
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_shozaichi")) {
                        bukken_details.add(returnData.getString("tenpo_shozaichi")); //index:81
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_eigyo_jikan")) {
                        bukken_details.add(returnData.getString("tenpo_eigyo_jikan")); //index:82
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_teikyubi")) {
                        bukken_details.add(returnData.getString("tenpo_teikyubi")); //index:83
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("lat")) {
                        bukken_details.add(returnData.getString("lat")); //index:84 lat
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("lon")) {
                        bukken_details.add(returnData.getString("lon")); //index:85 lon
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_ido_hokui")) {
                        bukken_details.add(returnData.getString("tenpo_ido_hokui")); //index:86 store lat
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_keido_tokei")) {
                        bukken_details.add(returnData.getString("tenpo_keido_tokei")); //index:87 store lon
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("tenpo_no")) {
                        bukken_details.add(returnData.getString("tenpo_no")); //index:88 store lon
                    } else {
                        bukken_details.add("");
                    }
                    if (!returnData.isNull("balcony_muki_name")) {
                        bukken_details.add(returnData.getString("balcony_muki_name")); //index:89
                    } else {
                        bukken_details.add("");
                    }

                    if (!returnData.isNull("setsubi")) {
                        JSONArray setsubilist = returnData.getJSONArray("setsubi");
                        for (int i = 0; i < setsubilist.length(); i++) {
                            JSONObject returnsetsubilist = setsubilist.getJSONObject(i);
                            if (!returnsetsubilist.isNull("name")) {
                                setsubi.add(returnsetsubilist.getString("name"));
                            }
                        }
                    }

                    //slider
                    if (!returnData.isNull("image_list")) {
                        JSONArray setsubilist = returnData.getJSONArray("image_list");
                        for (int j = 0; j < setsubilist.length(); j++) {
                            JSONObject returnsetsubilist = setsubilist.getJSONObject(j);
                            if (!returnsetsubilist.isNull("path")) {
                                sliderimgfiles.add(new ArrayList());
                                sliderimgfiles.get(j).add(returnsetsubilist.getString("path"));
                                sliderimgfiles.get(j).add(returnsetsubilist.getString("file_name"));
                            }
                            else{
                                break;
                            }
                        }
                    }

                    //shunenshisetsu
                    if (!returnData.isNull("shuhen_shisetsu_1")) {
                        for (int l = 1; l <= 10; l++) {
                            String shuhen_shisetsu = "shuhen_shisetsu_" + l;
                            String shuhen_shisetsu_dist = "shuhen_shisetsu_" + l + "_distance";
                            String shuhen_shisetsu_min = "shuhen_shisetsu_" + l + "_min";
                            if ((!returnData.isNull(shuhen_shisetsu)) && (!returnData.getString(shuhen_shisetsu).equals(""))) {
                                shunenshisetsu.add(new ArrayList());
                                shunenshisetsu.get(l - 1).add(returnData.getString(shuhen_shisetsu));
                                shunenshisetsu.get(l - 1).add(returnData.getString(shuhen_shisetsu_dist));
                                shunenshisetsu.get(l - 1).add(returnData.getString(shuhen_shisetsu_min));
                            } else {
                                break;
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
                    scv_radgroup.setVisibility(View.VISIBLE);
                    CD.cDismiss();
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
            btn_ookiniri.setText("登録済み");
        }
        if((!(bukken_details.get(7).equals("")))&&(!(bukken_details.get(7).equals("null")))) {
            tv_pr_memo.setText(bukken_details.get(2));
        }
        else {
            tv_pr_memo.setVisibility(View.GONE);
        }

        String bukkentype;
        if(bukken_details.get(1).equals("1")){
            ll_bukken_type_bukken_name.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams tv_bukken_typeparams = (LinearLayout.LayoutParams) tv_bukken_type.getLayoutParams();
            tv_bukken_typeparams.setMargins(getDPscale(20),0,0,0);
            tv_bukken_type.setLayoutParams(tv_bukken_typeparams);

            LinearLayout.LayoutParams tv_bukken_nameparams = (LinearLayout.LayoutParams) tv_bukken_name.getLayoutParams();
            tv_bukken_nameparams.setMargins(0,0,getDPscale(20),0);
            tv_bukken_name.setLayoutParams(tv_bukken_nameparams);
            bukkentype = bukken_details.get(3);
        }
        else {
            ll_bukken_type_bukken_name.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams tv_bukken_typeparams = (LinearLayout.LayoutParams) tv_bukken_type.getLayoutParams();
            tv_bukken_typeparams.setMargins(getDPscale(20),0,getDPscale(20),0);
            tv_bukken_type.setLayoutParams(tv_bukken_typeparams);

            LinearLayout.LayoutParams tv_bukken_nameparams = (LinearLayout.LayoutParams) tv_bukken_name.getLayoutParams();
            tv_bukken_nameparams.setMargins(getDPscale(20),0,getDPscale(20),0);
            tv_bukken_name.setLayoutParams(tv_bukken_nameparams);
            bukkentype = "事業用・収益物件" + bukken_details.get(3);
        }
        tv_bukken_type.setText(bukkentype);
        tv_bukken_name.setText(bukken_details.get(4));

        tv_heya_update_date.setText("情報公開日 ：" + bukken_details.get(5));
        tv_next_heya_update_date.setText("次回更新予定日 ：" + bukken_details.get(6));

        if((!(bukken_details.get(7).equals("")))&&(!(bukken_details.get(7).equals("null")))&&(!(bukken_details.get(7).equals("0")))){
            tv_bukken_10m_fee.setText(bukken_details.get(7));
        }
        else{
            ll_bukken_10m_fee.setVisibility(View.GONE);
        }

        //tv_bukken_10k_fee.setText(String.format("%,d", bukken_details.get(8)));
        tv_bukken_10k_fee.setText(format.format(Double.parseDouble(bukken_details.get(8))));

        if(bukken_details.get(1).equals("1")) {
            ll_max_income_yield.setVisibility(View.GONE);
            ll_lot_area.setVisibility(View.GONE);
            ll_tochi.setVisibility(View.GONE);
            ll_private_road.setVisibility(View.GONE);

            tv_madori_text.setText(bukken_details.get(11));

            String senyuu = bukken_details.get(12) + "m\u00B2(" + bukken_details.get(13) + ")";
            tv_senyuu_m2_senyuu_m2_tsubo.setText(senyuu);

            String balcony = bukken_details.get(14) + "m\u00B2(" + bukken_details.get(15) + ")";
            if((!(bukken_details.get(58).equals("")))&&(!(bukken_details.get(58).equals("null")))) {
                balcony = balcony + "\n" + bukken_details.get(58) + "向き";
            }
            tv_balcony_m2_balcony_m2_tsubo.setText(balcony);
        }
        else {
            ll_madori.setVisibility(View.GONE);
            ll_senyuu.setVisibility(View.GONE);
            ll_balcony.setVisibility(View.GONE);

            String maxincomeyeild = bukken_details.get(9) + "万円/" + bukken_details.get(10) + "%";
            tv_max_income_max_yield.setText(maxincomeyeild);

            if((!(bukken_details.get(16).equals("")))&&(!(bukken_details.get(16).equals("null")))&&(!(bukken_details.get(16).equals("0")))) {
                String lotarea = bukken_details.get(16) + "m\u00B2(" + bukken_details.get(17) + ")";
                tv_lot_area_m2_lot_area_m2_tsubo.setText(lotarea);
            }
            else{
                ll_lot_area.setVisibility(View.GONE);
            }

            if((!(bukken_details.get(18).equals("")))&&(!(bukken_details.get(18).equals("null")))&&(!(bukken_details.get(18).equals("0")))) {
                String tochi = bukken_details.get(18) + "m\u00B2(" + bukken_details.get(19) + ")";
                tv_tochi_m2_tochi_m2_tsubo.setText(tochi);
            }
            else {
                ll_tochi.setVisibility(View.GONE);
            }

            if((!(bukken_details.get(20).equals("")))&&(!(bukken_details.get(20).equals("null")))) {
                tv_include_private_road.setText(bukken_details.get(20));            }
            else {
                tv_include_private_road.setText("-");
            }
        }

        String address = "";
        if  ((!bukken_details.get(21).equals("null"))&&(!bukken_details.get(21).equals(""))) {
            address = "　" + bukken_details.get(21);
            if  ((!bukken_details.get(22).equals("null"))&&(!bukken_details.get(22).equals(""))) {
                address = address + bukken_details.get(22);
            }
            if  ((!bukken_details.get(23).equals("null"))&&(!bukken_details.get(23).equals(""))) {
                address = address + "\n(" + bukken_details.get(23) + ")";
            }
        }
        tv_shozaichi.setText(address);

        String eki1;
        if  ((!bukken_details.get(24).equals("null"))&&(!bukken_details.get(24).equals(""))) {
            eki1 = "　" + bukken_details.get(24);
            if  ((!bukken_details.get(25).equals("null"))&&(!bukken_details.get(25).equals(""))) {
                eki1 = eki1 + bukken_details.get(25);
            }
            if  ((!bukken_details.get(26).equals("null"))&&(!bukken_details.get(26).equals("")) && (!bukken_details.get(26).equals("0"))) {
                eki1 = eki1 + "徒歩" + bukken_details.get(26) + "分";
            }
            tv_eki1.setText(eki1);
        }
        else if ((!bukken_details.get(33).equals("null"))&&(!bukken_details.get(33).equals(""))) {
            eki1 = "　" + bukken_details.get(33);
            if  ((!bukken_details.get(34).equals("null"))&&(!bukken_details.get(34).equals(""))) {
                eki1 = eki1 + bukken_details.get(34);
            }
            if  ((!bukken_details.get(35).equals("null"))&&(!bukken_details.get(35).equals("")) && (!bukken_details.get(35).equals("0"))) {
                eki1 = eki1 + "徒歩" + bukken_details.get(35) + "分";
            }
            if  ((!bukken_details.get(36).equals("null"))&&(!bukken_details.get(36).equals("")) && (!bukken_details.get(36).equals("0"))) {
                eki1 = eki1 + bukken_details.get(36) + "分";
            }
            tv_eki1.setText(eki1);
        }
        else {
            cl_eki1.setVisibility(View.GONE);
        }


        String eki2;
        if  ((!bukken_details.get(27).equals("null"))&&(!bukken_details.get(27).equals(""))) {
            eki2 = "　" + bukken_details.get(27);
            if  ((!bukken_details.get(28).equals("null"))&&(!bukken_details.get(28).equals(""))) {
                eki2 = eki2 + bukken_details.get(28);
            }
            if  ((!bukken_details.get(29).equals("null"))&&(!bukken_details.get(29).equals("")) && (!bukken_details.get(29).equals("0"))) {
                eki2 = eki2 + "徒歩" + bukken_details.get(29) + "分";
            }
            tv_eki2.setText(eki2);
        }
        else if  ((!bukken_details.get(37).equals("null"))&&(!bukken_details.get(37).equals(""))) {
            eki2 = "　" + bukken_details.get(37);
            if  ((!bukken_details.get(38).equals("null"))&&(!bukken_details.get(38).equals(""))) {
                eki2 = eki2 + bukken_details.get(38);
            }
            if  ((!bukken_details.get(39).equals("null"))&&(!bukken_details.get(39).equals("")) && (!bukken_details.get(39).equals("0"))) {
                eki2 = eki2 + "徒歩" + bukken_details.get(39) + "分";
            }
            if  ((!bukken_details.get(40).equals("null"))&&(!bukken_details.get(40).equals("")) && (!bukken_details.get(40).equals("0"))) {
                eki2 = eki2 + bukken_details.get(40) + "分";
            }
            tv_eki2.setText(eki2);
        }
        else {
            cl_eki2.setVisibility(View.GONE);
        }

        String eki3;
        if  ((!bukken_details.get(30).equals("null"))&&(!bukken_details.get(30).equals(""))) {
            eki3 = "　" + bukken_details.get(30);
            if  ((!bukken_details.get(31).equals("null"))&&(!bukken_details.get(31).equals(""))) {
                eki3 = eki3 + bukken_details.get(31);
            }
            if  ((!bukken_details.get(32).equals("null"))&&(!bukken_details.get(32).equals("")) && (!bukken_details.get(32).equals("0"))) {
                eki3 = eki3 + "徒歩" + bukken_details.get(32) + "分";
            }
            tv_eki3.setText(eki3);
        }
        else if  ((!bukken_details.get(41).equals("null"))&&(!bukken_details.get(41).equals(""))) {
            eki3 = "　" + bukken_details.get(41);
            if  ((!bukken_details.get(42).equals("null"))&&(!bukken_details.get(42).equals(""))) {
                eki3 = eki3 + bukken_details.get(42);
            }
            if  ((!bukken_details.get(43).equals("null"))&&(!bukken_details.get(43).equals("")) && (!bukken_details.get(43).equals("0"))) {
                eki3 = eki3 + "徒歩" + bukken_details.get(43) + "分";
            }
            if  ((!bukken_details.get(44).equals("null"))&&(!bukken_details.get(44).equals("")) && (!bukken_details.get(44).equals("0"))) {
                eki3 = eki3 + bukken_details.get(44) + "分";
            }
            tv_eki3.setText(eki3);
        }
        else {
            cl_eki3.setVisibility(View.GONE);
        }

        tv_bukkentypename.setText(bukken_details.get(3));

        tv_genkyou_name.setText(bukken_details.get(45));
        tv_hikiwatashi_name.setText(bukken_details.get(46));
        tv_torihiki_name.setText(bukken_details.get(47));

        if(bukken_details.get(1).equals("1")) {
            ll_tatamono_kansei_cat2.setVisibility(View.GONE);
            ll_bukken_room_num_cat2.setVisibility(View.GONE);
        }
        else {
            String tatemonokanseicat2 = bukken_details.get(48) + "年" + bukken_details.get(49) + "月";
            tv_tatemono_kansei_year_tatemono_kansei_month_cat2.setText(tatemonokanseicat2);
            String bukkenroomnumcat2 = bukken_details.get(50) + "戸";
            tv_bukken_room_num_cat2.setText(bukkenroomnumcat2);
        }

        LinearLayout MM = scroll_MM; //Linear Layout ID in XML File.
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView[] mainImgs = new ImageView[sliderimgfiles.size()];
        for (int mm = 0; mm < sliderimgfiles.size(); mm++) {
            mainImgs[mm] = new ImageView(getActivity());
            String url = DOMAIN_NAME + "/files/jogsale/files/" + sliderimgfiles.get(mm).get(0) + sliderimgfiles.get(mm).get(1);
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

        tv_bukken_kouzou_name.setText(bukken_details.get(54));

        if(bukken_details.get(1).equals("1")) {
            ll_kaidate_upper.setVisibility(View.GONE);
            ll_kaidate_under.setVisibility(View.GONE);
            ll_toshi_keikaku_name.setVisibility(View.GONE);
            ll_youto_chiiki.setVisibility(View.GONE);
            ll_toshi_keikaku_memo.setVisibility(View.GONE);
            ll_chimoku.setVisibility(View.GONE);
            ll_tochi_kenri_name.setVisibility(View.GONE);
            ll_setsudo_name.setVisibility(View.GONE);

            String kai = bukken_details.get(53) + "階部分" ;
            tv_kai.setText(kai);


            String tatemonokanseicat1 = bukken_details.get(48) + "年" + bukken_details.get(49) + "月";
            tv_tatemono_kansei_year_tatemono_kansei_month_cat1.setText(tatemonokanseicat1);

            String bukkenroomnumcat1 = bukken_details.get(50) + "戸";
            tv_bukken_room_num_cat1.setText(bukkenroomnumcat1);

            String kaidateupun;
            if  ((!bukken_details.get(51).equals("null"))&&(!bukken_details.get(51).equals("")) && (!bukken_details.get(51).equals("0"))) {
                kaidateupun = bukken_details.get(51) + "階/";
            }
            else {
                kaidateupun = "-" + "/";
            }
            if  ((!bukken_details.get(52).equals("null"))&&(!bukken_details.get(52).equals("")) && (!bukken_details.get(52).equals("0"))) {
                kaidateupun = kaidateupun + bukken_details.get(52) + "階";
            }
            else {
                kaidateupun = kaidateupun + "-";
            }
            tv_kaidate_upper_kaidate_under.setText(kaidateupun);

            String sekoucompanyname ="";
            if  ((!bukken_details.get(55).equals("null"))&&(!bukken_details.get(55).equals(""))) {
                sekoucompanyname = sekoucompanyname + bukken_details.get(55);
            }
            else {
                sekoucompanyname = sekoucompanyname + "-";
            }
            tv_sekou_company_name.setText(sekoucompanyname);

            String kanrikeitainame ="";
            if  ((!bukken_details.get(56).equals("null"))&&(!bukken_details.get(56).equals(""))) {
                kanrikeitainame = kanrikeitainame + bukken_details.get(56);
            }
            else {
                kanrikeitainame = kanrikeitainame + "-";
            }
            tv_kanri_keitai_name.setText(kanrikeitainame);

            String kanrifee = "";
            if  ((!bukken_details.get(57).equals("null"))&&(!bukken_details.get(57).equals("")) && (!bukken_details.get(57).equals("0"))) {
                kanrifee = kanrifee + bukken_details.get(57);
                kanrifee = kanrifee + "円/月";
            }
            else {
                kanrifee = kanrifee + "-";
            }
            tv_kanri_fee.setText(kanrifee);

            String shuzenfee = "";
            if  ((!bukken_details.get(58).equals("null"))&&(!bukken_details.get(58).equals("")) && (!bukken_details.get(58).equals("0"))) {
                shuzenfee = shuzenfee + bukken_details.get(58);
                shuzenfee = shuzenfee + "円/月";
            }
            else {
                shuzenfee = shuzenfee + "-";
            }
            tv_shuzen_fee.setText(shuzenfee);

            if(shunenshisetsu.size()>0) {
                for (int i = 0; i < shunenshisetsu.size(); i++) {
                    LinearLayout.LayoutParams shunenshisetsuparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tv_shuhen_shisetsu = new TextView(getActivity());
                    tv_shuhen_shisetsu.setPadding(5, 0, 5, 0);
                    String shuhen_shisetsu = "";

                    if ((!(shunenshisetsu.get(i).get(0).equals(""))) && (!(shunenshisetsu.get(i).get(0).equals(""))))
                        shuhen_shisetsu = shuhen_shisetsu + shunenshisetsu.get(i).get(0);
                    if ((!(shunenshisetsu.get(i).get(1).equals(""))) && (!(shunenshisetsu.get(i).get(1).equals("null"))) && (!(shunenshisetsu.get(i).get(1).equals("0"))))
                        shuhen_shisetsu = shuhen_shisetsu + shunenshisetsu.get(i).get(1) + "km";
                    if ((!(shunenshisetsu.get(i).get(2).equals(""))) && (!(shunenshisetsu.get(i).get(2).equals("null"))) && (!(shunenshisetsu.get(i).get(2).equals("0"))))
                        shuhen_shisetsu = shuhen_shisetsu + "徒歩" + shunenshisetsu.get(i).get(2) + "分";
                    tv_shuhen_shisetsu.setText(shuhen_shisetsu);
                    tv_shuhen_shisetsu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    tv_shuhen_shisetsu.setTextColor(Color.parseColor("#ff000000"));
                    ll_shuhen_shisetsu_list.addView(tv_shuhen_shisetsu, shunenshisetsuparams);
                }
            }
            else{
                ll_shuhen_shisetsu.setVisibility(View.GONE);
            }
        }
        else {
            ll_kai.setVisibility(View.GONE);
            ll_tatamono_kansei_cat1.setVisibility(View.GONE);
            ll_bukken_room_num_cat1.setVisibility(View.GONE);
            ll_kaidate.setVisibility(View.GONE);
            ll_sekou_company_name.setVisibility(View.GONE);
            ll_kanri_keitai_name.setVisibility(View.GONE);
            ll_kanri_fee.setVisibility(View.GONE);
            ll_shuzen_fee.setVisibility(View.GONE);
            ll_shuhen_shisetsu.setVisibility(View.GONE);

            String kaidateupper = "";
            if  ((!bukken_details.get(51).equals("null"))&&(!bukken_details.get(51).equals("")) && (!bukken_details.get(51).equals("0"))) {
                kaidateupper = kaidateupper + bukken_details.get(51) + "階";
            }
            else {
                kaidateupper = kaidateupper + "-";
            }
            tv_kaidate_upper.setText(kaidateupper);

            String kaidateunder = "";
            if  ((!bukken_details.get(52).equals("null"))&&(!bukken_details.get(52).equals("")) && (!bukken_details.get(52).equals("0"))) {
                kaidateunder = kaidateunder + bukken_details.get(52) + "階";
            }
            else {
                kaidateunder = kaidateunder + "-";
            }
            tv_kaidate_under.setText(kaidateunder);

            String toshikeikakuname = "";
            if  ((!bukken_details.get(62).equals("null"))&&(!bukken_details.get(62).equals(""))) {
                toshikeikakuname = toshikeikakuname + bukken_details.get(62);
            }
            else {
                toshikeikakuname = toshikeikakuname + "-";
            }
            tv_toshi_keikaku_name.setText(toshikeikakuname);

            String youtochiiki = "";
            if  ((!bukken_details.get(63).equals("null"))&&(!bukken_details.get(63).equals(""))) {
                youtochiiki = youtochiiki + bukken_details.get(63);
            }
            else {
                youtochiiki = youtochiiki + "-";
            }
            tv_youto_chiiki.setText(youtochiiki);

            String toshikeikakumemo = "";
            if  ((!bukken_details.get(64).equals("null"))&&(!bukken_details.get(64).equals(""))) {
                toshikeikakumemo = toshikeikakumemo + bukken_details.get(64);
            }
            else {
                toshikeikakumemo = toshikeikakumemo + "-";
            }
            tv_toshi_keikaku_memo.setText(toshikeikakumemo);

            String chimokuname = "";
            if  ((!bukken_details.get(65).equals("null"))&&(!bukken_details.get(65).equals(""))) {
                chimokuname = chimokuname + bukken_details.get(65);
            }
            else {
                chimokuname = chimokuname + "-";
            }
            tv_chimoku_name.setText(chimokuname);

            String tochikenriname = "";
            if  ((!bukken_details.get(66).equals("null"))&&(!bukken_details.get(66).equals(""))) {
                tochikenriname = tochikenriname + bukken_details.get(66);
            }
            else {
                tochikenriname = tochikenriname + "-";
            }
            tv_tochi_kenri_name.setText(tochikenriname);

            String setsudoname = "";
            if  ((!bukken_details.get(67).equals("null"))&&(!bukken_details.get(67).equals(""))) {
                setsudoname = setsudoname + bukken_details.get(67);
            }
            else {
                setsudoname = setsudoname + "-";
            }
            tv_setsudo_name.setText(setsudoname);
        }

        String parking = "";
        if ((!(bukken_details.get(59).equals("")))&&(!(bukken_details.get(59).equals("無し"))))
            parking = parking + bukken_details.get(59);
        if ((!bukken_details.get(60).equals(""))&&(!(bukken_details.get(60).equals("0"))))
            parking = parking + " (" + bukken_details.get(60) + "台駐車可能)";
        if ((!(bukken_details.get(61).equals("")))&&(!(bukken_details.get(61).equals("0"))))
            parking = parking + bukken_details.get(61) + "円/月";

        tv_park_type_name_park_free_park_fee.setText(parking);

        String primaryschool = "";
        if ((!(bukken_details.get(68).equals("")))&&(!(bukken_details.get(68).equals("null"))))
            primaryschool = primaryschool + bukken_details.get(68);
        if ((!bukken_details.get(69).equals(""))&&(!(bukken_details.get(69).equals("0"))))
            primaryschool = primaryschool + bukken_details.get(69) + "km";
        if ((!(bukken_details.get(70).equals("")))&&(!(bukken_details.get(70).equals("0"))))
            primaryschool = primaryschool + "徒歩" + bukken_details.get(70) + "分";
        tv_primary_school_district.setText(primaryschool);

        String juniorhighschool = "";
        if ((!(bukken_details.get(71).equals("")))&&(!(bukken_details.get(71).equals("null"))))
            juniorhighschool = juniorhighschool + bukken_details.get(71);
        if ((!bukken_details.get(72).equals(""))&&(!(bukken_details.get(72).equals("0"))))
            juniorhighschool = juniorhighschool + bukken_details.get(72) + "km";
        if ((!(bukken_details.get(73).equals("")))&&(!(bukken_details.get(73).equals("0"))))
            juniorhighschool = juniorhighschool + "徒歩" + bukken_details.get(73) + "分";
        tv_junior_high_school_district.setText(juniorhighschool);

        String strsetsubi = "";
        for (int i = 0; i < setsubi.size(); i++) {
            if(i < setsubi.size() - 1) {
                strsetsubi = strsetsubi + setsubi.get(i) + "、";
            }
            else {
                strsetsubi = strsetsubi + setsubi.get(i);
            }
        }
        tv_setsubi.setText(strsetsubi);

        tv_public_memo.setText(bukken_details.get(74));

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


        String tenpo_name_yobi2 = bukken_details.get(75) + " " + bukken_details.get(76);
        Drawable imgtenpo_name = getContext().getResources().getDrawable(R.drawable.topbar_title_left3x);
        int imgtenpo_namepixelDrawableSizeH = Math.round(tv_tenpo_tel.getLineHeight());
        int imgtenpo_namepixelDrawableSizeW = (int) Math.round((double) imgtenpo_name.getIntrinsicWidth() * ((double) tv_tenpo_tel.getLineHeight() / (double) imgtenpo_name.getIntrinsicHeight()));
        imgtenpo_name.setBounds(0, 0, imgtenpo_namepixelDrawableSizeW, imgtenpo_namepixelDrawableSizeH);
        tv_tenpo_name_yobi2.setCompoundDrawables(imgtenpo_name, null, null, null);
        tv_tenpo_name_yobi2.setText(tenpo_name_yobi2);

        tv_licence.setText(bukken_details.get(77));

        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String img_shop_gaikanURL = DOMAIN_NAME + bukken_details.get(78);
        imgparams.setMargins(0, 0, 0, 0);
        Picasso.get().load(img_shop_gaikanURL).into(img_shop_gaikan);
        img_shop_gaikan.setAdjustViewBounds(true);
        img_shop_gaikan.setScaleType(ImageView.ScaleType.FIT_START);

        tv_tenpo_tel.setText(bukken_details.get(79));
        tv_tenpo_tel.setTextColor(Color.parseColor("#ff0000ff"));
        tv_tenpo_fax.setText(bukken_details.get(45));
        tv_tenpo_shozaichi.setText(bukken_details.get(81));
        tv_tenpo_eigyo_jikan.setText(bukken_details.get(82));
        tv_tenpo_teikyubi.setText(bukken_details.get(83));

    }

    private void SetPageGauge() {
        if (sliderimgfiles.size() <= 1) {
            txtView_indicators.setVisibility(View.GONE);
        } else {
            txtView_indicators.setVisibility(View.VISIBLE);
            String S = "";
            for (int n = 0; n < sliderimgfiles.size(); n++) {
                if (n == currentImg) {
                    S += "●";
                } else {
                    S += "○";
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
                tv_comment.setText("・" + panoramacomment.get(i));
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
                tv_disppanorama.setText("パノラマで表示する");
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

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedGreenFavorites", MODE_PRIVATE);
        String favoriteJSON = sharedPreferences.getString("favorite list", null);

        if (favoriteJSON != null) {

            try {
                favoritesArray = new ArrayList<String>();
                favoritesDetailsArray = new JSONArray(favoriteJSON);
                JSONObject faveDetailsObject = new JSONObject();
                for (int i = 0; i < favoritesDetailsArray.length(); i++) {
                    faveDetailsObject = favoritesDetailsArray.getJSONObject(i);
                    favoritesArray.add(faveDetailsObject.getString("heya_no"));
                }

            } catch (JSONException e) {

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
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedGreenFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favoritesJSON = favoritesDetailsArray.toString();

        editor.putString("favorite list", favoritesJSON);
        editor.apply();
        System.out.println("List of favorites: " + sharedPreferences.getString("favorite list", ""));
    }

    /* LISTENERS*/
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
                    GlobalVar.previousScreen = "greenSchoolPropertyDetailFragment";
                    selectedFragment = new greenSchoolRentListFragment();
                }
                else if (parentfragment.equals("greenSchoolMapInformation")){
                    selectedFragment = new greenSchoolMapInformation();
                }
                else {}
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_inquirestore:
                GlobalVar.setparent_fragment("greenSchoolPropertyDetailFragment");
                GlobalVar.setTenpo_no(bukken_details.get(88)); //tenpo_no
                selectedFragment = new greenInquireStore();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_storeareamap: //change this
                //selectedFragment = new yellowAddressMapProperty();
                //((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                //hello!
                GlobalVar.setLatitude(bukken_details.get(86));
                GlobalVar.setLongitude(bukken_details.get(87));
                openMapDialog();
                break;
            case R.id.btn_loan:
                //ローン計算
                //https://www.jogjog.com/ + /loan/?price=10500000
                String price =  bukken_details.get(90);
                String loadURL = DOMAIN_NAME + "/loan/?price=" + price;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loadURL));
                startActivity(browserIntent);
                break;
            case R.id.btn_showareamap:
                //selectedFragment = new yellowAddressMapProperty();
                //((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                //hello!
                GlobalVar.setLatitude(bukken_details.get(84));
                GlobalVar.setLongitude(bukken_details.get(85));
                openMapDialog();
                break;
            case R.id.btn_inquireproperty:
                GlobalVar.setparent_fragment("greenSchoolPropertyDetailFragment");
                GlobalVar.setTenpo_no(bukken_details.get(88)); //tenpo_no
                selectedFragment = new greenInquireProperty();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_visitreserve:
                GlobalVar.setparent_fragment("greenSchoolPropertyDetailFragment");
                GlobalVar.setTenpo_no(bukken_details.get(88)); //tenpo_no
                selectedFragment = new greenInquireReservation();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.tv_tenpo_tel:
                alertDialogcalltenpotel();
                break;
            case R.id.btn_ookiniri:
                createFavoritesObject();
                // message
                alertDialogAddtoFavorites();
                btn_ookiniri.setEnabled(false);
                btn_ookiniri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
                btn_ookiniri.setTextColor(Color.parseColor("#aaaaaa"));
                btn_ookiniri.setText("登録済み");

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


    private void alertDialogcalltenpotel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "でんわばんご " + bukken_details.get(79) + " へお問い合わせをします。";
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri u = Uri.parse("tel:" + bukken_details.get(79));

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try {
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

    public void openMapDialog() {
        DialogFragment mapFragment = greenMapProperty.newInstance();
        mapFragment.show(getFragmentManager(), "dialog");
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
    private void createFavoritesObject() {

        try {
            JSONObject favoriteDetailsObject = new JSONObject();
            Random rand = new Random();
            int randId = rand.nextInt(1000000);
            favoriteDetailsObject.put("tenpo_no1", bukken_details.get(88));
            favoriteDetailsObject.put("address1", bukken_details.get(21));
            favoriteDetailsObject.put("bukkenName", bukken_details.get(4));
            favoriteDetailsObject.put("bukken_type_name", bukken_details.get(3));
            favoriteDetailsObject.put("ensen_name", bukken_details.get(90));
            favoriteDetailsObject.put("eki_name", bukken_details.get(91));
            favoriteDetailsObject.put("eki_min1", bukken_details.get(26));
            favoriteDetailsObject.put("kakaku_bukken_fee", bukken_details.get(92));
            favoriteDetailsObject.put("bukken_no", bukken_details.get(0));
            favoriteDetailsObject.put("random_id", randId);

            favoritesDetailsArray.put(favoriteDetailsObject);
            addToFavorites();

        } catch(JSONException e) {

        }
    }
}

