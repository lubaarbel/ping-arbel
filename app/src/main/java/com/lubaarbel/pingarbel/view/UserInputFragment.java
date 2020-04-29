package com.lubaarbel.pingarbel.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.databinding.FragmentUserInputBinding;
import com.lubaarbel.pingarbel.viewmodel.UserInputViewModel;


public class UserInputFragment extends BaseFragment {
    public static final String TAG = UserInputFragment.class.getSimpleName();

    private FragmentUserInputBinding binding;
    private UserInputViewModel viewModel;

    private Observer<String> userInputObserver = text -> {
            dismissKeyboard();
            viewModel.handleUserInput(text);
    };

    public static UserInputFragment newInstance() {
        return new UserInputFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_input,
                container, false);

        viewModel = new ViewModelProvider(getActivity()).get(UserInputViewModel.class);

        binding.setUserInput(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.registerToObserveUserInput(getViewLifecycleOwner(), userInputObserver);
    }
}
