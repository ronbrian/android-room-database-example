package com.ron.mytodo.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ron.mytodo.model.Task;
import com.ron.mytodo.model.TaskRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);


    }


    public LiveData<List<Task>> getAllTask() {
        return taskRepository.getMutableLiveData();
    }
}
