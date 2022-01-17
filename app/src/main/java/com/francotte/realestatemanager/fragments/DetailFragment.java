package com.francotte.realestatemanager.fragments;


import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.Utils;
import com.francotte.realestatemanager.activities.MapActivity;
import com.francotte.realestatemanager.adapter.GalleryRecyclerAdapter;
import com.francotte.realestatemanager.injection.Injection;
import com.francotte.realestatemanager.injection.ViewModelFactory;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.model.Illustration;
import com.francotte.realestatemanager.ui.RealEstateManagerViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    public static final int MAPS_ACTIVITY_REQUEST_CODE = 22;
    public static final String BUNDLE_HOUSE_CLICKED = "BUNDLE_HOUSE_CLICKED";
    private static final long HOUSE_ID = 1;
    private final List<Illustration> gallery = new ArrayList<>();
    private TextView area;
    private TextView rooms;
    private TextView bedrooms;
    private TextView bathrooms;
    private TextView pointOfInterest;
    private TextView address;
    private TextView description;
    private TextView label;
    private TextView agent;
    private TextView dateOfEntry;
    private ImageView mapView;
    private House house;
    private long id;

    private RecyclerView recyclerView;
    private GalleryRecyclerAdapter adapter;
    private RealEstateManagerViewModel realEstateManagerViewModel;

    // Required empty public constructor
    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        this.initActivity(view);
        this.configureViewModel();

        return view;
    }


    // Configure methods

    private void initActivity(View view) {
        area = view.findViewById(R.id.tv_fragment_detail_surface_value);
        rooms = view.findViewById(R.id.tv_fragment_detail_rooms_value);
        bedrooms = view.findViewById(R.id.tv_fragment_detail_bedrooms_value);
        bathrooms = view.findViewById(R.id.tv_fragment_detail_bathrooms_value);
        pointOfInterest = view.findViewById(R.id.tv_fragment_detail_poi_value);
        address = view.findViewById(R.id.tv_fragment_detail_address_value);
        description = view.findViewById(R.id.tv_fragment_detail_description_value);
        agent = view.findViewById(R.id.tv_fragment_detail_agent_value);
        dateOfEntry = view.findViewById(R.id.tv_fragment_detail_date_of_entry_value);
        label = view.findViewById(R.id.lbl_no_house);
        mapView = view.findViewById(R.id.iv_fragment_detail_mapview);
        recyclerView = view.findViewById(R.id.rv_fragment_detail);

        mapView.setOnClickListener(this);
    }

    public void configureRecyclerView() {
        // this.gallery = new ArrayList<>();
        this.adapter = new GalleryRecyclerAdapter(this.gallery);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }

    private void getGalleryHouseFromDatabase(long houseId) {
        this.realEstateManagerViewModel.getGallery(houseId).observe(this, illustrations -> {
            adapter.setData(illustrations);
            updateDisplayList();
        });
    }


    //Tablet display
    public void updateData(House house) {
        updateHouseDetails(house);
        configureViewModel();
        configureRecyclerView();
        getGalleryHouseFromDatabase(house.getId());
    }


    // Update the display of the list
    private void updateDisplayList() {
        if (gallery.size() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            configureRecyclerView();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    //Update the display
    public void updateDisplay(House house) {
        if (house == null) {
            getView().setVisibility(View.GONE);
        } else {
            getView().setVisibility(View.VISIBLE);
        }
    }

    //Fill in the editText
    public void updateHouseDetails(House house) {
        area.setText(String.valueOf(house.getArea()));
        rooms.setText(String.valueOf(house.getNumberOfRooms()));
        bedrooms.setText(String.valueOf(house.getNumberOfBedrooms()));
        bathrooms.setText(String.valueOf(house.getNumberOfBathrooms()));

        List<String> pois = new ArrayList<>();
        if (house.getSchool() == 1) {
            pois.add("école");
        }
        if (house.getShopping() == 1) {
            pois.add("commerce");
        }
        if (house.getPublicTransport() == 1) {
            pois.add("transport");
        }
        if (house.getSwimmingPool() == 1) {
            pois.add("piscine");
        }
        String list = "";
        for (String poi : pois) {
            list = list + " " + poi;
        }

        pointOfInterest.setText(list);
        address.setText(house.getAddress());
        description.setText(house.getDescription());
        agent.setText(house.getRealEstateAgent());
        dateOfEntry.setText(house.getDateOfEntry());

        //Display real estate on staticMap
        Glide.with(mapView.getContext())
                .load(convertAndShowAddressOnStaticMap(house.getAddress()))
                .into(mapView);
    }

    //Convert Address of the real estate and show it on the static map
    public String convertAndShowAddressOnStaticMap(String address) {
        Geocoder coder = new Geocoder(getContext());
        List<Address> addresses;
        try {
            addresses = coder.getFromLocationName(address, 10);
            if (addresses == null) {
            }
            Address location = addresses.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&size=200x200" +
                    "&markers=color:red%7C" + lat + "," + lng + "&sensor=false&key=AIzaSyCV1RfW578IO-3Sr5zeb8uK5xdLx_Gz_5M";
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Listener
    public void onHouseClick(House house) {
        Log.e("Test", "DETAILFRAGMENT OnHouseClick" + house);
        if (house != null) {
            this.house = house;
            //Use for modify
            this.id = house.getId();
        }
    }

    // Override methods

    @Override
    public void onStart() {
        //Smartphone
        if (house != null) {
            this.updateData(house);
        }
        //Tablet
        this.updateDisplay(house);
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        //Start mapsViewActivity if user has network
        if (Utils.haveNetwork()) {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            getActivity().startActivityForResult(intent, MAPS_ACTIVITY_REQUEST_CODE);
            Log.e("Test", "DETAILFRAGMENT Lancement MapActivity");
        } else {
            Toast.makeText(getContext(), "Vous êtes connecté à aucun réseau", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Display house clicked on MapsActivity on smartphone
        if (MAPS_ACTIVITY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
            House houseClicked = (House) data.getSerializableExtra(BUNDLE_HOUSE_CLICKED);
            updateData(houseClicked);
            this.house = houseClicked;
        }
    }

}