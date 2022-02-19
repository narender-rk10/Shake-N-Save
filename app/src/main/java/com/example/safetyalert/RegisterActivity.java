package com.example.safetyalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText name,age,phonenumber;
    AppCompatButton register;
    private FirebaseFirestore db;
    LinearLayout goToLogin;
    Map<String, Object> usermap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=findViewById(R.id.entername);
        age=findViewById(R.id.enterage);
        phonenumber=findViewById(R.id.enterphonenumber);
        register=findViewById(R.id.registerbtn);
        db = FirebaseFirestore.getInstance();
        goToLogin=findViewById(R.id.gotoLogin);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginScreen.class));
                RegisterActivity.this.finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nam=name.getText().toString();
                String phone=phonenumber.getText().toString();
                String agge=age.getText().toString();

                usermap.put("age", agge);
                usermap.put("gender", "male");
                usermap.put("name", nam);
                usermap.put("phonenumber", phone);

                db.collection("users").document(phone).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();

                        if(!documentSnapshot.exists()) {
                            db.collection("users").document(phone).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(RegisterActivity.this, LoginScreen.class));
                                    Toast.makeText(RegisterActivity.this,"Registration Successfull",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this,"This phone number is already taken"+phone,Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


    }
}