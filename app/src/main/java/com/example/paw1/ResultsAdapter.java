package com.example.paw1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.VH> {

    public static class BreedItem {
        public final String name, desc, imageUrl;
        public BreedItem(String name, String desc, String imageUrl) {
            this.name = name;
            this.desc = desc;
            this.imageUrl = imageUrl;
        }
    }

    private final List<BreedItem> items = new ArrayList<>();

    public void submit(List<BreedItem> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        BreedItem b = items.get(pos);
        h.title.setText(b.name == null ? "" : b.name);
        h.desc.setText(b.desc == null ? "" : b.desc);
        if (b.imageUrl != null && !b.imageUrl.isEmpty()) {
            Glide.with(h.image.getContext()).load(b.imageUrl).centerCrop().into(h.image);
        } else {
            h.image.setImageDrawable(null);
        }
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, desc;
        VH(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.resultImage);
            title = v.findViewById(R.id.resultTitle);
            desc  = v.findViewById(R.id.resultDesc);
        }
    }
}
