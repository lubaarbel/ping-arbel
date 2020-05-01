package com.lubaarbel.pingarbel.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.action.IUserInputResult;
import com.lubaarbel.pingarbel.biometrics.AppBiometricsManager;
import com.lubaarbel.pingarbel.databinding.FragmentUserInputResultBinding;
import com.lubaarbel.pingarbel.viewmodel.UserInputViewModel;

/** Second fragment **/
public class UserInputResultFragment extends BaseFragment implements IUserInputResult {
    public static final String TAG = UserInputResultFragment.class.getSimpleName();

    private FragmentUserInputResultBinding binding;
    private UserInputViewModel viewModel; // common vm
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private AppBiometricsManager biometricsManager;

    private Observer<Boolean> cryptoStatesVerifyObserver = isEncrypted -> {
        if (isEncrypted) {
            String newCurrent = binding.fragUserInputResultUpdates.getText().toString() +
                    getString(R.string.frag_user_input_result_state_verify);
            binding.fragUserInputResultUpdates.setText(newCurrent);
        }
    };
    private Observer<Boolean> cryptoStatesDecObserver = isEncrypted -> {
        if (isEncrypted) {
            String newCurrent = binding.fragUserInputResultUpdates.getText().toString() +
                    getString(R.string.frag_user_input_result_state_dec) +
                    getString(R.string.frag_user_input_result_state_result);
            binding.fragUserInputResultUpdates.setText(newCurrent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_input_result,
                container, false);

        viewModel = new ViewModelProvider(getActivity()).get(UserInputViewModel.class);
        viewModel.initAndSaveResultsModel();

        binding.setModel(viewModel.getResultsModel());
        binding.setAction(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.registerToCryptoStatesVerifyingLd(getViewLifecycleOwner(), cryptoStatesVerifyObserver);
        viewModel.registerToCryptoStatesDecryptingLd(getViewLifecycleOwner(), cryptoStatesDecObserver);
        viewModel.setShouldNavigateStraightToResults(false);

        biometricsManager = AppBiometricsManager.getInstance();

        biometricPrompt = biometricsManager.getBiometricsPrompt(this, bioCallback);
        promptInfo = biometricsManager.getBiometricsPromptInfo();
    }

    public void launchBiometricAuthenticationWindow() {
        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (biometricsManager.canAuthenticate() && viewModel.isShouldBioAuthenticate()) {
            launchBiometricAuthenticationWindow();
        } else {
            decipherDataPayloadFromPush();
        }
    }

    private BiometricPrompt.AuthenticationCallback bioCallback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode,
        @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            showToast("Authentication error: " + errString);
        }

        @Override
        public void onAuthenticationSucceeded(
                @NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            showToast("Authentication succeeded!");
            viewModel.getResultsModel().setAuthenticated(true);
            decipherDataPayloadFromPush();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            showToast("Authentication failed");
        }
    };

    private void decipherDataPayloadFromPush() {
        viewModel.handleEncryptedDataFromNotification();
    }

    private void showToast(String msg) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onUserClickedReAuth(View view) {
        launchBiometricAuthenticationWindow();
    }
}
