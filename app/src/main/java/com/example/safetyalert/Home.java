package com.example.safetyalert;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class Home extends AppCompatActivity {
    CardView addNumber,yourcontactcard,capimg,logoutCard;
    FirebaseFirestore db;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        addNumber=findViewById(R.id.addnumbers);
        yourcontactcard=findViewById(R.id.yourcontactcard);
        name=findViewById(R.id.yourname);
        capimg=findViewById(R.id.capimg);
        logoutCard=findViewById(R.id.logout);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String userphonenumber = sh.getString("phone", null);
        if (userphonenumber!=null)
        {
            db.collection("users").document(userphonenumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot=task.getResult();
                    name.setText("Welcome, "+documentSnapshot.get("name").toString());
                }
            });

            addNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Home.this,AddNumbers.class));
                }
            });

            yourcontactcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Home.this,ContactList.class));
                }
            });

            capimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Home.this,DemoCamActivity.class));
                }
            });

            logoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Home.this, "Logout", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("phone",null);
                    myEdit.commit();
                    startActivity(new Intent(Home.this,LoginScreen.class));
                    Home.this.finish();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("MYID", "CHANNEL", NotificationManager.IMPORTANCE_HIGH);

                NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                m.createNotificationChannel(channel);
            }
        }
        else
        {
            Intent main = new Intent(Home.this, LoginScreen.class);
            startActivity(main);
            finish();
        }


    }


    private ActivityResultLauncher<String[]> multiplePermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {

            for (Map.Entry<String,Boolean> entry : result.entrySet())
                if(!entry.getValue()){
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Permission Must Be Granted!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Grant Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            multiplePermissions.launch(new String[]{entry.getKey()});
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }


        }

    });



    public void stopService(View view) {

        Intent notificationIntent = new Intent(Home.this,ServiceMine.class);
        notificationIntent.setAction("stop");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(notificationIntent);
            Snackbar.make(findViewById(android.R.id.content),"Service Stopped!", Snackbar.LENGTH_LONG).show();
        }
    }

    public void startServiceV(View view) {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String userphonenumber = sh.getString("phone", "");
        if (userphonenumber!=null || userphonenumber!="") {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Intent notificationIntent = new Intent(Home.this, ServiceMine.class);
                notificationIntent.setAction("Start");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getApplicationContext().startForegroundService(notificationIntent);
                    Snackbar.make(findViewById(android.R.id.content), "Service Started!", Snackbar.LENGTH_LONG).show();
                }
            } else {
                multiplePermissions.launch(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
            }
        }
        else{
            Toast.makeText(Home.this, "Please login in system", Toast.LENGTH_LONG).show();

        }

    }


}