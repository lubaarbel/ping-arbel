package com.lubaarbel.pingarbel.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.lubaarbel.pingarbel.view.UserInputFragment;
import com.lubaarbel.pingarbel.view.UserInputResultFragment;

public class CryptoFragmentFactory extends FragmentFactory {

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class clazz = loadFragmentClass(classLoader, className);
        Fragment fragment;

        if (clazz == UserInputFragment.class) {
            fragment = UserInputFragment.newInstance();
        } else
        if (clazz == UserInputResultFragment.class) {
            fragment = UserInputResultFragment.newInstance();
        } else {
            fragment = super.instantiate(classLoader, className);
        }
        return fragment;

    }
}
