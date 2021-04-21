package com.cck.jogjog.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.cck.jogjog.Fragments.favorite.FavoriteFragment;
import com.cck.jogjog.Fragments.history.HistoryFragment;
import com.cck.jogjog.Fragments.history.NotificationsHistory;
import com.cck.jogjog.Fragments.home.HomeFragment;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity  {
    BottomNavigationView bottomNav;
    View badge;
    //int pref_code;
    String pref_code;
    String webtown_code;
    List<String> code_jis = new ArrayList<>();
    String district_name = "";
    //Districts
    List<String> code_chouaza = new ArrayList<>();

    int unreadannounces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container,
                    new HomeFragment()).addToBackStack(null).commit();
        }
        //getNotifsResponse();
    }

    public BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                            //getNotifsResponse();

                            //clearing of data
                            //list
                            //yellow
                            BukkenList.clearbukken();
                            //green and blue
                            BukkenList.bukken_list.clear();
                            BukkenList.sliderattr.clear();
                            BukkenList.bukken_list_setsubi.clear();
                            BukkenList.bukken_list_imagelist.clear();
                            GlobalVar.clearheya_no();
                            GlobalVar.clearcodes();
                            GlobalVar.isFromMap=false;
                            GlobalVar.selectedtab = 0;
                            GlobalVar.lat = "0";
                            GlobalVar.lat2 = "0";
                            GlobalVar.lon = "0";
                            GlobalVar.lon2 = "0";

                            //search

                            //address
                            BukkenList.jouchomei.clear();
                            BukkenList.shikuchouson.clear();
                            //line
                            BukkenList.lineline.clear();
                            GlobalVar.data_codestation.clear();

                            //school
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_dashboard:
                            //startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                            //getNotifsResponse();
                            selectedFragment = new FavoriteFragment();
                            break;
                        case R.id.navigation_notifications:
                            //startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                            //getNotifsResponse();
                            selectedFragment = new HistoryFragment();
                            break;
                    }
                    setSelectedFragment(selectedFragment);
                    return true;
                }
            };

    public void setSelectedFragment(Fragment selectedFragment) {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_home_container,selectedFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
 /*       if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();

        //SharedPrefManager.getInstance(MainActivity.this).setPaidLeave(0);
    }

    public void goHomeScreen() {
        // getNotifsResponse();
        bottomNav.setSelectedItemId((R.id.navigation_home));
    }
    public String getwebtown_code(){return webtown_code;}
    public void setwebtown_code(String webtowncode){ webtown_code = webtowncode;}

/*
    public void getNotifsResponse() {

        //ok ang connection
        if(!Global.isNetworkConnected(this)) {
            Toast.makeText(getApplicationContext(), "インターネット接続を確認してください。", Toast.LENGTH_LONG).show();
            return;
        }

        //prepare for db access??
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetroAPI.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetroAPI api = retrofit.create(RetroAPI.class);
        Global.token = SharedPrefManager.getInstance(getApplicationContext()).getToken();

        //make call
        Call<String> notifcall = api.getNotifications(Global.token, Global.authFCM);
        notifcall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        countUnreadNotifications(jsonresponse);
                        getAnouncementResponse();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    } // end getResponse

    private void getAnouncementResponse() {
        if(!Global.isNetworkConnected(this)) {
            Toast.makeText(getApplicationContext(), "インターネット接続を確認してください。", Toast.LENGTH_LONG).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetroAPI.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetroAPI api = retrofit.create(RetroAPI.class);
        Global.token = SharedPrefManager.getInstance(getApplicationContext()).getToken();
        Call<String> anouncecall = api.getAnnouncements(Global.token, Global.authFCM);
        anouncecall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        countUnreadAnnouncements(jsonresponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void countUnreadNotifications(String response) {
        unreadnotifs=0;
        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);

            //Global.modelListViewArrayList_notification = new ArrayList<>();
            JSONArray dataArray = obj.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {

                Notifications retroModel = new Notifications();
                JSONObject dataobj = dataArray.getJSONObject(i);
                retroModel.setIs_read(dataobj.getInt("is_read"));

                if(retroModel.getIs_read() == 0){
                    unreadnotifs++;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void countUnreadAnnouncements(String response) {
        unreadannounces = 0;

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);

            JSONArray dataArray = obj.getJSONArray("data");

            if(dataArray.length() > 0) {
                for (int i = 0; i < dataArray.length(); i++) {

                    Announcements retroModel = new Announcements();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    retroModel.setIs_read(dataobj.getInt("is_read"));
                    if(retroModel.getIs_read() == 0){
                        unreadannounces++;
                    }
                }
            }
            if ((unreadnotifs + unreadannounces) > 0){
                if(badge==null){
                    addBadge(bottomNav,1);
                }

                TextView notificationCount = badge.findViewById(R.id.notifications_badge);

                if((unreadnotifs+unreadannounces) <=99) {
                    notificationCount.setText(Integer.toString(unreadnotifs+unreadannounces));
                }else {
                    notificationCount.setText("99+");
                }
                badge.setVisibility(View.VISIBLE);
            }
            else{
                removeBadge(bottomNav,1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addBadge(BottomNavigationView navigationView, int index) {
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(index);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        badge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true);
    }

    public void removeBadge(BottomNavigationView navigationView, int index) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(index);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        badge = itemView.findViewById(R.id.notifications_badge);
        if ((badge) == null) {

        } else {
            ((ViewGroup) badge.getParent()).removeView(badge);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if ( hasFocus == true){
            getNotifsResponse();
        }
    }

 */
}
