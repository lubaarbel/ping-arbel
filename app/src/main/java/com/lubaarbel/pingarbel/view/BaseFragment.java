package com.lubaarbel.pingarbel.view;


import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

/**
 * BaseFragment should be applied to provide common visibility and actions to all app fragments
 * Here I added a func to dismiss keyboard.
 * **/
public abstract class BaseFragment extends Fragment {

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getApplicationWindowToken(), 0);
        }
    }
}
