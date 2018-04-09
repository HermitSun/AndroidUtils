package com.wssgs.servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by LaoZhao on 2017/11/19.(Thanks!)
 * Updated by WenSun on 2018/4/6.
 * version 1.1
 * 用于实现可兼容的发送通知（已在6.0、7.0、8.0测试）
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context){
        super(context);
    }

    public void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public Notification.Builder getChannelNotification(String title, String content){
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
    }
    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
    }
    //目前默认AutoCancel,实现了可选支持PendingIntent
    //增加了构建通知的功能，减少使用其他功能时的不便
    public Notification buildNotification(String title, String content, PendingIntent...pi){
        Notification notification;
        if(pi.length>0){
            if (Build.VERSION.SDK_INT>=26){
                createNotificationChannel();
                notification = getChannelNotification(title, content)
                        .setContentIntent(pi[0])
                        .build();
            }else{
                notification = getNotification_25(title, content)
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        .setContentIntent(pi[0])
                        .build();
            }
        }
        else{
            if (Build.VERSION.SDK_INT>=26){
                createNotificationChannel();
                notification = getChannelNotification(title, content)
                        .build();
            }else{
                notification = getNotification_25(title, content)
                        .build();
            }
        }
        return notification;
    }
    //该方法适用于只发送通知，使用默认图标
    public void sendNotification(String title, String content, PendingIntent...pi){
        if(pi.length>0){
            if (Build.VERSION.SDK_INT>=26){
                createNotificationChannel();
                Notification notification = getChannelNotification(title, content)
                        .setContentIntent(pi[0])
                        .build();
                getManager().notify(1,notification);
            }else{
                Notification notification = getNotification_25(title, content)
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        .setContentIntent(pi[0])
                        .build();
                getManager().notify(1,notification);
            }
        }
        else{
            if (Build.VERSION.SDK_INT>=26){
                createNotificationChannel();
                Notification notification = getChannelNotification(title, content)
                        .build();
                getManager().notify(1,notification);
            }else{
                Notification notification = getNotification_25(title, content)
                        .build();
                getManager().notify(1,notification);
            }
        }
    }
}
