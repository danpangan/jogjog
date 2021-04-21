package com.cck.jogjog.Fragments.yellow.school;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static android.content.Context.MODE_PRIVATE;

public class yellowSchoolRentListFragment extends Fragment implements View.OnClickListener {

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
        adapter.addFragment(new yellowSchoolPropertyListFragment(), "物件ー覧");
        adapter.addFragment(new yellowSchoolMapDisplayFragment(), "マップ表示");
        viewPager.setCurrentItem(1);
        viewPager.setAdapter(adapter);
        if (GlobalVar.previousScreen.equals("yellowSchoolPropertyDetailFragment")) {
            viewPager.setCurrentItem(0);
        }
        else if (GlobalVar.previousScreen.equals("yellowSchoolSearchSettings")){
            viewPager.setCurrentItem(GlobalVar.selectedtab);
        }
        else if(GlobalVar.previousScreen.equals("yellowSchoolMapInformation")) {
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
                BukkenList.clearbukken();
                GlobalVar.clearheya_no();
                GlobalVar.isFromMap=false;
                GlobalVar.selectedtab = 1;

                selectedFragment = new yellowSchoolListDisplay();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_search:
                GlobalVar.setparent_fragment("yellowSchoolRentListFragment");
                GlobalVar.selectedtab = viewPager.getCurrentItem();
                selectedFragment = new yellowSchoolSearchSettings();
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
