package com.lubaarbel.pingarbel.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.utils.Utils;
import com.lubaarbel.pingarbel.viewmodel.UserInputViewModel;

/**
 * Single Activity principle
 * Used as nav_controller, push intend receiver and permissions handler
 * **/
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private UserInputViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic(Utils.PUSH_NOTIFICATION_TOPIC);

        viewModel = new ViewModelProvider(this).get(UserInputViewModel.class);
        viewModel.handleNotificationIfNeeded(getIntent());

        ensureAppPermissions();
    }

    /** WRITE_EXTERNAL_STORAGE permissions **/
    public static final int PERMISSIONS_REQUEST_CODE = 12321;

    private void ensureAppPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // continue to use the app
            } else {
                showAlertOnLackOfPermissions();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showAlertOnLackOfPermissions() {
        DialogInterface.OnClickListener dialogListener = (dialog, id) -> finish();

        AlertDialog alert = new AlertDialog.Builder(this)
            .setTitle(getString(R.string.permissions_alert_title))
            .setMessage(getString(R.string.permissions_alert_message))
            .setCancelable(false)
            .setNeutralButton(getString(R.string.permissions_alert_bnt), dialogListener)
            .create();
        alert.show();
    }
}
