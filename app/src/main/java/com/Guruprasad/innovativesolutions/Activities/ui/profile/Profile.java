package com.Guruprasad.innovativesolutions.Activities.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.Guruprasad.innovativesolutions.Activities.Authentication.LoginActivity;
import com.Guruprasad.innovativesolutions.Constants;
import com.Guruprasad.innovativesolutions.Model.RegisterModel;
import com.Guruprasad.innovativesolutions.R;
import com.Guruprasad.innovativesolutions.databinding.FragmentHomeBinding;
import com.Guruprasad.innovativesolutions.databinding.FragmentProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profile extends Fragment {

    FragmentProfileBinding binding ;
    FirebaseDatabase database ;
    DatabaseReference reference ;
    FirebaseStorage storage ;
    StorageReference storageReference ;

    FirebaseAuth auth ;
    Bitmap bitmap ;
    private String dbemail ;
    private String dbpassword;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        String userid = auth.getCurrentUser().getUid();

        ProgressDialog pd = Constants.progress_dialog(getContext(),"Please Wait","Fetching Data...");
        pd.show();

        reference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pd.dismiss();
                RegisterModel model = snapshot.getValue(RegisterModel.class);
                if (model!=null)
                {
                    pd.dismiss();
                    binding.name.setText(model.getFullName());
                    binding.address.setText(model.getAddress());
                    binding.email.setText(model.getEmail());
                    binding.phone.setText(model.getPhoneNo());
                    binding.userid.setText(model.getUserId());
                    Glide.with(getContext()).load(model.getProfile_pic()).placeholder(R.drawable.logo_1).into(binding.profilePic);

                    dbemail = model.getEmail();
                    dbpassword = model.getPassword();



                }
                else
                {
                    pd.dismiss();
                    Constants.error(getContext(),"Failed to fetch data , Please try again");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Constants.error(getContext(),"Error : "+error.getMessage());
            }
        });

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadpic();
            }
        });

        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyemail();
            }
        });

        return root;
    }

    private void verifyemail() {
        final EditText input = new EditText(getContext());
        new MaterialAlertDialogBuilder(getContext(),R.style.RoundShapeTheme)
                .setTitle("Email Verification")
                .setMessage("Enter the Registered Email Address")
                .setCancelable(false)
                .setIcon(R.drawable.logo_1)
                .setView(input)
                .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = input.getText().toString();
                        if (email.equals(dbemail))
                        {
                            verifypassword();
                        }
                        else
                        {
                            Constants.error(getContext(),"Email is not valid");
                        }
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    private void verifypassword() {

        final EditText pass = new EditText(getContext());
        new MaterialAlertDialogBuilder(getContext(),R.style.RoundShapeTheme)
                .setTitle("Password Verification")
                .setMessage("Enter the Last Registered Password")
                .setCancelable(false)
                .setIcon(R.drawable.logo_1)
                .setView(pass)
                .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String password  = pass.getText().toString();

                        if (password.equals(dbpassword))
                        {
                            changpassword(dbemail);
                        }
                        else
                        {
                            Constants.error(getContext(),"Password is not valid");

                        }
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    private void changpassword( String email) {

        ProgressDialog pd = Constants.progress_dialog(getContext(),"Please Wait","Sending password reset link");
        pd.show();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Constants.success(getContext(),"Password reset link send to the registered email address");
                    pd.dismiss();
                }
                else
                {
                    Constants.error(getContext(),"Error : "+task.getException().getMessage());
                    pd.dismiss();
                }
            }
        });


    }

    private void uploadpic() {
        openfilemanager();
    }

    private void openfilemanager() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select the Image."),101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && resultCode== RESULT_OK)
        {
            if (data!=null)
            {
                Uri image = data.getData();

                ProgressDialog pd = Constants.progress_dialog(getContext(),"Please Wait" , "Uploading Profile...");
                pd.show();

                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(image);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.profilePic.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Constants.error(getContext(),"Error : "+e.getMessage());
                }

                final StorageReference reference1 = storage.getReference("Profile").child(auth.getCurrentUser().getUid());
                        reference1.putFile(image)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("profile_pic").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful())
                                                {
                                                    pd.dismiss();
                                                    Constants.success(getContext(),"Profile Updated Successfully");
                                                }
                                                else
                                                {
                                                    pd.dismiss();
                                                    Constants.error(getContext(),"Error : "+task.getException().getMessage());
                                                }
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Constants.error(getContext(),"Error : "+e.getMessage());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Constants.error(getContext(),"Error : "+e.getMessage());
                            }
                        });
            }
        }
    }
}