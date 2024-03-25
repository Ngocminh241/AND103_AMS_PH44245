package com.example.and103_ams_ph44245;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.and103_ams_ph44245.Adapter.AdapterFruit;
import com.example.and103_ams_ph44245.Model.Fruit;
import com.example.and103_ams_ph44245.Model.Response;
import com.example.and103_ams_ph44245.R;
import com.example.and103_ams_ph44245.service.HttpRequest;
import com.example.and103_ams_ph44245.service.OnClickListen;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import com.example.and103_ams_ph44245.databinding.DialogEditBinding;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    DialogEditBinding binding1;
    FirebaseAuth mAuth;
    ArrayList<Fruit> list = new ArrayList<>();
    RecyclerView rcv;
    AdapterFruit adapterFruit;
    HttpRequest httpRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    String token;
    SearchView searchView;
    ArrayList<Fruit> listSeacrch = new ArrayList<>();
    TextView tv_list_rong;
    Spinner spn_sapxep;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        token = getIntent().getStringExtra("token");
        sharedPreferences = getSharedPreferences("INFO",MODE_PRIVATE);

        token = sharedPreferences.getString("token","");
//        getListCity();
        tv_list_rong = findViewById(R.id.list_rong);
        rcv = findViewById(R.id.rcv_food);
        searchView = findViewById(R.id.search_view);
        spn_sapxep = findViewById(R.id.spn_sapxep);
        httpRequest = new HttpRequest();
