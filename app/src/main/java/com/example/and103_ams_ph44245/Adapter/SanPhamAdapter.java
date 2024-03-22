package com.example.and103_ams_ph44245.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.and103_ams_ph44245.Model.SanPhamModel;
import com.example.and103_ams_ph44245.R;
import com.example.and103_ams_ph44245.service.APIServer;
import com.example.and103_ams_ph44245.service.ItemClickListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private String url = "http://192.168.1.65:3000/";
    APIServer apiService;

    Context context;
    ArrayList<SanPhamModel> list;
    Retrofit retrofit;
    Uri imageUri;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SanPhamAdapter(Context context, ArrayList<SanPhamModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sp, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPhamModel sanPhamModel = list.get(position);
        holder.tenTextView.setText(sanPhamModel.getName());
        holder.giaTextView.setText(sanPhamModel.getPrice() + "");
        holder.soLuongTextView.setText(sanPhamModel.getQuantity() + "");
        holder.tonKhoTextView.setText(sanPhamModel.getInventory() + "");
        Glide.with(context)
                .load(sanPhamModel.getImage())
                .placeholder(R.drawable.noimageicon)
                .into(holder.imageView);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIServer.class);
        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.create();
                builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
                builder.setIcon(R.drawable.i_warning).setTitle("Cảnh báo");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = apiService.deleteSanPham(sanPhamModel.get_id());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                Log.e("onResponse: ", t.getMessage());
                            }
                        });

                    }
                }).setNegativeButton("Cancel", null).show();
            }
        });
        holder.btn_upda.setOnClickListener(view -> {
            if (itemClickListener !=null){
                itemClickListener.UpdateItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, btn_del,btn_upda;
        TextView tenTextView, giaTextView, soLuongTextView, tonKhoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tenTextView = itemView.findViewById(R.id.tenTextView);
            giaTextView = itemView.findViewById(R.id.giaTextView);
            soLuongTextView = itemView.findViewById(R.id.soLuongTextView);
            tonKhoTextView = itemView.findViewById(R.id.tonKhoTextView);
            btn_del = itemView.findViewById(R.id.btn_delete);
            btn_upda = itemView.findViewById(R.id.btn_update);
        }
    }
}
