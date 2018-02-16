package comcast.stb.fm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import org.greenrobot.eventbus.EventBus;

import comcast.stb.R;
import comcast.stb.entity.events.FmLauncherEvent;

import static comcast.stb.StringData.AUTH_TOKEN;
import static comcast.stb.StringData.FM_ID;


public class FmReceiver extends BroadcastReceiver {
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    public static final int NOTIFICATION_ID = 101;
    int fmID;
    String token;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        this.fmID = intent.getIntExtra(FM_ID, 0);
        this.token = intent.getStringExtra(AUTH_TOKEN);

        int notificationId = intent.getIntExtra("notification_id", 0);
        if (notificationId == NOTIFICATION_ID) {
            Intent stopIntent = new Intent(context, FmBindService.class);
            stopIntent.setAction(ACTION_CLOSE);
            context.startService(stopIntent);
        } else {
            buildNotification(context, action);
        }
    }

    public void buildNotification(Context context, String action) {
//        Timber.d("action %s",action);
        Intent intent = new Intent(context, FmReceiver.class);
        intent.putExtra("notification_id", NOTIFICATION_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (action.equals(ACTION_PLAY)) {
            Intent stopIntent = new Intent(context, FmBindService.class);
            stopIntent.putExtra("id", fmID);
            stopIntent.putExtra("token", token);
            stopIntent.setAction(ACTION_PLAY);
            PendingIntent resultPendingIntent =
                    PendingIntent.getService(
                            context,
                            0,
                            stopIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            EventBus.getDefault().post(new FmLauncherEvent("FM","Hello World",false,resultPendingIntent));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // The id of the channel.
                String id = "comcast";
                // The user-visible name of the channel.
                CharSequence name = "Play";
                // The user-visible description of the channel.
                String description = "Playing";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mNotificationManager.createNotificationChannel(mChannel);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context, id)
                                .setSmallIcon(R.drawable.ic_autorenew_white_24dp)
                                .setContentTitle("Comcast FM")
                                .setDeleteIntent(pendingIntent)
                                .addAction(R.drawable.ic_play_circle_outline_black_24dp, "Play", resultPendingIntent)
                                .setContentText("Hello World!");

                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            } else {

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context, "comcast")
                                .setSmallIcon(R.drawable.ic_autorenew_white_24dp)
                                .setContentTitle("Comcast FM")
                                .setDeleteIntent(pendingIntent)
                                .addAction(R.drawable.ic_play_circle_outline_black_24dp, "Play", resultPendingIntent)
                                .setContentText("Hello World!");
                ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, mBuilder.build());
            }
        } else if (action.equals(ACTION_STOP)) {
            Intent stopIntent = new Intent(context, FmBindService.class);
            stopIntent.putExtra("id", fmID);
            stopIntent.putExtra("token", token);
            stopIntent.setAction(ACTION_STOP);
            PendingIntent resultPendingIntent =
                    PendingIntent.getService(
                            context,
                            0,
                            stopIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            EventBus.getDefault().post(new FmLauncherEvent("FM","Hello World",true,resultPendingIntent));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // The id of the channel.
                String id = "comcast";
                // The user-visible name of the channel.
                CharSequence name = "Stop";
                // The user-visible description of the channel.
                String description = "Stop";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mNotificationManager.createNotificationChannel(mChannel);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context, id)
                                .setSmallIcon(R.drawable.ic_autorenew_white_24dp)
                                .setContentTitle("Comcast FM")
                                .setDeleteIntent(pendingIntent)
                                .addAction(R.drawable.ic_play_circle_outline_black_24dp, "Stop", resultPendingIntent)
                                .setContentText("Hello World!");

                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            } else {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context, "comcast")
                                .setSmallIcon(R.drawable.ic_autorenew_white_24dp)
                                .setContentTitle("Comcast FM")
                                .setDeleteIntent(pendingIntent)
                                .addAction(R.drawable.ic_pause_circle_outline_black_24dp, "Stop", resultPendingIntent)
                                .setContentText("Hello World!");
                ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, mBuilder.build());
            }
        }


    }

    /*public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "comcast",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Comcast Mobile");
        notificationManager.createNotificationChannel(channel);
        Notification notification = buildNotification(context);
        notificationManager.notify(101, notification);
    }*/
}