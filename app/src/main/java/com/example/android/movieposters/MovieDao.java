package com.example.android.movieposters;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.movieposters.moviePoster.MoviePoster;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movieposter ORDER BY mId")
    List<MoviePoster> loadAllTasks();

    @Insert
    void insertMovie(MoviePoster moviePoster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MoviePoster moviePoster);

    @Delete
    void deleteMovie(MoviePoster moviePoster);
}
