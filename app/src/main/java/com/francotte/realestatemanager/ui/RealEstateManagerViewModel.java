package com.francotte.realestatemanager.ui;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.model.Illustration;
import com.francotte.realestatemanager.repositories.HouseDataRepository;
import com.francotte.realestatemanager.repositories.IllustrationDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class RealEstateManagerViewModel extends ViewModel {

    //Repositories
    private final HouseDataRepository houseDataSource;
    private final IllustrationDataRepository illustrationDataSource;
    private final Executor executor;

    //Data
    private LiveData<House> currentHouse;

    public RealEstateManagerViewModel(HouseDataRepository houseDataSource, IllustrationDataRepository illustrationDataSource, Executor executor) {
        this.houseDataSource = houseDataSource;
        this.illustrationDataSource = illustrationDataSource;
        this.executor = executor;
    }

    public void init(long houseId) {
        if (this.currentHouse != null) {
            return;
        }
        currentHouse = houseDataSource.getHouse(houseId);
    }

    //For house
    public void createHouse(House house) {
        executor.execute(() -> {
            houseDataSource.createHouse(house);
        });
    }

    public void updateIsEuro(boolean isEuro, long id) {
        executor.execute(() -> {
            houseDataSource.updateIsEuro(isEuro,id);
        });
    }

    public void updateHouse(String category,
                            String district,
                            int price,
                            boolean isEuro,
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
        executor.execute(() -> {
            houseDataSource.updateHouse(category,
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
        });
    }

    public void updateIllustration(String illustration, long id) {
        executor.execute(() -> {
            houseDataSource.updateIllustration(illustration, id);
        });
    }

    public LiveData<List<House>> getAll() { return houseDataSource.getAll(); }

    public LiveData<House> getHouse(long houseId) { return  houseDataSource.getHouse(houseId); }

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
        return houseDataSource.getSearchedHouse(district,
                miniPrice,
                maxiPrice,
                miniArea,
                maxiArea,
                miniRoom,
                maxiRoom,
                school,
                shopping,
                publicTransport,
                swimmingPool);}

    //For illustration
    public void createIllustration(Illustration illustration) {
        executor.execute(() -> {
            illustrationDataSource.createIllustration(illustration);
        });
    }

    public LiveData<List<Illustration>> getGallery(long houseId) { return illustrationDataSource.getGallery(houseId); }
}
