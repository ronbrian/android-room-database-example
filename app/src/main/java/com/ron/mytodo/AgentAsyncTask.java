package com.ron.mytodo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.ron.mytodo.Database.DatabaseClient;
import com.ron.mytodo.model.Task;
import com.ron.mytodo.rest.ApiClient;
import com.ron.mytodo.rest.UserService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentAsyncTask extends AsyncTask<Void, Void, Integer> {

    private Context mCtx;
        //trying to prevent memory Leak
        private WeakReference<Activity> weakActivity;

        public AgentAsyncTask(Activity activity) {
            weakActivity = new WeakReference<>(activity);

        }

        @Override
        protected Integer doInBackground(Void... params) {


            //Loading tasks from MySQL to RoomDB

            UserService apiService = ApiClient.getClient().create(UserService.class);
            Call<allTasks> call = apiService.getAllTasks();

            call.enqueue(new Callback<allTasks>() {
                public List<Task> tasks = new ArrayList<>();
                List<String> mobileArray = new ArrayList<>();
                @Override
                public void onResponse(Call<allTasks> call, Response<allTasks> response) {
                    tasks  = response.body().getData();
       /*         for(Task task : tasks)
                {*/
                    //mobileArray.add(task.getId()+": "+task.getTask() );
                    Task task2 = new Task();
                    task2.setTask("Testing");
                    task2.setDescription("Funny Desc");
                    task2.setFinishBy("Never");
                    task2.setFinished(false);

                    DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .taskDao()
                            .insert(task2);


                    // }
                    //System.out.println(mobileArray);
                }
                @Override
                public void onFailure(Call<allTasks> call, Throwable t) {
                    System.out.println(" !!! Error !! " + t.getMessage());
                }
            });




        return null;



        }





}
