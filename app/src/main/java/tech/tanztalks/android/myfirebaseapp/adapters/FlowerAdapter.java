package tech.tanztalks.android.myfirebaseapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.internal.ConnectionCallbacks;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.flow.internal.FusibleFlow;
import tech.tanztalks.android.myfirebaseapp.R;
import tech.tanztalks.android.myfirebaseapp.models.FlowerResponseModel;

public class FlowerAdapter extends ArrayAdapter<FlowerResponseModel> {

    private Context context;
    private List<FlowerResponseModel> flowerResponseModels;
  public FlowerAdapter(Context context, List<FlowerResponseModel> flowerResponseModels){
      super(context,R.layout.display_row,flowerResponseModels);
      this.context=context;
      this.flowerResponseModels = flowerResponseModels;


  }
    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent){
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView=inflater.inflate(R.layout.display_row,parent, Boolean.parseBoolean(null));
      TextView nameTV = convertView.findViewById(R.id.flower_name);
      //TextView priceTv = convertView.findViewById(R.id.flower_price);

      nameTV.setText(flowerResponseModels.get(position).getName());
      //priceTv.setText(String.valueOf(flowerResponseModels.get(position).getPrice()));
      return convertView;
    }
}
