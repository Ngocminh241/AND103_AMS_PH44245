package com.example.and103_ams_ph44245;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.and103_ams_ph44245.Dialog.LoadingDialog;
import com.example.and103_ams_ph44245.Model.User;
import com.example.and103_ams_ph44245.service.HttpRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import com.example.and103_ams_ph44245.Model.Response;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText edt_username;
    EditText edt_password;
    Button btn_dangnhap;
    TextView tv_quenmatkhau;
    LoadingDialog loadingDialog;
    HttpRequest httpRequest = new HttpRequest();
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // ánh xạ
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        tv_quenmatkhau = findViewById(R.id.tv_quenmatkhau);
        btn_dangnhap = findViewById(R.id.btn_login);
        loadingDialog = new LoadingDialog(this);
        //

//        String username = getIntent().getStringExtra("username");
        edt_username.setText(getIntent().getStringExtra("username"));
        edt_password.setText(getIntent().getStringExtra("password"));
        // sự kiện click
        tv_quenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                intent.putExtra("email", edt_username.getText().toString().trim());
                startActivity(intent);
            }
        });
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogin();
            }
        });
        findViewById(R.id.tv_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
    private void onClickLogin() {
        String _username = edt_username.getText().toString().trim();
        String _password = edt_password.getText().toString().trim();
        if (!_username.isEmpty() || !_password.isEmpty()) {
            loadingDialog.show();
            User user = new User();
            user.setUsername(_username);
            user.setPassword(_password);
            httpRequest.callAPI().login(user).enqueue(responseUser);
//            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        // Lấy thông tin tài khoản mới vừa đăng nhập
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            user.getIdToken(true)
//                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
//                                            if (task.isSuccessful()) {
//                                                String token = task.getResult().getToken();
//                                                loadingDialog.cancel();
//                                                Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                                                finishAffinity();
//                                                Intent intent = new Intent(Login.this, MainActivity.class);
//                                                intent.putExtra("token", token);
//                                                startActivity(intent);
//                                                // Token lấy được sẽ được sử dụng ở đây
//                                                Log.d("TOKEN", token);
//                                            } else {
//                                                // Xử lý khi không lấy được token
//                                                Log.e("TOKEN", "Failed to get token");
//                                            }
//                                        }
//                                    });
//                        }
//
//                    } else  {
//                        Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
//                        loadingDialog.cancel();
//                        Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        } else {
            Toast.makeText(this, "Vui lòng nhập thông tin đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() ==200) {
                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("INFO",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("id", response.body().getData().get_id());
                    editor.apply();
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            } else {
                Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                loadingDialog.cancel();
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            loadingDialog.cancel();
            t.getMessage();
        }
    };
}