//        progressBar = findViewById(R.id.progress);
        swipeRefreshLayout = findViewById(R.id.sf_data);
        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);
        //
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");
        getSupportActionBar().setSubtitle("Fruits Management");
        //
        handleCallDataFruits();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
        ArrayList<String> listSx = new ArrayList<>();
        listSx.add("-- Mặc định --");
        listSx.add("Tăng dần");
        listSx.add("Giàm dần");
        ArrayAdapter ad = new ArrayAdapter(
                MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                listSx);
        spn_sapxep.setAdapter(ad);
        spn_sapxep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    handleCallDataFruits();
                } else if (i == 1) {
                    SapXepList(0);
                } else {
                    SapXepList(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filterList(String text) {

        if (!text.equals("")) {
            listSeacrch.clear();
            httpRequest.callAPI().searchFruitByName(text).enqueue(new Callback<Response<ArrayList<Fruit>>>() {
                @Override
                public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listSeacrch = response.body().getData();
                            getData(listSeacrch);
                        }
                    }
                }
                @Override
                public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

                }
            });
            httpRequest.callAPI().searchFruitById(text).enqueue(new Callback<Response<ArrayList<Fruit>>>() {
                @Override
                public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listSeacrch = response.body().getData();
                            getData(listSeacrch);
                        }
                    }
                }
                @Override
                public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

                }
            });
            httpRequest.callAPI().searchFruitByPrice(text).enqueue(new Callback<Response<ArrayList<Fruit>>>() {
                @Override
                public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listSeacrch = response.body().getData();
                            getData(listSeacrch);
                        }
                    }
                }
                @Override
                public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

                }
            });
        } else {
            handleCallDataFruits();
        }
    }
    private void SapXepList(int sapxep) {
        listSeacrch.clear();
        httpRequest.callAPI().getFruits().enqueue(new Callback<Response<ArrayList<Fruit>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listSeacrch = response.body().getData();
                        if (sapxep == 0) {
                            sapXepTang();
                        } else {
                            sapXepGiam();
                        }
                        getData(listSeacrch);
                    }
                }
            }
            @Override
            public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

            }
        });
    }public void sapXepGiam() {
        Collections.sort(listSeacrch, new Comparator<Fruit>() {
            @Override
            public int compare(Fruit fruit, Fruit t1) {
                if (Double.valueOf(fruit.getPrice()) > Double.valueOf(t1.getPrice())) {
                    return -1;
                } else {
                    if (Double.valueOf(fruit.getPrice()) == Double.valueOf(t1.getPrice()) ) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            }
        });
    }
    public void sapXepTang() {
        Collections.sort(listSeacrch, new Comparator<Fruit>() {
            @Override
            public int compare(Fruit fruit, Fruit t1) {
                if (Double.valueOf(fruit.getPrice()) > Double.valueOf(t1.getPrice())) {
                    return 1;
                } else {
                    if (Double.valueOf(fruit.getPrice()) == Double.valueOf(t1.getPrice()) ) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
    }
    private void handleCallDataFruits() {
        swipeRefreshLayout.setRefreshing(true);
        httpRequest.callAPI().getListFruit("Bearer " + token).enqueue(new Callback<Response<ArrayList<Fruit>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() ==200) {
                        list = response.body().getData();
                        getData(list);
//                    Toast.makeText(HomeActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

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
            Intent intent = new Intent(MainActivity.this, AddUpdate.class);
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
//                bundle.putSerializable("fruit", fruit);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.item_home) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            Toast.makeText(MainActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.item_distributor) {
            startActivity(new Intent(MainActivity.this, DistributorActivity.class));
            Toast.makeText(MainActivity.this, "Nhà phân phối", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.item_dangxuat) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, Login.class));
            Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getData(ArrayList<Fruit> listF) {
        adapterFruit = new AdapterFruit(MainActivity.this, listF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(adapterFruit);
        adapterFruit.setOnClickListen(new OnClickListen() {
            @Override
            public void updateItem(Fruit fruit) {
                Intent intent = new Intent(MainActivity.this, AddUpdate.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", fruit.get_id());
                bundle.putString("name", fruit.getName());
                if (fruit.getImage().size()!=0){
                    bundle.putString("image", fruit.getImage().get(0));
                }else {
                    bundle.putString("image", String.valueOf(R.drawable.baseline_broken_image_24));
                }
//                bundle.putString("image", fruit.getImage().get(0));
                bundle.putString("quantity", fruit.getQuantity());
                bundle.putString("price", fruit.getPrice());
                bundle.putString("status", fruit.getStatus());
                bundle.putString("description", fruit.getDescription());
                bundle.putString("id_distributor", fruit.getDistributor().get_id());
                bundle.putInt("type", 0);
//                bundle.putSerializable("fruit", fruit);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void deleteItem(Fruit fruit) {
                Dialod_Delete(fruit);
            }
            @Override
            public void MoreItem(Fruit fruit) {
                showDialogMore(fruit);
            }
        });
//        progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        if (listF.size() == 0) {
            tv_list_rong.setVisibility(View.VISIBLE);
        } else {
            tv_list_rong.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        handleCallDataFruits();
    }

    @Override
    public void onRefresh() {
        handleCallDataFruits();
    }
    private void showDialogMore(Fruit fruit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        binding1 = DialogEditBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();

//        ds_image = new ArrayList<>();
//        configSpinner();

        binding1.edtTen.setText(fruit.getName());
        binding1.edtSoluong.setText(fruit.getQuantity());
        binding1.edtGia.setText(fruit.getPrice());
//        binding1.edStatus.setText(fruit.getStatus());
        binding1.edtMota.setText(fruit.getDescription());
//        binding1.spnNhaphanphoi.setSelection(fruit.getDistributor());
//        binding1.spnTrangthai.setSelection(Integer.parseInt(fruit.getStatus()));

        if (fruit.getImage().size()!=0){
            String url  = fruit.getImage().get(0);
            String newUrl = url.replace("localhost", "10.24.8.202");

            Glide.with(MainActivity.this)
                    .load(newUrl)
                    .thumbnail(Glide.with(MainActivity.this).load(R.drawable.baseline_broken_image_24))
                    .into(binding1.imgHinhanh);
//            Log.d("321321", "onBindViewHolder: "+fruit.get_id().getImage().get(0));
        }else {
//            String url  = fruit.getImage().get(0);
//            String newUrl = url.replace("localhost", "192.168.1.65");
//            Glide.with(this)
//                    .load(newUrl)
//                    .thumbnail(Glide.with(this).load(R.drawable.baseline_broken_image_24))
//                    .into(binding1.avatar);
        }
        alertDialog.show();
    }
    private void Dialod_Delete(Fruit fruit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
        builder.setIcon(R.drawable.warning).setTitle("Cảnh Báo");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                httpRequest.callAPI().deleteFruit(fruit.get_id()).enqueue(new Callback<Response<Void>>() {
                    @Override
                    public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                handleCallDataFruits();
                                adapterFruit.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Void>> call, Throwable t) {

                    }
                });
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }
}