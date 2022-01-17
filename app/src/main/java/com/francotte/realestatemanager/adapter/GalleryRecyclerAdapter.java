package com.francotte.realestatemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.Utils;
import com.francotte.realestatemanager.model.Illustration;

import java.util.List;

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder> {

    private List<Illustration> gallery;

    public GalleryRecyclerAdapter(List<Illustration> gallery) {
        this.gallery = gallery;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updateIllustration(gallery.get(position));
    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public void setData(List<Illustration> newData) {
        if (gallery != null) {
            gallery.clear();
            gallery.addAll(newData);
        } else {
            gallery = newData;
        }
    }


    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView description;
        private final ImageView illustrationView;
        private String picture;

        // Constructor
        public ViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.tv_fragment_detail_item_description);
            illustrationView = itemView.findViewById(R.id.iv_fragment_detail_item);

        }

        public void updateIllustration(Illustration illustration) {

            description.setText(illustration.getDescription());

            if (illustration.getPicture().isEmpty()) {
                illustrationView.setImageResource(R.drawable.add_picture);
            } else {
                picture = com.francotte.realestatemanager.Utils.getIllustrationGalleryFromDevice(illustration);

                Glide.with(illustrationView.getContext())
                        .load(picture)
                        .into(illustrationView);
            }
        }
    }
}
