package com.example.paw1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SheltersActivity extends AppCompatActivity {

    private static final int REQ_LOC = 42;

    private SheltersAdapter adapter;
    private FusedLocationProviderClient fused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelters);

        RecyclerView rv = findViewById(R.id.sheltersRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SheltersAdapter();
        rv.setAdapter(adapter);

        fused = LocationServices.getFusedLocationProviderClient(this);

        loadShelters();    // loads Firestore data into adapter
        tryGetLocation();  // fetches user location and passes it to adapter
    }

    private void loadShelters() {
        FirebaseFirestore.getInstance()
                .collection("shelters")
                .get()
                .addOnSuccessListener((QuerySnapshot snaps) -> {
                    List<SheltersAdapter.ShelterItem> items = new ArrayList<>();
                    for (DocumentSnapshot d : snaps) {
                        String name    = safe(d.getString("name"));
                        String city    = safe(d.getString("city"));
                        String phone   = safe(d.getString("phone"));
                        String address = safe(d.getString("address"));
                        String website = safe(d.getString("website"));
                        Double lat     = d.getDouble("lat");
                        Double lng     = d.getDouble("lng");

                        String subtitle = buildSubtitle(city, phone);

                        items.add(new SheltersAdapter.ShelterItem(
                                name,
                                subtitle,
                                address,
                                website,
                                lat == null ? 0.0 : lat,
                                lng == null ? 0.0 : lng
                        ));
                    }
                    adapter.submit(items);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load shelters.", Toast.LENGTH_SHORT).show()
                );
    }

    private void tryGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOC
            );
            return;
        }
        fused.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener(loc -> {
                    if (loc != null) {
                        adapter.setUserLocation(loc.getLatitude(), loc.getLongitude());
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] res) {
        super.onRequestPermissionsResult(requestCode, perms, res);
        if (requestCode == REQ_LOC && res.length > 0 && res[0] == PackageManager.PERMISSION_GRANTED) {
            tryGetLocation();
        }
    }

    private String buildSubtitle(String city, String phone) {
        StringBuilder sb = new StringBuilder();
        if (!city.isEmpty()) sb.append(city);
        if (!phone.isEmpty()) {
            if (sb.length() > 0) sb.append(" • ");
            sb.append(phone);
        }
        return sb.toString(); // distance is rendered by the adapter
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
