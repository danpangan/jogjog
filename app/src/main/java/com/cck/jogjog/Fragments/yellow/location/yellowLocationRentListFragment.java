package com.cck.jogjog.Fragments.yellow.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.yellow.line.yellowLineSearchSettings;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class yellowLocationRentListFragment extends Fragment{

    public static int items = 2;
    ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rentlist, container, false);

        viewPager= view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {


        yellowLocationRentListFragment.Adapter adapter = new yellowLocationRentListFragment.Adapter(getChildFragmentManager());
        adapter.addFragment(new yellowLocationPropertyListFragment(), "物件ー覧");
        adapter.addFragment(new yellowLocationMapDisplayFragment(), "マップ表示");

        viewPager.setAdapter(adapter);

        if (GlobalVar.previousScreen.equals("yellowLocationPropertyDetailFragment")) {
            viewPager.setCurrentItem(0);
        }
        else if (GlobalVar.previousScreen.equals("yellowLocationSearchSettings")){
            viewPager.setCurrentItem(GlobalVar.selectedtab);
        }
        else if(GlobalVar.previousScreen.equals("yellowLocationMapInformation")) {
            viewPager.setCurrentItem(GlobalVar.selectedtab);
        }
        else {
            viewPager.setCurrentItem(1);
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

}
