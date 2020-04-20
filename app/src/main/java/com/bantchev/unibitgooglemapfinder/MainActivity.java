package com.bantchev.unibitgooglemapfinder;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapsInitializer.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.sv_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (!location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        Address address = Objects.requireNonNull(addressList).get(0);

                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        Toast toast = Toast.makeText(getApplicationContext(),
                                address.getLatitude() + "   "
                                        + address.getLongitude(), Toast.LENGTH_LONG);

                        toast.setGravity(Gravity.CENTER, 0, 150);
                        toast.show();
                        
                        map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(location.toUpperCase()));

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng sofia = new LatLng(42.69751, 23.32415);
        map.addMarker(new MarkerOptions().position(sofia).title("София"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sofia, 5));
    }
}
