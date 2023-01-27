package tech.tanztalks.android.myfirebaseapp.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tech.tanztalks.android.myfirebaseapp.R;
import tech.tanztalks.android.myfirebaseapp.models.ModelComment;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder> {

    Context context;
    List<ModelComment> commentList;

    public AdapterComments(Context context, List<ModelComment> commentList){
        this.context = context;
        this.commentList = commentList;
    }


    @NonNull
    @Override
    public AdapterComments.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //bind the row_comments.xml layout

        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, viewGroup, false);


        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterComments.MyHolder myHolder, int i) {
      // get the data
        String uid = commentList.get(i).getUid();
        String name = commentList.get(i).getuName();
        String email = commentList.get(i).getuEmail();
        String image = commentList.get(i).getuDp();
        String cid = commentList.get(i).getcId();
        String comment = commentList.get(i).getComment();
        String timestamp = commentList.get(i).getTimestamp();

        //
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        //set the data
        myHolder.nameTv.setText(name);
        myHolder.commentTv.setText(comment);
        myHolder.timeTv.setText(pTime);
        //set user dp

        try{
            Picasso.with(context).load(image).placeholder(R.drawable.ic_default_img).into(myHolder.avatarIv);
        } catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }




    class MyHolder extends RecyclerView.ViewHolder {
        //declar views from row_comment.xml
        ImageView avatarIv;
        TextView nameTv, commentTv, timeTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            timeTv = itemView.findViewById(R.id.timeTv);


        }
    }

}