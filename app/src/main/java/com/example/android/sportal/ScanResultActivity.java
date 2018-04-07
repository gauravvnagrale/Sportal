package com.example.android.sportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScanResultActivity extends AppCompatActivity {

    DatabaseReference databaseUsers;
    String scanResult;

    ImageView user_iv;
    TextView name_tv;
    TextView degree_tv;
    TextView branch_tv;
    TextView student_id_tv;
    TextView message_tv;

    LinearLayout equipment_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        scanResult = getIntent().getStringExtra("scanResult");

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        user_iv = findViewById(R.id.user_iv);
        name_tv = findViewById(R.id.name_tv);
        degree_tv = findViewById(R.id.degree_tv);
        branch_tv = findViewById(R.id.branch_tv);
        student_id_tv = findViewById(R.id.student_id_tv);
        message_tv = findViewById(R.id.message_tv);
        equipment_layout = findViewById(R.id.equipment_layout);

        equipment_layout.setVisibility(View.GONE);

    }

    @Override
    public void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User correct_user = new User();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if(user.UID.equals(scanResult) ){
                        correct_user = user;
                    }
                }
                if(TextUtils.isEmpty(correct_user.name)){
                    Toast.makeText(ScanResultActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ScanResultActivity.this, ScannerActivity.class);
                    startActivity(intent);
                }

                Glide.with(getApplicationContext()).load(correct_user.photo_url).into(user_iv);
                name_tv.setText(correct_user.name);
                degree_tv.setText(correct_user.degree);
                branch_tv.setText(correct_user.branch);
                student_id_tv.setText(correct_user.student_id);

                if(correct_user.booked){
                    message_tv.setText("");
                    equipment_layout.setVisibility(View.VISIBLE);
                }




                //Toast.makeText(ScanResultActivity.this, correct_user.name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
