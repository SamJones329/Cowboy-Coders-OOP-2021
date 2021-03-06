package com.example.medication_reminder_android_app.SQLiteDB;

import android.os.AsyncTask;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * @author Hayley Roberts
 * @lastModified 3/24/2021 by Hayley Roberts
 */

//if we want a copy of schema in a file, change exportSchema to true
// https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we
//If need to change/update db and inc version w/out losing all old data:
// https://synetech.cz/en/blog/how-to-migrate-room-database-painlessly-en

@Database(entities = {MedicationEntity.class, DoctorEntity.class, AppointmentEntity.class, ReminderEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DataAccessObject dataAccessObject();

    private  static AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() //This deletes all data in old tables if change the version number.
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
        }
    };
}
