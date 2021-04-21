package com.cck.jogjog.Fragments.blue.location;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.home.HomeFragment;
import com.cck.jogjog.Fragments.yellow.location.yellowLocationRentListFragment;
import com.cck.jogjog.Fragments.yellow.location.yellowLocationSearchSettings;
import com.cck.jogjog.GlobalVar.BukkenList;
import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import static android.content.Context.MODE_PRIVATE;

public class blueLocationFragment extends Fragment implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_location, container, false);

        Button btn_back = view.findViewById(R.id.btn_back_location);
        Button btn_search = view.findViewById(R.id.btn_search);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_yellow_location_container,
                    new blueLocationRentListFragment()).commit();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {
            case R.id.btn_back_location:
                clearSearchPref();
                BukkenList.bukken_list.clear();
                BukkenList.sliderattr.clear();
                BukkenList.bukken_list_imagelist.clear();
                BukkenList.bukken_list_setsubi.clear();
                GlobalVar.clearheya_no();
                GlobalVar.isFromMap=false;
                GlobalVar.selectedtab = 0;
                GlobalVar.lat = "0";
                GlobalVar.lat2 = "0";
                GlobalVar.lon = "0";
                GlobalVar.lon2 = "0";
                selectedFragment = new HomeFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_search:
                selectedFragment = new blueLocationSearchSettings();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
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