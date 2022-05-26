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

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-smart-pillow-default-rtdb.firebaseio.com/");

    Button logIn;
    TextView signUp;
    EditText phoneNumber, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn = findViewById(R.id.button);
        signUp = findViewById(R.id.textView2);

        phoneNumber = findViewById(R.id.editTextNumber2);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        logIn.setOnClickListener(v -> {
            String phone = phoneNumber.getText().toString();
            String eml = email.getText().toString();
            String pass = password.getText().toString();

            if (phone.isEmpty() || eml.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if mobile number is existing or not
                        if (snapshot.hasChild(phone)) {
                            //now get the password and email and match it with entered values
                            String getEmail = snapshot.child(phone).child("Email").getValue(String.class);
                            String getPassword = snapshot.child(phone).child("Password").getValue(String.class);

                            if (getEmail.equals(eml) && getPassword.equals(pass)) {
                                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, PersonalProfile.class);
                                intent.putExtra("tele", phone);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Password or email entered", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Mobile number is not registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}