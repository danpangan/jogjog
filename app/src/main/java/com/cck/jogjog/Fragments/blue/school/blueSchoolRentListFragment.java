package com.cck.jogjog.Fragments.blue.school;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.yellow.school.yellowSchoolListDisplay;
import com.cck.jogjog.Fragments.yellow.school.yellowSchoolMapDisplayFragment;
import com.cck.jogjog.Fragments.yellow.school.yellowSchoolPropertyListFragment;
import com.cck.jogjog.Fragments.yellow.school.yellowSchoolSearchSettings;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class blueSchoolRentListFragment extends Fragment implements View.OnClickListener {

    public static int items = 2;
    ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_rent_list, container, false);


        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_search = view.findViewById(R.id.btn_search);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        viewPager = view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new blueSchoolPropertyListFragment(), "物件ー覧");
        adapter.addFragment(new blueSchoolMapDisplayFragment(), "マップ表示");
        viewPager.setCurrentItem(1);
        viewPager.setAdapter(adapter);
        if (GlobalVar.previousScreen.equals("blueSchoolPropertyDetailFragment")) {
            viewPager.setCurrentItem(0);
        }
        else if (GlobalVar.previousScreen.equals("blueSchoolSearchSettings")){
            viewPager.setCurrentItem(GlobalVar.selectedtab);
        }
        else if(GlobalVar.previousScreen.equals("blueSchoolMapInformation")) {
            viewPager.setCurrentItem(GlobalVar.selectedtab);
        }
        else {
            viewPager.setCurrentItem(1);
        }


    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;

        switch (v.getId()) {
            case R.id.btn_back:
                clearSearchPref();
                BukkenList.bukken_list.clear();
                BukkenList.sliderattr.clear();
                BukkenList.bukken_list_imagelist.clear();
                BukkenList.bukken_list_setsubi.clear();
                GlobalVar.clearheya_no();
                GlobalVar.isFromMap=false;
                GlobalVar.selectedtab = 1;
                GlobalVar.lat = "0";
                GlobalVar.lat2 = "0";
                GlobalVar.lon = "0";
                GlobalVar.lon2 = "0";
                selectedFragment = new blueSchoolListDisplay();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_search:
                GlobalVar.setparent_fragment("blueSchoolRentListFragment");
                GlobalVar.selectedtab = viewPager.getCurrentItem();
                selectedFragment = new blueSchoolSearchSettings();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            default:
                break;
        }

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            //mFragmentList.set(1, fragment);

            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void clearSearchPref(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("searchpref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
        editor.commit();
    }
}
