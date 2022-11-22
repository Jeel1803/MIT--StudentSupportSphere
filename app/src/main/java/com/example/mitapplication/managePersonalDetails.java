package com.example.mitapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class managePersonalDetails extends AppCompatActivity {
    EditText Sid, Name, Address, Phone, PersonalEmail, Password;
    TextView Username;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ImageView ProfileImage;
    Button Save;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_personal_details);

        Sid = findViewById(R.id.sid);
        Name = findViewById(R.id.name);
        Address = findViewById(R.id.address);
        Phone = findViewById(R.id.phone);
       PersonalEmail = findViewById(R.id.email);
        Save = findViewById(R.id.save);
        Username = findViewById(R.id.username);
        ProfileImage = findViewById(R.id.profileImage);

        Password = findViewById(R.id.password);

        Sid.setEnabled(false);
        Name.setEnabled(false);
        Address.setEnabled(false);
        Phone.setEnabled(false);
        PersonalEmail.setEnabled(false);
        Password.setEnabled(false);
        Save.setEnabled(false);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Toast.makeText(managePersonalDetails.this, "Fetching Image1", Toast.LENGTH_LONG).show();

                Picasso.get().load(uri).into(ProfileImage);
               // Toast.makeText(managePersonalDetails.this, "Fetching Image", Toast.LENGTH_LONG).show();
            }
        });

        DocumentReference documentReference = fStore.collection("students").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Phone.setText(documentSnapshot.getString("Phone"));
                Name.setText(documentSnapshot.getString("Name"));
             PersonalEmail.setText(documentSnapshot.getString("PersonalEmail"));
                Address.setText(documentSnapshot.getString("Address"));
                Sid.setText((documentSnapshot.getString("StudentID")));
                Password.setText((documentSnapshot.getString("Password")));
                Username.setText((documentSnapshot.getString("Email")));
            }



        });







        //updating data

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty() || Phone.getText().toString().isEmpty() || PersonalEmail.getText().toString().isEmpty() || Address.getText().toString().isEmpty())
                {
                    Toast.makeText(managePersonalDetails.this, "One of Many fields are empty",Toast.LENGTH_SHORT).show();
                    return;
                }



                String email = Username.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = fStore.collection("students").document(userId);
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("Name", Name.getText().toString());
                        edited.put("Address", Address.getText().toString());
                        edited.put("Password", Password.getText().toString());
                        edited.put("Phone", Phone.getText().toString());
                        edited.put("PersonalEmail", PersonalEmail.getText().toString());


                        documentReference.update(edited);
                        //Toast.makeText(managePersonalDetails.this, "Hello",Toast.LENGTH_SHORT).show();

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(managePersonalDetails.this, "Profile updated",Toast.LENGTH_SHORT).show();

                            }
                        });
                        //Toast.makeText(managePersonalDetails.this, "Email updated",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(managePersonalDetails.this, e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1000);

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            //ProfileImage.setImageURI(imageUri);
            uploadImageToFirebase(imageUri);
            Toast.makeText(managePersonalDetails.this, "Uploading",Toast.LENGTH_LONG).show();


        }else {
            Toast.makeText(managePersonalDetails.this, "You haven't picked Image",Toast.LENGTH_LONG).show();

        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(ProfileImage);
                    }
                });
                Toast.makeText(managePersonalDetails.this, "Image Uploaded",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(managePersonalDetails.this, "Failed",Toast.LENGTH_LONG).show();

            }
        });

    }

    public void edit(View view) {
        Name.setEnabled(true);
        Address.setEnabled(true);
        Phone.setEnabled(true);
        PersonalEmail.setEnabled(true);
        Password.setEnabled(true);
        Save.setEnabled(true);

    }
}

