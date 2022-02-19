package com.example.safetyalert;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;

public class ServiceMine extends Service {
    boolean isRunning = false;

    FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    SmsManager manager = SmsManager.getDefault();
    String myLocation;
    @Override
    public void onCreate() {
        super.onCreate();

        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);

        int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Logic to handle location object
                            location.getLatitude();
                            location.getLongitude();
                            Log.d("a", String.valueOf(location.getAltitude()+ location.getLongitude()));
                            myLocation = "http://maps.google.com/maps?q=loc:"+location.getLatitude()+","+location.getLongitude() + " and my Battery Percentage is "+percentage+" %";
                        }else {
                            myLocation = "Unable to Find Location :(";
                        }
                    }
                });


        ShakeDetector.create(this, () -> {

            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String userphonenumber = sh.getString("phone", "");
                db.collection("users").document(userphonenumber).collection("contacts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size()>0){
                            QuerySnapshot documentSnapshot=task.getResult();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                manager.sendTextMessage(document.get("phonenumber").toString(),null,"I' m in Trouble!\nSending My Location :\n"+myLocation,null,null);

                            }
                        }
                    }
                });


        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        if (intent.getAction().equalsIgnoreCase("STOP")) {
            if(isRunning) {
                this.stopForeground(true);
                this.stopSelf();
            }
        } else {


            Intent notificationIntent = new Intent(this, Home.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ) {
                NotificationChannel channel = new NotificationChannel("MYID", "CHANNEL", NotificationManager.IMPORTANCE_HIGH);

                NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                m.createNotificationChannel(channel);

                Notification notification = new Notification.Builder(this, "MYID")
                        .setContentTitle("Women Safety")
                        .setContentText("Shake Device to Send SOS")
                        .setSmallIcon(R.drawable.ic_baseline_camera_alt_24)
                        .setContentIntent(pendingIntent)
                        .setChannelId("MYID")
                        .build();
                this.startForeground(115, notification);
                isRunning = true;





            }
            return START_NOT_STICKY;
        }

        return super.onStartCommand(intent,flags,startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
