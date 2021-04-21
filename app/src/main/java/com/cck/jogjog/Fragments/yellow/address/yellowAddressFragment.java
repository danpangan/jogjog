package com.cck.jogjog.Fragments.yellow.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cck.jogjog.Activities.MainActivity;
import com.cck.jogjog.Fragments.home.HomeFragment;
import com.cck.jogjog.R;

import androidx.fragment.app.Fragment;

public class yellowAddressFragment extends Fragment implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yellow_address, container, false);

        Button btn_back = (Button) view.findViewById(R.id.btn_back);
        Button btn_search = (Button) view.findViewById(R.id.btn_search);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_yellow_address_container,
                    new yellowAddressRentListFragment()).commit();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment=null;
        switch (v.getId()){
            case R.id.btn_back:
                selectedFragment = new HomeFragment();
                ((MainActivity)getActivity()).setSelectedFragment(selectedFragment);
                break;
            case R.id.btn_search:
                break;
        }
    }
}
