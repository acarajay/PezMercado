package com.example.pezmercado;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pezmercado.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, passwordEditText, addressEditText;
    private EditText sexEditText, ageEditText, emailEditText;
   // private EditText phoneEditText;
    private TextView profileChangeTextBtn, closetextBtn, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicturesRef;
    private String checker = "";
//    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

      //  textView.setText("Hello World!");

        //storageProfilePicturesRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        passwordEditText = (EditText) findViewById(R.id.settings_password);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        sexEditText = (EditText) findViewById(R.id.settings_sex);
        ageEditText = (EditText) findViewById(R.id.settings_age);
        emailEditText =(EditText) findViewById(R.id.settings_email_ad);
       // phoneEditText = (EditText) findViewById(R.id.settings_phone);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closetextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings);

        userInfoDisplay(profileImageView, fullNameEditText, passwordEditText, addressEditText, sexEditText, ageEditText, emailEditText );
        //, phoneEditText

        closetextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    updateOnlyuserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checker = "clicked";

                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });
    }

    private void updateOnlyuserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("age",ageEditText.getText().toString());
        userMap.put("sex",sexEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("password",passwordEditText.getText().toString());
        userMap.put("email",emailEditText.getText().toString());
       // userMap.put("phone",phoneEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this, HomeActivity.class ));
        Toast.makeText(SettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);


        }else{
            Toast.makeText(this, "Error, Try to upload photo again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(fullNameEditText.getText().toString())){
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Address is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(passwordEditText.getText().toString())){
            Toast.makeText(this, "Password is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(sexEditText.getText().toString())){
            Toast.makeText(this, "Sex section is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(ageEditText.getText().toString())){
            Toast.makeText(this, "Age is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(emailEditText.getText().toString())){
            Toast.makeText(this, "Email address is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(checker.equals("clicked")){
            //Toast.makeText(this, "Uploading Image", Toast.LENGTH_SHORT).show();
            uploadImage();
        }
    }

    private void uploadImage() {
        //final ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setTitle("Update Profile");
        //progressDialog.setMessage("Please wait, while we are updating your account information.");
        //progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.show();
        final AlertDialog alertDialog = new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        if(imageUri != null){
            final StorageReference fileRef = storageProfilePicturesRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name",fullNameEditText.getText().toString());
                        userMap.put("age",ageEditText.getText().toString());
                        userMap.put("sex",sexEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("password",passwordEditText.getText().toString());
                        userMap.put("email",emailEditText.getText().toString());
                       // userMap.put("phone",phoneEditText.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                       //progressDialog.dismiss();
                       alertDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class ));
                        Toast.makeText(SettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                        finish();

                    }else{
                       // progressDialog.dismiss();
                        alertDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    // , final EditText phoneEditText
    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText passwordEditText, final EditText addressEditText, final EditText sexEditText, final EditText ageEditText, final EditText emailEditText){
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   if(dataSnapshot.child("image").exists()){
                       String image = dataSnapshot.child("image").getValue().toString();
                       String name = dataSnapshot.child("name").getValue().toString();
                       String password = dataSnapshot.child("password").getValue().toString();
                       String address = dataSnapshot.child("address").getValue().toString();
                       String age = dataSnapshot.child("age").getValue().toString();
                       String sex = dataSnapshot.child("sex").getValue().toString();
                       String email = dataSnapshot.child("email").getValue().toString();
                      // String phone = dataSnapshot.child("phone").getValue().toString();

                       Picasso.get().load(image).into(profileImageView);
                       fullNameEditText.setText(name);
                       passwordEditText.setText(password);
                       addressEditText.setText(address);
                       sexEditText.setText(sex);
                       ageEditText.setText(age);
                       emailEditText.setText(email);
                       //phoneEditText.setText(phone);

                   }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
