package com.example.android.movieposters.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.movieposters.moviePoster.MoviePoster;

@android.arch.persistence.room.Database(entities = {MoviePoster.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static final String LOG_TAG = Database.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoriteMovies";
    private static Database sInstance;

    public static Database getInstance(Context context){
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        Database.class, Database.DATABASE_NAME)
                        .addCallback(sDatabaseCallback)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();

    private static Database.Callback sDatabaseCallback = new Database.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDatabaseAsync(sInstance).execute();
        }
    };
}
