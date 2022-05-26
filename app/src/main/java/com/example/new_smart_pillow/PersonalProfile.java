package com.example.new_smart_pillow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalProfile extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-smart-pillow-default-rtdb.firebaseio.com/");

    TextView userName, email, mobile, userType;
    Button bluetooth, progress, bedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

        userName = findViewById(R.id.textView10);
        email = findViewById(R.id.textView11);
        mobile = findViewById(R.id.textView12);
        userType = findViewById(R.id.textView13);

        bluetooth = findViewById(R.id.button5);
        progress = findViewById(R.id.button4);
        bedTime = findViewById(R.id.button8);

        String tele = getIntent().getStringExtra("tele");
        mobile.setText(tele);

        String phone = mobile.getText().toString();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(phone)) {
                    String getEmail = snapshot.child(phone).child("Email").getValue(String.class);
                    String getUserName = snapshot.child(phone).child("User Name").getValue(String.class);
                    String getUserType = snapshot.child(phone).child("User Type").getValue(String.class);

                    userName.setText(getUserName);
                    email.setText(getEmail);
                    userType.setText(getUserType);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bluetooth.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalProfile.this, BluetoothActivity.class);
            startActivity(intent);
        });

        bedTime.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalProfile.this, BedTimeActivity.class);
            intent.putExtra("tele", phone);
            startActivity(intent);
        });

        progress.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProgressActivity.class);
            intent.putExtra("tele", phone);
            startActivity(intent);
        });
    }
}