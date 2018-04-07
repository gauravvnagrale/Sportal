package com.example.android.sportal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Zamaan on 06-04-2018.
 */

public class MySportal extends Fragment {

    ImageView profile_iv;
    TextView display_name_tv;

    TextView degree_tv;
    TextView branch_tv;
    TextView student_id_tv;

    FirebaseAuth firebaseAuth;

    DatabaseReference databaseUsers;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mysportal, container, false);

        profile_iv = view.findViewById(R.id.profile_iv);
        display_name_tv = view.findViewById(R.id.display_name_tv);
        degree_tv = view.findViewById(R.id.degree_tv);
        branch_tv = view.findViewById(R.id.branch_tv);
        student_id_tv = view.findViewById(R.id.student_id_tv);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");


        display_name_tv.setText(firebaseAuth.getCurrentUser().getDisplayName());

        if(firebaseAuth.getCurrentUser().getPhotoUrl()!=null)
            Glide.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl().toString()).into(profile_iv);

        return view;
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
                    if(user.UID.equals(firebaseAuth.getCurrentUser().getUid()) ){
                        correct_user = user;
                    }
                }

                degree_tv.setText(correct_user.degree);
                branch_tv.setText(correct_user.branch);
                student_id_tv.setText(correct_user.student_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
