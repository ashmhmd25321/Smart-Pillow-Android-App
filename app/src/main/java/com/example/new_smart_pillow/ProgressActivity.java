package com.example.new_smart_pillow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

public class ProgressActivity extends AppCompatActivity {
    //Creating object of DatabaseReference class to access firebase's Realtime DB
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-smart-pillow-default-rtdb.firebaseio.com/");

    GraphView graphView;
    TextView mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        graphView = findViewById(R.id.idGraphView);

        mobile = findViewById(R.id.textView24);

        String tele = getIntent().getStringExtra("tele");
        mobile.setText(tele);

        String phone = mobile.getText().toString();

        try {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild(phone)) {
                        String first = snapshot.child(phone).child("3h").getValue(String.class);
                        String second = snapshot.child(phone).child("4h").getValue(String.class);
                        String third = snapshot.child(phone).child("6h").getValue(String.class);
                        String fourth = snapshot.child(phone).child("7h").getValue(String.class);

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                                new DataPoint(3, Double.parseDouble(first)),
                                new DataPoint(4, Double.parseDouble(second)),
                                new DataPoint(6, Double.parseDouble(third)),
                                new DataPoint(7, Double.parseDouble(fourth))
                        });
                        graphView.setTitle("Bed Time Hour Chart");
                        graphView.setTitleColor(R.color.purple_200);
                        graphView.setTitleTextSize(36);
                        graphView.addSeries(series);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }
}