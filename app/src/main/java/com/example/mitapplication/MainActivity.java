package com.example.mitapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView manageProfileCard, viewTimetbableCard, viewGradesCard, chatAssistanceCard, karkiaCard,findFriendsCard, studentCupportCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manageProfileCard = (CardView) findViewById(R.id.managePersonalInfoCard);
        viewGradesCard = (CardView) findViewById(R.id.viewGradesCard);
        viewTimetbableCard = (CardView) findViewById(R.id.viewTimetableCard);
        chatAssistanceCard = (CardView) findViewById(R.id.chatAssistanceCard);
        karkiaCard = (CardView) findViewById(R.id.karakia);
        findFriendsCard = (CardView) findViewById(R.id.findFriendsCard);
        studentCupportCard = (CardView) findViewById(R.id.StudentSupportCard);



        manageProfileCard.setOnClickListener(this);
        viewTimetbableCard.setOnClickListener(this);
        viewGradesCard.setOnClickListener(this);
        chatAssistanceCard.setOnClickListener(this);
        karkiaCard.setOnClickListener(this);
        findFriendsCard.setOnClickListener(this);
        studentCupportCard.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.managePersonalInfoCard:
                intent = new Intent(this, managePersonalDetails.class);
                startActivity(intent);
                break;

            case R.id.viewTimetableCard:
                intent = new Intent(this, viewTimetable.class);
                startActivity(intent);
                break;

            case R.id.viewGradesCard:
                intent = new Intent(this, viewGrades.class);
                startActivity(intent);
                break;

            case R.id.chatAssistanceCard:
                intent = new Intent(this, chatAssistance.class);
                startActivity(intent);
                break;
            case R.id.karakia:
                intent = new Intent(this, karakia.class);
                startActivity(intent);
                break;
            case R.id.findFriendsCard:
                intent = new Intent(this, findFriends.class);
                startActivity(intent);
                break;
            case R.id.StudentSupportCard:
                intent = new Intent(this, studentSupport.class);
                startActivity(intent);
                break;
        }


    }
}