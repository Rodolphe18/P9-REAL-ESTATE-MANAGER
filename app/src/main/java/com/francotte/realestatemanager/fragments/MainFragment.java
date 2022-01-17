package com.francotte.realestatemanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.activities.MainActivity;
import com.francotte.realestatemanager.adapter.HouseRecyclerAdapter;
import com.francotte.realestatemanager.injection.Injection;
import com.francotte.realestatemanager.injection.ViewModelFactory;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.ui.RealEstateManagerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements HouseRecyclerAdapter.OnHouseListener, View.OnClickListener {

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 26;
    public static final String BUNDLE_RESULT_LIST = "BUNDLE_RESULT_LIST";
    private final List<House> houseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button displayListButton;
    private List<House> searchHouseList = new ArrayList<>();
    private HouseRecyclerAdapter adapter;
    private TextView lblNoHouse;
    private RealEstateManagerViewModel realEstateManagerViewModel;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = view.findViewById(R.id.fragment_main_recyclerview);
        lblNoHouse = view.findViewById(R.id.lbl_no_house);
        displayListButton = view.findViewById(R.id.bt_display_list);

        displayListButton.setOnClickListener(this);

        this.configureViewModel();
        this.configureRecyclerView();

        return view;
    }

    //Configure methods


    private void configureRecyclerView() {
        this.adapter = new HouseRecyclerAdapter(this.houseList, this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        realEstateManagerViewModel.getAll().observe((LifecycleOwner) this, houseList -> {
            adapter.setData(houseList);
            updateDisplay();
        });

    }

    private void updateDisplay() {
        if (houseList.size() == 0) {
            lblNoHouse.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            lblNoHouse.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    //Override methods

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SEARCH_ACTIVITY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
            displayListButton.setVisibility(View.VISIBLE);
            searchHouseList = (List<House>) data.getSerializableExtra(BUNDLE_RESULT_LIST);
            adapter.setData(searchHouseList);
        }
    }

    @Override
    public void onHouseClick(int position) {
        ((MainActivity) getActivity()).onHouseClick(houseList.get(position));
    }


    @Override
    public void onClick(View v) {
        configureViewModel();
        displayListButton.setVisibility(View.GONE);

    }
}