package com.example.android.sportal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScanResultActivity1 extends AppCompatActivity {

    ImageView user_iv;
    TextView name_tv;
    TextView degree_tv;
    TextView branch_tv;
    TextView student_id_tv;
    TextView message_tv;

    LinearLayout equipment_layout;
    TextView equipment_name;
    TextView equipment_sport;
    TextView equipment_contents;
    TextView equipment_status;

    String user_id;
    String user_name;
    String user_degree;
    String user_branch;
    String user_student_id;
    String user_photo_url;
    String booking_id;
    Boolean user_received;

    ArrayList<String> contents_list;

    DatabaseReference databaseEquipment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result1);

        user_iv = findViewById(R.id.equipment_iv);
        name_tv = findViewById(R.id.name_tv);
        degree_tv = findViewById(R.id.degree_tv);
        branch_tv = findViewById(R.id.branch_tv);
        student_id_tv = findViewById(R.id.student_id_tv);
        message_tv = findViewById(R.id.message_tv);
        equipment_layout = findViewById(R.id.equipment_layout);
        equipment_name = findViewById(R.id.equipment_name);
        equipment_sport = findViewById(R.id.equipment_sport);
        equipment_contents = findViewById(R.id.equipment_contents);
        equipment_status = findViewById(R.id.equipment_status);

        user_id = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("user_name");
        user_degree = getIntent().getStringExtra("user_degree");
        user_branch = getIntent().getStringExtra("user_branch");
        user_student_id = getIntent().getStringExtra("user_student_id");
        user_photo_url = getIntent().getStringExtra("user_photo_url");
        booking_id = getIntent().getStringExtra("booking_id");
        user_received = getIntent().getBooleanExtra("user_received",false);

        databaseEquipment = FirebaseDatabase.getInstance().getReference("equipments");
        contents_list = new ArrayList<String>(10);

        Glide.with(getApplicationContext()).load(user_photo_url).into(user_iv);
        name_tv.setText(user_name);
        degree_tv.setText(user_degree);
        branch_tv.setText(user_branch);
        student_id_tv.setText(user_student_id);


    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseEquipment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Equipment equipment_obj = new Equipment();

                for(DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()){
                    Equipment equipment = equipmentSnapshot.getValue(Equipment.class);
                    if(equipment.id.equals(booking_id))
                        equipment_obj = equipment;
                }
                equipment_name.setText(equipment_obj.name);
                equipment_sport.setText(equipment_obj.sport);

                int i;
                String contents_string = "";
                contents_list = equipment_obj.contents;
                for(i=0;i< contents_list.size();i++){
                    contents_string = contents_string + "\n" +(i+1)+". "+equipment_obj.contents.get(i);
                }

                equipment_contents.setText(contents_string);

                if(user_received){
                    equipment_status.setText("Received");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
