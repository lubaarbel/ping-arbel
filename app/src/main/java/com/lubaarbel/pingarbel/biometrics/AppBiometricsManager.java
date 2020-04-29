package com.lubaarbel.pingarbel.biometrics;

import android.util.Log;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lubaarbel.pingarbel.AppHolder;
import java.util.concurrent.Executor;

public class AppBiometricsManager {
    public static final String TAG = AppBiometricsManager.class.getSimpleName();

    private static AppBiometricsManager INSTANCE;

    private AppBiometricsManager() {}

    public static AppBiometricsManager getInstance() {
        AppBiometricsManager instance = INSTANCE;
        if (instance == null) {
            synchronized (AppBiometricsManager.class) {
                instance = INSTANCE;
                if (instance == null) {
                    instance = new AppBiometricsManager();
                    INSTANCE = instance;
                }
            }
        }
        return INSTANCE;
    }

    public boolean canAuthenticate() {
        BiometricManager biometricManager = BiometricManager.from(AppHolder.getContext());
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

    public BiometricPrompt getBiometricsPrompt(Fragment fragmentActivity,
                                               BiometricPrompt.AuthenticationCallback callback) {
        Executor executor = ContextCompat.getMainExecutor(AppHolder.getContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(fragmentActivity,
                executor, callback);
        return biometricPrompt;
    }

    public BiometricPrompt.PromptInfo getBiometricsPromptInfo() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build();
    }
}
