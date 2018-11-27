package at.tacticaldevc.panictrigger;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SMSListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction()))
        {
            Set<String> contacts = context.getSharedPreferences("conf", Context.MODE_PRIVATE).getStringSet("triggerNumbers", new HashSet<String>());
            Set<String> msgs = context.getSharedPreferences("conf", Context.MODE_PRIVATE).getStringSet("triggerWords", new HashSet<String>());
            for(SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent))
            {
                if(contacts.contains(msg.getOriginatingAddress()) && msgs.contains(msg.getMessageBody()))
                {
                    triggerAlarm(context, msg.getOriginatingAddress());
                    break;
                }
            }
        }
    }

    private void triggerAlarm(Context context, String address) {
        AudioManager audioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        MediaPlayer mp = new MediaPlayer();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + address));

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        try {
            mp.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            //TODO: Ringtone doesn't seem to loop...
            mp.setLooping(true);
            mp.prepare();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                    .setContentTitle("!!! PANIC !!!")
                    .setContentText(address + "triggered alarm! Calling in 1 minute!")
                    .setPriority(NotificationCompat.PRIORITY_MAX);
                    //TODO: Make PendingIntent interrupt the countdown and call immediately
                    // .setContentIntent(PendingIntent.getBroadcast(context, 0, callIntent, 0));
            NotificationManagerCompat.from(context).notify(0, builder.build());
            mp.start();
            TimeUnit.MINUTES.sleep(1);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        mp.stop();
        mp.release();
        context.startActivity(callIntent); //THIS IS NOT AN ERROR!!!
    }
}
