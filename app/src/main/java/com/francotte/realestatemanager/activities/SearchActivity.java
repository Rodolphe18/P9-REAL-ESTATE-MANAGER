package com.francotte.realestatemanager.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.injection.Injection;
import com.francotte.realestatemanager.injection.ViewModelFactory;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.ui.RealEstateManagerViewModel;

import java.io.Serializable;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    public static final String BUNDLE_RESULT_LIST = "BUNDLE_RESULT_LIST";
    private static final long HOUSE_ID = 1;
    private RealEstateManagerViewModel realEstateManagerViewModel;

    //For Data
    private String district;
    private int miniPrice;
    private int maxiPrice;
    private int school = 0;
    private int shopping = 0;
    private int publicTransport = 0;
    private int swimmingPool = 0;
    private int miniArea;
    private int maxiArea;
    private int miniRoom;
    private int maxiRoom;

    //For Design
    private EditText etMiniPrice;
    private EditText etMaxiPrice;
    private EditText etDistrict;
    private EditText etMiniArea;
    private EditText etMaxiArea;
    private EditText etMiniRoom;
    private EditText etMaxiRoom;
    private Button btFilter;
    private CheckBox schoolCb;
    private CheckBox shoppingCb;
    private CheckBox publicTransportCb;
    private CheckBox swimmingPoolCb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.initActivity();
        this.configureViewModel();
    }

    private void initActivity() {
        etMiniPrice = findViewById(R.id.et_search_activity_price_mini);
        etMaxiPrice = findViewById(R.id.et_search_activity_price_max);
        etDistrict = findViewById(R.id.et_search_activity_district);
        etMiniArea = findViewById(R.id.et_search_activity_mini_area);
        etMaxiArea = findViewById(R.id.et_search_activity_maxi_area);
        etMiniRoom = findViewById(R.id.et_search_activity_mini_room);
        etMaxiRoom = findViewById(R.id.et_search_activity_maxi_room);

        schoolCb = findViewById(R.id.cb_search_activity_school);
        shoppingCb = findViewById(R.id.cb_search_activity_shopping);
        publicTransportCb = findViewById(R.id.cb_search_activity_public_transport);
        swimmingPoolCb = findViewById(R.id.cb_search_activity_swimming_pool);

        btFilter = findViewById(R.id.bt_search_activity_filter_);

        btFilter.setOnClickListener(this);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }

    private void getDataFromEditText() {

        district = etDistrict.getText().toString().toUpperCase();
        String miniPriceInput = etMiniPrice.getText().toString();
        String maxiPriceInput = etMaxiPrice.getText().toString();
        String miniAreaInput = etMiniArea.getText().toString();
        String maxiAreaInput = etMaxiArea.getText().toString();
        String miniRoomInput = etMiniRoom.getText().toString();
        String maxiRoomInput = etMaxiRoom.getText().toString();

        if (!TextUtils.isEmpty(miniPriceInput)) {
            miniPrice = Integer.parseInt(miniPriceInput);
        }
        if (!TextUtils.isEmpty(maxiPriceInput)) {
            maxiPrice = Integer.parseInt(maxiPriceInput);
        }

        if (!TextUtils.isEmpty(miniAreaInput)) {
            miniArea = Integer.parseInt(miniAreaInput);
        }
        if (!TextUtils.isEmpty(maxiAreaInput)) {
            maxiArea = Integer.parseInt(maxiAreaInput);
        }

        if (!TextUtils.isEmpty(miniRoomInput)) {
            miniRoom = Integer.parseInt(miniRoomInput);
        }
        if (!TextUtils.isEmpty(maxiRoomInput)) {
            maxiRoom = Integer.parseInt(maxiRoomInput);
        }

        if (schoolCb.isChecked()) {
            school = 1;
        }
        if (shoppingCb.isChecked()) {
            shopping = 1;
        }
        if (publicTransportCb.isChecked()) {
            publicTransport = 1;
        }
        if (swimmingPoolCb.isChecked()) {
            swimmingPool = 1;
        }
    }

    private Boolean checkInput() {

        Boolean isOk;

        if (district.isEmpty()) {
            Toast.makeText(this, "Please indicate the district", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (miniPrice == 0) {
            Toast.makeText(this, "Please indicate the minimum price", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (maxiPrice == 0) {
            Toast.makeText(this, "Please indicate the maximum price", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (miniArea == 0) {
            Toast.makeText(this, "Please indicate the minimum area", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (maxiArea == 0) {
            Toast.makeText(this, "Please indicate the maximum area", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (miniRoom == 0) {
            Toast.makeText(this, "Please indicate the minimum number of rooms", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (maxiRoom == 0) {
            Toast.makeText(this, "Please indicate the maximum number of rooms", Toast.LENGTH_LONG).show();
            isOk = false;
        } else {
            isOk = true;
        }

        Log.e("Test", "Value checkInput :" + isOk.toString());

        return isOk;
    }

    private void getSearchedList(List<House> houseList) {

        if (houseList.isEmpty()) {
            Toast.makeText(this, "No property corresponding", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_RESULT_LIST, (Serializable) houseList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        this.getDataFromEditText();

        if (checkInput()) {
            realEstateManagerViewModel.getSearchedHouse(district,
                    miniPrice,
                    maxiPrice,
                    miniArea,
                    maxiArea,
                    miniRoom,
                    maxiRoom,
                    school,
                    shopping,
                    publicTransport,
                    swimmingPool).observe(this, this::getSearchedList);
        }
    }
}
