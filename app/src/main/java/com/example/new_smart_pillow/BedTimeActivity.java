package com.example.new_smart_pillow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BedTimeActivity extends AppCompatActivity {

    //Creating object of DatabaseReference class to access firebase's Realtime DB
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-smart-pillow-default-rtdb.firebaseio.com/");

    TextView phone;
    EditText count1, count2, count3, count4;
    Button addBedTime, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_time);

        phone = findViewById(R.id.textView26);
        count1 =findViewById(R.id.editTextNumber3);
        count2 = findViewById(R.id.editTextNumber4);
        count3 = findViewById(R.id.editTextNumber5);
        count4 = findViewById(R.id.editTextNumber6);


        addBedTime = findViewById(R.id.button9);
        back = findViewById(R.id.button10);

        String tele = getIntent().getStringExtra("tele");
        phone.setText(tele);

        String mobile = phone.getText().toString();

        addBedTime.setOnClickListener(v -> {

            String c1 = count1.getText().toString();
            String c2 = count2.getText().toString();
            String c3 = count3.getText().toString();
            String c4 = count4.getText().toString();


            databaseReference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                //Sending data to firebase realtime DB
                                //setting mobile number as unique
                                databaseReference.child(mobile).child("3h").setValue(c1);
                                databaseReference.child(mobile).child("4h").setValue(c2);
                                databaseReference.child(mobile).child("6h").setValue(c3);
                                databaseReference.child(mobile).child("7h").setValue(c4);

                                Toast.makeText(BedTimeActivity.this, "Bed Time added", Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(BedTimeActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        });

        back.setOnClickListener(v -> {
            finish();
        });
    }
}