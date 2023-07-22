package com.Guruprasad.innovativesolutions.Activities.ui.home.Contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivityContactBinding;

public class ContactActivity extends AppCompatActivity {

    ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name  = " Hi My name is guruprasadd ";
                share(name);


            }
        });


    }

    private void share(String name) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT , name);
        startActivity(Intent.createChooser(intent,"Share Via"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}