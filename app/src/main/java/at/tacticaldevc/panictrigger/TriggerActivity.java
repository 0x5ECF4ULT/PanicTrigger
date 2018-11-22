package at.tacticaldevc.panictrigger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TriggerActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.triggerButton:
                break;
            case R.id.configure:
                break;
        }
    }
}
