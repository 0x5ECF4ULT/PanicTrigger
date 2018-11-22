package at.tacticaldevc.panictrigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {
            for(SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent))
            {
                if(msg.getMessageBody().contains("panic") || msg.getMessageBody().contains("Panic") || msg.getMessageBody().contains("PANIC"))
                {
                    triggerAlarm();
                }
            }
        }
    }

    private void triggerAlarm() {

    }
}
