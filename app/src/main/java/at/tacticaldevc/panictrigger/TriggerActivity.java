package at.tacticaldevc.panictrigger;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

import java.util.HashSet;
import java.util.Set;

public class TriggerActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);

        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE
            }, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.triggerButton:
                Set<String> contacts = getSharedPreferences("conf", MODE_PRIVATE).getStringSet("notifyNumbers", new HashSet<String>());
                String keyword = getSharedPreferences("conf", MODE_PRIVATE).getString("keyword", "Panic");
                SmsManager manager = SmsManager.getDefault();
                for (String number : contacts)
                {
                    manager.sendTextMessage(number, null, keyword, null, null);
                }
                break;
            case R.id.configure:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
        }
    }
}
