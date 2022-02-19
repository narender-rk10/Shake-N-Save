package com.example.safetyalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.SharedPreferences;
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

public class AddNumbers extends AppCompatActivity {

    TextInputEditText addnumber,addname;
    AppCompatButton addnumberbtn;
    Map<String, Object> contactmap = new HashMap<>();
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers);

        addnumber=findViewById(R.id.contactnumber);
        addnumberbtn=findViewById(R.id.addcontactbtn);
        addname=findViewById(R.id.contactname);
        db = FirebaseFirestore.getInstance();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String userphonenumber = sh.getString("phone", "");
        Toast.makeText(AddNumbers.this,userphonenumber,Toast.LENGTH_SHORT).show();
        addnumberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactmap.put("name",addname.getText().toString());
                contactmap.put("phonenumber",addnumber.getText().toString());
                db.collection("users").document(userphonenumber).collection("contacts").document(addnumber.getText().toString()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                if(!documentSnapshot.exists()) {
                                    db.collection("users").document(userphonenumber).collection("contacts").document(addnumber.getText().toString()).set(contactmap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(AddNumbers.this,"Contacted added Successfull",Toast.LENGTH_SHORT).show();
                                                    addname.setText("");
                                                    addnumber.setText("");
                                                }
                                            });
                                }else{
                                    Toast.makeText(AddNumbers.this,"Number already exist",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }
}