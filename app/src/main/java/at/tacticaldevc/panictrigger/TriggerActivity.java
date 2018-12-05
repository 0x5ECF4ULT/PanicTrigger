package at.tacticaldevc.panictrigger;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class TriggerActivity extends AppCompatActivity implements View.OnClickListener, LocationListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);

        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CALL_PRIVILEGED) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PRIVILEGED,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
            }, 1);
        }

        if(!getSharedPreferences("conf", MODE_PRIVATE).getBoolean("firstStartDone", false))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Oh, hey there!")
                    .setMessage("Looks like you are using my app! :)\n" +
                            "Welcome! First I will tell you a little bit about the purpose of this app.\n" +
                            "It has been made for emergency situations where you may need help or just want " +
                            "to alarm your friends. This is especially useful e.g. if you feel like someone " +
                            "is following you and don't want to call the police because it could cause your " +
                            "follower to attack you.\n" +
                            "Another example would be if you don't feel good and can't really move you only " +
                            "need to tap this shiny red button.\n\n" +
                            "Got it? Good! :-)\n" +
                            "In fact this app does nothing else than sending out a special SMS to your configured " +
                            "contacts. This triggers an alarm on their side for a minute. At this time of " +
                            "development they will automatically call you after the alarm is over. The only " +
                            "requirement is that your opponent has this app installed otherwise they will just " +
                            "see a SMS with human-readable content.\n" +
                            "Now, let's configure some contacts to notify!")
                    .setPositiveButton("Let's do it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(TriggerActivity.this, SettingsActivity.class));
                        }
                    })
                    .setNegativeButton("No thanks.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(TriggerActivity.this)
                                    .setTitle("!!! WARNING !!!")
                                    .setMessage("You decided to not configure PanicTrigger. This will " +
                                            "cause the app to call emergency services if you tap on the " +
                                            "big red button!\n\n" +
                                            "Are you SURE you don't want to configure contacts?")
                                    .setPositiveButton("NO! I tapped the wrong button.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(TriggerActivity.this, "Alright! Let's configure some things...", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(TriggerActivity.this, SettingsActivity.class));
                                        }
                                    })
                                    .setNegativeButton("Yes.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .show();
                        }
                    })
                    .show();
            new AlertDialog.Builder(this)
                    .setTitle("DISCLAIMER")
                    .setMessage("I am NOT responsible for any damage caused by this app! " +
                            "If you come crying to me with tears in your eyes because you lost your " +
                            "job or something I WILL POINT MY FINGER AT YOU AND LAUGH! " +
                            "I am not responsible for ANYTHING! Period.\n\n" +
                            "Do you agree with that?\n" +
                            "Note: You need to agree to use this app!")
                    .setPositiveButton("Yes.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setNegativeButton("No.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
            getSharedPreferences("conf", MODE_PRIVATE).edit().putBoolean("firstStartDone", true).apply();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.triggerButton:
                getCurrentLocationAndPanic();
                break;
            case R.id.configure:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
        }
    }

    private void sendOutPanic(Location loc)
    {
        Set<String> contacts = getSharedPreferences("conf", MODE_PRIVATE).getStringSet(getString(R.string.var_numbers_notify), new HashSet<String>());
        if(contacts.isEmpty())
        {
            Intent emergService = new Intent(Intent.ACTION_CALL, Uri.parse("tel:112"));
            startActivity(emergService);
        }
        String keyword = getSharedPreferences("conf", MODE_PRIVATE).getString(getString(R.string.var_words_keyword), "Panic");
        SmsManager manager = SmsManager.getDefault();
        for (String number : contacts)
        {
            manager.sendTextMessage(number, null,
                    keyword + "\n" +
                    loc.getLatitude() + "\n" +
                    loc.getLongitude(), null, null);
        }
    }

    private void getCurrentLocationAndPanic()
    {
        Location currLoc;
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            locManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        else if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        else
            sendOutPanic(locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
    }

    @Override
    public void onLocationChanged(Location location) {
        sendOutPanic(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
