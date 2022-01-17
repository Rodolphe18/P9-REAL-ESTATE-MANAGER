package com.francotte.realestatemanager.repositories;


import androidx.lifecycle.LiveData;

import com.francotte.realestatemanager.database.dao.HouseDao;
import com.francotte.realestatemanager.model.House;

import java.util.List;

public class HouseDataRepository {

    private final HouseDao houseDao;

    public HouseDataRepository(HouseDao houseDao) {
        this.houseDao = houseDao;
    }

    //Create house
    public void createHouse(House house) {
        houseDao.createHouse(house);
    }

    //Get house
    public LiveData<House> getHouse(long houseId) {
        return this.houseDao.getHouse(houseId);
    }

    //Get all
    public LiveData<List<House>> getAll() {
        return this.houseDao.getAll();
    }

    //Update isEuro
    public void updateIsEuro(boolean isEuro, long id) {
        houseDao.updateIsEuro(isEuro, id);
    }

    //Update illustration for description picture
    public void updateIllustration(String illustration, long id) {
        houseDao.updateIllustration(illustration, id);
    }

    //Update house
    public void updateHouse(String category,
                            String district,
                            boolean isEuro,
                            int price,
                            int area,
                            int numberOfRooms,
                            int numberOfBathrooms,
                            int numberOfBedRooms,
                            int school,
                            int shopping,
                            int publicTransport,
                            int swimmingPool,
                            String description,
                            boolean available,
                            String dateOfEntry,
                            String dateOfSale,
                            String realEstateAgent,
                            long id) {
        houseDao.updateHouse(category,
                district,
                price,
                isEuro,
                area,
                numberOfRooms,
                numberOfBathrooms,
                numberOfBedRooms,
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

    //For search
    public LiveData<List<House>> getSearchedHouse(String district,
                                                  int miniPrice,
                                                  int maxiPrice,
                                                  int miniArea,
                                                  int maxiArea,
                                                  int miniRoom,
                                                  int maxiRoom,
                                                  int school,
                                                  int shopping,
                                                  int publicTransport,
                                                  int swimmingPool) {

        return this.houseDao.getSearchedHouse(district,
                miniPrice,
                maxiPrice,
                miniArea,
                maxiArea,
                miniRoom,
                maxiRoom,
                school,
                shopping,
                publicTransport,
                swimmingPool);
    }

}
