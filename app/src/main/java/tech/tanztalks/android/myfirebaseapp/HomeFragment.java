package tech.tanztalks.android.myfirebaseapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tech.tanztalks.android.myfirebaseapp.adapters.AdapterPosts;
import tech.tanztalks.android.myfirebaseapp.models.ModelPost;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.postsRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //
        recyclerView.setLayoutManager(layoutManager);

        postList= new ArrayList<>();
        loadPosts();


        return view;


    }

    private void loadPosts() {
        postList = new ArrayList<>();
        postList.clear();
        adapterPosts = new AdapterPosts(getActivity(),postList);
        recyclerView.setAdapter(adapterPosts);
        //path
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from this ref

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              postList.clear();
              for(DataSnapshot ds: dataSnapshot.getChildren()){
                  ModelPost modelPost = ds.getValue(ModelPost.class);
                  postList.add(modelPost);

                  //adapter
                  adapterPosts = new AdapterPosts(getActivity(), postList);
                  // set adapter
                  recyclerView.setAdapter(adapterPosts);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // search post method
    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            //user is signed in stay here
            //set email of logged in user
            //mProfileTv.setText(user.getEmail());

        }
        else {
            //user not signed in, go to main acitivity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);//to show menu option in fragment
        super.onCreate(savedInstanceState);
    }

    //start
    /*inflate options menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.common_menu, menu);

        //hide some options
//        menu.findItem(R.id.action_create_group).setVisible(false);
//        menu.findItem(R.id.action_add_participant).setVisible(false);
//        menu.findItem(R.id.action_groupinfo).setVisible(false);
//
//        //searchview to search posts by post title/description
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
//
//        //search listener
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                //called when user press search button
//                if (!TextUtils.isEmpty(s)){
//                    searchPosts(s);
//                }
//                else {
//                    loadPosts();
//                }
//                return false;
//            }
//
////            @Override
////            public boolean onQueryTextChange(String s) {
////                //called as and when user press any letter
////                if (!TextUtils.isEmpty(s)){
////                    searchPosts(s);
////                }
////                else {
////                    loadPosts();
////                }
////                return false;
////            }
//        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    /*handle menu item clicks*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
//        if (id == R.id.action_logout){
//            firebaseAuth.signOut();
//            checkUserStatus();
//        }
//        else if (id == R.id.action_add_post){
//            startActivity(new Intent(getActivity(), AddPostActivity.class));
//        }
//        else if (id==R.id.action_settings){
//            //go to settings activity
//            startActivity(new Intent(getActivity(), SettingsActivity.class));
//        }

        return super.onOptionsItemSelected(item);
    }

}
//}
