package com.example.mitapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

public class managePersonalDetails extends AppCompatActivity {
    EditText Sid, Name, Address, Phone, Email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;


    Button Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_personal_details);

        Sid = findViewById(R.id.sid);
        Name = findViewById(R.id.name);
        Address = findViewById(R.id.address);
        Phone = findViewById(R.id.phone);
        Email = findViewById(R.id.email);
        Save = findViewById(R.id.save);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        DocumentReference documentReference = fStore.collection("students").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Phone.setText(documentSnapshot.getString("Password"));
                Name.setText(documentSnapshot.getString("Name"));
                Email.setText(documentSnapshot.getString("Email"));
                Address.setText(documentSnapshot.getString("Address"));
            }



        });



        //updating data

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty() || Phone.getText().toString().isEmpty() || Email.getText().toString().isEmpty() || Address.getText().toString().isEmpty()) {
                    Toast.makeText(managePersonalDetails.this, "One of Many fields are empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = Email.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = fStore.collection("students").document(userId);
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("Email", email);
                        edited.put("Name", Name.getText().toString());
                        edited.put("Address", Address.getText().toString());
                        edited.put("Password", Phone.getText().toString());
                        documentReference.update(edited);
                        Toast.makeText(managePersonalDetails.this, "Hello",Toast.LENGTH_SHORT).show();

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(managePersonalDetails.this, "Profile updated",Toast.LENGTH_SHORT).show();

                            }
                        });
                        Toast.makeText(managePersonalDetails.this, "Email updated",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(managePersonalDetails.this, e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }




















}