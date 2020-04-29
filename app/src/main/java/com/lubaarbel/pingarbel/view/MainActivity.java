package com.lubaarbel.pingarbel.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.databinding.ActivityMainBinding;
import com.lubaarbel.pingarbel.model.UserInputModel;
import com.lubaarbel.pingarbel.navigation.CryptoFragmentFactory;
import com.lubaarbel.pingarbel.viewmodel.UserInputViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding dataBinding;
    private UserInputViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportFragmentManager().setFragmentFactory(new CryptoFragmentFactory());
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("input");

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(UserInputViewModel.class);

        UserInputModel.getInstance().registerToIncomingUserInputEncryptedLd(this, incomingUserInputEncryptedObserver);
        viewModel.handleNotificationIfNeeded(getIntent());

        loadUserInputFragment();
    }

    private Observer<String> incomingUserInputEncryptedObserver = text -> {
        loadUserInputResultFragment();
    };

    /** Fragments **/
    private void loadUserInputFragment() {
        commitFragment(UserInputFragment.class, false, UserInputFragment.TAG, new Bundle());
    }

    private void loadUserInputResultFragment() {
        commitFragment(UserInputResultFragment.class, true, UserInputResultFragment.TAG, new Bundle());
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
