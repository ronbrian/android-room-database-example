package com.ron.mytodo.Database;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ron.mytodo.model.Task;
import com.ron.mytodo.TaskDao;

@Database(entities = {Task.class},exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();


}



