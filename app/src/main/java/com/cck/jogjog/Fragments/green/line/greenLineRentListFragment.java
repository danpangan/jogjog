package com.cck.jogjog.Fragments.green.line;

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
import com.cck.jogjog.Fragments.blue.line.blueLineMapDisplayFragment;
import com.cck.jogjog.Fragments.blue.line.blueLinePropertyListFragment;
import com.cck.jogjog.Fragments.blue.line.blueLineSearchSettings;
import com.cck.jogjog.Fragments.blue.line.blueLineStationSelectionFragment;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import java.util.ArrayList;
import java.util.List;

public class greenLineRentListFragment extends Fragment implements View.OnClickListener {


    public static int int_items = 2;
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

        viewPager= view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        String code_station = GlobalVar.getcode_station();
        System.out.println("code_station: "+code_station);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new greenLinePropertyListFragment(), "物件ー覧");
        adapter.addFragment(new greenLineMapDisplayFragment(), "マップ表示");

        viewPager.setAdapter(adapter);
        if(GlobalVar.previousScreen.equals("greenLineSearchSettings")) {
            viewPager.setCurrentItem(GlobalVar.selectedtab);
        }
        if(GlobalVar.previousScreen.equals("greenLineMapInformation")) {
            viewPager.setCurrentItem(GlobalVar.selectedtab);
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
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;

        switch (v.getId()) {
            case R.id.btn_back:
                BukkenList.bukken_list.clear();
                BukkenList.bukken_list_setsubi.clear();
                GlobalVar.clearheya_no();
                GlobalVar.isFromMap=false;
                GlobalVar.selectedtab = 0;
                GlobalVar.lat = "0";
                GlobalVar.lat2 = "0";
                GlobalVar.lon = "0";
                GlobalVar.lon2 = "0";
                selectedFragment = new greenLineStationSelectionFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_search:
                GlobalVar.setparent_fragment("greenLineRentListFragment");
                GlobalVar.selectedtab = viewPager.getCurrentItem();
                selectedFragment = new greenLineSearchSettings();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            default:
                break;
        }

    }
}
