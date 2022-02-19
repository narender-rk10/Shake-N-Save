package com.example.safetyalert;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {

    PinView otp;
    AppCompatButton verify;
    TextView codesentdetailtext,resendotp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        String otpbackend=getIntent().getStringExtra("otp");
        String phone=getIntent().getStringExtra("phonenum");

        codesentdetailtext=findViewById(R.id.codesentdetailtext);
        verify=findViewById(R.id.verifyotp);
        otp=findViewById(R.id.otp);
        codesentdetailtext.setText("Code is sent to +91-"+phone);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp.length()==6){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpbackend, otp.getText().toString());

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                        myEdit.putString("phone",phone);
                                        myEdit.commit();
                                        startActivity(new Intent(OtpActivity.this,Home.class));
                                        OtpActivity.this.finish();

                                    }else{

                                        Toast.makeText(OtpActivity.this,"Wrong OTP Enter",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}