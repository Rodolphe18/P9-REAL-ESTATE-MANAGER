package com.francotte.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.francotte.realestatemanager.model.House;

import java.util.List;

@Dao
public interface HouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createHouse(House house);

    @Query("SELECT * FROM House WHERE id = :houseId")
    LiveData<House> getHouse(long houseId);

    @Query("SELECT * FROM House WHERE id = :houseId")
    Cursor getHousesWithCursor(long houseId);

    @Query("SELECT * FROM House")
    LiveData<List<House>> getAll();

    @Query("UPDATE House SET isEuro = :isEuro WHERE id = :id")
    int updateIsEuro(boolean isEuro, long id);

    @Query("UPDATE House SET category = :category," +
            "district = :district," +
            "price = :price," +
            "isEuro = :isEuro," +
            "area = :area," +
            "numberOfRooms = :numberOfRooms," +
            "numberOfBathrooms= :numberOfBathrooms," +
            "numberOfBedRooms = :numberOfBedRooms," +
            "school = :school," +
            "shopping = :shopping," +
            "publicTransport = :publicTransport," +
            "swimmingPool = :swimmingPool," +
            "description = :description," +
            "available = :available," +
            "dateOfEntry = :dateOfEntry," +
            "dateOfSale = :dateOfSale," +
            "realEstateAgent = :realEstateAgent WHERE id = :id")
    int updateHouse(String category,
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
                    long id);

    @Query("UPDATE House SET illustration = :illustration WHERE id =:id")
    int updateIllustration(String illustration, long id);

    @Query("SELECT * FROM House WHERE district = :district" +
            " AND price BETWEEN :miniPrice AND :maxiPrice " +
            " AND area BETWEEN :miniArea AND :maxiArea" +
            " AND numberOfRooms BETWEEN :miniRoom AND :maxiRoom" +
            " AND school LIKE :school" +
            " AND shopping LIKE :shopping" +
            " AND publicTransport LIKE :publicTransport" +
            " AND swimmingPool LIKE :swimmingPool")
    LiveData<List<House>> getSearchedHouse(String district,
                                           int miniPrice,
                                           int maxiPrice,
                                           int miniArea,
                                           int maxiArea,
                                           int miniRoom,
                                           int maxiRoom,
                                           int school,
                                           int shopping,
                                           int publicTransport,
                                           int swimmingPool);

    @Query("DELETE FROM House WHERE id = :houseId")
    int deleteHouse(long houseId);

}
