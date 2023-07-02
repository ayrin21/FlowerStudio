package tech.tanztalks.android.myfirebaseapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tech.tanztalks.android.myfirebaseapp.R;
import tech.tanztalks.android.myfirebaseapp.ReportItem;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ViewHolder> {

    private List<ReportItem> reportItems;

    public ReportItemAdapter(List<ReportItem> reportItems) {
        this.reportItems = reportItems;
    }

    public void setData(List<ReportItem> reportItems) {
        this.reportItems = reportItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportItem reportItem = reportItems.get(position);

        holder.userNameTextView.setText(reportItem.getFullName());
        holder.likesTextView.setText("Likes: " + reportItem.getLikes());
        holder.commentsTextView.setText("Comments: " + reportItem.getComments());
    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView likesTextView;
        TextView commentsTextView;

        ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            likesTextView = itemView.findViewById(R.id.likesTextView);
            commentsTextView = itemView.findViewById(R.id.commentsTextView);
        }
    }
}

