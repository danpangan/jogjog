package com.cck.jogjog.Activities.yellow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cck.jogjog.R;
import com.cck.jogjog.Fragments.yellow.address.yellowAddressRentListFragment;

public class yellowAddressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellowaddress);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container,
                    new yellowAddressRentListFragment()).commit();
        }
    }

}
