package com.example.mitapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class studentSupport extends AppCompatActivity {

    TextView askyourself, studentSupport, counsellor, disability, askyourselfPhone, studentsupportPhone, counsellorPhone, disabilityPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_support);

        askyourself = findViewById(R.id.askYourselfEmail);
        askyourself.setText(Html.fromHtml("<a href =\"mailto:askyourself@mit.com\">askyourself@mit.com</a>"));
        askyourself.setMovementMethod(LinkMovementMethod.getInstance());

        studentSupport = findViewById(R.id.studentSupportEmail);
        studentSupport.setText(Html.fromHtml("<a href =\"mailto:advisor@mit.com\">advisor@mit.com</a>"));
        studentSupport.setMovementMethod(LinkMovementMethod.getInstance());

        counsellor = findViewById(R.id.counsellorEmail);
        counsellor.setText(Html.fromHtml("<a href =\"mailto:counsellor@mit.com\">counsellor@mit.com</a>"));
        counsellor.setMovementMethod(LinkMovementMethod.getInstance());

        disability = findViewById(R.id.disabilityEmail);
        disability.setText(Html.fromHtml("<a href =\"mailto:disability@mit.com\">disability@mit.com</a>"));
        disability.setMovementMethod(LinkMovementMethod.getInstance());

        askyourselfPhone = findViewById(R.id.askYpurselfPhone);
        askyourselfPhone.setText(Html.fromHtml("<a href =\"tel:12345678\">12345678</a>"));
        askyourselfPhone.setMovementMethod(LinkMovementMethod.getInstance());

        studentsupportPhone = findViewById(R.id.studentSupportPhone);
        studentsupportPhone.setText(Html.fromHtml("<a href =\"tel:12345678\">12345678</a>"));
        studentsupportPhone.setMovementMethod(LinkMovementMethod.getInstance());

        counsellorPhone = findViewById(R.id.counsellorPhone);
        counsellorPhone.setText(Html.fromHtml("<a href =\"tel:12345678\">12345678</a>"));
        counsellorPhone.setMovementMethod(LinkMovementMethod.getInstance());

        disabilityPhone = findViewById(R.id.disabilityPhone);
        disabilityPhone.setText(Html.fromHtml("<a href =\"tel:12345678\">12345678</a>"));
        disabilityPhone.setMovementMethod(LinkMovementMethod.getInstance());









    }
}