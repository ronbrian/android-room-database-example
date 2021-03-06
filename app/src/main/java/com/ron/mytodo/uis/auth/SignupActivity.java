package com.ron.mytodo.uis.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ron.mytodo.R;
import com.ron.mytodo.User;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity {
    //private static final String TAG = "ThisClass";
    private static final String TAG = "$NAME";
    private static final String EMAIL = "email";


    private EditText inputEmail, inputPassword,inputUserName;
    private Button btnSignIn, btnSignUp, btnResetPassword,loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String Semail,Spassword,Susername;

    private DatabaseReference mFirebaseDatabase,mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;
    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);




        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputUserName = (EditText) findViewById(R.id.name);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        loginButton = (Button) findViewById(R.id.login_button);

        auth = FirebaseAuth.getInstance();



        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseDatabase2 = mFirebaseInstance.getReference("uids");


        // store app title to 'app_title' node
        //mFirebaseInstance.getReference("Location View").setValue("Realtime Database");








        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  Semail= inputEmail.getText().toString().trim();
                 Spassword = inputPassword.getText().toString().trim();
                 Susername = inputEmail.getText().toString().trim();

                Log.e(TAG, "onClick: .e"+Semail);

                if (TextUtils.isEmpty(Semail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Spassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Spassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
              //  create user
                auth.createUserWithEmailAndPassword(Semail, Spassword)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                String name = inputUserName.getText().toString();
                                String email = inputEmail.getText().toString();


                                if (TextUtils.isEmpty(userId)) {
                                    createUser(email, Spassword, name);
                                } else {
                                    updateUser(Semail, Spassword, Susername);
                                }
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });




        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code


                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            String email = object.getString("email");
                                            String name = object.getString("name");
                                            inputEmail.setText(email);
                                            inputUserName.setText(name);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });






    }


    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSignUp.setText("Save");
        } else {
            btnSignUp.setText("Update");
        }
    }

    private void createUser(String Semail, String Spassword, String Susername) {
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            userId = currentFirebaseUser.getUid();

        }

        String locationn="";
        String data = "data";

        User user = new User(Semail, Susername, locationn, userId);

        mFirebaseDatabase.child(userId).setValue(user);



        User myuser = new User();

        myuser.setName(Susername);
        myuser.setEmail(Semail);
        myuser.setLocation("");
        myuser.setUserId(userId);


        addUser2(myuser);


       /*   try {
            Response<UserApiResponse> response = callSync.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        addUserChangeListener();
    }



    /*
    User data change listener
     */

    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

               // Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);

                // Display newly updated name and email
                //txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                inputEmail.setText("");
                inputUserName.setText("");

                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name, String email, String Susername) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }




    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();



    //Send user details using retrofit to rest API
    public void addUser2(User myuser){
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.148:9786/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//
//        UserService service = retrofit.create(UserService.class);
//
//
//        Call<UserApiResponse> callSync = service.addUser("Bearer "+token, myuser);
//        callSync.enqueue(new Callback<UserApiResponse>() {
//            @Override
//            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
//                if (response.isSuccessful()){
//                    Log.e("TAG", "it added successfully");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserApiResponse> call, Throwable t) {
//
//            }
//        });

    }

}
