package at.tacticaldevc.panictrigger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.views.MapView;

public class MapActivity extends AppCompatActivity {
    private MapView map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}
