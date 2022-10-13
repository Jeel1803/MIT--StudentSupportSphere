package com.example.mitapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    public static final  String TAG = "TAG";

    EditText Name, Email, Password, Address;
    Button registerButton, loginButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore ;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        Name = findViewById(R.id.name);
        Password = findViewById(R.id.password);
        Email = findViewById(R.id.email);
        Address = findViewById(R.id.address);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }



        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String address = Address.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Password.setError("Password is Required");
                    return;
                }
                if(password.length()<6){
                    Password.setError("Password must be >=6 Character");
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Toast.makeText(Registration.this, "User Created 111111", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("students").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name",name);
                            user.put("Email",email);
                            user.put("Address",address);
                            user.put("Password",password);
                            Toast.makeText(Registration.this, "User Created", Toast.LENGTH_LONG).show();

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Log.d("TAG", "onSuccess: user Profile is created for "+ userID);
                                    Toast.makeText(Registration.this , "User Entered correctly "+ userID,Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "OnFailure: "+e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else{
                            Toast.makeText(Registration.this , "Error! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }}