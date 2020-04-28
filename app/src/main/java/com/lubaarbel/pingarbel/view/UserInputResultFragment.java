package com.lubaarbel.pingarbel.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.action.IUserInputResult;
import com.lubaarbel.pingarbel.databinding.FragmentUserInputResultBinding;
import com.lubaarbel.pingarbel.viewmodel.UserInputViewModel;

import java.util.concurrent.Executor;

public class UserInputResultFragment extends BaseFragment implements IUserInputResult {
    public static final String TAG = UserInputResultFragment.class.getSimpleName();

    private FragmentUserInputResultBinding binding;
    private UserInputViewModel viewModel; // common vm
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public static UserInputResultFragment newInstance() {
        return new UserInputResultFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_input_result,
                container, false);

        viewModel = new ViewModelProvider(getActivity()).get(UserInputViewModel.class);

        binding.setNothingYet("Nothing yet to show...");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBiometricsPrompt();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (canAuthenticate()) {
            // fragment transaction of current Fragment haven't finished by the time biometricPrompt fragment committed
            new Handler().postDelayed(() -> launchBiometricAuthenticationWindow(), 500);
        } else {
            // notify user for alternative authentication
        }
    }

    public void launchBiometricAuthenticationWindow() {
        biometricPrompt.authenticate(promptInfo);
    }

    private void initBiometricsPrompt() {
        Executor executor = ContextCompat.getMainExecutor(getActivity());
        biometricPrompt = new BiometricPrompt(getActivity(),
                executor, new BiometricPrompt.AuthenticationCallback() {
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
                decipherDataPayloadFromPush();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToast("Authentication failed");
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build();
    }

    private void decipherDataPayloadFromPush() {

        // TODO hide re-auth button and build model
        if (getArguments() != null) {
            String dataToDecipher = (String) getArguments().get("userInput");
            viewModel.handleEncryptedDataFromNotification(dataToDecipher);
        }
    }

    private void showToast(String msg) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show());
    }

    private boolean canAuthenticate() {
        BiometricManager biometricManager = BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(TAG, "App can authenticate using biometrics.");
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(TAG, "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(TAG, "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(TAG, "The user hasn't associated " +
                        "any biometric credentials with their account.");
                break;
        }
        return false;
    }

    @Override
    public void onUserClickedReAuth(View view) {
        launchBiometricAuthenticationWindow();
    }
}
