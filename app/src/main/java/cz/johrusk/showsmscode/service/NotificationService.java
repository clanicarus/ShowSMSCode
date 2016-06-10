package cz.johrusk.showsmscode.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import cz.johrusk.showsmscode.R;
import cz.johrusk.showsmscode.activity.MainActivity;
import timber.log.Timber;

/**
 * Service which sends notification which contain the code and number of sender.
 *
 * @author Josef Hruska (pepa.hruska@gmail.com)
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String[] dataArray = new String[4];
        Bundle bundle = intent.getExtras();
        dataArray = bundle.getStringArray("key");
        String notifType = dataArray[3];

        String smsContent = null;
        String smsSender = null;

        Intent appIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        appIntent.addCategory(Intent.CATEGORY_DEFAULT);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        int nID = 0;
        switch (notifType) {
            case "notifCode":
                appIntent = new Intent(this, MainActivity.class);
                smsContent = dataArray[0];
                smsSender = dataArray[2];
                nID = 1;
                break;
            default:
                Timber.d("Switch statement didn't catch the case:" + notifType);
        }
        PendingIntent startAppIntent =
                PendingIntent.getActivity(this, 0, appIntent, 0);

        // Create a big text style for the second page
        android.support.v4.app.NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle("Page 2")
                .bigText("A lot of text...");

        // Create second page notification
        Notification secondPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(secondPageStyle)
                        .build();

        NotificationCompat.Builder notifBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_sms)
                        .setContentTitle(smsContent)
                        .setContentText(smsSender)
                        .setAutoCancel(true);

        if (notifType.equals("notifCode")) {
            notifBuilder.setContentIntent(startAppIntent);
        }

        Notification notif = notifBuilder
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(nID, notif);
    }
}