package com.cck.jogjog.Fragments.blue.address;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.cck.jogjog.Fragments.blue.common.blueMapProperty;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BUKKEN_LIST;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_CHINTAI;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_TYPE_TENANT;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class blueAddressPropertyListFragment extends Fragment implements View.OnClickListener {

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

        View view = inflater.inflate(R.layout.fragment_blue_address_property_list, container, false);

        llcont = view.findViewById(R.id.llcont);
        sv_list = view.findViewById(R.id.sv_list);
        btn_nextpage = view.findViewById(R.id.btn_nextpage);
        btn_nextpage.setOnClickListener(this);

        btn_inquire = view.findViewById(R.id.btn_inquire);
        btn_inquire.setOnClickListener(this);
        if(GlobalVar.selectedtab == 0) {


            if (GlobalVar.previousScreen.equals("blueAddressSearchSettings")) {
                //if search apply button is pressed refrech list
                if (GlobalVar.isapplylinesearchsetting) {
                    BukkenList.clearbukken();
                    BukkenList.bukken_list.clear();
                    BukkenList.bukken_list_setsubi.clear();
                    BukkenList.bukken_list_imagelist.clear();
                    BukkenList.sliderattr.clear();
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
                    pagecount = BukkenList.bukken_list.size() / 10 + ((BukkenList.bukken_list.size() % 10 == 0) ? 0 : 1);
                    for (int i = 0; i < BukkenList.bukken_list.size(); i++) {
                        addProperties(i);
                    }
                    currpage = pagecount;
                }
            } else if (GlobalVar.previousScreen.equals("blueAddressPropertyDetailFragment")) {
                llcont.removeAllViews();
                pagecount = BukkenList.bukken_list.size() / 10 + ((BukkenList.bukken_list.size() % 10 == 0) ? 0 : 1);
                for (int i = 0; i < BukkenList.bukken_list.size(); i++) {
                    addProperties(i);
                }
                currpage = pagecount;
            } else if (GlobalVar.previousScreen.equals("blueAddressDistrictSelectionFragment")) {
                llcont.removeAllViews();
                BukkenList.clearbukken();
                BukkenList.bukken_list.clear();
                BukkenList.bukken_list_setsubi.clear();
                BukkenList.bukken_list_imagelist.clear();
                BukkenList.sliderattr.clear();
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
                BukkenList.bukken_list.clear();
                BukkenList.bukken_list_setsubi.clear();
                BukkenList.bukken_list_imagelist.clear();
                BukkenList.sliderattr.clear();
                sv_list.setVisibility(View.INVISIBLE);
                if (GlobalVar.selectedtab == 0) {
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
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_TENANT +
                    "&page=" + currpage +
                    searchurl;
        } else {
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_TENANT +
                    "&pref_code=" + prefecture +
                    "&code_jis=" + municipalities +
                    "&code_chouaza=" + districts +
                    "&page=" + currpage +
                    searchurl;
        }
        System.out.println(url);
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

                        int i = index + ((currpage - 1) * 10);

                        if (!returnBukken.isNull("bukken_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_name"));    // index:0
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("shozaichi")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("shozaichi"));    // index:1
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("eki1_kanji")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki1_kanji"));    // index:2
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("eki1_distance")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki1_distance"));    // index:3
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("eki2_kanji")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki2_kanji"));    // index:4
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("eki2_distance")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki2_distance"));    // index:5
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("eki3_kanji")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki3_kanji"));    // index:6
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("eki3_distance")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("eki3_distance"));    // index:7
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bus1_kanji")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus1_kanji"));    // index:8
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bus1_distance")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus1_distance"));    // index:9
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bus2_kanji")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus2_kanji"));    // index:10
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bus2_distance")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus2_distance"));    // index:11
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bus3_kanji")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus3_kanji"));    // index:12
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bus3_distance")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bus3_distance"));    // index:13
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("heya_tubo")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("heya_tubo"));    // index:14
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("heya_m2")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("heya_m2"));    // index:15
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_chinryo")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_chinryo"));    // index:16
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_chinryo_tax")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_chinryo_tax"));    // index:17
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("chinryo_unit_price")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("chinryo_unit_price"));    // index:18
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_kanrihi")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_kanrihi"));    // index:19
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_kyoekihi")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_kyoekihi"));    // index:20
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("tenpo_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_name"));    // index:21
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("tenpo_tel")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_tel"));    // index:22
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("kyouekihi_tubo_tanka")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("kyouekihi_tubo_tanka"));    // index:23
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bukken_type_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_type_name"));    // index:24
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("ido_hokui")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("ido_hokui"));    // index:25
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("keido_toukei")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("keido_toukei"));    // index:26
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_shikikin")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_shikikin"));    // index:27
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("tatemono_no")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tatemono_no"));    // index:28
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bukken_type_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_type_name"));    // index:29
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("tenpo_name")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_name"));    // index:30
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("tenpo_tel")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_tel"));    // index:31
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_shikikin")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_shikikin"));    // index:32
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("floor_info")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("floor_info"));    // index:33
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_kanrihi_tax")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_kanrihi_tax"));    // index:34
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("disp_kyoekihi_tax")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("disp_kyoekihi_tax"));    // index:35
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        if (!returnBukken.isNull("bukken_no")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("bukken_no"));    // index:36
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }
                        //favorites
                        if (!returnBukken.isNull("tenpo_no1")) {
                            BukkenList.bukken_list.get(i).add(returnBukken.getString("tenpo_no1"));    // index:37
                        } else {
                            BukkenList.bukken_list.get(i).add("null");
                        }

                        if (!returnBukken.isNull("setsubi_list")) {
                            BukkenList.bukken_list_setsubi.add(new ArrayList());
                            JSONArray setsubilist = returnBukken.getJSONArray("setsubi_list");
                            for (int j = 0; j < setsubilist.length(); j++) {
                                JSONObject returnsetsubilist = setsubilist.getJSONObject(j);
                                if (!returnsetsubilist.isNull("name")) {
                                    BukkenList.bukken_list_setsubi.get(i).add(new ArrayList());
                                    BukkenList.bukken_list_setsubi.get(i).get(j).add(returnsetsubilist.getString("name"));
                                    BukkenList.bukken_list_setsubi.get(i).get(j).add(returnsetsubilist.getString("seleced"));
                                }
                            }
                        }

                        if (!returnBukken.isNull("image_list")) {
                            BukkenList.bukken_list_imagelist.add(new ArrayList());
                            JSONArray imageList = returnBukken.getJSONArray("image_list");
                            for (int j = 0; j < imageList.length(); j++) {
                                BukkenList.bukken_list_imagelist.get(i).add(imageList.getString(j));

                            }
                            BukkenList.sliderattr.add(new ArrayList());
                            BukkenList.sliderattr.get(i).add(0f);// index 0 : t0X
                            BukkenList.sliderattr.get(i).add(0f);// index 1 : t0Y
                            BukkenList.sliderattr.get(i).add(0f);// index 2 : current image
                            BukkenList.sliderattr.get(i).add(Float.parseFloat(Integer.toString(imageList.length())));// index 3 : imagenumber
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

        int i  = index + ((currpage-1)*10);
        LinearLayout.LayoutParams contentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        contentparams.setMargins(getDPscale(10),0,getDPscale(10),getDPscale(5));

        LinearLayout ll_bukkenname = new LinearLayout(getActivity());
        ll_bukkenname.setOrientation(LinearLayout.VERTICAL);
        ll_bukkenname.setBackgroundColor(Color.parseColor("#c0dcf3"));

        llcont.addView(ll_bukkenname,contentparams);

        AppCompatCheckBox chkboxpricecheck = new AppCompatCheckBox(getActivity());
        chkboxpricecheck.setId(View.generateViewId());
        chkboxpricecheck.setId(CheckboxIDMain + (i));
        chkboxpricecheck.setHighlightColor(Color.parseColor("#ffffff"));
        chkboxpricecheck.setText("この建物の物件を金チェック");
        chkboxpricecheck.setTag(BukkenList.bukken_list.get(i).get(28));
        chkboxpricecheck.setTextColor(Color.parseColor("#000000"));
        chkboxpricecheck.setHeight(130);
        chkboxpricecheck.setOnClickListener(dynamiccheckboxOnclickListener);
        ll_bukkenname.addView(chkboxpricecheck);

        TextView tv_bukkenname = new TextView(getActivity());
        LinearLayout.LayoutParams bukkennamesparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bukkennamesparams.setMargins(0,getDPscale(5),0,getDPscale(5));
        tv_bukkenname.setText(BukkenList.bukken_list.get(i).get(0));
        tv_bukkenname.setTextColor(Color.parseColor("#000000"));
        tv_bukkenname.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_bukkenname.addView(tv_bukkenname,bukkennamesparams);

        LinearLayout ll_bukkenimage = new LinearLayout(getActivity());
        ll_bukkenimage.setOrientation(LinearLayout.VERTICAL);
        ll_bukkenimage.setBackgroundColor(Color.parseColor("#ffffff"));
        llcont.addView(ll_bukkenimage,contentparams);


        HorizontalScrollView mainHSV = new HorizontalScrollView(getActivity());
        mainHSV.setId(View.generateViewId());
        mainHSV.setOnTouchListener(sliderOnTouchListener);
        mainHSV.setTag(i);
        int HSVW = 1080;
        int HSVH = (int) Math.round((double) 3 * ((double) 1080 / (double) 4));
        //int HSVW = Math.round(llcont.getMeasuredWidth());
        //int HSVH = (int) Math.round((double) 3 * ((double) llcont.getMeasuredWidth() / (double) 4));
        LinearLayout.LayoutParams hsvParams = new LinearLayout.LayoutParams(HSVW, HSVH);
        mainHSV.setLayoutParams(hsvParams);
        ll_bukkenimage.addView(mainHSV);

        LinearLayout scroll_MM = new LinearLayout(getActivity());
        mainHSV.addView(scroll_MM);

        LinearLayout MM = scroll_MM; //Linear Layout ID in XML File.
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView[] mainImgs = new ImageView[BukkenList.bukken_list_imagelist.get(i).size()];
        for (int mm = 0; mm < BukkenList.bukken_list_imagelist.get(i).size(); mm++) {
            mainImgs[mm] = new ImageView(getActivity());
            String url = DOMAIN_NAME + "/files/tenant/" + BukkenList.bukken_list_imagelist.get(i).get(mm);
            Picasso.get().load(url).into(mainImgs[mm]);
            //mainImgs[mm].setImageBitmap(sliderimg.get(mm));
            int imgmmpixelDrawableSizeW = 1080;//Math.round(ll_bukkenimage.getMeasuredWidth());
            int imgmmpixelDrawableSizeH = (int) Math.round((double) 3 * ((double) 1080 / (double) 4)); //ll_bukkenimage.getMeasuredWidth() / (double) 4));
            mParams = new LinearLayout.LayoutParams(imgmmpixelDrawableSizeW, imgmmpixelDrawableSizeH);
            mainImgs[mm].setLayoutParams(mParams);
            mainImgs[mm].setAdjustViewBounds(true);
            mainImgs[mm].setScaleType(ImageView.ScaleType.FIT_XY);
            MM.addView(mainImgs[mm]);
        }
        //SetPageGauge();


        LinearLayout ll_bukkendetails = new LinearLayout(getActivity());
        ll_bukkendetails.setOrientation(LinearLayout.VERTICAL);
        ll_bukkendetails.setBackgroundColor(Color.parseColor("#ffffff"));
        llcont.addView(ll_bukkendetails,contentparams);

        TextView tv_bukken_type = new TextView(getActivity());
        Drawable imgbukken_type = getContext().getResources().getDrawable(R.drawable.ic033x);
        int imgbukkentypeDrawableSize = Math.round(tv_bukken_type.getLineHeight());
        imgbukken_type.setBounds(0, 0, imgbukkentypeDrawableSize, imgbukkentypeDrawableSize);
        tv_bukken_type.setCompoundDrawables(imgbukken_type, null, null, null);
        tv_bukken_type.setText(BukkenList.bukken_list.get(i).get(29));
        tv_bukken_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_bukken_type.setTextColor(Color.parseColor("#000000"));
        tv_bukken_type.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(tv_bukken_type);

        LinearLayout ll_chinryo = new LinearLayout(getActivity());
        ll_chinryo.setOrientation(LinearLayout.HORIZONTAL);
        ll_chinryo.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_chinryo);

        TextView tv_disp_chinryo = new TextView(getActivity());
        tv_disp_chinryo.setText(BukkenList.bukken_list.get(i).get(16));
        tv_disp_chinryo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        tv_disp_chinryo.setTextColor(Color.parseColor("#ff0000"));
        ll_chinryo.addView(tv_disp_chinryo);

        TextView tv_disp_chinryo_tax = new TextView(getActivity());
        String disp_chinryo_tax = " 円 " + BukkenList.bukken_list.get(i).get(17);
        tv_disp_chinryo_tax.setText(disp_chinryo_tax);
        tv_disp_chinryo_tax.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_disp_chinryo_tax.setTextColor(Color.parseColor("#000000"));
        tv_disp_chinryo_tax.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_chinryo.addView(tv_disp_chinryo_tax);

        LinearLayout ll_keiyaku_menseki = new LinearLayout(getActivity());
        ll_keiyaku_menseki.setOrientation(LinearLayout.HORIZONTAL);
        ll_keiyaku_menseki.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_keiyaku_menseki);

        TextView tv_keiyaku_menseki = new TextView(getActivity());
        tv_keiyaku_menseki.setText("契約面積");
        tv_keiyaku_menseki.setWidth(getDPscale(125));
        tv_keiyaku_menseki.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_keiyaku_menseki.setTextColor(Color.parseColor("#000000"));
        ll_keiyaku_menseki.addView(tv_keiyaku_menseki);

        TextView tv_heya_tubo_heya_m2 = new TextView(getActivity());
        String heya_tubo_heya_m2 = ": " + BukkenList.bukken_list.get(i).get(14) + "坪(" + BukkenList.bukken_list.get(i).get(15) + "m\u00B2)";
        tv_heya_tubo_heya_m2.setText(heya_tubo_heya_m2);
        tv_heya_tubo_heya_m2.setMaxLines(5);
        tv_heya_tubo_heya_m2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_heya_tubo_heya_m2.setTextColor(Color.parseColor("#000000"));
        tv_heya_tubo_heya_m2.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_keiyaku_menseki.addView(tv_heya_tubo_heya_m2);

        LinearLayout ll_kanrihi = new LinearLayout(getActivity());
        ll_kanrihi.setOrientation(LinearLayout.HORIZONTAL);
        ll_kanrihi.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_kanrihi);

        TextView tv_kanrihi = new TextView(getActivity());
        tv_kanrihi.setText("管理費");
        tv_kanrihi.setWidth(getDPscale(125));
        tv_kanrihi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_kanrihi.setTextColor(Color.parseColor("#000000"));
        ll_kanrihi.addView(tv_kanrihi);

        TextView tv_disp_kanrihi = new TextView(getActivity());
        String disp_kanrihi = ": " + BukkenList.bukken_list.get(i).get(19) + " " + BukkenList.bukken_list.get(i).get(34);
        tv_disp_kanrihi.setText(disp_kanrihi);
        tv_disp_kanrihi.setMaxLines(5);
        tv_disp_kanrihi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_disp_kanrihi.setTextColor(Color.parseColor("#000000"));
        tv_disp_kanrihi.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_kanrihi.addView(tv_disp_kanrihi);

        LinearLayout ll_kyoekihi = new LinearLayout(getActivity());
        ll_kyoekihi.setOrientation(LinearLayout.HORIZONTAL);
        ll_kyoekihi.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_kyoekihi);

        TextView tv_kyoekihi = new TextView(getActivity());
        tv_kyoekihi.setText("共益費");
        tv_kyoekihi.setWidth(getDPscale(125));
        tv_kyoekihi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_kyoekihi.setTextColor(Color.parseColor("#000000"));
        ll_kyoekihi.addView(tv_kyoekihi);

        TextView tv_disp_kyoekihi = new TextView(getActivity());
        String disp_kyoekihi = ": " + BukkenList.bukken_list.get(i).get(20) + " " + BukkenList.bukken_list.get(i).get(35);
        tv_disp_kyoekihi.setText(disp_kyoekihi);
        tv_disp_kyoekihi.setMaxLines(5);
        tv_disp_kyoekihi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_disp_kyoekihi.setTextColor(Color.parseColor("#000000"));
        tv_disp_kyoekihi.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_kyoekihi.addView(tv_disp_kyoekihi);

        LinearLayout ll_shikikin = new LinearLayout(getActivity());
        ll_shikikin.setOrientation(LinearLayout.HORIZONTAL);
        ll_shikikin.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_shikikin);

        TextView tv_shikikin = new TextView(getActivity());
        tv_shikikin.setText("敷金");
        tv_shikikin.setWidth(getDPscale(125));
        tv_shikikin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_shikikin.setTextColor(Color.parseColor("#000000"));
        ll_shikikin.addView(tv_shikikin);

        TextView tv_disp_shikikin = new TextView(getActivity());
        String disp_shikikin = ": " + BukkenList.bukken_list.get(i).get(32);
        tv_disp_shikikin.setText(disp_shikikin);
        tv_disp_shikikin.setMaxLines(5);
        tv_disp_shikikin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_disp_shikikin.setTextColor(Color.parseColor("#000000"));
        tv_disp_shikikin.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_shikikin.addView(tv_disp_shikikin);


        LinearLayout ll_chinryo_uprice = new LinearLayout(getActivity());
        ll_chinryo_uprice.setOrientation(LinearLayout.HORIZONTAL);
        ll_chinryo_uprice.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_chinryo_uprice);

        TextView tv_chinryo_uprice = new TextView(getActivity());
        tv_chinryo_uprice.setText("賃料坪単価");
        tv_chinryo_uprice.setWidth(getDPscale(125));
        tv_chinryo_uprice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_chinryo_uprice.setTextColor(Color.parseColor("#000000"));
        ll_chinryo_uprice.addView(tv_chinryo_uprice);

        TextView tv_chinryo_unit_price = new TextView(getActivity());
        String chinryo_unit_price = ": " + BukkenList.bukken_list.get(i).get(18);
        tv_chinryo_unit_price.setText(chinryo_unit_price);
        tv_chinryo_unit_price.setMaxLines(5);
        tv_chinryo_unit_price.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_chinryo_unit_price.setTextColor(Color.parseColor("#000000"));
        tv_chinryo_unit_price.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_chinryo_uprice.addView(tv_chinryo_unit_price);

        LinearLayout ll_kyouheki_tsubo_tanka = new LinearLayout(getActivity());
        ll_kyouheki_tsubo_tanka.setOrientation(LinearLayout.HORIZONTAL);
        ll_kyouheki_tsubo_tanka.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(ll_kyouheki_tsubo_tanka);

        TextView tv_kyouheki_tsubo_tanka = new TextView(getActivity());
        tv_kyouheki_tsubo_tanka.setText("共益費坪単価");
        tv_kyouheki_tsubo_tanka.setWidth(getDPscale(125));
        tv_kyouheki_tsubo_tanka.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_kyouheki_tsubo_tanka.setTextColor(Color.parseColor("#000000"));
        ll_kyouheki_tsubo_tanka.addView(tv_kyouheki_tsubo_tanka);

        TextView tv_kyouheki_tubo_tanka = new TextView(getActivity());
        String kyouheki_tubo_tanka = ": " + BukkenList.bukken_list.get(i).get(23);
        tv_kyouheki_tubo_tanka.setText(kyouheki_tubo_tanka);
        tv_kyouheki_tubo_tanka.setMaxLines(5);
        tv_kyouheki_tubo_tanka.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_kyouheki_tubo_tanka.setTextColor(Color.parseColor("#000000"));
        tv_kyouheki_tubo_tanka.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_kyouheki_tsubo_tanka.addView(tv_kyouheki_tubo_tanka);


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

        TextView tv_shozaichi = new TextView(getActivity());
        Drawable imgshozaichi = getContext().getResources().getDrawable(R.drawable.ic023x);
        int imgshozaichipixelDrawableSize = Math.round(tv_shozaichi.getLineHeight());
        imgshozaichi.setBounds(0, 0, imgshozaichipixelDrawableSize, imgshozaichipixelDrawableSize);
        tv_shozaichi.setCompoundDrawables(imgshozaichi, null, null, null);
        tv_shozaichi.setText(BukkenList.bukken_list.get(i).get(1));
        tv_shozaichi.setTextColor(Color.parseColor("#000000"));
        tv_shozaichi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        ll_location_stations.addView(tv_shozaichi);

        String eki_kanji = "";
        if((!BukkenList.bukken_list.get(i).get(2).equals("")) && (!BukkenList.bukken_list.get(i).get(2).equals("null"))){
            eki_kanji = BukkenList.bukken_list.get(i).get(2);
            if((!BukkenList.bukken_list.get(i).get(3).equals("")) && (!BukkenList.bukken_list.get(i).get(3).equals("null"))&& (Integer.parseInt(BukkenList.bukken_list.get(i).get(3))>0)){
                eki_kanji = eki_kanji + "　(徒歩" + BukkenList.bukken_list.get(i).get(3) + "分)";
            }
        }
        else if((!BukkenList.bukken_list.get(i).get(8).equals("")) && (!BukkenList.bukken_list.get(i).get(8).equals("null"))){
            eki_kanji = BukkenList.bukken_list.get(i).get(8);
            if((!BukkenList.bukken_list.get(i).get(9).equals("")) && (!BukkenList.bukken_list.get(i).get(9).equals("null")) && (Integer.parseInt(BukkenList.bukken_list.get(i).get(9))>0)){
                eki_kanji = eki_kanji + "　(徒歩" + BukkenList.bukken_list.get(i).get(9) + "分)";
            }
        }
        else{
            eki_kanji = "";
        }
        if((!eki_kanji.equals("")) && (!eki_kanji.equals("null"))){
            TextView tv_eki_kanji = new TextView(getActivity());
            Drawable imgeki = getContext().getResources().getDrawable(R.drawable.ic043x);
            int imgekipixelDrawableSize = Math.round(tv_eki_kanji.getLineHeight());
            imgeki.setBounds(0, 0, imgekipixelDrawableSize, imgekipixelDrawableSize);
            tv_eki_kanji.setCompoundDrawables(imgeki, null, null, null);
            tv_eki_kanji.setText(eki_kanji);
            tv_eki_kanji.setMaxLines(3);
            tv_eki_kanji.setTextColor(Color.parseColor("#000000"));
            tv_eki_kanji.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            ll_location_stations.addView(tv_eki_kanji);
        }
        LinearLayout.LayoutParams floorplanparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        floorplanparams.setMargins(0, 0,0,getDPscale(1));
        TextView tv_floorplan = new TextView(getActivity());
        tv_floorplan.setText(BukkenList.bukken_list.get(i).get(33));
        tv_floorplan.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_floorplan.setTextColor(Color.parseColor("#000000"));
        tv_floorplan.setLayoutParams(floorplanparams);
        ll_location_stations.addView(tv_floorplan);

        TableLayout tl_seibetsu = new TableLayout(getActivity());

        TableRow.LayoutParams seibetsuparams = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 1f);
        seibetsuparams.setMargins(getDPscale(5),getDPscale(5),getDPscale(5),getDPscale(5));

