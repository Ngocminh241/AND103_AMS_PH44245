package com.example.and103_ams_ph44245;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText edt_email;
    Button btn_forgotpw;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        // ánh xạ
        mAuth = FirebaseAuth.getInstance();
        edt_email = findViewById(R.id.edt_username);
        btn_forgotpw = findViewById(R.id.btn_forgotpw);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        edt_email.setText(getIntent().getStringExtra("email"));
        // sự kiện click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();;
            }
        });
        btn_forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickForgotPW();
            }
        });
    }
    private void OnClickForgotPW() {
        String email = edt_email.getText().toString().trim();
        if (!email.isEmpty()) {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Vui lòng kiểm tra hộp thư để cập nhật lại mật khẩu", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Lỗi gửi mail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng nhập Email của bạn", Toast.LENGTH_SHORT).show();
        }
    }
}