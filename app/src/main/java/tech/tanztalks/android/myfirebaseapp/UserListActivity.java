package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<User> list;
    DatabaseReference databaseReference;
    MyAdapter adapter;
    ActionBar actionBar;

    //
    final int ITEM_LOAD_COUNT = 21;
    int total_item = 0, last_visible_item;
    boolean isLoading = false, isMaxDAta = false;

    String last_node = "", last_key = "";

    //
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserListActivity.this, UserAcitivity.class));
        finish();
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        actionBar = getSupportActionBar();
        actionBar.setTitle("User List");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);

        //
        getLastKeyFromFirebase();

        //
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        // DividerItem Decoration
        //
        adapter = new MyAdapter(this, list);
        recyclerView.setAdapter(adapter);

        //s
        getUsers();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                total_item = new LinearLayoutManager(UserListActivity.this).getItemCount();
                last_visible_item = new LinearLayoutManager(UserListActivity.this).findLastVisibleItemPosition();

                if (!isLoading && total_item <= ((last_visible_item+ITEM_LOAD_COUNT))){
                    getUsers();
                    isLoading = true;
                }
            }
        });
        //e


//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    list.add(user);
//                }
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private void getUsers() {
        if (!isMaxDAta) {
            Query query;
            if (TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().getReference().child("Registered Users").orderByKey().startAt(last_node).limitToFirst(ITEM_LOAD_COUNT);
            else
                query = FirebaseDatabase.getInstance().getReference().child("Registered Users").orderByKey().startAt(last_node).limitToFirst(ITEM_LOAD_COUNT);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        ArrayList<User> newUsers = new ArrayList<>();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            newUsers.add(userSnapshot.getValue(User.class));
                        }
                        last_node = newUsers.get(newUsers.size() - 1).getUid();
                        if (!last_node.equals(last_key))
                            newUsers.remove(newUsers.size() - 1);
                        else
                            last_node = "end"; //fix error infinity load final item

                        adapter.addAll(newUsers);
                        isLoading = false;

                    } else {
                        isLoading = false;
                        isMaxDAta = true;
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    isLoading = false;
                }
            });

        }
    }

    private void getLastKeyFromFirebase() {
        //Query get Last key
        Query getLastKey = FirebaseDatabase.getInstance().getReference().child("Registered Users").orderByKey().limitToLast(1);

        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot lastKey : dataSnapshot.getChildren()) {
                    last_key = lastKey.getKey();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserListActivity.this, "Can not get last key!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}