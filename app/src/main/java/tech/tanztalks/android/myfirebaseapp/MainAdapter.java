package tech.tanztalks.android.myfirebaseapp;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    //Initilize variable
    private ArrayList<MainData> dataArrayList;
    private Activity activity;

    //Create constructor
    public  MainAdapter(Activity activity, ArrayList<MainData> dataArrayList){
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Initialize main data
        MainData data = dataArrayList.get(position);
        //Set image on image view
        Picasso.with(activity).load(data.getImage()).into(holder.imageView);
        //set name on text view
        holder.textView.setText(data.getName());
     }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize viewholder
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Assign variable
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
