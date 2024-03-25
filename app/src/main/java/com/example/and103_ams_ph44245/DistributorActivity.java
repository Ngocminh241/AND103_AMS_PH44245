package com.example.and103_ams_ph44245;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.and103_ams_ph44245.Adapter.DistributorAdapter;
import com.example.and103_ams_ph44245.databinding.ActivityDistributorBinding;
import com.example.and103_ams_ph44245.databinding.DialogAddBinding;
import com.example.and103_ams_ph44245.Model.Distributor;
import com.example.and103_ams_ph44245.Model.Response;
import com.example.and103_ams_ph44245.service.HttpRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class DistributorActivity extends AppCompatActivity implements DistributorAdapter.DistributorClick {
    private ActivityDistributorBinding binding;
    private HttpRequest httpRequest;
    private ArrayList<Distributor> list = new ArrayList<>();
    private DistributorAdapter adapter;
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDistributorBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

//        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Distributor");
        getSupportActionBar().setSubtitle("Distributor Management");

        fetchAPI();
        userListener();
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
            showDialogAdd();
        }
        else if (item.getItemId() == R.id.item_home) {
            startActivity(new Intent(DistributorActivity.this, MainActivity.class));
            Toast.makeText(DistributorActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.item_distributor) {
            startActivity(new Intent(DistributorActivity.this, DistributorActivity.class));
            Toast.makeText(DistributorActivity.this, "Nhà phân phối", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.item_dangxuat) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finishAffinity();
            startActivity(new Intent(DistributorActivity.this, Login.class));
            Toast.makeText(DistributorActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void fetchAPI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        httpRequest = new HttpRequest();
        httpRequest.callAPI()
                .getDistributor()
                .enqueue(getDistributorAPI);

    }

    private void userListener() {
        binding.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String key = binding.edSearch.getText().toString().trim();
                    httpRequest.callAPI()
                            .searchDistributor(key)
                            .enqueue(getDistributorAPI);
                    Log.d(TAG, "onEditorAction: " + key);
                    return true;
                }
                return false;
            }
        });

    }
    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();
        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding1.etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(DistributorActivity.this, "you must enter name", Toast.LENGTH_SHORT).show();
                }   else {
                    Distributor distributor = new Distributor();
                    distributor.setName(name);
                    httpRequest.callAPI()
                            .addDistributor(distributor)
                            .enqueue(responseDistributorAPI);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }




    private void getData() {


        adapter = new DistributorAdapter(list, this,this );
        binding.rcvDistributor.setAdapter(adapter);
        progressDialog.dismiss();
    }


    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = response.body().getData();
                    getData();
                    Log.d(TAG, "onResponse: "+ list.size());
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.e(TAG, "onFailure: "+ t.getMessage() );
        }


    };


    Callback<Response<Distributor>> responseDistributorAPI  = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    httpRequest.callAPI()
                            .getDistributor()
                            .enqueue(getDistributorAPI);
                    Toast.makeText(DistributorActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {
            Log.e(TAG, "onFailure: "+t.getMessage() );
        }
    };

    private void showDialogEdit(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();

        binding1.etName.setText(distributor.getName());

        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = distributor.getName();

                if (name.isEmpty()) {
                    Toast.makeText(DistributorActivity.this, "you must enter name", Toast.LENGTH_SHORT).show();
                }   else {

                    Distributor distributor1 = new Distributor();
                    distributor1.setName(binding1.etName.getText().toString().trim());
                    httpRequest.callAPI()
                            .updateDistributor(distributor.get_id(),distributor1)
                            .enqueue(responseDistributorAPI);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
    @Override
    public void delete(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("yes", (dialog, which) -> {
            httpRequest.callAPI()
                    .deleteDistributor(distributor.get_id())
                    .enqueue(responseDistributorAPI);
        });
        builder.setNegativeButton("no", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();


    }

    @Override
    public void edit(Distributor distributor) {
        showDialogEdit(distributor);
    }
}

