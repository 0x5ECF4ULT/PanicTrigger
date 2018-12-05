package at.tacticaldevc.panictrigger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapActivity extends AppCompatActivity {
    private MapView map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setMultiTouchControls(true);

        Intent i = getIntent();
        GeoPoint geo = new GeoPoint(i.getDoubleExtra("latitude", 0d), i.getDoubleExtra("longitude", 0d));
        Marker pos = new Marker(map);
        pos.setPosition(geo);
        map.getOverlays().add(pos);

        IMapController controller = map.getController();
        controller.setCenter(geo);
        controller.setZoom(10d);
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
