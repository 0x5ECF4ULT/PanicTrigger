package at.tacticaldevc.panictrigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.HashSet;
import java.util.Set;

public class SMSListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction()))
        {
            Set<String> contacts = context.getSharedPreferences("conf", Context.MODE_PRIVATE).getStringSet("alarmContacts", new HashSet<String>());
            for(SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent))
            {
                if(contacts.contains(msg.getOriginatingAddress()) && (msg.getMessageBody().contains("panic") || msg.getMessageBody().contains("Panic") || msg.getMessageBody().contains("PANIC")))
                {
                    triggerAlarm();
                    break;
                }
            }
        }
    }

    private void triggerAlarm() {

    }
}
