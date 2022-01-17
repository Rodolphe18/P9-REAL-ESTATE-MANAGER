package com.francotte.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.francotte.realestatemanager.database.dao.IllustrationDao;
import com.francotte.realestatemanager.model.Illustration;

import java.util.List;

public class IllustrationDataRepository {

    private final IllustrationDao illustrationDao;

    public IllustrationDataRepository(IllustrationDao illustrationDao) {
        this.illustrationDao = illustrationDao;
    }

    //Create illustration
    public void createIllustration(Illustration illustration) {
        illustrationDao.createIllustration(illustration);
    }

    //Get gallery
    public LiveData<List<Illustration>> getGallery(long houseId) {
        return this.illustrationDao.getGallery(houseId);
    }

}
