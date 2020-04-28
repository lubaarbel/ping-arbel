package com.lubaarbel.pingarbel.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.databinding.ActivityMainBinding;
import com.lubaarbel.pingarbel.navigation.CryptoFragmentFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportFragmentManager().setFragmentFactory(new CryptoFragmentFactory());
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("input");

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        loadUserInputFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null &&
                getIntent().getAction().equals("OPEN_ACTIVITY_1") &&
                getIntent().getExtras() != null) {
            loadUserInputResultFragment(getIntent().getExtras());
        }
    }

    /** Fragments **/
    private void loadUserInputFragment() {
        commitFragment(UserInputFragment.class, false, UserInputFragment.TAG, new Bundle());
    }

    private void loadUserInputResultFragment(Bundle bundle) {
        commitFragment(UserInputResultFragment.class, true, UserInputResultFragment.TAG, bundle);
    }

    private void commitFragment(@NonNull Class<? extends Fragment> fragmentClass, boolean addToBackStack, String tag, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragmentClass, bundle);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }
}
