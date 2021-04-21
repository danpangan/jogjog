package com.cck.jogjog.Fragments.blue.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cck.jogjog.Fragments.yellow.location.yellowLocationMapDisplayFragment;
import com.cck.jogjog.Fragments.yellow.location.yellowLocationPropertyListFragment;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import java.util.ArrayList;
import java.util.List;

public class blueLocationRentListFragment extends Fragment{

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


        blueLocationRentListFragment.Adapter adapter = new blueLocationRentListFragment.Adapter(getChildFragmentManager());
        adapter.addFragment(new blueLocationPropertyListFragment(), "物件ー覧");
        adapter.addFragment(new blueLocationMapDisplayFragment(), "マップ表示");

        viewPager.setAdapter(adapter);

        if (GlobalVar.previousScreen.equals("blueLocationPropertyDetailFragment")) {
            viewPager.setCurrentItem(0);
        }
        else if (GlobalVar.previousScreen.equals("blueLocationSearchSettings")){
            viewPager.setCurrentItem(GlobalVar.selectedtab);
        }
        else if(GlobalVar.previousScreen.equals("blueLocationMapInformation")) {
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
