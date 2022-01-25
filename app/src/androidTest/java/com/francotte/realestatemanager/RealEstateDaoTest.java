package com.francotte.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.francotte.realestatemanager.database.RealEstateManagerDatabase;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.model.Illustration;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RealEstateDaoTest {
    //For data
    private RealEstateManagerDatabase database;

    //Data set for Test
    private static final long HOUSE_ID = 1;
    private static final House HOUSE_DEMO = new House("Maison", "HOURTON",
            true,
            700000,
            120,
            5,
            2,
            3,
            1,
            1,
            1,
            1,
            "Magnifique villa à Saint Aubin de Médoc située dans un quartier recherché.",
            " ",
            "15 route de Hourton, 33160 Saint-Aubin-de-Médoc",
            true,
            "20/01/2021",
            null,
            "Eddy");
    private static final Illustration ILLUSTRATION_SALON = new Illustration(HOUSE_ID, "Salon", " ");
    private static final Illustration ILLUSTRATION_CUISINE = new Illustration(HOUSE_ID, "Cuisine", " ");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.clearAllTables();
    }

    //HouseDao
    @Test
    public void insertAndGetHouse() throws InterruptedException {
        //Before: Adding demo house
        this.database.houseDao().createHouse(HOUSE_DEMO);
        //Test
        House house = LiveDataTestUtil.getValue(this.database.houseDao().getHouse(HOUSE_ID));
        assertTrue(house.getAddress().equals(HOUSE_DEMO.getAddress()) && house.getId() == HOUSE_ID);
    }

    @Test
    public void getHouseWhenNoItemInserted() throws InterruptedException {
        //TEST
        List<House> houses = LiveDataTestUtil.getValue(this.database.houseDao().getAll());
        TestCase.assertTrue(houses.isEmpty());
    }

    @Test
    public void insertAndUpdateHouse() throws InterruptedException {
        // BEFORE : Adding demo House. Get the house added & delete it.
        this.database.houseDao().createHouse(HOUSE_DEMO);
        // Get the house added & delete it.
        House houseAdded = LiveDataTestUtil.getValue(this.database.houseDao().getHouse(HOUSE_ID));
        houseAdded.setArea(130);
        this.database.houseDao().updateHouse(houseAdded.getCategory(), houseAdded.getDistrict(), houseAdded.getPrice(), houseAdded.isEuro(), houseAdded.getArea(),
                houseAdded.getNumberOfRooms(), houseAdded.getNumberOfBathrooms(), houseAdded.getNumberOfBedrooms(), houseAdded.getSchool(), houseAdded.getShopping(),
                houseAdded.getPublicTransport(), houseAdded.getSwimmingPool(), houseAdded.getDescription(), houseAdded.isAvailable(),
                houseAdded.getDateOfEntry(), houseAdded.getDateOfSale(), houseAdded.getRealEstateAgent(), houseAdded.getId());

        //TEST
        House house = LiveDataTestUtil.getValue(this.database.houseDao().getHouse(HOUSE_ID));
        assertEquals(130, house.getArea());
    }

    //Illustration Dao
    @Test
    public void insertAndGetIllustration() throws InterruptedException {
        //Before: Adding demo house and demo illustration
        this.database.houseDao().createHouse(HOUSE_DEMO);
        this.database.illustrationDao().createIllustration(ILLUSTRATION_SALON);
        this.database.illustrationDao().createIllustration(ILLUSTRATION_CUISINE);

        //Test
        List<Illustration> gallery = LiveDataTestUtil.getValue(this.database.illustrationDao().getGallery(HOUSE_ID));
        assertEquals(2, gallery.size());
    }

    @Test
    public void getGalleryWhenNoItemInserted() throws InterruptedException {
        //TEST
        List<Illustration> gallery = LiveDataTestUtil.getValue(this.database.illustrationDao().getGallery(HOUSE_ID));
        TestCase.assertTrue(gallery.isEmpty());
    }
}
