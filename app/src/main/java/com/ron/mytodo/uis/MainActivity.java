package com.ron.mytodo.uis;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ron.mytodo.Adapters.TasksAdapter;
import com.ron.mytodo.Database.DatabaseClient;
import com.ron.mytodo.MyPlacesAdapter;
import com.ron.mytodo.allTasks;
import com.ron.mytodo.model.Task;
import com.ron.mytodo.rest.ApiClient;
import com.ron.mytodo.rest.UserService;
import com.ron.mytodo.viewmodel.MainViewModel;

import net.simplifiedcoding.mytodo.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView places;
    MyPlacesAdapter adapter;

    private FloatingActionButton buttonAddTask;
    private FloatingActionButton buttonloadTasks;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    private MainViewModel mainViewModel;
    TasksAdapter mTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializationViews();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        getTask();
        // lambda expression
        swipeRefresh.setOnRefreshListener(() -> {

            Toast.makeText(this, " Results Displayed ", Toast.LENGTH_SHORT).show();


            getTask();
        });


        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        getTasks();



    }



    private void prepareRecyclerView(List<Task> taskList) {

        mTaskAdapter = new TasksAdapter(taskList);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mTaskAdapter);
        mTaskAdapter.notifyDataSetChanged();
    }

    public void getTask() {
        swipeRefresh.setRefreshing(true);
        mainViewModel.getAllTask().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> taskList) {
                swipeRefresh.setRefreshing(false);
                prepareRecyclerView(taskList);
            }
        });
    }

    private void getTasks() {
        //loadTasks();
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();







                UserService apiService = ApiClient.getClient().create(UserService.class);
                Call<allTasks> call = apiService.getAllTasks();

                call.enqueue(new Callback<allTasks>() {
                    public List<Task> tasks = new ArrayList<>();
                    List<String> mobileArray = new ArrayList<>();
                    @Override
                    public void onResponse(Call<allTasks>call, Response<allTasks> response) {
                        tasks  = response.body().getData();
                        for(Task task : tasks)
                        {
                            //mobileArray.add(task.getId()+": "+task.getTask() );

                            Task task2 = new Task();
                            task2.setId(task.getId());
                            task2.setTask(task.getTask());
                            task2.setFinishBy(task.getFinishBy());
                            task2.setFinished(false);
                            task2.setDescription(task.getDescription());
                            task2.setLocation(task.getLocation());


                            //DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().insert(task2);



                        }
                    }
                    @Override
                    public void onFailure(Call<allTasks> call, Throwable t) {
                        System.out.println(" !!! Error !! " + t.getMessage());
                    }
                });




                return taskList;

            }

            @Override
            protected void onPostExecute(List<Task> tasks) {

                super.onPostExecute(tasks);
                TasksAdapter adapter = new TasksAdapter(tasks);
                recyclerView.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


    public void addtest(){

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {




                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }



        }

        SaveTask st = new SaveTask();
        st.execute();


    }

    private void initializationViews() {
        swipeRefresh = findViewById(R.id.swiperefresh);
        recyclerView = findViewById(R.id.recyclerview_tasks);
        buttonAddTask = findViewById(R.id.floating_button_add);
    }


}
