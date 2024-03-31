package com.example.cattinder.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.cattinder.Activities.MainActivity;
import com.example.cattinder.Models.LikedCat;
import com.example.cattinder.R;
import com.example.cattinder.Tasks.ImageLoadTask;
import com.example.cattinder.Widgets.CatTinderWidget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CatTinderService extends Service {

    private static final int NOTIFICATION_ID = 999999;
    private int randomNotificationMessage;
    private int currentCatIndex = 0;


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

                updateWidget();

                handler.postDelayed(this, 60000); // Every minute it will change the notification
            }
        });

        return START_STICKY;
    }

    private void updateWidget() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.cat_tinder_widget);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        FirebaseFirestore
                .getInstance()
                .collection("history")
                .document(user.getUid())
                .collection("liked-cats")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        ArrayList<LikedCat> likedCats = new ArrayList<>(task.getResult().toObjects(LikedCat.class));

                        if (likedCats.isEmpty()) {
                            return;
                        }

                        LikedCat nextCat = getNextCat(likedCats);

                        assert nextCat != null;
                        new ImageLoadTask(CatTinderService.this, nextCat.getImageUrl(), remoteViews, R.id.cat_widget_image).execute();
                        remoteViews.setOnClickPendingIntent(R.id.cat_widget_image, pendingIntent);

                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this.getApplicationContext(), CatTinderWidget.class));
                        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                    }
                });


    }

    private LikedCat getNextCat(ArrayList<LikedCat> likedCats) {
        if (currentCatIndex > likedCats.size()) {
            return null;
        }

        LikedCat nextCat = likedCats.get(currentCatIndex);
        currentCatIndex = (currentCatIndex + 1) % likedCats.size(); // Wrap around to the start of the list
        return nextCat;
    }
}