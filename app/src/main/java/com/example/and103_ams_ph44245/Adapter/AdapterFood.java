package com.example.and103_ams_ph44245.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.and103_ams_ph44245.Model.Food;
import com.example.and103_ams_ph44245.R;
import com.example.and103_ams_ph44245.service.APIService;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.ViewHolder> {
    private Context context;
    private ArrayList<Food> list;
    APIService apiService;
    public AdapterFood(Context context, ArrayList<Food> list) {
        this.context = context;
        this.list = list;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Glide.with(context)
                .load(list.get(i).getImage())
                .centerCrop()
                .into(holder.imgAvatar);
        holder.tvID.setText("ID: " + list.get(i).getId());
        holder.tvName.setText("Name: " + list.get(i).getName());
        holder.tvPrice.setText("Price: $" + list.get(i).getPrice());
        holder.tvEvaluate.setText("Quantity: " + list.get(i).getQuantity());
        holder.tvDescribe.setText("Describe: " + list.get(i).getDescribe());
        holder.item_food.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvName, tvPrice, tvEvaluate, tvDescribe;
        ImageView imgAvatar;
        LinearLayout item_food;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = (TextView) itemView.findViewById(R.id.tv_id);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvEvaluate = (TextView) itemView.findViewById(R.id.tv_evaluate);
            tvDescribe = (TextView) itemView.findViewById(R.id.tv_describe);
            item_food = itemView.findViewById(R.id.item_food);
        }
    }

}
