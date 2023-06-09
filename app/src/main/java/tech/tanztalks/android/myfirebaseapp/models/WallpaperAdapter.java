package tech.tanztalks.android.myfirebaseapp.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.squareup.picasso.Target;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.sql.DataSource;

import tech.tanztalks.android.myfirebaseapp.R;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.viewHolder> {
    Context mContext;
    List<Wallpaper> wallpaperList;
    //RecyclerViewClickInterface recyclerViewClickInterface;

    public WallpaperAdapter(Context mContext, List<Wallpaper> imageList) {
        this.mContext = mContext;
        this.wallpaperList = wallpaperList;
        //this.recyclerViewClickInterface=recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public WallpaperAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(mContext).inflate(R.layout.row_item,parent,false);
        viewHolder holder=new viewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {


//        Glide.with(mContext)
//                .load(wallpaperList.get(position).getSrc().getMedium())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
//                        holder.progressbar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
//                        holder.progressbar.setVisibility(View.GONE);
//                        return false;
//                    }
//
////                    @Override
////                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
////                        holder.progressbar.setVisibility(View.GONE);
////                        return false;
////                    }
////
////                    @Override
////                    public boolean onResourceReady(Drawable resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
////                        holder.progressbar.setVisibility(View.GONE);
////                        return false;
////                    }
//                })
//
//                .into(holder.imageView);

        Picasso.with(mContext)
                .load(wallpaperList.get(position).getSrc().getMedium())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressbar.setVisibility(View.GONE);
                    }


                });





       /* holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recyclerViewClickInterface.onItemClick(position);

                /*AlertDialog builder=new AlertDialog.Builder(v.getRootView().getContext()).create();
                View dialogView= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.image_dialog,null);
                ImageView wallpaperImage, close;
                Button downloadNow,setHomeScreen;
                wallpaperImage=dialogView.findViewById(R.id.full_image);
                close=dialogView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        builder.dismiss();

                    }
                });
                String originalUrl=imageList.get(position).getSrc().getLarge();
                Glide.with(context)
                        .load(originalUrl)
                        .into(wallpaperImage);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();
                dialogView.cancelLongPress();
                downloadNow=dialogView.findViewById(R.id.download_now);
                setHomeScreen=dialogView.findViewById(R.id.set_home_screen);
                downloadNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(originalUrl);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadManager.enqueue(request);
                        Toast.makeText(context, "Downloading Start", Toast.LENGTH_SHORT).show();

                    }
                });

                setHomeScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                        Bitmap bitmap  = ((BitmapDrawable)wallpaperImage.getDrawable()).getBitmap();
                        try {
                            wallpaperManager.setBitmap(bitmap);
                            Toast.makeText(context, "Wallpaper Set", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }); */


    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar progressbar;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.wallpaper);
            progressbar=itemView.findViewById(R.id.progressBar);
        }
    }
}
