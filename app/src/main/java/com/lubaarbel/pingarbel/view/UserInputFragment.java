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
import androidx.navigation.Navigation;

import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.databinding.FragmentUserInputBinding;
import com.lubaarbel.pingarbel.viewmodel.UserInputViewModel;


public class UserInputFragment extends BaseFragment {
    public static final String TAG = UserInputFragment.class.getSimpleName();

    private FragmentUserInputBinding binding;
    private UserInputViewModel viewModel;

    private Observer<String> userInputObserver = text -> {
            dismissKeyboard();
            if (text.isEmpty()) {
                String newCurrent = binding.fragUserInputUpdates.getText().toString() +
                        getString(R.string.frag_user_input_state_empty);
                binding.fragUserInputUpdates.setText(newCurrent);
            } else {
                viewModel.handleUserInput(text);
            }
    };

    private Observer<Boolean> cryptoStatesSignObserver = isSigned -> {
        if (isSigned) {
            String newCurrent = binding.fragUserInputUpdates.getText().toString() +
                    getString(R.string.frag_user_input_state_sign);
            binding.fragUserInputUpdates.setText(newCurrent);
        }
    };
    private Observer<Boolean> cryptoStatesEncObserver = isEncrypted -> {
        if (isEncrypted) {
            String newCurrent = binding.fragUserInputUpdates.getText().toString() +
                    getString(R.string.frag_user_input_state_enc);
            binding.fragUserInputUpdates.setText(newCurrent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_input,
                container, false);

        viewModel = new ViewModelProvider(getActivity()).get(UserInputViewModel.class);

        binding.setViewModel(viewModel);
        binding.setUserInput(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.registerToObserveUserInput(getViewLifecycleOwner(), userInputObserver);
        viewModel.registerToCryptoStatesEncryptingLd(getViewLifecycleOwner(), cryptoStatesEncObserver);
        viewModel.registerToCryptoStatesSigningLd(getViewLifecycleOwner(), cryptoStatesSignObserver);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (viewModel.isShouldNavigateStraightToResults()) {
            Navigation.findNavController(this.getView()).navigate(R.id.action_userInputFragment_to_userInputResultFragment);
        }
    }
}
