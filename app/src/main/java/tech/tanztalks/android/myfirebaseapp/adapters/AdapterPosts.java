package tech.tanztalks.android.myfirebaseapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tech.tanztalks.android.myfirebaseapp.PostDetailActivity;
import tech.tanztalks.android.myfirebaseapp.R;
import tech.tanztalks.android.myfirebaseapp.models.ModelPost;



public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {
   Context context;
   List<ModelPost> postList;

   String myUid;

     private DatabaseReference likesRef; // for likes db node
     private DatabaseReference postsRef; //

     boolean mProcessLike = false;
    private int likedPostPosition = -1;


    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;

        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, viewGroup, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, @SuppressLint("RecyclerView") int i) {
//      //
        //ModelPost model = postList.get(i);

        //
        String uid = postList.get(i).getUid();
        String uEmail = postList.get(i).getuEmail();
        String uName = postList.get(i).getuName();
        String uDp = postList.get(i).getuDp();
        final String pId = postList.get(i).getpId();
        //Log.e("AdapterValue", pId);
        String pTitle = postList.get(i).getpTitle();
        String pDescription = postList.get(i).getpDescr();
        //rating
        String prating = postList.get(i).getpRating();
                //  rating
        String pImage = postList.get(i).getpImage();
        String pTimeStamp = postList.get(i).getpTime();
        String pLikes = postList.get(i).getpLikes();// contain total number of likes for a post
        String pComments = postList.get(i).getpComments();

        //
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();
        //set data

        myHolder.uNameTv.setText(uName);
        myHolder.pTimeTv.setText(pTime);
        myHolder.pTitleTv.setText(pTitle);
        myHolder.pDescriptionTv.setText(pDescription);
        myHolder.prating.setText(prating);
        myHolder.pLikesTv.setText(pLikes+" Likes"); //
        myHolder.pCommentsTv.setText(pComments + " Comments");
// Disable focusability of the like button
        myHolder.likeBtn.setFocusable(false);
        //set likes for each post
        setLikes(myHolder, pId);





        // set user dp...
        try{
            Picasso.with(context).load(uDp).placeholder(R.drawable.ic_default_img).into(myHolder.uPictureIv);
        }
        catch (Exception e){
            Picasso.with(context).load(uDp).placeholder(R.drawable.ic_default_img).into(myHolder.uPictureIv);
        }

        // set post image
        //if there is no image then hide image view
//        if (pImage.equals("noImage")){
//            myHolder.pImageIv.setVisibility(View.GONE);
//        }
        // else


        //handle button clicks
        myHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will implement later
                Toast.makeText(context, "More", Toast.LENGTH_SHORT).show();
            }
        });


//        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final int pLikes = Integer.parseInt(postList.get(i).getpLikes());
//                mProcessLike = true;
//                // get id of the post clicked
//                myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//                final String postIde = postList.get(i).getpId();
//
//                Log.d("AdapterPosts", "Like button clicked. Post ID: " + postIde);
//                likesRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (mProcessLike){
//                            if(dataSnapshot.child(postIde).hasChild(myUid)){
//                                 //already likes, so remove like
//                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
//                                likesRef.child(postIde).child(myUid).removeValue();
//                                mProcessLike = false;
//
//                            }
//                        }
//                        else{
//                            // not liked, like it
//                            postsRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
//                            likesRef.child(postIde).child(myUid).setValue("Liked");
//                            mProcessLike = false;
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        });
//        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final int pLikes = Integer.parseInt(postList.get(i).getpLikes());
//                mProcessLike = true;
//                // get id of the post clicked
//                myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                final String postIde = postList.get(i).getpId();
//
//                Log.d("AdapterPosts", "Like button clicked. Post ID: " + postIde);
//                likesRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (mProcessLike) {
//                            if (dataSnapshot.child(postIde).hasChild(myUid)) {
//                                //already liked, so remove like
//                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes - 1));
//                                likesRef.child(postIde).child(myUid).removeValue();
//                                mProcessLike = false;
//                            } else {
//                                // not liked, like it
//                                likesRef.child(postIde).child(myUid).setValue("Liked")
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                // The like has been successfully added to the Likes node
//                                                // You can update the pLikes value in the Posts node here
//                                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes + 1));
//                                                mProcessLike = false;
//                                            }
//                                        });
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final int pLikes = Integer.parseInt(postList.get(i).getpLikes());
//                mProcessLike = true;
//                myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                final String postIde = postList.get(i).getpId();
//
//                Log.d("AdapterPosts", "Like button clicked. Post ID: " + postIde);
//                likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (mProcessLike) {
//                            if (dataSnapshot.child(postIde).hasChild(myUid)) {
//                                // Already liked, so remove like
//                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes - 1))
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                likesRef.child(postIde).child(myUid).removeValue()
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void aVoid) {
//                                                                mProcessLike = false;
//                                                            }
//                                                        });
//                                            }
//                                        });
//
//                            } else {
//                                // Not liked, like it
//                                likesRef.child(postIde).child(myUid).setValue("Liked")
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes + 1))
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void aVoid) {
//                                                                mProcessLike = false;
//                                                            }
//                                                        });
//                                            }
//                                        });
//                            }
//                        } else {
//                            // This block handles the case when mProcessLike is false
//                            // It means the initial check for liking/unliking failed, so you can handle it accordingly
//                            // For example, you can display a toast or log an error message.
//                            Log.d("AdapterPosts", "Initial check for liking/unliking failed");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//
//        });

        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pLikes = Integer.parseInt(postList.get(i).getpLikes());
                mProcessLike = true;
                // get id of the post clicked
                myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String postIde = postList.get(i).getpId();

                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mProcessLike) {
                            if (dataSnapshot.child(postIde).hasChild(myUid)) {
                                // already liked, so remove like
                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes - 1));
                                likesRef.child(postIde).child(myUid).removeValue();
                                mProcessLike = false;

                                // Update the like count in the postList
                                int updatedLikesCount = pLikes - 1;
                                postList.get(i).setpLikes(String.valueOf(updatedLikesCount));

                                // Notify the adapter about the change in the specific item
                                notifyItemChanged(i);
                            } else {
                                // not liked, like it
                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes + 1));
                                likesRef.child(postIde).child(myUid).setValue("Liked");
                                mProcessLike = false;

                                // Update the like count in the postList
                                int updatedLikesCount = pLikes + 1;
                                postList.get(i).setpLikes(String.valueOf(updatedLikesCount));

                                // Notify the adapter about the change in the specific item
                                notifyItemChanged(i);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
