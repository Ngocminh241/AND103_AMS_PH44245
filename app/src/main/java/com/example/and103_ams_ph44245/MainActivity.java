package com.example.and103_ams_ph44245;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.and103_ams_ph44245.Adapter.AdapterFood;
import com.example.and103_ams_ph44245.Model.Food;
import com.example.and103_ams_ph44245.service.APIService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ArrayList<Food> list = new ArrayList<>();
    RecyclerView rcv_food;
    AdapterFood adapterFood;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getListCity();
        rcv_food = findViewById(R.id.rcv_food);
        //
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trang Chủ");
        getSupportActionBar().setSubtitle("Quản lý món ăn");
        //
        //
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<ArrayList<Food>> call = apiService.getFood();

        call.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapterFood = new AdapterFood(getApplicationContext(), list);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rcv_food.setLayoutManager(linearLayoutManager);
                    rcv_food.setAdapter(adapterFood);
                    adapterFood.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_them) {
            startActivity(new Intent(MainActivity.this, Add_UpdateActivity.class));
        } else if (item.getItemId() == R.id.item_dangxuat) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, Splashscreenactivity.class));
            Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}