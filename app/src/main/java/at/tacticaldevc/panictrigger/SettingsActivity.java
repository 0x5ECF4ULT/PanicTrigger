package at.tacticaldevc.panictrigger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashSet;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("conf", MODE_PRIVATE);

        for (String word : prefs.getStringSet("triggerWords", new HashSet<>(Arrays.asList("panic", "Panic", "PANIC"))))
        {
            ((EditText) findViewById(R.id.triggerWords)).append(word + ", ");
        }

        for (String number : prefs.getStringSet("notifyNumbers", new HashSet<String>()))
        {
            ((EditText) findViewById(R.id.notifyNumbers)).append(number + "\n");
        }

        for (String number : prefs.getStringSet("triggerNumbers", new HashSet<String>()))
        {
            ((EditText) findViewById(R.id.triggerNumbers)).append(number + "\n");
        }
    }

    @Override
    public void onClick(View v) {
        prefs.edit().putStringSet("triggerWords", new HashSet<>(
                Arrays.asList(((EditText) findViewById(R.id.triggerWords)).getText().toString().split(", "))
        )).apply();

        prefs.edit().putStringSet("notifyNumbers", new HashSet<>(
                Arrays.asList(((EditText) findViewById(R.id.notifyNumbers)).getText().toString().split("\n"))
        )).apply();

        prefs.edit().putStringSet("triggerNumbers", new HashSet<>(
                Arrays.asList(((EditText) findViewById(R.id.triggerNumbers)).getText().toString().split("\n"))
        )).apply();
        finish();
    }
}
