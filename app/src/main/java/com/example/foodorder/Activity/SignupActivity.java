package com.example.foodorder.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.foodorder.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends BaseActivity {
ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }
    private void setVariable(){
        binding.returnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            private void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onComplete: ");
                    Toast.makeText(SignupActivity.this, "SignUp is successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                } else {
                    int i = Log.i(TAG,"Flailed"+task.getException());
                    Toast.makeText(SignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {
                String email=binding.signupEmail.getText().toString();
                String password=binding.signupPass.getText().toString();

                if (password.length()<6){
                    Toast.makeText(SignupActivity.this,"Your password must be 6 characters",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Toast.makeText(SignupActivity.this,"Your password must be 6 characters",Toast.LENGTH_SHORT).show();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, this::onComplete);
                }

            }
        });
    }
}