/*
            seibetsuindex   seibetsudata
            0: denki        0: name
            1: gas          1: selected
            2: danbou
            3: reibou
            4: toire
            5: erebeta
            6: romenten
            7: kyuuhaisui
            8: wanfuroa
            9: OAfuroa
            10: sukeruton
            11: chuushajouyuu
        BukkenList.bukken_list_setsubi.get(index).get(seibetsuindex).get(seibetsudata)
        BukkenList.bukken_list_setsubi.get(i).get(0).get(0);
*/
        TableRow tr_seibetsu1 = new TableRow(getActivity());

        TextView tv_denkiyu = new TextView(getActivity());
        tv_denkiyu.setText(BukkenList.bukken_list_setsubi.get(i).get(0).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(0).get(1).equals("on")) {
            tv_denkiyu.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_denkiyu.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_denkiyu.setGravity(Gravity.LEFT);
        tv_denkiyu.setPadding( getDPscale(10),0,0,0);
        tv_denkiyu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu1.addView(tv_denkiyu,seibetsuparams);

        TextView tv_gasu = new TextView(getActivity());
        tv_gasu.setText(BukkenList.bukken_list_setsubi.get(i).get(1).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(1).get(1).equals("on")) {
            tv_gasu.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_gasu.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_gasu.setGravity(Gravity.LEFT);
        tv_gasu.setPadding( getDPscale(10),0,0,0);
        tv_gasu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu1.addView(tv_gasu,seibetsuparams);

        tl_seibetsu.addView(tr_seibetsu1,0);

        TableRow tr_seibetsu2 = new TableRow(getActivity());

        TextView tv_danbou = new TextView(getActivity());
        tv_danbou.setText(BukkenList.bukken_list_setsubi.get(i).get(2).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(2).get(1).equals("on")) {
            tv_danbou.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_danbou.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_danbou.setGravity(Gravity.LEFT);
        tv_danbou.setPadding( getDPscale(10),0,0,0);
        tv_danbou.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu2.addView(tv_danbou,seibetsuparams);

        TextView tv_reibou = new TextView(getActivity());
        tv_reibou.setText(BukkenList.bukken_list_setsubi.get(i).get(3).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(3).get(1).equals("on")) {
            tv_reibou.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_reibou.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_reibou.setGravity(Gravity.LEFT);
        tv_reibou.setPadding( getDPscale(10),0,0,0);
        tv_reibou.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu2.addView(tv_reibou,seibetsuparams);

        tl_seibetsu.addView(tr_seibetsu2,1);

        TableRow tr_seibetsu3 = new TableRow(getActivity());

        TextView tv_toire = new TextView(getActivity());
        tv_toire.setText(BukkenList.bukken_list_setsubi.get(i).get(4).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(4).get(1).equals("on")) {
            tv_toire.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_toire.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_toire.setGravity(Gravity.LEFT);
        tv_toire.setPadding( getDPscale(10),0,0,0);
        tv_toire.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu3.addView(tv_toire,seibetsuparams);

        TextView tv_erebeta = new TextView(getActivity());
        tv_erebeta.setText(BukkenList.bukken_list_setsubi.get(i).get(5).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(5).get(1).equals("on")) {
            tv_erebeta.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_erebeta.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_erebeta.setGravity(Gravity.LEFT);
        tv_erebeta.setPadding( getDPscale(10),0,0,0);
        tv_erebeta.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu3.addView(tv_erebeta,seibetsuparams);

        tl_seibetsu.addView(tr_seibetsu3,2);

        TableRow tr_seibetsu4 = new TableRow(getActivity());

        TextView tv_romenten = new TextView(getActivity());
        tv_romenten.setText(BukkenList.bukken_list_setsubi.get(i).get(6).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(6).get(1).equals("on")) {
            tv_romenten.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_romenten.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_romenten.setGravity(Gravity.LEFT);
        tv_romenten.setPadding( getDPscale(10),0,0,0);
        tv_romenten.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu4.addView(tv_romenten,seibetsuparams);

        TextView tv_kyuuhaisui = new TextView(getActivity());
        tv_kyuuhaisui.setText(BukkenList.bukken_list_setsubi.get(i).get(7).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(7).get(1).equals("on")) {
            tv_kyuuhaisui.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_kyuuhaisui.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_kyuuhaisui.setGravity(Gravity.LEFT);
        tv_kyuuhaisui.setPadding( getDPscale(10),0,0,0);
        tv_kyuuhaisui.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu4.addView(tv_kyuuhaisui,seibetsuparams);

        tl_seibetsu.addView(tr_seibetsu4,3);

        TableRow tr_seibetsu5 = new TableRow(getActivity());

        TextView tv_wanfuroa = new TextView(getActivity());
        tv_wanfuroa.setText(BukkenList.bukken_list_setsubi.get(i).get(8).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(8).get(1).equals("on")) {
            tv_wanfuroa.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_wanfuroa.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_wanfuroa.setGravity(Gravity.LEFT);
        tv_wanfuroa.setPadding( getDPscale(10),0,0,0);
        tv_wanfuroa.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu5.addView(tv_wanfuroa,seibetsuparams);

        TextView tv_OAfuroa = new TextView(getActivity());
        tv_OAfuroa.setText(BukkenList.bukken_list_setsubi.get(i).get(9).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(9).get(1).equals("on")) {
            tv_OAfuroa.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_OAfuroa.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_OAfuroa.setGravity(Gravity.LEFT);
        tv_OAfuroa.setPadding( getDPscale(10),0,0,0);
        tv_OAfuroa.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu5.addView(tv_OAfuroa,seibetsuparams);

        tl_seibetsu.addView(tr_seibetsu5,4);

        TableRow tr_seibetsu6 = new TableRow(getActivity());

        TextView tv_sukeruton = new TextView(getActivity());
        tv_sukeruton.setText(BukkenList.bukken_list_setsubi.get(i).get(10).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(10).get(1).equals("on")) {
            tv_sukeruton.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_sukeruton.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_sukeruton.setGravity(Gravity.LEFT);
        tv_sukeruton.setPadding( getDPscale(10),0,0,0);
        tv_sukeruton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu6.addView(tv_sukeruton,seibetsuparams);

        TextView tv_chuushajouyuu = new TextView(getActivity());
        tv_chuushajouyuu.setText(BukkenList.bukken_list_setsubi.get(i).get(11).get(0));
        if (BukkenList.bukken_list_setsubi.get(i).get(11).get(1).equals("on")) {
            tv_chuushajouyuu.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blu));
        }
        else{
            tv_chuushajouyuu.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
        }
        tv_chuushajouyuu.setGravity(Gravity.LEFT);
        tv_chuushajouyuu.setPadding( getDPscale(10),0,0,0);
        tv_chuushajouyuu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tr_seibetsu6.addView(tv_chuushajouyuu,seibetsuparams);

        tl_seibetsu.addView(tr_seibetsu6,5);

        ll_bukkendetails.addView(tl_seibetsu);

        TextView tv_tenpo_name = new TextView(getActivity());
        Drawable imgtenpo_name = getContext().getResources().getDrawable(R.drawable.topbar_title_left3x);
        int imgtenpo_namepixelDrawableSizeH = Math.round(tv_bukken_type.getLineHeight());
        int imgtenpo_namepixelDrawableSizeW = (int)Math.round((double)imgtenpo_name.getIntrinsicWidth() * ((double)tv_bukken_type.getLineHeight()/(double)imgtenpo_name.getIntrinsicHeight()));
        imgtenpo_name.setBounds(0, 0, imgtenpo_namepixelDrawableSizeW, imgtenpo_namepixelDrawableSizeH);
        tv_tenpo_name.setCompoundDrawables(imgtenpo_name, null, null, null);
        tv_tenpo_name.setText(BukkenList.bukken_list.get(i).get(30));
        tv_tenpo_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv_tenpo_name.setTextColor(Color.parseColor("#000000"));
        tv_tenpo_name.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_bukkendetails.addView(tv_tenpo_name);

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
        tv_tenpo_tel.setText(BukkenList.bukken_list.get(i).get(31));
        tv_tenpo_tel.setTag(BukkenList.bukken_list.get(i).get(31));
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
        btn_bukkenjouhou.setTag(BukkenList.bukken_list.get(i).get(36));
        btn_bukkenjouhou.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        btn_bukkenjouhou.setGravity(Gravity.CENTER);
        btn_bukkenjouhou.setTextColor(Color.parseColor("#ffffff"));
        btn_bukkenjouhou.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blk));
        btn_bukkenjouhou.setOnClickListener(dynamicbuttonOnclickListener);
        tr_buttons.addView(btn_bukkenjouhou,buttonsuparams);

        AppCompatButton btn_okiniiri = new AppCompatButton(getActivity());
        btn_okiniiri.setText("お気に入りに追加");
        btn_okiniiri.setId(ButtonID + 3000000 + ((index + ((currpage-1)*10))* 100));
        btn_okiniiri.setTag(BukkenList.bukken_list.get(i).get(36));
        btn_okiniiri.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        btn_okiniiri.setGravity(Gravity.CENTER);
        btn_okiniiri.setTextColor(Color.parseColor("#ffffff"));
        btn_okiniiri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_blk));
        btn_okiniiri.setOnClickListener(dynamicbuttonOnclickListener);
        tr_buttons.addView(btn_okiniiri,buttonsuparams);

        tl_buttons.addView(tr_buttons,0);

        ll_bukkendetails.addView(tl_buttons);

        if(isfavorite(BukkenList.bukken_list.get(i).get(36))){
            btn_okiniiri.setEnabled(false);
            btn_okiniiri.setBackground(getContext().getResources().getDrawable(R.drawable.button_nobordered_background_gry));
            btn_okiniiri.setTextColor(Color.parseColor("#aaaaaa"));
            btn_okiniiri.setText("登録済み");
        }

        if(bukkencount-1 > index){
            LinearLayout.LayoutParams spacerparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
            spacerparams.setMargins(20,0,20,getDPscale(10));
            LinearLayout ll_spacer = new LinearLayout(getActivity());
            ll_spacer.setOrientation(LinearLayout.VERTICAL);
            ll_spacer.setBackgroundColor(Color.parseColor("#dddddd"));
            llcont.addView(ll_spacer,spacerparams);
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
                    BukkenList.bukken_list_imagelist.clear();
                    BukkenList.sliderattr.clear();
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
            //url = GlobalVar.urlFromMap + searchurl;
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_TENANT +
                    "&page=" + (currpage + 1) +
                    searchurl;
        } else {
            url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BUKKEN_LIST + API_PATH_TYPE_TENANT +
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
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedBlueFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favoritesJSON = favoritesDetailsArray.toString();

        editor.putString("favorite list", favoritesJSON);
        editor.apply();
        System.out.println("List of favorites: " + sharedPreferences.getString("favorite list", ""));
    }

    public void loadFavorites() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedBlueFavorites", MODE_PRIVATE);
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

                selectedFragment = new blueAddressPropertyDetailFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
            }
            else if (v.getId() >= ButtonID + 3000000) {
                int id = v.getId() - (ButtonID + 3000000);
                int indexbukken = id / 100;
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
            }
            else if (!((CheckBox) v).isChecked()) {
                GlobalVar.removeheya_no(v.getTag().toString());
                selectedcount--;
                if (selectedcount == 0) {
                    btn_inquire.setVisibility(View.GONE);
                }
            }
            else {
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

            int index = Integer.parseInt(v.getTag().toString());
            int currentImg = BukkenList.sliderattr.get(index).get(2).intValue();

            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    BukkenList.sliderattr.get(index).set(0, x1);
                    BukkenList.sliderattr.get(index).set(1, y1);
                    return true;

                case MotionEvent.ACTION_UP:
                    if ((Math.abs(x1 - BukkenList.sliderattr.get(index).get(0)) < 5) && (Math.abs(y1 - BukkenList.sliderattr.get(index).get(1)) < 5)) {
                        return true;
                    } else if ((x1 - BukkenList.sliderattr.get(index).get(0)) < -100) {   //Swiped to the left
                        currentImg++;
                        if (currentImg >= BukkenList.sliderattr.get(index).get(3).intValue()) {
                            currentImg = BukkenList.sliderattr.get(index).get(3).intValue() - 1;
                            //currentImg = 0; //go to start
                        }
                    } else if ((x1 - BukkenList.sliderattr.get(index).get(0)) > 100) {   //Swiped to the right
                        currentImg--;
                        if (currentImg < 0) {
                            currentImg = 0;
                            //currentImg = sliderimgfiles.size() - 1; // go to end
                        }
                    }
                    BukkenList.sliderattr.get(index).set(2, Float.parseFloat(Integer.toString(currentImg)));
                    //txtView_indicators
                    HorizontalScrollView mainHSCV = view.findViewById(v.getId());
                    mainHSCV.smoothScrollTo(currentImg * Math.round(mainHSCV.getWidth()), 0);
                    //SetPageGauge();

                    return true;
            }

            return false;
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
        DialogFragment mapFragment = blueMapProperty.newInstance();
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

    public void clearSavedBlueFavorites() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedBlueFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();

        sharedPreferences = this.getActivity().getSharedPreferences("savedBlueHistory", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
        //System.out.println("NAGCLEAR NA");
    }
    public void addToHistory() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedBlueHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String historyJSON = historyDetailsArray.toString();

        editor.putString("history list", historyJSON);
        editor.apply();
        System.out.println("List of history: " + sharedPreferences.getString("history list", ""));
    }
    public void loadHistory() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedBlueHistory", MODE_PRIVATE);
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

            favoriteDetailsObject.put("tenpo_no1", BukkenList.bukken_list.get(indexbukken).get(37));
            favoriteDetailsObject.put("shozaichi", BukkenList.bukken_list.get(indexbukken).get(1));
            favoriteDetailsObject.put("bukkenName", BukkenList.bukken_list.get(indexbukken).get(0));
            favoriteDetailsObject.put("bukken_type_name", BukkenList.bukken_list.get(indexbukken).get(24));
            favoriteDetailsObject.put("shikikin", BukkenList.bukken_list.get(indexbukken).get(27));
            favoriteDetailsObject.put("bukken_no", BukkenList.bukken_list.get(indexbukken).get(36));
            favoriteDetailsObject.put("random_id", randId);

            favoritesArray.add(BukkenList.bukken_list.get(indexbukken).get(36));

            if(destination == 1) favoritesDetailsArray.put(favoriteDetailsObject);
            if(destination == 2) {
                checkHistory(BukkenList.bukken_list.get(indexbukken).get(36), favoriteDetailsObject);
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
