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

public class RegisterActivity extends AppCompatActivity {

    //Creating object of DatabaseReference class to access firebase's Realtime DB
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-smart-pillow-default-rtdb.firebaseio.com/");

    TextView signIn, userType;
    Button register;

    EditText userName, email, password, confirmPassword, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signIn = findViewById(R.id.textView2);
        userType = findViewById(R.id.textView4);

        register = findViewById(R.id.button);

        userName = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        confirmPassword = findViewById(R.id.editTextTextPassword2);
        phoneNumber = findViewById(R.id.editTextNumber);

        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        register.setOnClickListener(v -> {

            //getting data from edit text
            String uN = userName.getText().toString();
            String eml = email.getText().toString();
            String pass = password.getText().toString();
            String conPass = confirmPassword.getText().toString();
            String uT = userType.getText().toString();
            String phone = phoneNumber.getText().toString();

            //check if user filled all the details
            if (uN.isEmpty() || eml.isEmpty() || pass.isEmpty() || conPass.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show();
            }
            //checking if passwords are matching
            else if (!pass.equals(conPass)){
                Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
            }
            // validating phone number
            else if (phone.length() != 10) {
                Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
            }
            else {
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if phone number registered before
                        if (snapshot.hasChild(phone)) {
                            Toast.makeText(RegisterActivity.this, "This phone number is already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            //Sending data to firebase realtime DB
                            //setting mobile number as unique
                            databaseReference.child("users").child(phone).child("User Name").setValue(uN);
                            databaseReference.child("users").child(phone).child("Email").setValue(eml);
                            databaseReference.child("users").child(phone).child("Password").setValue(pass);
                            databaseReference.child("users").child(phone).child("User Type").setValue(uT);

                            Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                            finish();
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