package com.example.cattinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.cattinder.MainActivity;
import com.example.cattinder.R;

public class NotificationService extends Service {

    public int NOTIFICATION_ID = 999999;

    int randomNotificationMessage;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, getNotification());
    }

    public Notification getNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setContentTitle("Get swiping!")
                .setContentText(getNotificationMessage(randomNotificationMessage))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

    static String getNotificationMessage(int i) {
        String[] notificationMessages = {
                "Don't forget to swipe right on your favorite cats!",
                "Swipe left on cats you don't like!",
                "Swipe right on cats you like!"
        };
        return notificationMessages[i];
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {

                randomNotificationMessage = (int) (Math.random() * 3);

                handler.postDelayed(this, 5000);
            }
        });

        return START_STICKY;
    }
}
