package com.example.levelapp_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsList extends AppCompatActivity {

    ListView detailsList;
    List<TimeXY> details;

    public static ArrayList<TimeXY> donorInfo;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);

        detailsList = findViewById(R.id.listViewDetailsList);
        db = FirebaseDatabase.getInstance().getReference("UserDetails");
        details = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    TimeXY detail = postSnapshot.getValue(TimeXY.class);
                    details.add(detail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
