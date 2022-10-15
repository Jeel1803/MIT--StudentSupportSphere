package com.example.mitapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class viewGrades extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, grade;
    Spinner spinner;
    List<String> courses = new ArrayList<>();
    TextView assignment1, assignment2, test, marks1, marks2, testMarks, totalMarks,courseName, grades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grades);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        spinner = findViewById(R.id.spinner);
        assignment1 = findViewById(R.id.assignment1);
        assignment2 = findViewById(R.id.assignment2);
        test = findViewById(R.id.test);
        marks1 = findViewById(R.id.marks1);
        marks2 = findViewById(R.id.marks2);
        testMarks = findViewById(R.id.testMarks);
        totalMarks = findViewById(R.id.totalMarks);
        courseName = findViewById(R.id.courseName);
        grades = findViewById(R.id.grade);



        CollectionReference df = fStore.collection("students").document(userId).collection("Course");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        df.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String course = document.getString("Name");
                        courses.add(course);
                    }
                    adapter.notifyDataSetChanged();
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String item = parent.getSelectedItem().toString();
                            df.whereEqualTo("Name", item).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot document : task.getResult()){
                                                courseName.setText(document.getString("Name"));
                                                String assignemnt = document.getData().get("Assignment1").toString();
                                                marks1.setText(assignemnt + " / 30");
                                                String assignment2 = document.getData().get("Assignment2").toString();
                                                marks2.setText(assignment2 + " / 40") ;
                                                String test = document.getData().get("Test").toString();
                                                testMarks.setText(test + " / 30");
                                                int total =  Integer.parseInt(assignemnt) + Integer.parseInt(assignment2) + Integer.parseInt(test);
                                                totalMarks.setText(String.valueOf(total));
                                                if(total >= 90 &&  total<= 100){
                                                    grade = "A+";
                                                }
                                                if(total >= 85 &&  total<= 89){
                                                    grade = "A";
                                                }
                                                if(total >= 80 &&  total<= 84){
                                                    grade = "A-";
                                                }
                                                if(total >= 75 &&  total<= 79){
                                                    grade = "B+";
                                                }
                                                if(total >= 70 &&  total<= 74){
                                                    grade = "B";
                                                }
                                                if(total >= 65 &&  total<= 69){
                                                    grade = "B-";
                                                }
                                                if(total >= 60 &&  total<= 64){
                                                    grade = "C+";
                                                }
                                                if(total >= 55 &&  total<= 59){
                                                    grade = "C";
                                                }
                                                if(total >= 50 &&  total<= 54){
                                                    grade = "C-";
                                                }
                                                if(total >= 45 &&  total<= 49){
                                                    grade = "D+";
                                                }
                                                if(total >= 40 &&  total<= 44){
                                                    grade = "D";
                                                }
                                                if(total >= 0 &&  total<= 39){
                                                    grade = "D-";
                                                }
                                                grades.setText(grade);

                                            }
                                        }
                                    });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }


            }
        });
    }



}





