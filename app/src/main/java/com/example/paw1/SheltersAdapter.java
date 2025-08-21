package com.example.paw1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SheltersAdapter extends RecyclerView.Adapter<SheltersAdapter.VH> {

    public static class ShelterItem {
        public String name, subtitle, address, website;
        public double lat, lng;

        public ShelterItem(String name, String subtitle, String address, String website, double lat, double lng) {
            this.name = name;
            this.subtitle = subtitle;
            this.address = address;
            this.website = website;
            this.lat = lat;
            this.lng = lng;
        }
    }

    private final List<ShelterItem> items = new ArrayList<>();
    private Double userLat = null, userLng = null;

    public void submit(List<ShelterItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void setUserLocation(double lat, double lng) {
        this.userLat = lat;
        this.userLng = lng;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shelter_item, parent, false); // <-- matches your file name
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Context ctx = h.itemView.getContext();
        ShelterItem s = items.get(pos);

        h.name.setText(s.name);
        h.subtitle.setText(s.subtitle);
        h.address.setText(s.address);
        h.website.setText(s.website);

        if (userLat != null && userLng != null && s.lat != 0.0 && s.lng != 0.0) {
            float[] res = new float[1];
            android.location.Location.distanceBetween(userLat, userLng, s.lat, s.lng, res);
            double km = res[0] / 1000.0;
            h.distance.setVisibility(View.VISIBLE);
            h.distance.setText(String.format(Locale.getDefault(), "%.1f km away", km));
        } else {
            h.distance.setVisibility(View.GONE);
        }

        h.website.setOnClickListener(v -> {
            if (s.website != null && !s.website.isEmpty()) {
                String url = s.website.startsWith("http") ? s.website : "http://" + s.website;
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        h.itemView.setOnClickListener(v -> {
            if (s.lat != 0.0 && s.lng != 0.0) {
                Uri uri = Uri.parse("geo:" + s.lat + "," + s.lng + "?q=" + s.lat + "," + s.lng + "(" + Uri.encode(s.name) + ")");
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, subtitle, address, website, distance;
        VH(@NonNull View v) {
            super(v);
            name     = v.findViewById(R.id.shelterName);
            subtitle = v.findViewById(R.id.shelterSubtitle);
            address  = v.findViewById(R.id.shelterAddress);
            website  = v.findViewById(R.id.shelterWebsite);
            distance = v.findViewById(R.id.shelterDistance);
        }
    }
}
