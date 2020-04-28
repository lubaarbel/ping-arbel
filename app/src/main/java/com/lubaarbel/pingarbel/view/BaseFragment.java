package com.lubaarbel.pingarbel.view;


import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getApplicationWindowToken(), 0);
        }
    }
}
