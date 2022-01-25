package com.francotte.realestatemanager.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.francotte.realestatemanager.R;
import com.francotte.realestatemanager.fragments.DescriptionPictureDialog;
import com.francotte.realestatemanager.fragments.GalleryPictureDialog;
import com.francotte.realestatemanager.injection.Injection;
import com.francotte.realestatemanager.injection.ViewModelFactory;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.model.Illustration;
import com.francotte.realestatemanager.ui.RealEstateManagerViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, GalleryPictureDialog.DialogListener {

    public static final int RESULT_ADD_PICTURE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final long HOUSE_ID = 1;
    private final List<House> houseList = new ArrayList<>();
    //For Data
    public String category;
    public String district;
    public String address;
    public String streetNumber;
    public String streetName;
    public String zipCode;
    public String city;
    public String description;
    public String realEstateAgent;
    public String dateOfSale;
    public String dateOfEntry;
    public String picture;
    public String tvDescription;
    public boolean available = true;
    public int school = 0;
    public int shopping = 0;
    public int publicTransport = 0;
    public int swimmingPool = 0;
    public int price;
    public int area;
    public int numberOfRooms;
    public int numberOfBedRooms;
    public int numberOfBathrooms;
    private long id;
    private RealEstateManagerViewModel realEstateManagerViewModel;
    private House houseToAdd;
    private House houseToUpdate;
    private Illustration illustrationToAdd;
    //For Design
    private LinearLayout layout;
    private EditText categoryInput;
    private EditText districtInput;
    private EditText priceInput;
    private EditText areaInput;
    private EditText roomInput;
    private EditText bedroomInput;
    private EditText bathroomInput;
    private EditText descriptionValue;
    private EditText agentNameValue;
    private EditText dateOfSaleInput;
    private EditText streetNumberInput;
    private EditText streetNameInput;
    private EditText zipCodeInput;
    private EditText cityInput;
    private Button addBtn;
    private Button selectDescriptionPictureBtn;
    private Button selectGalleryPictureBtn;
    private Button addDescriptionPictureBtn;
    private Button addGalleryPictureBtn;
    private TextView addressTv;
    private CheckBox schoolCb;
    private CheckBox shoppingCb;
    private CheckBox publicTransportCb;
    private CheckBox swimmingPoolCb;
    private String picturePath = null;
    private Uri selectedPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        configureViewModel();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("id", -1);
        }
        //create display or modify display
        if (id == -1 || id == 0) {
            //Create
            this.initActivity();
            selectGalleryPictureBtn.setVisibility(View.GONE);
            addGalleryPictureBtn.setVisibility(View.GONE);
            addDescriptionPictureBtn.setVisibility(View.GONE);
            dateOfSaleInput.setVisibility(View.GONE);

            addBtn.setText("ADD");
        } else {
            //Modify
            this.initActivity();
            selectGalleryPictureBtn.setVisibility(View.VISIBLE);
            addGalleryPictureBtn.setVisibility(View.VISIBLE);
            dateOfSaleInput.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            addressTv.setVisibility(View.GONE);

            this.getCurrentHouse(id);
            addBtn.setText("MODIFY");
        }

        //Listeners
        addBtn.setOnClickListener(this);
        selectDescriptionPictureBtn.setOnClickListener(this);
        selectGalleryPictureBtn.setOnClickListener(this);
        addDescriptionPictureBtn.setOnClickListener(this);
        addGalleryPictureBtn.setOnClickListener(this);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.realEstateManagerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateManagerViewModel.class);
        this.realEstateManagerViewModel.init(HOUSE_ID);
    }

    private void initActivity() {

        categoryInput = findViewById(R.id.et_add_activity_category);
        districtInput = findViewById(R.id.et_add_activity_district);
        priceInput = findViewById(R.id.et_add_activity_price);
        areaInput = findViewById(R.id.et_add_activity_area);
        roomInput = findViewById(R.id.et_add_activity_room);
        bedroomInput = findViewById(R.id.et_add_activity_bedroom);
        bathroomInput = findViewById(R.id.et_add_activity_bathroom);

        schoolCb = findViewById(R.id.cb_add_activity_school);
        shoppingCb = findViewById(R.id.cb_add_activity_shopping);
        publicTransportCb = findViewById(R.id.cb_add_activity_public_transport);
        swimmingPoolCb = findViewById(R.id.cb_add_activity_swimming_pool);

        descriptionValue = findViewById(R.id.et_add_activity_description);
        layout = findViewById(R.id.et_add_activity_linearlayout_address);
        addressTv = findViewById(R.id.tv_add_activity_address);
        streetNumberInput = findViewById(R.id.et_add_activity_street_number);
        streetNameInput = findViewById(R.id.et_add_activity_street_name);
        zipCodeInput = findViewById(R.id.et_add_activity_zipcode);
        cityInput = findViewById(R.id.et_add_activity_city);
        agentNameValue = findViewById(R.id.et_add_activity_agent_name);

        addBtn = findViewById(R.id.bt_add_activity);
        selectDescriptionPictureBtn = findViewById(R.id.bt_add_activity_select_description_picture);
        addDescriptionPictureBtn = findViewById(R.id.bt_add_activity_add_description_picture);
        selectGalleryPictureBtn = findViewById(R.id.bt_add_activity_select_gallerie_picture);
        addGalleryPictureBtn = findViewById(R.id.bt_add_activity_add_gallerie_picture);
        dateOfSaleInput = findViewById(R.id.et_add_activity_date_of_sale);

    }

    private void collectInputUser() {
        category = categoryInput.getText().toString();
        district = districtInput.getText().toString().toUpperCase();

        streetNumber = streetNumberInput.getText().toString();
        streetName = streetNameInput.getText().toString();
        zipCode = zipCodeInput.getText().toString();
        city = cityInput.getText().toString();
        address = streetNumber + " " + streetName + " " + zipCode + " " + city;

        description = descriptionValue.getText().toString();
        realEstateAgent = agentNameValue.getText().toString();

        dateOfSale = dateOfSaleInput.getText().toString();

        Date date = Calendar.getInstance().getTime();
        dateOfEntry = new SimpleDateFormat("dd-MM-yyyy").format(date);

        String strPrice = priceInput.getText().toString();
        if (!TextUtils.isEmpty(strPrice)) {
            price = Integer.parseInt(strPrice);
        }

        String strArea = areaInput.getText().toString();
        if (!TextUtils.isEmpty(strArea)) {
            area = Integer.parseInt(strArea);
        }

        String strNumberOfRooms = roomInput.getText().toString();
        if (!TextUtils.isEmpty(strNumberOfRooms)) {
            numberOfRooms = Integer.parseInt(strNumberOfRooms);
        }

        String strNumberOfBedRooms = bedroomInput.getText().toString();
        if (!TextUtils.isEmpty(strNumberOfBedRooms)) {
            numberOfBedRooms = Integer.parseInt(strNumberOfBedRooms);
        }

        String strNumberOfBathrooms = bathroomInput.getText().toString();
        if (!TextUtils.isEmpty(strNumberOfBathrooms)) {
            numberOfBathrooms = Integer.parseInt(strNumberOfBathrooms);
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
        if (category.isEmpty()) {
            Toast.makeText(this, "Please indicate a category of the property", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (district.isEmpty()) {
            Toast.makeText(this, "Please indicate a district of the property", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (streetNumber.isEmpty()) {
            Toast.makeText(this, "Please indicate a number of the street", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (streetName.isEmpty()) {
            Toast.makeText(this, "Please indicate a street", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (zipCode.isEmpty()) {
            Toast.makeText(this, "Please indicate a postal code", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (city.isEmpty()) {
            Toast.makeText(this, "Please indicate a city", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (description.isEmpty()) {
            Toast.makeText(this, "Please indicate a description", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (realEstateAgent.isEmpty()) {
            Toast.makeText(this, "Please indcate the real estate agent name", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (price == 0) {
            Toast.makeText(this, "Please indcate th price", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (area == 0) {
            Toast.makeText(this, "Please indicate the area", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (numberOfRooms == 0) {
            Toast.makeText(this, "Please indicate the number of rooms", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (numberOfBedRooms == 0) {
            Toast.makeText(this, "Please indicate the number of bedrooms", Toast.LENGTH_LONG).show();
            isOk = false;
        } else if (numberOfBathrooms == 0) {
            Toast.makeText(this, "Please indicate the number of bathrooms", Toast.LENGTH_LONG).show();
            isOk = false;
        } else {
            isOk = true;
        }
        return isOk;
    }

    private void prepopulateTextView(House houseToDisplay) {
        categoryInput.setText(houseToDisplay.getCategory());
        districtInput.setText(houseToDisplay.getDistrict());
        descriptionValue.setText(houseToDisplay.getDescription());
        agentNameValue.setText(houseToDisplay.getRealEstateAgent());

        priceInput.setText(String.valueOf(houseToDisplay.getPrice()));
        areaInput.setText(String.valueOf(houseToDisplay.getArea()));
        roomInput.setText(String.valueOf(houseToDisplay.getNumberOfRooms()));
        bedroomInput.setText(String.valueOf(houseToDisplay.getNumberOfBedrooms()));
        bathroomInput.setText(String.valueOf(houseToDisplay.getNumberOfBathrooms()));

        if (houseToDisplay.getSchool() == 1) {
            schoolCb.setChecked(true);
        }
        if (houseToDisplay.getShopping() == 1) {
            shoppingCb.setChecked(true);
        }
        if (houseToDisplay.getPublicTransport() == 1) {
            publicTransportCb.setChecked(true);
        }
        if (houseToDisplay.getSwimmingPool() == 1) {
            swimmingPoolCb.setChecked(true);
        }
    }

    // Get methods

    private void getCurrentHouse(long id) {
        this.realEstateManagerViewModel.getHouse(id).observe(this, this::prepopulateTextView);
    }

    private void getHouseToUpdate(long id) {
        this.realEstateManagerViewModel.getHouse(id).observe(this, this::updateHouseInDatabase);
    }

    // Create methods

    private void createHouseAndAddItToDatabase() {
        if (picture == null) {
            picture = "";
        }
        houseToAdd = new House(category, district, true, price, area, numberOfRooms, numberOfBathrooms, numberOfBedRooms, school, shopping, publicTransport, swimmingPool,
                description, picture, address, available, dateOfEntry, null, realEstateAgent);
        this.realEstateManagerViewModel.createHouse(houseToAdd);
    }

    private void createIllustrationAndAddItToDatabase(Long id) {
        if (picture == null) {
            picture = "";
        }
        illustrationToAdd = new Illustration(id, tvDescription, picture);
        this.realEstateManagerViewModel.createIllustration(illustrationToAdd);
    }

    // Update methods

    private void updateHouseInDatabase(House house) {

        if (!dateOfSale.isEmpty()) {
            available = false;
        }
        dateOfEntry = house.getDateOfEntry();
        houseToUpdate = new House(category, district, true, price, area, numberOfRooms, numberOfBathrooms, numberOfBedRooms,
                school,
                shopping,
                publicTransport,
                swimmingPool,
                description, picture, available, dateOfEntry, dateOfSale, realEstateAgent);

        this.realEstateManagerViewModel.updateHouse(category, district,  price, true, area, numberOfRooms, numberOfBathrooms, numberOfBedRooms,
                school,
                shopping,
                publicTransport,
                swimmingPool,
                description,
                available,
                dateOfEntry,
                dateOfSale,
                realEstateAgent,
                id);
    }

    private void updatePictureDescriptionInDatabase() {
        if (picture != null) {
            this.realEstateManagerViewModel.updateIllustration(picture, id);
        }
    }

    //Picture

    //Start Activity for get picture from device
    public void addPictureFromDevice() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_ADD_PICTURE);
    }

    //Start Activity for take picture with device
    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //check intent can be managed
        if (intent.resolveActivity(getPackageManager()) != null) {
            //Create a unique file name
            String time = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            File pictureDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File pictureFile = File.createTempFile("picture" + time, ".jpg", pictureDir);
                //Save full path
                picturePath = pictureFile.getAbsolutePath();
                //Create uri
                Uri pictureUri = FileProvider.getUriForFile(com.francotte.realestatemanager.activities.AddActivity.this, com.francotte.realestatemanager.activities.AddActivity.this.getApplicationContext().getPackageName() + ".provider", pictureFile);
                //Uri to intent for save picture in temporary file
                intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                //Open activity
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        //Cursor for access
        Cursor cursor = this.getContentResolver().query(uri, filePathColumn, null, null, null);
        //position on line
        cursor.moveToFirst();
        //get path
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgPath = cursor.getString(columnIndex);
        cursor.close();
        this.picture = imgPath;

        return picture;
    }

    @Override
    public void applyDescription(String pictureDescription) {
        tvDescription = pictureDescription;
    }

    //OnClick methods
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_activity:

                if (id == -1 || id == 0) {
                    //Create new house in database
                    this.collectInputUser();
                    if (checkInput()) {
                        this.createHouseAndAddItToDatabase();
                        Toast.makeText(this, "The property has been added ", Toast.LENGTH_LONG).show();
                        com.francotte.realestatemanager.activities.AddActivity.this.finish();
                    }
                } else {
                    //Update house in database
                    this.collectInputUser();
                    this.getHouseToUpdate(id);
                    Toast.makeText(this, "The property has been modified ", Toast.LENGTH_LONG).show();
                    com.francotte.realestatemanager.activities.AddActivity.this.finish();
                }
                break;

            case R.id.bt_add_activity_add_description_picture:
                //Update description picture in database
                this.updatePictureDescriptionInDatabase();
                Toast.makeText(this, "The description picture has been modified ", Toast.LENGTH_LONG).show();
                break;

            case R.id.bt_add_activity_add_gallerie_picture:
                //Add illustration picture in database
                createIllustrationAndAddItToDatabase(id);
                Toast.makeText(this, "The picture has been added in the galery ", Toast.LENGTH_LONG).show();
                break;

            case R.id.bt_add_activity_select_description_picture:
                //Add picture from device
                openDescriptionPictureDialog();
                break;

            case R.id.bt_add_activity_select_gallerie_picture:
                //Take picture with device
                openGalleryPictureDialog();
                break;
        }
    }

    //Pictures dialog

    private void openGalleryPictureDialog() {
        GalleryPictureDialog galleryPictureDialog = new GalleryPictureDialog();
        galleryPictureDialog.show(getSupportFragmentManager(), "gallery picture dialog");
    }

    private void openDescriptionPictureDialog() {
        DescriptionPictureDialog descriptionPictureDialog = new DescriptionPictureDialog();
        descriptionPictureDialog.show(getSupportFragmentManager(), "description picture dialog");
    }

    //Result for Add Picture and Take Picture
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case RESULT_ADD_PICTURE:
                    //Access to picture from data
                    selectedPicture = data.getData();
                    getPathFromUri(selectedPicture);
                    break;

                case REQUEST_IMAGE_CAPTURE:
                    //Get picture
                    picture = picturePath;
                    break;
            }
        }
    }
}