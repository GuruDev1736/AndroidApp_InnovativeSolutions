package com.Guruprasad.innovativesolutions.Activities.ui.home.Contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;

import com.Guruprasad.innovativesolutions.Activities.ui.home.RealTimeDevelopement.RealTimeDevelopment;
import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.ActivityContactBinding;

public class ContactActivity extends AppCompatActivity {

    ActivityContactBinding binding;
    private static final String CONTACT_NAME = "Innovative Solutions";
    private static final String PHONE_NO = "+919697981736";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name  = "Innovative Solutions Email : InnovativeSolutions@gmail.com "+
                                "Innovative Solutions Phone No : 9697981736 "+
                                "Innovative Solutions Website : InnovativeSolutions.com ";
                share(name);
            }
        });


        binding.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.info(ContactActivity.this,"Please Wait Loading Chat");
                openWhatsAppChat(PHONE_NO,"Hi, I want to build a project");
            }
        });
    }

    private void share(String name) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT , name);
        startActivity(Intent.createChooser(intent,"Share Via"));

    }

    private void openWhatsAppChat(String phoneNumber, String message) {
        try {
            Uri uri = Uri.parse("https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Constants.error(ContactActivity.this,"Whatsapp Not Installed");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}