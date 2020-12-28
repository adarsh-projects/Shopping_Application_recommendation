package com.example.trima.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trima.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.trima.prevalent.*;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private EditText fullname, phone, address;
    private TextView choose_image, close, update;
    private CircleImageView user_profile;
    private Button setSecurityQuestion;
    private Uri imageUri;
    private String myurl = "";
    private StorageTask storageTask;
    private StorageReference storageReference;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Image");


        setSecurityQuestion = (Button)findViewById(R.id.security_question);

        fullname = (EditText)findViewById(R.id.setting_fullname);
        phone = (EditText)findViewById(R.id.setting_phone_number);
        address = (EditText)findViewById(R.id.setting_address);

        choose_image = (TextView)findViewById(R.id.setting_change_profile);
        close = (TextView)findViewById(R.id.setting_close);
        update = (TextView)findViewById(R.id.setting_update);

        user_profile = (CircleImageView)findViewById(R.id.setting_user_profile);

        setSecurityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, PasswordResetActivity.class);
                i.putExtra("check", "setting");
                startActivity(i);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    updateOnlyUserInfo();
                }
            }
        });

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);
            }
        });

        userInfoDisplay(user_profile, fullname, phone, address);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null ){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            user_profile.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("UserName", fullname.getText().toString());
        userMap.put("address", address.getText().toString());
        userMap.put("Phone", phone.getText().toString());
        ref.child(prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
        Toast.makeText(SettingActivity.this, "Profile Info update Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSaved() {

        if(TextUtils.isEmpty(fullname.getText().toString())){
            Toast.makeText(this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this, "Phone number is Mandatory", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(this, "Address is Mandatory", Toast.LENGTH_SHORT).show();
        }else if(checker.equals("clicked")){
                uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait, while we are  updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageReference.child(prevalent.CurrentOnlineUser.getPhone() + ".jpg");
            storageTask = fileRef.putFile(imageUri);
            storageTask.continueWithTask(new Continuation() {
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
                        Uri downloadUri = task.getResult();
                        myurl = downloadUri.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("UserName", fullname.getText().toString());
                        userMap.put("address", address.getText().toString());
                        userMap.put("Phone", phone.getText().toString());
                        userMap.put("image", myurl);

                        ref.child(prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        Toast.makeText(SettingActivity.this, "Profile Info update Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }

    }

    private void userInfoDisplay(final CircleImageView user_profile, final EditText fullname, final EditText phone, final EditText address) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(prevalent.CurrentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String name  = snapshot.child("UserName").getValue().toString();
                        String phone1 =  snapshot.child("Phone").getValue().toString();
                        String address1 = snapshot.child("address").getValue().toString();
                        Picasso.get().load(image).into(user_profile);
                        fullname.setText(name);
                        phone.setText(phone1);
                        address.setText(address1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}