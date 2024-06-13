package com.example.notificationreader;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "Notification Listener connected");
        initializeMediaPlayer();
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG, "Notification Listener disconnected, requesting rebind");
        requestRebind(new ComponentName(this, NotificationListener.class));
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if (notification != null && notification.extras != null) {
            CharSequence notificationText = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
            if (notificationText != null && containsIgnoreCase(notificationText.toString(), "waiting for approval")) {
                playSound();
            }
        }
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.gate_approval);
        }
    }

    private boolean containsIgnoreCase(String src, String target) {
        return src.toLowerCase().contains(target.toLowerCase());
    }

    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            Log.e(TAG, "MediaPlayer is null, cannot play sound.");
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
