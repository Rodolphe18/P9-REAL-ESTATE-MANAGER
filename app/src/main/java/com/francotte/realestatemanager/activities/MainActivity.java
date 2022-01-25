package com.francotte.realestatemanager.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.Utils;
import com.francotte.realestatemanager.adapter.HouseRecyclerAdapter;
import com.francotte.realestatemanager.fragments.DetailFragment;
import com.francotte.realestatemanager.fragments.MainFragment;
import com.francotte.realestatemanager.injection.Injection;
import com.francotte.realestatemanager.injection.ViewModelFactory;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.ui.RealEstateManagerViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 26;
    public static final int MAPS_ACTIVITY_REQUEST_CODE = 22;
    public static final String BUNDLE_HOUSE_CLICKED = "BUNDLE_HOUSE_CLICKED";
    private static final long HOUSE_ID = 1;

    private TextView textViewMain;
    private TextView textViewQuantity;

    private RealEstateManagerViewModel realEstateManagerViewModel;
    private List<House> houseList = new ArrayList<>();
    private long id;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private HouseRecyclerAdapter adapter;
    private MainFragment mainFragment;
    private DetailFragment detailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bug 1: Problème de ressources

       // this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
       // this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        // this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        // this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        //this.configureTextViewMain();
        //this.configureTextViewQuantity();

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        this.configureViewModel();
        this.getAllHousesFromDatabase();

        Utils.context = this;

        this.configureAndShowMainFragment();
        this.configureAndShowDetailFragment();
    }

    private void configureTextViewMain() {
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        // Bug 2 : Problème de typage
        // this.textViewQuantity.setText(quantity);
        this.textViewQuantity.setText(String.valueOf(quantity));
    }

    //Configure display Tablet or SmartPhone

    public void configureAndShowMainFragment() {
        // Get FragmentManager (support) and Try to find existing instance of fragment in FrameLayout container
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (!(fragment instanceof MainFragment)) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_layout, mainFragment)
                    .commit();
        }
    }

    public void configureAndShowDetailFragment() {
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);
        // Add detailFragment only if in Tablet mode
        if (detailFragment == null && findViewById(R.id.frame_layout_detail) != null) {
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_detail, detailFragment)
                    .commit();
        }
    }

    // Configure methods

    private void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }

    private void getAllHousesFromDatabase() {
        this.realEstateManagerViewModel.getAll().observe(this, this::updateList);
    }

    private void updateList(List<House> houses) {
        houseList = new ArrayList<>();
        houseList.addAll(houses);
    }

    //Currency conversion methods

    private void changeCurrencyToDollarsAndUpdateDataBase(List<House> houses) {
        if (houses != null) {
            for (House house : houses) {
                long houseId = house.getId();
                this.realEstateManagerViewModel.updateIsEuro(false, houseId);
            }
        }
    }

    private void changeCurrencyToEuroAndUpdateDataBase(List<House> houses) {
        if (houses != null) {
            for (House house : houses) {
                long houseId = house.getId();
                this.realEstateManagerViewModel.updateIsEuro(true, houseId);
            }
        }
    }

    //Transmission of the information of the clicked property for the display of details on DetailsFragment
    public void onHouseClick(House house) {

        if (detailFragment != null && Utils.isTablet(this)) {
            //Tablet
            detailFragment.updateData(house);
            detailFragment.updateDisplay(house);

        } else {
            //Smartphone
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, detailFragment)
                    .addToBackStack(null)
                    .commit();
            detailFragment.onHouseClick(house);
        }
        this.id = house.getId();
    }

    //Override methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the toolbar menu
        getMenuInflater().inflate(R.menu.activity_main_menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_conversion_euro_dollars:
                this.changeCurrencyToDollarsAndUpdateDataBase(houseList);
                Toast.makeText(this, "Price in dollars", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_conversion_dollars_euro:
                this.changeCurrencyToEuroAndUpdateDataBase(houseList);
                Toast.makeText(this, "Price in euros", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_loan_calculator:
                Intent intentCalculator = new Intent(MainActivity.this, LoanCalculatorActivity.class);
                startActivity(intentCalculator);
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle actions on menu items
        switch (item.getItemId()) {
            //Start AddActivity when add button is clicked
            case R.id.menu_activity_main_toolbar_add:
                Intent intentAdd = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intentAdd);
                return true;

            //Start AddActivity when modify button is clicked
            case R.id.menu_activity_main_toolbar_modify:
                if (id != 0) {
                    Intent intentModify = new Intent(MainActivity.this, AddActivity.class);
                    intentModify.putExtra("id", id);
                    startActivity(intentModify);
                } else {
                    Toast.makeText(this, "Select a property to sell", Toast.LENGTH_LONG).show();
                }
                return true;

            //Start AddActivity when modify button is clicked
            case R.id.menu_activity_main_toolbar_map:
                    Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intentMap);
                return true;

            //Start SearchActivity when add button is clicked
            case R.id.menu_activity_main_toolbar_search:
                Intent searchActivityIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(searchActivityIntent, SEARCH_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case SEARCH_ACTIVITY_REQUEST_CODE:
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
                    fragment.onActivityResult(requestCode, resultCode, data);

                    break;

                case MAPS_ACTIVITY_REQUEST_CODE:
                    //display house clicked on smartphone
                    if (!Utils.isTablet(this)) {
                        House houseClicked = (House) data.getSerializableExtra(BUNDLE_HOUSE_CLICKED);
                        onHouseClick(houseClicked);
                    } else {
                        // the result from MapsActivity to DetailFragment
                        Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);
                        detailsFragment.onActivityResult(requestCode, resultCode, data);
                    }

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
