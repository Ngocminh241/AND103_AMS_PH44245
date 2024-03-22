package com.example.and103_ams_ph44245;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.and103_ams_ph44245.Adapter.SanPhamAdapter;
import com.example.and103_ams_ph44245.Model.SanPhamModel;
import com.example.and103_ams_ph44245.service.APIServer;
import com.example.and103_ams_ph44245.service.ItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SanPhamAdapter adapter;
    ArrayList<SanPhamModel> list;
    private String url =  "http://192.168.1.65:3000/";
    FloatingActionButton btn_add;
    ImageView imgUp;
    Uri imageUri;

    Retrofit retrofit;

    APIServer apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_home);
        recyclerView = findViewById(R.id.recycler);
        btn_add = findViewById(R.id.btn_add);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIServer.class);
        handleCallData();
        btn_add.setOnClickListener(v -> {
            showDialog(0, (SanPhamModel) null);
        });
        toolbar.setTitle("Danh sách sản phẩm");

    }


    private void showDialog(int type, SanPhamModel sanPhamModel) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        View v1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_add, null);
        builder.setView(v1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        EditText edtName = v1.findViewById(R.id.edt_name);
        EditText edtPrice = v1.findViewById(R.id.edt_price);
        EditText edtQuantity = v1.findViewById(R.id.edt_quantity);
        EditText edtInventory = v1.findViewById(R.id.edt_inventory);
        TextView tv_title = v1.findViewById(R.id.tv_title);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imgUp = v1.findViewById(R.id.img_dialog);
        if (type != 0) {
            tv_title.setText("Sửa Sản Phẩm");
            edtName.setText(sanPhamModel.getName());
            edtPrice.setText(sanPhamModel.getPrice()+"");
            edtQuantity.setText(sanPhamModel.getQuantity()+"");
            edtInventory.setText(sanPhamModel.getInventory()+"");
            Glide.with(MainActivity.this)
                    .load(sanPhamModel.getImage())
                    .placeholder(R.drawable.noimageicon)
                    .into(imgUp);
        }
        imgUp.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
        Button btnSave = v1.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    if (edtName.getText().toString().isEmpty() || edtPrice.getText().toString().isEmpty() || edtQuantity.getText().toString().isEmpty() || edtInventory.getText().toString().isEmpty() && imageUri != null) {
                        Toast.makeText(MainActivity.this, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = edtName.getText().toString();
                    int price = Integer.parseInt(edtPrice.getText().toString());
                    int quantity = Integer.parseInt(edtQuantity.getText().toString());
                    int inventory = Integer.parseInt(edtInventory.getText().toString());

                    String imgUri = imageUri.toString();

                    SanPhamModel sanPhamModelNew = new SanPhamModel();
                    sanPhamModelNew.setName(name);
                    sanPhamModelNew.setQuantity(quantity);
                    sanPhamModelNew.setInventory(inventory);
                    sanPhamModelNew.setImage(imgUri);
                    sanPhamModelNew.setPrice(price);

                    retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Call<Void> call = apiService.postSanPham(sanPhamModelNew);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                handleCallData();
                                alertDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                Log.e("loi", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                } else {
                    if (edtName.getText().toString().isEmpty() || edtPrice.getText().toString().isEmpty() || edtQuantity.getText().toString().isEmpty() || edtInventory.getText().toString().isEmpty() && imageUri != null) {
                        Toast.makeText(MainActivity.this, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = edtName.getText().toString();
                    int price = Integer.parseInt(edtPrice.getText().toString());
                    int quantity = Integer.parseInt(edtQuantity.getText().toString());
                    int inventory = Integer.parseInt(edtInventory.getText().toString());
                    String imgUri;
                    if (imageUri != null) {
                        imgUri = imageUri.toString();
                    } else {
                        imgUri = sanPhamModel.getImage();
                    }
                    sanPhamModel.setName(name);
                    sanPhamModel.setPrice(price);
                    sanPhamModel.setImage(imgUri);
                    sanPhamModel.setInventory(inventory);
                    sanPhamModel.setQuantity(quantity);
                    Call<Void> call = apiService.putSanPham(sanPhamModel.get_id(), sanPhamModel);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                handleCallData();
                                alertDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }
            }
        });
        Button btnCancel = v1.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void handleCallData() {
        Call<ArrayList<SanPhamModel>> call = apiService.getSanPham();
        call.enqueue(new Callback<ArrayList<SanPhamModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SanPhamModel>> call, Response<ArrayList<SanPhamModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapter = new SanPhamAdapter(MainActivity.this, list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                    adapter.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void UpdateItem(int position) {
                            SanPhamModel sanPhamModel = list.get(position);
                            showDialog(1,sanPhamModel);
                        }
                    });
                    Log.d("API_Response", "Dữ liệu nhận được từ API: " + list.size());
                } else {
                    Toast.makeText(MainActivity.this, "Hết cứu", Toast.LENGTH_SHORT).show();
                    Log.e("onResponse: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SanPhamModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        imgUp.setImageURI(imageUri);
                    }
                }
            }
    );

}