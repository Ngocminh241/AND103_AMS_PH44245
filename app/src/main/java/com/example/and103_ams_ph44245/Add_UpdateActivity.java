package com.example.and103_ams_ph44245;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.and103_ams_ph44245.Model.Food;
import com.example.and103_ams_ph44245.service.APIService;
import com.google.logging.type.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Add_UpdateActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText edt_name, edt_price, edt_quantity, edt_describe;
    ImageView img_avatar;
    Button btn_add;
    File file;
    private HttpRequest httpRequest;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        anhXa();
        clickEvent();
        callAPI();
    }
    private void callAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }
    private void anhXa() {
        // ánh xạ
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ADD FOOD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        img_avatar = findViewById(R.id.img_avatar);
        edt_name = findViewById(R.id.edt_name);
        edt_price = findViewById(R.id.edt_price);
        edt_quantity = findViewById(R.id.edt_quantity);
        edt_describe = findViewById(R.id.edt_describe);
        btn_add = findViewById(R.id.btn_add);
    }
    private void clickEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();;
            }
        });
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_name.getText().toString().trim();
                String price = edt_price.getText().toString().trim();
                String quantity = edt_quantity.getText().toString().trim();
                String describe = edt_describe.getText().toString().trim();

                if (!name.isEmpty() || !price.isEmpty() || !quantity.isEmpty() || !describe.isEmpty()) {
                    RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                    RequestBody _price = RequestBody.create(MediaType.parse("multipart/form-data"), price);
                    RequestBody _quantity = RequestBody.create(MediaType.parse("multipart/form-data"), quantity);
                    RequestBody _describe = RequestBody.create(MediaType.parse("multipart/form-data"), describe);
                    MultipartBody.Part multipartBody;

                    if (file != null) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                        multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                    } else {
                        multipartBody = null;
                    }
                    Call<Response<Food>> call = apiService.addFood(multipartBody, _name, _price, _quantity, _describe);

                    call.enqueue(new Callback<Response<Food>>() {
                        @Override
                        public void onResponse(Call<Response<Food>> call, Response<Response<Food>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(Add_UpdateActivity.this, "Add success", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<Food>> call, Throwable t) {
                            Log.d(">>> ADD Success", "onFailure" + t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(Add_UpdateActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImage.launch(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                Intent data = o.getData();
                Uri imagePath = data.getData();
                file = createFileFromUri(imagePath, "avatar");
                Glide.with(Add_UpdateActivity.this)
                        .load(file)
//                        .thumbnail(Glide.with(Add_UpdateActivity.this).load(R.mipmap.loading))
                        .centerCrop()
//                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(img_avatar);

            }
        }
    });
    private File createFileFromUri(Uri path, String name) {
        File _file = new File(Add_UpdateActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Add_UpdateActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}