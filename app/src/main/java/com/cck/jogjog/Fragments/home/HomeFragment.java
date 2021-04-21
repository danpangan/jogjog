package com.cck.jogjog.Fragments.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.blue.address.blueAddressPrefectureSelectionFragment;
import com.cck.jogjog.Fragments.blue.line.blueLinePrefectureSelectionFragment;
import com.cck.jogjog.Fragments.blue.location.blueLocationFragment;
import com.cck.jogjog.Fragments.blue.school.blueSchoolTownSelectionFragment;
import com.cck.jogjog.Fragments.green.address.greenAddressPrefectureSelectionFragment;
import com.cck.jogjog.Fragments.green.line.greenLinePrefectureSelectionFragment;
import com.cck.jogjog.Fragments.green.location.greenLocationFragment;
import com.cck.jogjog.Fragments.green.school.greenSchoolTownSelectionFragment;
import com.cck.jogjog.Fragments.yellow.address.yellowAddressPrefectureSelectionFragment;
import com.cck.jogjog.Fragments.yellow.line.yellowLinePrefectureSelectionFragment;
import com.cck.jogjog.Fragments.yellow.location.yellowLocationFragment;
import com.cck.jogjog.Fragments.yellow.school.yellowSchoolTownSelectionFragment;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static android.content.Context.MODE_PRIVATE;
import static com.cck.jogjog.GlobalVar.Common.API_KEY;
import static com.cck.jogjog.GlobalVar.Common.API_PATH_GET_BANNER;
import static com.cck.jogjog.GlobalVar.Common.BASE_URL;
import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private String jjBlueImgURL, jjPinkImgURL;
    private String newSliderURL;

    Button yellowLocationBtn, yellowAddressBtn, yellowLineBtn, yellowSchoolBtn;
    Button blueLocationBtn, blueAddressBtn, blueLineBtn, blueSchoolBtn;
    Button greenLocationBtn, greenAddressBtn, greenLineBtn, greenSchoolBtn;

    ImageView jjblueImg, jjpinkImg;

    ViewFlipper viewFlipper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        viewFlipper = root.findViewById(R.id.viewFlipper);

        jjblueImg = root.findViewById(R.id.jjblueImg);
        jjpinkImg = root.findViewById(R.id.jjpinkImg);

        yellowLocationBtn = root.findViewById(R.id.yellowLocationBtn);
        yellowAddressBtn = root.findViewById(R.id.yellowAddressBtn);
        yellowLineBtn = root.findViewById(R.id.yellowLineBtn);
        yellowSchoolBtn = root.findViewById(R.id.yellowSchoolBtn);

        blueLocationBtn = root.findViewById(R.id.blueLocationBtn);
        blueAddressBtn = root.findViewById(R.id.blueAddressBtn);
        blueLineBtn = root.findViewById(R.id.blueLineBtn);
        blueSchoolBtn = root.findViewById(R.id.blueSchoolBtn);

        greenLocationBtn = root.findViewById(R.id.greenLocationBtn);
        greenAddressBtn = root.findViewById(R.id.greenAddressBtn);
        greenLineBtn = root.findViewById(R.id.greenLineBtn);
        greenSchoolBtn = root.findViewById(R.id.greenSchoolBtn);

        yellowLocationBtn.setOnClickListener(this);
        yellowAddressBtn.setOnClickListener(this);
        yellowLineBtn.setOnClickListener(this);
        yellowSchoolBtn.setOnClickListener(this);

        blueLocationBtn.setOnClickListener(this);
        blueAddressBtn.setOnClickListener(this);
        blueLineBtn.setOnClickListener(this);
        blueSchoolBtn.setOnClickListener(this);

        greenLocationBtn.setOnClickListener(this);
        greenAddressBtn.setOnClickListener(this);
        greenLineBtn.setOnClickListener(this);
        greenSchoolBtn.setOnClickListener(this);

        jjblueImg.setOnClickListener(this);
        jjpinkImg.setOnClickListener(this);
        clearSchoolTypeSelectionData();
        clearSearchPref();
        clearSchoolData();
        //clearSavedYellowFavorites();
       // clearSavedBlueFavorites();
       // clearSavedGreenFavorites();
        getBanner();
        getSlider();
        GlobalVar.clearpreviousscreen();
        return root;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {

            //banner
            case R.id.jjblueImg:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jjBlueImgURL));
                startActivity(browserIntent);
                System.out.println("JOGJOG BLUE LINK");
                System.out.println(jjBlueImgURL);
                break;

            case R.id.jjpinkImg:
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(jjPinkImgURL));
                startActivity(browserIntent2);
                System.out.println("JOGJOG PINK LINK");
                System.out.println(jjPinkImgURL);
                break;

                //buttons

            //YELLOW
            case R.id.yellowLocationBtn:
                selectedFragment = new yellowLocationFragment();
                ((MainActivity)getActivity()).setSelectedFragment(selectedFragment);
                break;

            case R.id.yellowAddressBtn:
                GlobalVar.clearcodes();
                BukkenList.clearbukken();
                selectedFragment = new yellowAddressPrefectureSelectionFragment();
                ((MainActivity)getActivity()).setSelectedFragment(selectedFragment);
                break;

            case R.id.yellowLineBtn:
                selectedFragment = new yellowLinePrefectureSelectionFragment();
                ((MainActivity)getActivity()).setSelectedFragment(selectedFragment);
                break;

            case R.id.yellowSchoolBtn:
                selectedFragment = new yellowSchoolTownSelectionFragment();
                ((MainActivity)getActivity()).setSelectedFragment(selectedFragment);
                break;

                //BLUE
            case R.id.blueLocationBtn:
                selectedFragment = new blueLocationFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.blueAddressBtn:
                GlobalVar.clearcodes();
                BukkenList.clearbukken();
                selectedFragment = new blueAddressPrefectureSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.blueLineBtn:
                selectedFragment = new blueLinePrefectureSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.blueSchoolBtn:
                selectedFragment = new blueSchoolTownSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;

                //GREEN
            case R.id.greenLocationBtn:
                selectedFragment = new greenLocationFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.greenAddressBtn:
                selectedFragment = new greenAddressPrefectureSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.greenLineBtn:
                selectedFragment = new greenLinePrefectureSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.greenSchoolBtn:
                selectedFragment = new greenSchoolTownSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;

                //slider


        }
    }

    public void getBanner() {
        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BANNER;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray banner = data.getJSONArray("big_banner_list");

                    JSONObject first_banner = banner.getJSONObject(0);
                    JSONObject second_banner = banner.getJSONObject(1);

                    System.out.println("BANNER");
                    System.out.println("FIRST BANNER: " + first_banner);
                    System.out.println("SECOND BANNER: " + second_banner);

                    jjBlueImgURL = first_banner.getString("target_url");
                    String first_dirPath = first_banner.getString("dir_path");
                    String first_bannerImg = first_banner.getString("banner_img");
                    String first_bannerName = first_banner.getString("banner_name");

                    System.out.println("FIRST Target URL: " + jjBlueImgURL);
                    System.out.println("FIRST dir_path: " + first_dirPath);
                    System.out.println("FIRST banner_img: " + first_bannerImg);
                    System.out.println("FIRST banner_name: " + first_bannerName);

                    jjPinkImgURL = second_banner.getString("target_url");
                    String second_dirPath = second_banner.getString("dir_path");
                    String second_bannerImg = second_banner.getString("banner_img");
                    String second_bannerName = second_banner.getString("banner_name");

                    System.out.println("SECOND Target URL: " + jjPinkImgURL);
                    System.out.println("SECOND dir_path: " + second_dirPath);
                    System.out.println("SECOND banner_img: " + second_bannerImg);
                    System.out.println("SECOND banner_name: " + second_bannerName);

                    String blueImgURL = DOMAIN_NAME + first_dirPath + first_bannerImg;
                    String pinkImgURL = DOMAIN_NAME + second_dirPath + second_bannerImg;

                    System.out.println("BLUEIMGURL: " + blueImgURL);

                    Picasso.get().load(blueImgURL).into(jjblueImg);
                    Picasso.get().load(pinkImgURL).into(jjpinkImg);


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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void getSlider() {

        String url = BASE_URL + "?key=" + API_KEY + "&" + API_PATH_GET_BANNER;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray sliderbanner = data.getJSONArray("slider_banner_list");

                    for (int i = 0; i < sliderbanner.length(); i++) {
                        JSONObject returnSliderBanner = sliderbanner.getJSONObject(i);
                        System.out.println(returnSliderBanner);

                        String bannerNo = returnSliderBanner.getString("banner_no");
                        String bannerName = returnSliderBanner.getString("banner_name");
                        String targetUrl = returnSliderBanner.getString("target_url");
                        String targetBlank = returnSliderBanner.getString("target_blank");
                        String alt = returnSliderBanner.getString("alt");
                        String dispNo = returnSliderBanner.getString("disp_no");
                        String bannerImg = returnSliderBanner.getString("banner_img");
                        String topFlag = returnSliderBanner.getString("top_flag");
                        String dirPath = returnSliderBanner.getString("dir_path");

                        System.out.println("SLIDER");
                        System.out.println("bannerNo URL: " + bannerNo);
                        System.out.println("bannerName: " + bannerName);
                        System.out.println("targetUrl: " + targetUrl);
                        System.out.println("targetBlank: " + targetBlank);
                        System.out.println("alt: " + alt);
                        System.out.println("dispNo: " + dispNo);
                        System.out.println("bannerImg: " + bannerImg);
                        System.out.println("topFlag: " + topFlag);
                        System.out.println("dirPath: " + dirPath);

                        if (targetUrl.startsWith("/")){
                            newSliderURL = DOMAIN_NAME + targetUrl;
                            System.out.println("NEW SLIDER URL: " + newSliderURL);
                        }

                        //IF TARGET URL HAS NO "HTTPS" THEN NEWSLIDERURL

                        String[] sliderDir = {"https://" + DOMAIN_NAME + dirPath + bannerImg};
                        System.out.println("SLIDER DIR: " + sliderDir);

//                        for(int j=0;j<sliderDir.length;j++)
//                        {
//                            // create dynamic image view and add them to ViewFlipper
//                            setImageInFlipper(sliderDir[j]);
//                        }

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void clearSchoolTypeSelectionData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolTypeSelectionData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    public void clearSearchPref(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("searchpref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
        editor.commit();
    }
    public void clearSchoolData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("schoolData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void clearSavedYellowFavorites() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedYellowFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();

        sharedPreferences = this.getActivity().getSharedPreferences("savedYellowHistory", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
        //System.out.println("NAGCLEAR NA");
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

    public void clearSavedGreenFavorites() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("savedGreenFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();

        sharedPreferences = this.getActivity().getSharedPreferences("savedGreenHistory", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
        //System.out.println("NAGCLEAR NA");
    }

//    private void setImageInFlipper(String imgUrl) {
//
//        ImageView image = new ImageView(getActivity().getApplicationContext());
//        Picasso.get().load(imgUrl).into(image);
//        viewFlipper.addView(image);
//    }

}


//                    for (int i = 0; i < sliderbanner.length(); i++) {
//                        JSONObject returnSliderBanner = sliderbanner.getJSONObject(i);
//                        JSONObject banner1 = sliderbanner.getJSONObject(0);
//
//                        System.out.println(returnSliderBanner);
//
//                        String targelURL = returnSliderBanner.getString("target_url");
//                        String dirPath = returnSliderBanner.getString("dir_path");
//                        String bannerImg = returnSliderBanner.getString("banner_img");
//                        String bannerName = returnSliderBanner.getString("banner_name");
//
//                        System.out.println("Target URL: " + targelURL);
//                        System.out.println("dir_path: " + dirPath);
//                        System.out.println("banner_img: " + bannerImg);
//                        System.out.println("banner_name: " + bannerName);
//
//                        System.out.println("PICTURE JOGJOG: " + DOMAIN_NAME + dirPath + bannerImg);
//
//                        System.out.println("SLIDER BANNER: " + sliderbanner.length());
//
//                    }

//    public void testing() {
//
//        String searchMe = "Green Eggs and Ham";
//        String findMe = "Eggs";
//        int searchMeLength = searchMe.length();
//        int findMeLength = findMe.length();
//        boolean foundIt = false;
//        for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
//            if (searchMe.regionMatches(i, findMe, 0, findMeLength)) {
//                foundIt = true;
//                System.out.println(searchMe.substring(i, i + findMeLength));
//                break;
//            }
//        }
//        if (!foundIt)
//            System.out.println("No match found.");
//
//        String blogName = "howtodoinjava.com";
//
//        if(blogName.startsWith("how")){
//            System.out.println("This is the answer");
//        }
//
//        System.out.println( blogName.startsWith("how") );               //true
//
//        System.out.println( "howtodoinjava.com".startsWith("howto") );  //true
//
//        System.out.println( "howtodoinjava.com".startsWith("hello") );  //false
//
//    }