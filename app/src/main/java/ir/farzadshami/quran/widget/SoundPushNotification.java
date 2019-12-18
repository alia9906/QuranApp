package ir.farzadshami.quran.widget;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.models.SoundToPlay;
import ir.farzadshami.quran.models.Sura;

public class SoundPushNotification {
    private static final String CHANNEL_ID = "234";
    private static final int NOTIFICATION_ID = 243;
    public class Previous extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(soundPlayer != null) {
                soundPlayer.goToPreviousAye();
                content.setTextViewText(R.id.aye_sura_notification , "سوره "+ Sura.suraNamesArray[soundPlayer.getSuraId()] + " آیه " + (soundPlayer.getVerseId() + 1));
                show();
            }else{
                clear();
            }
        }
    }
    public class PlayPause extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(soundPlayer != null){
                if(soundPlayer.isPlaying()){
                    soundPlayer.pause();
                }else {
                    soundPlayer.play();
                }
            }else{
                clear();
            }
        }
    }
    public class Next extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(soundPlayer != null) {
                soundPlayer.goToNextAye();
                content.setTextViewText(R.id.aye_sura_notification , "سوره "+ Sura.suraNamesArray[soundPlayer.getSuraId()] + " آیه " + (soundPlayer.getVerseId() + 1));
                show();
            }else{
                clear();
            }
        }
    }
    private NotificationCompat.Builder builder;
    private RemoteViews content;
    private SoundToPlay soundPlayer;
    private Context context;
    private Intent clickOn;

    public SoundPushNotification(SoundToPlay soundPlayer , Context context , Intent clickOn){
        this.soundPlayer = soundPlayer;
        this.context = context;
        this.clickOn = clickOn;

        createNotificationChannel();

    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "soundBackPlayQuran";
            String describtion = "Background sound play for quran";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID , name , importance);
            notificationChannel.setDescription(describtion);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void show(){
        clear();
        build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID , builder.build());
    }

    public void clear(){
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID);
    }

    private void build(){

        content = new RemoteViews(context.getPackageName() , R.layout.sound_notification_push);
        context.registerReceiver(new Next() , new IntentFilter("ir.farzadshami.quran.SPN.next"));
        context.registerReceiver(new PlayPause() , new IntentFilter("ir.farzadshami.quran.SPN.playpause"));
        context.registerReceiver(new Previous() , new IntentFilter("ir.farzadshami.quran.SPN.previous"));
        content.setOnClickPendingIntent(R.id.previous , PendingIntent.getBroadcast(context , 0 , new Intent("ir.farzadshami.quran.SPN.previous") , PendingIntent.FLAG_UPDATE_CURRENT));
        content.setOnClickPendingIntent(R.id.pause , PendingIntent.getBroadcast(context , 0 , new Intent("ir.farzadshami.quran.SPN.playpause") , PendingIntent.FLAG_UPDATE_CURRENT));
        content.setOnClickPendingIntent(R.id.next , PendingIntent.getBroadcast(context , 0 , new Intent("ir.farzadshami.quran.SPN.next") , PendingIntent.FLAG_UPDATE_CURRENT));
        content.setTextViewText(R.id.aye_sura_notification , "سوره "+ Sura.suraNamesArray[soundPlayer.getSuraId()] + " آیه " + (soundPlayer.getVerseId() + 1));

        builder = new NotificationCompat.Builder(context)
                .setContent(content)
                .setSmallIcon(R.mipmap.ic_ab_ayeh)
                .setContentIntent(PendingIntent.getActivity(context , 0 , clickOn , 0 ))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setAutoCancel(false)
                .setOngoing(true)
                .setChannelId(CHANNEL_ID);
    }
}
