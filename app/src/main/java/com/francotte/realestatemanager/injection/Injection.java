package com.francotte.realestatemanager.injection;

import android.content.Context;

import com.francotte.realestatemanager.database.RealEstateManagerDatabase;
import com.francotte.realestatemanager.repositories.HouseDataRepository;
import com.francotte.realestatemanager.repositories.IllustrationDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static HouseDataRepository provideHouseDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getINSTANCE(context);
        return new HouseDataRepository(database.houseDao());
    }

    public static IllustrationDataRepository provideIllustrationDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getINSTANCE(context);
        return new IllustrationDataRepository(database.illustrationDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        HouseDataRepository dataSourceHouse = provideHouseDataSource(context);
        IllustrationDataRepository dataSourceIllustration = provideIllustrationDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceHouse, dataSourceIllustration, executor);
    }
}
