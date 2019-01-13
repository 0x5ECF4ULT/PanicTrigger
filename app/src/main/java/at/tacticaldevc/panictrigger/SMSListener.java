package at.tacticaldevc.panictrigger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
                    try
                    {
                        triggerAlarm(context, msg.getOriginatingAddress(), msgParts[1], msgParts[2]);
                        break;
                    }
                    catch (Exception e)
                    {
                        triggerAlarm(context, msg.getOriginatingAddress(), null, null);
                    }
                }
            }
        }
    }

    private void triggerAlarm(final Context context, String address, String latitude, String longitude) {
        AudioManager audioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        SmsManager smsManager = SmsManager.getDefault();
        final MediaPlayer mp = new MediaPlayer();
        final PendingIntent callIntent = PendingIntent.getActivity(context, 0, new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + address)), PendingIntent.FLAG_ONE_SHOT);
        PendingIntent mapIntent = null;
        if(latitude != null && longitude != null)
            mapIntent = PendingIntent.getActivity(context, 0, new Intent(context, MapActivity.class)
                    .putExtra("lat", Double.parseDouble(latitude))
                    .putExtra("long", Double.parseDouble(longitude)), PendingIntent.FLAG_UPDATE_CURRENT);

        smsManager.sendTextMessage(address, null, "Panic triggered!", null, null);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel("Trigger", "Trigger", NotificationManager.IMPORTANCE_HIGH);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(ch);
        }

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        try {
            mp.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            //TODO: Ringtone doesn't seem to loop...
            mp.setLooping(true);
            mp.prepare();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Trigger")
                    .setSmallIcon(R.drawable.ic_sms_failed)
                    .setContentTitle("!!! PANIC !!!")
                    .setContentText(address + "triggered alarm! Calling in 1 minute!\n" +
                            latitude != null && longitude != null ? "Sender is at " + latitude + "; " + longitude : "")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(R.drawable.ic_call, "Call now!", callIntent)
                    .setContentIntent(mapIntent);
            NotificationManagerCompat.from(context).notify(0, builder.build());
            mp.start();
            Runnable cntDown = new Runnable() {
                @Override
                public void run() {
                    NotificationManagerCompat.from(context).cancel(0);
                    mp.stop();
                    mp.release();
                    try {
                        callIntent.send();
                    } catch (PendingIntent.CanceledException e) {}
                }
            };
            new Handler().postDelayed(cntDown, 60000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
