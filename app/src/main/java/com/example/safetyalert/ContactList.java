package com.example.safetyalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Tasks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ContactList extends AppCompatActivity {

    private RecyclerView recyclerView;
    ContactAdapter adapter; // Create Object of the Adapter class
    FirebaseFirestore firebaseFirestore;
    ArrayList<Contact> l=new ArrayList<>();

    String userphonenumber = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        recyclerView = findViewById(R.id.recycler1);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        userphonenumber = sh.getString("phone", "");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        firebaseFirestore= FirebaseFirestore.getInstance();






        readData(new MyCallback() {


            @Override
            public void onCallback() {

                show();
            }
        });
    }

    public interface MyCallback {
        void onCallback();
    }

    public void show() {


        ContactAdapter adapter=new ContactAdapter(l);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }



    public void readData(final MyCallback myCallback) {
        firebaseFirestore.collection("users").document(userphonenumber).collection("contacts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.getResult().size()>0){
                            int i=task.getResult().size();

                            for (DocumentSnapshot document : task.getResult()) {
                                Contact c=document.toObject(Contact.class);
//                                l.add(new Contact(document.get("name").toString(),document.get("phonenumber").toString()));
                                l.add(c);
                                if(i==1)myCallback.onCallback();
                                i--;


                            }

                        }



            }
        });
    }


    }