//        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final int pLikes = Integer.parseInt(postList.get(i).getpLikes());
//                mProcessLike = true;
//                // get id of the post clicked
//                myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                final String postIde = postList.get(i).getpId();
//
//                Log.d("AdapterPosts", "Like button clicked. Post ID: " + postIde);
//                likesRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (mProcessLike) {
//                            if (dataSnapshot.child(postIde).hasChild(myUid)) {
//                                // already liked, so remove like
//                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes - 1));
//                                likesRef.child(postIde).child(myUid).removeValue();
//                                mProcessLike = false;
//
//                                // Add the following code to keep the liked post in view
//                                if (likedPostPosition == -1) {
//                                    likedPostPosition = i; // Set the liked post position
//                                }
//                            } else {
//                                // not liked, like it
//                                postsRef.child(postIde).child("pLikes").setValue(String.valueOf(pLikes + 1));
//                                likesRef.child(postIde).child(myUid).setValue("Liked");
//                                mProcessLike = false;
//
//                                // Add the following code to keep the liked post in view
//                                if (likedPostPosition == -1) {
//                                    likedPostPosition = i; // Set the liked post position
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//
//        // Rest of the code...
//
//        // Add the following code to scroll to the liked post
//        if (likedPostPosition == i) {
//            recyclerView.scrollToPosition(i);
//            likedPostPosition = -1; // Reset the position
//        }





        myHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (context, PostDetailActivity.class);
                intent.putExtra("postId", pId); // will get detail of post using this id
                context.startActivity(intent);
            }
        });
        myHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
            }
        });

    }





    private void setLikes(final MyHolder holder, final String postKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(postKey).hasChild(postKey)){
                    //user has liked the post

                    /* ... */
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                    holder.likeBtn.setText("Liked");
                }
                else {
                    //user has not liked the post

                    /* ... */
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0);
                    holder.likeBtn.setText("Like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder {

        //
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pTitleTv, pDescriptionTv, prating, pLikesTv, pCommentsTv;
        ImageButton moreBtn;
        Button likeBtn, commentBtn, shareBtn;
        DatabaseReference likereference;


        public MyHolder(@NonNull View itemView){
            super(itemView);

            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            //pImageIv = itemView.findViewById(R.id.pImageIv);
            uNameTv=itemView.findViewById(R.id.uNameTv);
            pTitleTv=itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv=itemView.findViewById(R.id.pDescriptionTv);
            prating=itemView.findViewById(R.id.prating);
            pLikesTv=itemView.findViewById(R.id.pLikesTv);
            pCommentsTv=itemView.findViewById(R.id.pCommentsTv);
            moreBtn=itemView.findViewById(R.id.moreBtn);
            likeBtn=itemView.findViewById(R.id.likeBtn);
            commentBtn=itemView.findViewById(R.id.commentBtn);
            shareBtn=itemView.findViewById(R.id.shareBtn);
            pTimeTv=itemView.findViewById(R.id.pTimeTv);
        }



     }
}




