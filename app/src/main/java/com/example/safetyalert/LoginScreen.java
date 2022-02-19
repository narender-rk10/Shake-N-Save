package com.example.safetyalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginScreen extends AppCompatActivity {
    TextInputEditText phonenumber;
    AppCompatButton sendotpbtn;
    FirebaseFirestore db;
    LinearLayout goToRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        phonenumber=findViewById(R.id.loginphonenumber);
        sendotpbtn=findViewById(R.id.sendOtp);
        db = FirebaseFirestore.getInstance();

        goToRegister=findViewById(R.id.gotoRegister);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginScreen.this,RegisterActivity.class));
                LoginScreen.this.finish();
            }
        });

        sendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone=phonenumber.getText().toString();
                if(phone.length()==10){
                    db.collection("users").document(phone).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if(documentSnapshot.exists()) {
                                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phonenumber.getText().toString(),60, TimeUnit.SECONDS,LoginScreen.this,
                                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                            }

                                            @Override
                                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                                Toast.makeText(LoginScreen.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String otp, @NonNull  PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                super.onCodeSent(otp, forceResendingToken);
                                                Intent intent=new Intent(getApplicationContext(),OtpActivity.class);
                                                intent.putExtra("phonenum",phone);
                                                intent.putExtra("otp",otp);
                                                startActivity(intent);
                                                Toast.makeText(LoginScreen.this,"otp sent "+otp,Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else{
                                Toast.makeText(LoginScreen.this,"This phone number is not registered",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });
    }
}