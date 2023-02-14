package tech.tanztalks.android.myfirebaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> list; // userList

    //s
    public  void addAll(ArrayList<User> newUsers){
       int initSize = list.size();
       list.addAll(newUsers);
       notifyItemChanged(initSize, newUsers.size());
    }

    public String getLastItemId (){
        return list.get(list.size()-1).getUid();
    }
    //e
    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
        //s

        //e
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent, false);// v = itemView

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.getFullname());
        holder.gender.setText(user.getGender());
        holder.mobile.setText(user.getMobile());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, gender, mobile, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            gender = itemView.findViewById(R.id.textGender);
            mobile = itemView.findViewById(R.id.textdob);
            email = itemView.findViewById(R.id.textemail);
        }
    }
}

