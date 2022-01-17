package com.francotte.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.francotte.realestatemanager.database.dao.HouseDao;
import com.francotte.realestatemanager.database.dao.IllustrationDao;
import com.francotte.realestatemanager.model.House;
import com.francotte.realestatemanager.model.Illustration;

@Database(entities = {House.class, Illustration.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    //Singleton
    private static volatile RealEstateManagerDatabase INSTANCE;

    //Instance
    public static RealEstateManagerDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateManagerDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues houseOne = new ContentValues();

                houseOne.put("id", 1);
                houseOne.put("category", "Maison");
                houseOne.put("district", "HOURTON");
                houseOne.put("price", 900000);
                houseOne.put("isEuro", true);
                houseOne.put("area", 300);
                houseOne.put("numberOfRooms", 8);
                houseOne.put("numberOfBathrooms", 4);
                houseOne.put("numberOfBedrooms", 4);
                houseOne.put("school", false);
                houseOne.put("shopping", false);
                houseOne.put("publicTransport", true);
                houseOne.put("swimmingPool", true);
                houseOne.put("description", "Magnifique villa à Saint Aubin de Médoc située dans un quartier recherché.\nVous découvrirez une entrée, une belle pièce de vie avec salon et salle à manger, un deuxième salon séparé par une cheminée centrale, une cuisine fermée donnant sur la cuisine d'été, une buanderie, une salle de jeu, une cave.\n Le rez-de-chaussée est complété par une chambre avec salle d'eau, un bureau et une salle de sport avec sauna et salle d'eau.\nA l'étage, vous accédez à une suite parentale avec une chambre donnant sur une terrasse, une salle de bains avec baignoire et douche, un double dressing, puis trois chambres avec dressing et une salle de bains avec baignoire et douche.\n L'ensemble sur un jardin paysagé d'environ 1400 m² dispose d'une grande terrasse en bois, d'une cuisine d'été, d'une piscine au sel chauffée.\nTrès belles prestations pour cette maison d'exception à quelques minutes du centre de Saint Aubin de Médoc, des écoles, collège et lycée.");
                houseOne.put("illustration", "");
                houseOne.put("address", "15 route de Hourton, 33160 Saint-Aubin-de-Médoc");
                houseOne.put("available", true);
                houseOne.put("dateOfEntry", "20/01/2021");
                houseOne.put("dateOfSale", "null");
                houseOne.put("realEstateAgent", "Eddy");

                db.insert("House", OnConflictStrategy.IGNORE, houseOne);

                ContentValues houseTwo = new ContentValues();

                houseTwo.put("id", 2);
                houseTwo.put("category", "Maison");
                houseTwo.put("district", "CENTRE VILLE");
                houseTwo.put("price", 800000);
                houseTwo.put("isEuro", true);
                houseTwo.put("area", 200);
                houseTwo.put("numberOfRooms", 6);
                houseTwo.put("numberOfBathrooms", 4);
                houseTwo.put("numberOfBedrooms", 4);
                houseTwo.put("school", false);
                houseTwo.put("shopping", false);
                houseTwo.put("publicTransport", false);
                houseTwo.put("swimmingPool", true);
                houseTwo.put("description", "Nichée au fond d'une impasse, construite en 2015 sur une parcelle clôturée et piscinable de 1000 m² environ, très belle maison de plain pied ossature bois avec vue sur les champs.\nSituée à 35mn en voiture de Bordeaux et des plages de Lacanau, cette maison vous charmera par sa luminosité grâce au patio central, et par la qualité de vie qu'elle propose.\nL'entrée dessert d'un côté la partie jour avec une vaste pièce de vie et cuisine ouverte, une buanderie, un bureau et une grande suite parentale avec dressing et salle de douche.\nDe l'autre côté se trouve la partie enfants avec 4 chambres supplémentaires, une grande salle de bain, un WC séparé et une salle de jeu.\nToute la maison tourne autour d'un joli patio de 80 m² environ, végétalisé et terrassé. L'ensemble est complété d'un garage de 15 m², de 3 places de stationnement devant la maison, une terrasse à l'ombre et un grand jardin.");
                houseTwo.put("illustration", "");
                houseTwo.put("address", "68 ter Route de Loustaou Vieil, 33160 Saint-Aubin-de-Médoc");
                houseTwo.put("available", true);
                houseTwo.put("dateOfEntry", "15/12/2021");
                houseTwo.put("dateOfSale", "null");
                houseTwo.put("realEstateAgent", "Charlotte");

                db.insert("House", OnConflictStrategy.IGNORE, houseTwo);

                ContentValues houseThree = new ContentValues();

                houseThree.put("id", 3);
                houseThree.put("category", "Villa");
                houseThree.put("district", "CENTRE VILLE");
                houseThree.put("price", 700000);
                houseThree.put("isEuro", true);
                houseThree.put("area", 100);
                houseThree.put("numberOfRooms", 5);
                houseThree.put("numberOfBathrooms", 2);
                houseThree.put("numberOfBedrooms", 3);
                houseThree.put("school", true);
                houseThree.put("shopping", true);
                houseThree.put("publicTransport", true);
                houseThree.put("swimmingPool", false);
                houseThree.put("description", "Maison de 100 m² environ comprenant 5 belles chambres, toutes en parquet bois naturel.\nVous serez séduit par le volume de sa chambre parentale, qui comprend un dressing équipé et sa pièce d'eau attenante disposant d'une baignoire, d'une douche à l'italienne, d'un toilette et d'une double vasque.\nUn ensemble de 37 m² environ entièrement pour vous. Les 4 autres chambres en parquet bois disposent toutes d'un placard. L'une d'entre elle, attenante à la pièce de vie pourra aisément se transformer en bureau afin de vous permettre d'être au calme et indépendant.\nQue dire de la pièce de vie de plus de 67 m², lumineuse grâce à sa double exposition Est/Ouest et ses grandes baies vitrées (5m d'un côté et 3m de l'autre), qui n'attend que vous pour se transformer en différents espaces: salle à manger, salon, coin cheminée, coin lecture...\nLa cuisine de près de 19 m² avec un îlot central et équipée, vous séduira par sa convivialité et sa luminosité grâce à un puit de lumière situé au dessus de l'îlot.\nUn cellier avec un accès sur le garage complète cette cuisine. Côté extérieur vous serez séduit par son terrain de plus de 1600 m². La piscine de 4X10 exposée Ouest ajoute encore à cette quiétude.");
                houseThree.put("illustration", "");
                houseThree.put("address", "22 Allée des Châtaigniers, 33160 Saint-Aubin-de-Médoc");
                houseThree.put("available", true);
                houseThree.put("dateOfEntry", "10/11/2020");
                houseThree.put("dateOfSale", "null");
                houseThree.put("realEstateAgent", "Eddy");

                db.insert("House", OnConflictStrategy.IGNORE, houseThree);

                ContentValues photoA = new ContentValues();

                photoA.put("id", 1);
                photoA.put("houseId", 1);
                photoA.put("description", "Salon");
                photoA.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoA);

                ContentValues photoB = new ContentValues();

                photoB.put("id", 2);
                photoB.put("houseId", 1);
                photoB.put("description", "Cuisine");
                photoB.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoB);

                ContentValues photoC = new ContentValues();

                photoC.put("id", 3);
                photoC.put("houseId", 1);
                photoC.put("description", "Suite parentale");
                photoC.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoC);

                ContentValues photoD = new ContentValues();

                photoD.put("id", 4);
                photoD.put("houseId", 1);
                photoD.put("description", "Chambre enfant");
                photoD.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoD);

                ContentValues photoE = new ContentValues();

                photoE.put("id", 5);
                photoE.put("houseId", 1);
                photoE.put("description", "Salle de bain");
                photoE.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoE);

                ContentValues photoF = new ContentValues();

                photoF.put("id", 6);
                photoF.put("houseId", 2);
                photoF.put("description", "Salon");
                photoF.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoF);

                ContentValues photoG = new ContentValues();

                photoG.put("id", 7);
                photoG.put("houseId", 2);
                photoG.put("description", "Cuisine");
                photoG.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoG);

                ContentValues photoH = new ContentValues();

                photoH.put("id", 8);
                photoH.put("houseId", 2);
                photoH.put("description", "Patio");
                photoH.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoH);

                ContentValues photoI = new ContentValues();

                photoI.put("id", 9);
                photoI.put("houseId", 2);
                photoI.put("description", "Chambre");
                photoI.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoI);

                ContentValues photoJ = new ContentValues();

                photoJ.put("id", 10);
                photoJ.put("houseId", 2);
                photoJ.put("description", "Chambre enfant");
                photoJ.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoJ);

                ContentValues photoK = new ContentValues();

                photoK.put("id", 11);
                photoK.put("houseId", 2);
                photoK.put("description", "Salle de bain");
                photoK.put("picture", "");
                db.insert("Illustration", OnConflictStrategy.IGNORE, photoK);

                ContentValues photoL = new ContentValues();

                photoL.put("id", 12);
                photoL.put("houseId", 3);
                photoL.put("description", "Salon");
                photoL.put("picture", "");
                db.insert("Illustration", OnConflictStrategy.IGNORE, photoL);

                ContentValues photoM = new ContentValues();

                photoM.put("id", 13);
                photoM.put("houseId", 3);
                photoM.put("description", "Cuisine");
                photoM.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoM);

                ContentValues photoN = new ContentValues();

                photoN.put("id", 14);
                photoN.put("houseId", 3);
                photoN.put("description", "Chambre");
                photoN.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoN);

                ContentValues photoO = new ContentValues();

                photoO.put("id", 15);
                photoO.put("houseId", 3);
                photoO.put("description", "Salle de bain");
                photoO.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoO);
                ContentValues photoP = new ContentValues();

                photoP.put("id", 16);
                photoP.put("houseId", 3);
                photoP.put("description", "Piscine");
                photoP.put("picture", "");

                db.insert("Illustration", OnConflictStrategy.IGNORE, photoP);

            }
        };
    }

    //Dao
    public abstract HouseDao houseDao();

    public abstract IllustrationDao illustrationDao();
}
