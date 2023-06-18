package com.Guruprasad.innovativesolutions.Activities.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding ;
    FirebaseAuth auth;
    FirebaseDatabase database ;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");


        Intent intent = getIntent();
        String mobile = intent.getStringExtra("phoneno");
        binding.etPhoneno.setText("9697981736");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname  = binding.etFullname.getText().toString();
                String phoneno = binding.etPhoneno.getText().toString();
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                String confirmpassword  = binding.etConfirmPassword.getText().toString();
                String address  = binding.etAddress.getText().toString();

                if (fullname.isEmpty())
                {
                    binding.etFullname.setError("Full name is required");
                    return;
                }
                if (phoneno.isEmpty())
                {
                    binding.etPhoneno.setError("Phone no is required");
                    return;
                }
                if (email.isEmpty())
                {
                    binding.etEmail.setError("Email is required");
                    return;
                }
                if (password.isEmpty())
                {
                    binding.etPassword.setError("Password is required");
                    return;
                }
                if (password.length()<8)
                {
                    Constants.error(SignupActivity.this,"Password must be gerater than 8 characters");
                    return;
                }
                if (confirmpassword.isEmpty())
                {
                    binding.etConfirmPassword.setError("Please Re-type the password for confirmation");
                    return;
                }
                if (!confirmpassword.equals(password))
                {
                    Constants.error(SignupActivity.this,"Password does not match recheck the password ");
                    return;
                }
                if (address.isEmpty())
                {
                    binding.etAddress.setError("Address is required");
                    return;
                }
                if (address.length()<10)
                {
                    Constants.error(SignupActivity.this,"Address must be greater than 10 characters");
                    return;
                }




            }
        });


    }

    public void create_account()
    {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}