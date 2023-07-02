package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.tanztalks.android.myfirebaseapp.adapters.ReportItemAdapter;
//notused
public class ReportActivity extends AppCompatActivity {

    private ReportItemAdapter reportItemAdapter;
    private RecyclerView reportRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportRecyclerView = findViewById(R.id.reportRecyclerView);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportItemAdapter = new ReportItemAdapter(new ArrayList<>());
        reportRecyclerView.setAdapter(reportItemAdapter);

        processPosts();
    }

    private void processPosts() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);
                }

                // Process each post
                for (Post post : posts) {
                    processPost(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });
    }

    private void processPost(Post post) {
        String postId = post.getpId();

        // Fetch the likes count for the post from Firebase
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes").child(postId);
        likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int likesCount = (int) dataSnapshot.getChildrenCount();

                // Fetch the comments count for the post from Firebase
                DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comment");
                commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int commentsCount = (int) dataSnapshot.getChildrenCount();

                        // Process the post with the likes and comments counts
                        processPostWithCounts(post, likesCount, commentsCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error if necessary
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });
    }

    private void processPostWithCounts(Post post, int likesCount, int commentsCount) {
        // Perform any necessary processing on the fetched post
        // For example, calculate the total likes and comments per user
        Map<String, Integer> userLikesCount = new HashMap<>();
        Map<String, Integer> userCommentsCount = new HashMap<>();

        String userId = post.getuId();

        // Count the likes per user
        if (userLikesCount.containsKey(userId)) {
            int userLikes = userLikesCount.get(userId);
            userLikes += likesCount;
            userLikesCount.put(userId, userLikes);
        } else {
            userLikesCount.put(userId, likesCount);
        }

        // Count the comments per user
        if (userCommentsCount.containsKey(userId)) {
            int userComments = userCommentsCount.get(userId);
            userComments += commentsCount;
            userCommentsCount.put(userId, userComments);
        } else {
            userCommentsCount.put(userId, commentsCount);
        }

        // Generate report items per user
        List<ReportItem> reportItems = new ArrayList<>();
        for (String uid : userLikesCount.keySet()) {
            int totalLikes = userLikesCount.get(uid);
            int totalComments = userCommentsCount.get(uid);

            // Get the user's details from the registered users list
            getUserDetails(uid, totalLikes, totalComments, reportItems);
        }

        // Update the UI with the generated report items
        reportItemAdapter.setData(reportItems);
    }

    private void getUserDetails(String userId, int totalLikes, int totalComments, List<ReportItem> reportItems) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegisteredUser user = dataSnapshot.getValue(RegisteredUser.class);
                if (user != null) {
                    String fullName = user.getFullName();
                    ReportItem reportItem = new ReportItem(fullName, totalLikes, totalComments);
                    reportItems.add(reportItem);
                    reportItemAdapter.setData(reportItems);
                } else {
                    // User data not available or fetch failed
                    // Handle the error case appropriately, such as skipping the ReportItem or displaying an error message
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
                // Handle database error
                String errorMessage = databaseError.getMessage();
                Toast.makeText(ReportActivity.this, "Database error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
