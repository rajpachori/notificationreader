package com.example.notificationreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isNotificationServiceEnabled()) {
            // Redirect user to notification access settings
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        } else {
            // Start the service explicitly
            startService(new Intent(this, NotificationListener.class));
        }

        if (!isBackgroundActivityPermissionGranted()) {
            showBackgroundPermissionDialog();
        }

        findViewById(R.id.btnEnableNotificationAccess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        });
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return flat != null && flat.contains(pkgName);
    }

    private boolean isBackgroundActivityPermissionGranted() {
        // Check if the app is allowed to run in the background
        // This method may vary depending on the device manufacturer and Android version
        // Here is a basic check that should be customized for specific needs
        return true; // Replace with actual check for your specific requirement
    }

    private void showBackgroundPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Background Activity Permission Required")
                .setMessage("To ensure the app functions correctly, please allow it to run in the background.")
                .setPositiveButton("Grant Permission", (dialog, which) -> {
                    // Redirect user to the background activity settings
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
