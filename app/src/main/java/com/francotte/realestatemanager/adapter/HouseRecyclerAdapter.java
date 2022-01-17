package com.francotte.realestatemanager.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.Utils;
import com.francotte.realestatemanager.model.House;

import java.util.List;

public class HouseRecyclerAdapter extends RecyclerView.Adapter<HouseRecyclerAdapter.ViewHolder> {

    private final OnHouseListener onHouseListener;
    private List<House> houseList;

    public HouseRecyclerAdapter(List<House> houseList, OnHouseListener onHouseListener) {
        this.houseList = houseList;
        this.onHouseListener = onHouseListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_item, parent, false);
        return new ViewHolder(view, onHouseListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updateHouse(houseList.get(position));
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    public void setData(List<House> newData) {
        if (houseList != null) {
            houseList.clear();
            houseList.addAll(newData);
            notifyDataSetChanged();
        } else {
            houseList = newData;
        }
    }

    // Use to detect the click
    public interface OnHouseListener {
        void onHouseClick(int position);
    }

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView category;
        private final TextView district;
        private final TextView price;
        private final TextView sale;
        private final ImageView illustrationView;
        private final ImageView changeView;
        private final OnHouseListener onHouseListener;
        public Resources res;
        private String illustration;

        // Constructor
        public ViewHolder(View itemView, OnHouseListener onHouseListener) {
            super(itemView);
            category = itemView.findViewById(R.id.tv_fragment_main_item_category);
            district = itemView.findViewById(R.id.tv_fragment_main_item_district);
            price = itemView.findViewById(R.id.tv_fragment_main_item_price);
            illustrationView = itemView.findViewById(R.id.fragment_main_item_illustration);
            changeView = itemView.findViewById(R.id.iv_fragment_main_item_change);
            sale = itemView.findViewById(R.id.tv_fragment_main_item_sale);
            this.onHouseListener = onHouseListener;

            itemView.setOnClickListener(this);
        }

        public void updateHouse(House house) {

            category.setText(house.getCategory());
            district.setText(house.getDistrict());

            if (!house.isAvailable()) {
                sale.setVisibility(View.VISIBLE);
                sale.setText("VENDU LE " + house.getDateOfSale());
            }

            RequestOptions myOptions = new RequestOptions()
                    .centerCrop()
                    .override(100, 100);

            boolean isEuro = house.isEuro();


            if (isEuro == true) {
                price.setText(String.valueOf(house.getPrice()));
                changeView.setImageResource(R.drawable.ic_baseline_eur_24);
            } else if (!isEuro) {
                price.setText(String.valueOf(Utils.convertEuroToDollars(house.getPrice())));
                changeView.setImageResource(R.drawable.ic_baseline_price_change_24);
            }

            if (house.getIllustration().isEmpty()) {
                illustrationView.setImageResource(R.drawable.sell_house);

                if (house.isAvailable() == false) {
                    int color = Color.parseColor("#80FF333F");
                    illustrationView.setColorFilter(color);
                }
            } else {
                illustration = Utils.getIllustrationFromDevice(house);

                Glide.with(illustrationView.getContext())
                        .load(illustration)
                        .apply(myOptions)
                        .into(illustrationView);
                if (house.isAvailable() == false) {
                    int color = Color.parseColor("#80FF333F");
                    illustrationView.setColorFilter(color);
                }
            }
        }

        @Override
        public void onClick(View view) {
            onHouseListener.onHouseClick(getAdapterPosition());
            Log.e("Test", "ADAPTER Clic sur la vue");
        }
    }
}
