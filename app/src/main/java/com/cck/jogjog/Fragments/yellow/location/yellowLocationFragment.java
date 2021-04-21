package com.cck.jogjog.Fragments.yellow.location;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.home.HomeFragment;
import com.cck.jogjog.R;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class yellowLocationFragment extends Fragment implements View.OnClickListener {


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
                    new yellowLocationRentListFragment()).commit();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {
            case R.id.btn_back_location:
                clearSearchPref();
                selectedFragment = new HomeFragment();
                ((MainActivity) getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_search:
                selectedFragment = new yellowLocationSearchSettings();
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