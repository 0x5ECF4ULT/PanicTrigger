package at.tacticaldevc.panictrigger;

import android.app.PendingIntent;
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
import android.telephony.SmsManager;
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
            Set<String> contacts = context.getSharedPreferences("conf", Context.MODE_PRIVATE).getStringSet(context.getString(R.string.var_numbers_trigger), new HashSet<String>());
            String msgs = context.getSharedPreferences("conf", Context.MODE_PRIVATE).getString(context.getString(R.string.var_words_trigger), "Panic");
            for(SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent))
            {
                String[] msgParts = msg.getMessageBody().split("\n");
                if(contacts.contains(msg.getOriginatingAddress()) && msgs.contains(msgParts[0]))
                {
                    triggerAlarm(context, msg.getOriginatingAddress(), msgParts[1], msgParts[2]);
                    break;
                }
            }
        }
    }

    private void triggerAlarm(Context context, String address, String latitude, String longitude) {
        AudioManager audioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        SmsManager smsManager = SmsManager.getDefault();
        MediaPlayer mp = new MediaPlayer();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + address));
        Intent mapIntent = new Intent(context, MapActivity.class)
                .putExtra("lat", Double.parseDouble(latitude))
                .putExtra("long", Double.parseDouble(longitude));

        smsManager.sendTextMessage(address, null, "Panic triggered!", null, null);

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        try {
            mp.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            //TODO: Ringtone doesn't seem to loop...
            mp.setLooping(true);
            mp.prepare();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Trigger")
                    .setContentTitle("!!! PANIC !!!")
                    .setContentText(address + "triggered alarm! Calling in 1 minute!\n" +
                            "Sender is at " + latitude + "; " + longitude)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(R.drawable.ic_call, "Call now!", PendingIntent.getActivity(context, 0, callIntent, 0))
                    .setContentIntent(PendingIntent.getActivity(context, 0, mapIntent, 0));
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
