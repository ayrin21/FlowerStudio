package tech.tanztalks.android.myfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;


public class Review extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    //private Toolbar mainToolbar;
    private StorageReference storageReference;

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        firebaseAuth = FirebaseAuth.getInstance();

        //mainToolbar = findViewById(R.id.main_toolbar);

        mRecyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingActionButton);
        //setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Reviews");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Review.this , AddReviewActivity.class));
            }
        });




    }
}