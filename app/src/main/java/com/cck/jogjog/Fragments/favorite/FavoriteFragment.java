package com.cck.jogjog.Fragments.favorite;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.blue.common.blueInquireProperty;
import com.cck.jogjog.Fragments.green.common.greenInquireProperty;
import com.cck.jogjog.Fragments.yellow.common.yellowInquireProperty;
import com.cck.jogjog.Fragments.yellow.common.yellowLineInquireProperty;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteFragment extends Fragment{

    private DashboardViewModel dashboardViewModel;

    CardView cardview;
    LinearLayoutCompat.LayoutParams layoutparams;
    TextView textview;
    Button btn_back;
    int screenWidth;

    LinearLayoutCompat ll_favorites, ll_header, ll_container, ll_wrapper, ll_div, ll_label;

    public static JSONArray yellowFavoriteDetailsArray = new JSONArray();
    public static JSONArray blueFavoriteDetailsArray = new JSONArray();
    public static JSONArray greenFavoriteDetailsArray = new JSONArray();
    public static JSONObject favoriteDetailsObject = new JSONObject();
    public static JSONArray favoritesDetailsArray = new JSONArray();

    //history
    public static JSONArray yellowHistoryDetailsArray = new JSONArray();
    public static JSONArray blueHistoryDetailsArray = new JSONArray();
    public static JSONArray greenHistoryDetailsArray = new JSONArray();
    public static JSONObject historyDetailsObject = new JSONObject();
    //
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        ll_favorites=root.findViewById(R.id.ll_favorites);
        layoutparams = new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        GlobalVar.setdetailed_bukken_no("");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        loadFavorites();
        generateFavorites();
        loadHistory();
        btn_back = root.findViewById(R.id.btn_back_store);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return root;
    }

    public void generateFavorites(){

        if(yellowFavoriteDetailsArray.length() > 0) {
            generateFavoritesHeader(1, "?????????(????????????)");
            generateFavoritesCard(yellowFavoriteDetailsArray, 1);
            generateSourceCard("1");
        }

        if(blueFavoriteDetailsArray.length() > 0) {
            generateFavoritesHeader(2, "?????????(???????????????????????????)");
            generateFavoritesCard(blueFavoriteDetailsArray, 2);
            generateSourceCard("2");
        }

        if(greenFavoriteDetailsArray.length() > 0) {
            generateFavoritesHeader(3, "??????");
            generateFavoritesCard(greenFavoriteDetailsArray, 3);
            generateSourceCard("3");
        }
    }

    public void generateFavoritesHeader(int source, String lbl) {

        addLinearLayout(0,0,0,0);
        addImageIcon(source);
        addImageLabel(source, lbl);
    }

    public void generateFavoritesCard(JSONArray faveDetailsArray, int source) {

        String shozaichi = "";
        String bukkenName = "";
        String madori = "";
        String bukken_type_name = "";
        String shikikin = "";
        String heya_no = "";
        String tenpo1_no = "";
        String randId = "";
        String bukken_no ="";

        String address1 ="";
        String ensen_name ="";
        String eki_name ="";
        String eki_min1 ="";
        String kakaku_bukken_fee ="";

        JSONObject faveDetailsObject = new JSONObject();

        for(int i=0; i<faveDetailsArray.length(); i++) {

            try {
                faveDetailsObject = faveDetailsArray.getJSONObject(i);

                if(source!=3) {
                    shozaichi = faveDetailsObject.getString("shozaichi");
                    shikikin = faveDetailsObject.getString("shikikin");
                }
                bukkenName = faveDetailsObject.getString("bukkenName");
                bukken_type_name = faveDetailsObject.getString("bukken_type_name");

                tenpo1_no = faveDetailsObject.getString("tenpo_no1");
                randId = faveDetailsObject.getString("random_id");

                if(source==1) {
                    heya_no = faveDetailsObject.getString("heya_no");
                    madori = faveDetailsObject.getString("madori");
                }
                else { bukken_no = faveDetailsObject.getString("bukken_no"); }

                if (source == 3) {
                    address1 = faveDetailsObject.getString("address1");
                    ensen_name = faveDetailsObject.getString("ensen_name");
                    eki_name = faveDetailsObject.getString("eki_name");
                    eki_min1 = faveDetailsObject.getString("eki_min1");
                    kakaku_bukken_fee = faveDetailsObject.getString("kakaku_bukken_fee");
                }

                //create card view
                addCardView("4", randId);
                //create linear layout wrapper inside card view with vertical orientation
                addLinearLayout(1, -1, -1, -1);
                //create linear layout container for row 1 inside wrapper that is to be divided by the divider - horizontal orientation
                addLinearLayout(2, 1, -1, -1);
                //create linear layout inside the container which will contain the checkbox element - Row 1
                addLinearLayout(3, 1, 3, -1);
                //checkbox inside row 1
                addCheckbox(randId, source);
                //divider between row 1 and row 2
                addDivider();
                //create linear layout container for row 2 inside wrapper that is to be divided by the divider - horizontal orientation
                addLinearLayout(2, 1, -1, -1);
                //create linear layout inside the container which will contain the label elements with 30% width - Row 2
                addLinearLayout(3, 1, 1, 1);
                //create textView for label - Row 2
                addTextView("?????????", 0, 0);
                //create linear layout inside the container which will contain the text elements with 70% width - Row 2
                addLinearLayout(3, 1, 1, 2);
                //create textView for label - Row 2
                addPropertyDetailsLink(bukkenName, source, randId);
                //divider between row 2 and row 3
                addDivider();
                //create linear layout container for row 2 inside wrapper that is to be divided by the divider - horizontal orientation
                addLinearLayout(2, 1, -1, -1);
                //create linear layout inside the container which will contain the label elements with 30% width - Row 3 upper
                addLinearLayout(3, 1, 1, 1);
                //create textView for label - Row 3 upper
                addTextView("??????????????????", 0, 0);
                //create linear layout inside the container which will contain the text elements with 70% width - Row 3 upper
                addLinearLayout(3, 1, 1, 2);
                //create textView for label - Row 3 upper
                if(source==3){ addTextView(address1, 1, 0); }
                else { addTextView(shozaichi, 1, 0); }
                //create linear layout container for row 2 inside wrapper that is to be divided by the divider - horizontal orientation
                addLinearLayout(2, 1, -1, -1);
                //create linear layout inside the container which will contain the label elements with 30% width - Row 3 lower
                addLinearLayout(3, 1, 1, 1);
                //create textView for label - Row 3 lower
                addTextView("", 0, 0);
                //create linear layout inside the container which will contain the text elements with 70% width - Row 3 lower
                addLinearLayout(3, 1, 1, 2);
                //create textView for label - Row 3 lower
                if(source==3){ addTextView(ensen_name + eki_name + "??????" + eki_min1 + "???", 1, 0); }
                else { addTextView(shozaichi, 1, 0); } //TENPO KOTSU DAPAT
                addDivider();
                //create linear layout container for row 4 inside wrapper that is to be divided by the divider - horizontal orientation
                addLinearLayout(2, 1, -1, -1);
                //create linear layout inside the container which will contain the label elements with 30% width - Row 4
                addLinearLayout(3, 1, 1, 1);
                //create textView for label - Row 4
                addTextView("??????", 0, 0);
                //create linear layout inside the container which will contain the text elements with 70% width - Row 4
                addLinearLayout(3, 1, 1, 2);
                //create textView for label - Row 4
                if(source==3){ addTextView(kakaku_bukken_fee, 1, 0); }
                else{ addTextView(shikikin, 1, 0); }
                //divider between row 4 and row 5
                addDivider();
                //create linear layout container for row 5 inside wrapper that is to be divided by the divider - horizontal orientation
                addLinearLayout(2, 1, -1, -1);
                //create linear layout inside the container which will contain the label elements with 30% width - Row 5
                addLinearLayout(3,1,1,1);
                //create textView for label - Row 5
                addTextView("??????????????????", 0, 0);
                //create linear layout inside the container which will contain the text elements with 70% width - Row 5
                addLinearLayout(3,1,1,2);
                //create textView for label - Row 5
                if(source==1) { addTextView(madori + " " + bukken_type_name, 1, 0); }
                else{ addTextView(bukken_type_name, 1, 0); }
                //divider between row 5 and row 6
                addDivider();

                if((source == 2) || (source == 3)) {
                    //create linear layout container for row 6 inside wrapper that is to be divided by the divider - horizontal orientation
                    addLinearLayout(2, 1, -1, -1);
                    //create linear layout inside the container which will contain the left button with 50% width - Row 6
                    addLinearLayout(3, 1, 2, -1);
                    //create left button - Row 6
                    addButton("??????", 1, source, randId);
                    //create linear layout inside the container which will contain the right button with 50% width - Row 6
                    addLinearLayout(3, 1, 2, -1);
                    //create right button - Row 6
                    addButton("??????????????????", 2, source, randId);

                } else {
                    //create linear layout container for row 6 inside wrapper that is to be divided by the divider - horizontal orientation
                    addLinearLayout(2, 1, -1, -1);
                    //create linear layout inside the container which will contain the left button with 50% width - Row 6
                    addLinearLayout(3, 1, 2, -1);
                    //create left button - Row 6
                    addButton("??????????????????", 2, source, randId);
                    //create linear layout inside the container which will contain the right button with 50% width - Row 6
                    addLinearLayout(3, 1, 2, -1);
                    //create right button - Row 6
                    addButton("LINE", 3, source, randId);
                    //create linear layout container for row 6 inside wrapper that is to be divided by the divider - horizontal orientation
                    addLinearLayout(2, 1, -1, -1);
                    //create linear layout inside the container which will contain the left button with 50% width - Row 6
                    addLinearLayout(3, 1, 2, -1);
                    //create left button - Row 6
                    addButton("??????", 1, source, randId);
                }

            } catch(JSONException e) {

            }
        }
    }

    private void generateSourceCard(String source) {

        addCardView(source, "999");
        //create linear layout wrapper inside card view with vertical orientation
        addLinearLayout(1, -1, -1, -1);
        //create linear layout container for row 1 inside wrapper that is to be divided by the divider - horizontal orientation
        addLinearLayout(2, 1, -1, -1);
        //create linear layout inside the container which will contain the checkbox element - Row 1
        addLinearLayout(3, 1, 3, -1);

        addSourceContactButton(source);

    }

    /* Add dynamic Source Contact Form button */
    private void addSourceContactButton(final String source) {

        textview = new TextView(this.getActivity());
        textview.setLayoutParams(layoutparams);
        textview.setText("?????????????????????????????????????????????????????????");
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textview.setTextColor(Color.BLACK);
        textview.setPadding(5,0,5,0);
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER);

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFavoritesContactRequirements(source);
            }
        });

        ll_div.addView(textview);
    }
    /* End of dynamic Source Contact Form button */

    /* Add dynamic card view */
    private void addCardView(String source, String unique){

        String cardId = source + "" + unique;


        cardview = new CardView(getActivity());
        layoutparams.setMargins(30, 10, 30, 10);
        cardview.setLayoutParams(layoutparams);
        cardview.setRadius(20);
        cardview.setCardElevation(50);
        cardview.setId(Integer.parseInt(cardId));

        if(source.equals("1")) {
            cardview.setCardBackgroundColor(Color.parseColor("#ff8844"));
        } else if(source.equals("2")) {
            cardview.setCardBackgroundColor(Color.parseColor("#5599dd"));
        } else if(source.equals("3")) {
            cardview.setCardBackgroundColor(Color.parseColor("#77bb30"));
        } else {
            cardview.setPadding(25, 25, 25, 25);
            cardview.setCardBackgroundColor(Color.WHITE);
        }

        ll_favorites.addView(cardview);
    }
    /* end of dynamic card view */

    /* Add dynamic linear layout */
    private void addLinearLayout(int pos, int orientation, int contPos, int num)  {

        if(pos == 1) {

            ll_wrapper = new LinearLayoutCompat(getActivity());
            ll_wrapper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll_wrapper.setOrientation(LinearLayoutCompat.VERTICAL);

            cardview.addView(ll_wrapper);

        } else if(pos == 2) {

            ll_container = new LinearLayoutCompat(getActivity());
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            ll_container.setLayoutParams(params);

            if(orientation == 1) ll_container.setOrientation(LinearLayoutCompat.HORIZONTAL);
            else if(orientation == 2) ll_container.setOrientation(LinearLayoutCompat.VERTICAL);

            ll_wrapper.addView(ll_container);

        } else if(pos == 3) {

            ll_div = new LinearLayoutCompat(getActivity());

            if(contPos == 1) {

                int width = 0;

                if(num == 1) {
                    width = ((screenWidth - 60) / 3) - 40;
                } else if(num == 2) {
                    width = (((screenWidth - 60) / 3) * 2) + 40;
                }

                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.LEFT;
                ll_div.setLayoutParams(params);
                ll_div.setOrientation(LinearLayoutCompat.HORIZONTAL);

            } else if(contPos == 2) {

                int width = ((screenWidth - 60) / 2);
                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                ll_div.setLayoutParams(params);
                ll_div.setOrientation(LinearLayoutCompat.HORIZONTAL);

            } else if(contPos == 3){

                ll_div.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ll_div.setOrientation(LinearLayoutCompat.HORIZONTAL);
                //ll_div.setBackgroundColor(Color.GREEN);

            }

            ll_container.addView(ll_div);

        } else {

            ll_header = new LinearLayoutCompat(getActivity());
            ll_header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll_header.setOrientation(LinearLayoutCompat.HORIZONTAL);

            ll_favorites.addView(ll_header);

        }

    }
    /* End of dynamic linear layout */

    private void addImageIcon(int source) {

        ImageView icon = new ImageView(this.getActivity().getApplicationContext());
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 40, 0, 5);
        icon.setLayoutParams(layoutParams);

        if(source == 1) icon.setImageResource(R.drawable.mapicon_rent);
        if(source == 2) icon.setImageResource(R.drawable.mapicon_tenant);
        if(source == 3) icon.setImageResource(R.drawable.mapicon_sell);

        ll_header.addView(icon);
    }

    private void addImageLabel(int source, String lbl) {

        textview = new TextView(this.getActivity());
        textview.setLayoutParams(layoutparams);
        textview.setText(lbl);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textview.setTextColor(Color.BLACK);
        textview.setPadding(0,45,0,0);
        textview.setTextColor(Color.BLACK);
        textview.setGravity(Gravity.LEFT);

        ll_header.addView(textview);
    }

    /* Add dynamic button */
    private void addButton(String lbl, final int pos, int src, String unique) {

        final String btnId = pos + "" + src + "" + unique;
        final int id = Integer.parseInt(btnId);

        TextView btn = new TextView(getActivity());
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);
        btn.setLayoutParams(params);
        btn.setPadding(0,20,0,20);
        btn.setText(lbl);
        btn.setId(Integer.parseInt(btnId));
        if(pos == 3) btn.setBackgroundColor(Color.parseColor("#42c507"));
        else btn.setBackgroundColor(Color.BLACK);
        btn.setTextColor(Color.WHITE);
        btn.setGravity(Gravity.CENTER);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos == 1) deleteFavorites(id);
                else if(pos == 2) getContactRequirement(id, 1);
                else if(pos == 3) getContactRequirement(id, 2);
            }
        });
        //add button to the layout //onclick listener
        ll_div.addView(btn);
    }
    /* End of dynamic button */

    /* Add dynamic divider */
    private void addDivider(){

        View viewDivider = new View(getActivity());
        viewDivider.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        viewDivider.setBackgroundColor(Color.parseColor("#bbbbbb"));
        ll_wrapper.addView(viewDivider);

    }
    /* End of dynamic divider */

    /* Add dynamic TextView */
    private void addTextView(String txt, int gravity, int txtColor) {

        textview = new TextView(this.getActivity());
        textview.setLayoutParams(layoutparams);
        textview.setText(txt);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textview.setTextColor(Color.BLACK);
        textview.setPadding(5,0,5,0);

        if(txtColor == 0) textview.setTextColor(Color.BLACK);
        else textview.setTextColor(Color.WHITE);

        if(gravity == 0) textview.setGravity(Gravity.CENTER);
        else textview.setGravity(Gravity.LEFT);

        ll_div.addView(textview);
    }
    /* End of dynamic TextView */

    /* Add dynamic checkbox */
    public void addCheckbox(String id, int source) {

        int chkboxId = Integer.parseInt(9 + "" + source + "" + id);

        AppCompatCheckBox chkbox = new AppCompatCheckBox(this.getActivity());
        chkbox.setId(chkboxId);
        chkbox.setHighlightColor(Color.parseColor("#ffffff"));
        chkbox.setText("???????????????????????????");
        chkbox.setTextColor(Color.parseColor("#000000"));
        chkbox.setHeight(130);
        ll_div.addView(chkbox);

    }
    /* End of dynamic checkbox */

    private void addPropertyDetailsLink(String txt, int source, String id){

        id = "8" + source + "" + id;
        final int linkId = Integer.parseInt(id);

        textview = new TextView(this.getActivity());
        textview.setLayoutParams(layoutparams);
        textview.setText(txt);
        textview.setId(linkId);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textview.setPadding(5,0,5,0);

        textview.setTextColor(Color.BLUE);
        textview.setGravity(Gravity.LEFT);

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPropertyDetails(linkId);
            }
        });

        ll_div.addView(textview);

    }

    private void goToPropertyDetails(int id) {

        Fragment selectedFragment = new Fragment();
        String strId = String.valueOf(id);
        String source = strId.substring(1,2);
        String index = strId.substring(2, strId.length());

        try {

            if(source.equals("1")) {
                for(int i=0; i<yellowFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = yellowFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        GlobalVar.setContact_bukken_no(favoriteDetailsObject.getString("heya_no"));
                        checkHistory(source, favoriteDetailsObject.getString("heya_no"), favoriteDetailsObject);
                    }
                }
                selectedFragment = new FavoriteYellowPropertyDetailsFragment();
            } else if(source.equals("2")) {
                for(int i=0; i<blueFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = blueFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        GlobalVar.setContact_bukken_no(favoriteDetailsObject.getString("bukken_no"));
                        checkHistory(source, favoriteDetailsObject.getString("bukken_no"), favoriteDetailsObject);
                    }
                }
                selectedFragment = new FavoriteBluePropertyDetailsFragment();
            } else if(source.equals("3")) {
                for(int i=0; i<greenFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = greenFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        GlobalVar.setContact_bukken_no(favoriteDetailsObject.getString("bukken_no"));
                        checkHistory(source, favoriteDetailsObject.getString("bukken_no"), favoriteDetailsObject);
                    }
                }
                selectedFragment = new FavoriteGreenPropertyDetailsFragment();
            }
            ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

        } catch (JSONException e) {

        }
    }

    private void loadFavorites() {

        SharedPreferences sharedPreferences;

        sharedPreferences = this.getActivity().getSharedPreferences("savedYellowFavorites", MODE_PRIVATE);
        String yellowFavoriteJSON = sharedPreferences.getString("favorite list", null);

        sharedPreferences = this.getActivity().getSharedPreferences("savedBlueFavorites", MODE_PRIVATE);
        String blueFavoriteJSON = sharedPreferences.getString("favorite list", null);

        sharedPreferences = this.getActivity().getSharedPreferences("savedGreenFavorites", MODE_PRIVATE);
        String greenFavoriteJSON = sharedPreferences.getString("favorite list", null);

        try {

            if(yellowFavoriteJSON != null) { yellowFavoriteDetailsArray = new JSONArray(yellowFavoriteJSON); }
            if(blueFavoriteJSON != null) { blueFavoriteDetailsArray = new JSONArray(blueFavoriteJSON); }
            if(greenFavoriteJSON != null) { greenFavoriteDetailsArray = new JSONArray(greenFavoriteJSON); }

            System.out.println("yellow favorites: " + yellowFavoriteDetailsArray);
            System.out.println("blue favorites: " + blueFavoriteDetailsArray);
            System.out.println("green favorites: " + greenFavoriteDetailsArray);

        } catch (JSONException e) {

        }
    }

    private void addToFavorites(String src) {
        String favoritesJSON = "";
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("", MODE_PRIVATE);;

        if(src.equals("1")) {
            favoritesJSON = yellowFavoriteDetailsArray.toString();
            sharedPreferences = this.getActivity().getSharedPreferences("savedYellowFavorites", MODE_PRIVATE);
        } else if(src.equals("2")) {
            favoritesJSON = blueFavoriteDetailsArray.toString();
            sharedPreferences = this.getActivity().getSharedPreferences("savedBlueFavorites", MODE_PRIVATE);
        } else if(src.equals("3")) {
            favoritesJSON = greenFavoriteDetailsArray.toString();
            sharedPreferences = this.getActivity().getSharedPreferences("savedGreenFavorites", MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favorite list", favoritesJSON);
        editor.apply();
        System.out.println("List of favorites: " + sharedPreferences.getString("favorite list", ""));
    }

    private void deleteFavorites(int id) {

        String strId = String.valueOf(id);
        String pos = strId.substring(0,1);
        String source = strId.substring(1,2);
        String index = strId.substring(2, strId.length());

        try {

            if(source.equals("1")) {
                for(int i=0; i<yellowFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = yellowFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        yellowFavoriteDetailsArray.remove(i);
                    }
                }
            } else if(source.equals("2")) {
                for(int i=0; i<blueFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = blueFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        blueFavoriteDetailsArray.remove(i);
                    }
                }
            } else if(source.equals("3")) {
                for(int i=0; i<greenFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = greenFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        greenFavoriteDetailsArray.remove(i);
                    }
                }
            }

            removeCardView(index);
            addToFavorites(source);
            alertDialogDeleteFavorite();
            Fragment selectedFragment = new FavoriteFragment();
            ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);

        } catch (JSONException e) {

        }
    }

    private void removeCardView(String id) {
        id = "4" + id;
        int childCount = ll_favorites.getChildCount();
        for (int i=0; i < childCount; i++) {
            View v = ll_favorites.getChildAt(i);

            if(v instanceof CardView) {
                int cvId = ((CardView) v).getId();
                if(cvId == Integer.parseInt(id)){
                    ((CardView) v).removeView(v);
                }
            }
        }
    }

    private void alertDialogDeleteFavorite() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("????????????????????????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void getContactRequirement(int id, int form) {

        GlobalVar.setContact_bukken_no("");
        String strId = String.valueOf(id);
        String source = strId.substring(1,2);
        String index = strId.substring(2, strId.length());
        GlobalVar.setFavoritesSrc(source);

        try {

            if(source.equals("1")) {
                for(int i=0; i<yellowFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = yellowFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        //System.out.println("heya_no to contact form: " + favoriteDetailsObject.getString("heya_no"));
                        GlobalVar.setContact_bukken_no(favoriteDetailsObject.getString("heya_no"));
                        GlobalVar.setTenpo_no(favoriteDetailsObject.getString("tenpo_no1"));

                    }
                }
            } else if(source.equals("2")) {
                for(int i=0; i<blueFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = blueFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        GlobalVar.setContact_bukken_no(favoriteDetailsObject.getString("bukken_no"));
                        GlobalVar.setTenpo_no(favoriteDetailsObject.getString("tenpo_no1"));
                    }
                }
            } else if(source.equals("3")) {
                for(int i=0; i<greenFavoriteDetailsArray.length(); i++) {
                    favoriteDetailsObject = new JSONObject();
                    favoriteDetailsObject = greenFavoriteDetailsArray.getJSONObject(i);

                    if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                        GlobalVar.setContact_bukken_no(favoriteDetailsObject.getString("bukken_no"));
                        GlobalVar.setTenpo_no(favoriteDetailsObject.getString("tenpo_no1"));
                    }
                }
            }
            if(form==1) contactUs(source);
            else goToLineApp();



        } catch (JSONException e) {
            System.out.println("Error: " + e);
        }
    }

    public void getFavoritesContactRequirements(String source) {

        GlobalVar.setContact_bukken_no("");
        GlobalVar.setFavoritesSrc(source);

        int childCount = ll_favorites.getChildCount();
        int id = 0;
        for (int i = 0; i < childCount; i++) {
            View v = ll_favorites.getChildAt(i);

            if(v instanceof  CardView) {
                int card_count = ((CardView)v).getChildCount();

                for(int a=0; a<card_count; a++) {
                    View v_card = ((CardView)v).getChildAt(a);

                    if(v_card instanceof  LinearLayoutCompat) {
                        int ll1_count = ((LinearLayoutCompat)v_card).getChildCount();

                        for(int b=0; b<ll1_count; b++) {
                            View v_ll1 = ((LinearLayoutCompat)v_card).getChildAt(b);

                            if(v_ll1 instanceof  LinearLayoutCompat) {
                                int ll2_count = ((LinearLayoutCompat)v_ll1).getChildCount();

                                for(int c=0; c<ll2_count; c++) {
                                    View v_ll2 = ((LinearLayoutCompat)v_ll1).getChildAt(c);

                                    if(v_ll2 instanceof  LinearLayoutCompat) {
                                        int ll3_count = ((LinearLayoutCompat)v_ll2).getChildCount();

                                        for(int d=0; d<ll3_count; d++) {
                                            View v_ll3 = ((LinearLayoutCompat)v_ll2).getChildAt(d);

                                            if(v_ll3 instanceof CheckBox) {
                                                if(((CheckBox) v_ll3).isChecked()) {
                                                    id = ((CheckBox) v_ll3).getId();
                                                    String strId = String.valueOf(id);
                                                    String src = strId.substring(1,2);
                                                    String index = strId.substring(2, strId.length());

                                                    try {

                                                        if(source.equals("1") && (source.equals(src))) {
                                                            for(int j=0;j<yellowFavoriteDetailsArray.length(); j++) {
                                                                favoriteDetailsObject = new JSONObject();
                                                                favoriteDetailsObject = yellowFavoriteDetailsArray.getJSONObject(j);

                                                                if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                                                                    //System.out.println("heya_no to contact form: " + favoriteDetailsObject.getString("heya_no"));
                                                                    GlobalVar.setTenpo_no(favoriteDetailsObject.getString("tenpo_no1"));
                                                                    checkBukkenDetails(favoriteDetailsObject.getString("heya_no"));

                                                                }
                                                            }
                                                        } else if(source.equals("2") && (source.equals(src))) {
                                                            for(int j=0; j<blueFavoriteDetailsArray.length(); j++) {
                                                                favoriteDetailsObject = new JSONObject();
                                                                favoriteDetailsObject = blueFavoriteDetailsArray.getJSONObject(j);

                                                                if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                                                                    GlobalVar.setTenpo_no(favoriteDetailsObject.getString("tenpo_no1"));
                                                                    checkBukkenDetails(favoriteDetailsObject.getString("bukken_no"));
                                                                }
                                                            }
                                                        } else if(source.equals("3") && (source.equals(src))) {
                                                            for(int j=0; j<greenFavoriteDetailsArray.length(); j++) {
                                                                favoriteDetailsObject = new JSONObject();
                                                                favoriteDetailsObject = greenFavoriteDetailsArray.getJSONObject(j);

                                                                if(favoriteDetailsObject.getString("random_id").equals(String.valueOf(index))) {
                                                                    GlobalVar.setTenpo_no(favoriteDetailsObject.getString("tenpo_no1"));
                                                                    checkBukkenDetails(favoriteDetailsObject.getString("bukken_no"));
                                                                }
                                                            }
                                                        }

                                                    } catch (JSONException e) {
                                                        System.out.println("Error: " + e);
                                                    }

                                                } else {
                                                    //removeSearchCriteria(v);
                                                }
                                            }

                                        }
                                    }

                                }
                            }

                        }
                    }

                }
            }
        }

        contactUs(source);
    }

    private void contactUs(String source) {

        Fragment selectedFragment = new Fragment();

        if(source.equals("1")) {
            selectedFragment = new yellowInquireProperty();
        } else if(source.equals("2")) {
            selectedFragment = new blueInquireProperty();
        } else if(source.equals("3")) {
            selectedFragment = new greenInquireProperty();
        }

        ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
    }

    private void checkBukkenDetails(String bukken_no) {
        if (GlobalVar.getContact_bukken_no().length() == 0) {
            GlobalVar.setContact_bukken_no(bukken_no);
        } else {
            bukken_no = GlobalVar.getContact_bukken_no() + "," + bukken_no;
            GlobalVar.setContact_bukken_no(bukken_no);
        }
    }

    private void addToHistory(int source) {

        String historyJSON = "";
        SharedPreferences sharedPreferences = null;
        if(source==1) {
            historyJSON = yellowHistoryDetailsArray.toString();
            sharedPreferences = this.getActivity().getSharedPreferences("savedYellowHistory", MODE_PRIVATE);
        } else if(source==2) {
            historyJSON = blueHistoryDetailsArray.toString();
            sharedPreferences = this.getActivity().getSharedPreferences("savedBlueHistory", MODE_PRIVATE);
        } else if(source ==3) {
            historyJSON = greenHistoryDetailsArray.toString();
            sharedPreferences = this.getActivity().getSharedPreferences("savedGreenHistory", MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("history list", historyJSON);
        editor.apply();
        System.out.println("List of history: " + sharedPreferences.getString("history list", ""));
    }

    private void loadHistory() {
        SharedPreferences sharedPreferences;
        sharedPreferences = this.getActivity().getSharedPreferences("savedYellowHistory", MODE_PRIVATE);
        String yellowHistoryJSON = sharedPreferences.getString("history list", null);
        sharedPreferences = this.getActivity().getSharedPreferences("savedBlueHistory", MODE_PRIVATE);
        String blueHistoryJSON = sharedPreferences.getString("history list", null);

        sharedPreferences = this.getActivity().getSharedPreferences("savedGreenHistory", MODE_PRIVATE);
        String greenHistoryJSON = sharedPreferences.getString("history list", null);

        try {

            if(yellowHistoryJSON != null) { yellowHistoryDetailsArray = new JSONArray(yellowHistoryJSON); }
            if(blueHistoryJSON != null) { blueHistoryDetailsArray = new JSONArray(blueHistoryJSON); }
            if(greenHistoryJSON != null) { greenHistoryDetailsArray = new JSONArray(greenHistoryJSON); }

            System.out.println("yellow history: " + yellowHistoryDetailsArray);
            System.out.println("blue history: " + blueHistoryDetailsArray);
            System.out.println("green history: " + greenHistoryDetailsArray);

        } catch (JSONException e) {

        }
    }

    public void goToLineApp(){
        Fragment selectedFragment = new Fragment();
        selectedFragment = new yellowLineInquireProperty();
        ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
    }

    private void checkHistory(String src, String bukken_no, JSONObject faveDetailsObject) {

        boolean isExisting = false;
        try {
            if(src.equals("1")) {
                for(int i=0; i<yellowHistoryDetailsArray.length(); i++) {
                    historyDetailsObject = new JSONObject();
                    historyDetailsObject = yellowHistoryDetailsArray.getJSONObject(i);

                    if(historyDetailsObject.length()>0) {
                        if (historyDetailsObject.getString("heya_no").equals(bukken_no)) {
                            isExisting = true;
                        }
                    }
                }

                if(!isExisting) {
                    yellowHistoryDetailsArray.put(faveDetailsObject);
                    addToHistory(Integer.parseInt(src));
                }

            } else if(src.equals("2")) {
                for(int i=0; i<blueHistoryDetailsArray.length(); i++) {
                    historyDetailsObject = new JSONObject();
                    historyDetailsObject = blueHistoryDetailsArray.getJSONObject(i);

                    if(historyDetailsObject.length()>0) {
                        if (historyDetailsObject.getString("bukken_no").equals(bukken_no)) {
                            isExisting = true;
                        }
                    }
                }

                if(!isExisting) {
                    blueHistoryDetailsArray.put(faveDetailsObject);
                    addToHistory(Integer.parseInt(src));
                }

            } else if(src.equals("3")) {
                for(int i=0; i<greenHistoryDetailsArray.length(); i++) {
                    historyDetailsObject = new JSONObject();
                    historyDetailsObject = greenHistoryDetailsArray.getJSONObject(i);

                    if(historyDetailsObject.length()>0) {
                        if (historyDetailsObject.getString("bukken_no").equals(bukken_no)) {
                            isExisting = true;
                        }
                    }
                }

                if(!isExisting) {
                    greenHistoryDetailsArray.put(faveDetailsObject);
                    addToHistory(Integer.parseInt(src));
                }
            }

        } catch(JSONException e) {
            System.out.println("JSON error: " + e);
        }
    }
}