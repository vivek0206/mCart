package com.example.mcart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText username,hostel,room,contactno;
    Button upload;
    ImageView profilePic;
    FirebaseAuth firebaseAuth;
    private static int PICK_IMAGE=123;
    Uri imagPath;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imagPath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagPath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        username=findViewById(R.id.u_name);
        hostel=findViewById(R.id.u_Hostel);
        room=findViewById(R.id.u_Room);
        contactno=findViewById(R.id.u_phone);
        profilePic=findViewById(R.id.u_Profile);
        upload=findViewById(R.id.btn_upload);
        firebaseAuth=FirebaseAuth.getInstance();
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Images"),PICK_IMAGE);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name=username.getText().toString().trim();
                String Hostel=hostel.getText().toString().trim();
                String Room=room.getText().toString().trim();
                String Contact=contactno.getText().toString().trim();
                String Email=firebaseAuth.getCurrentUser().getEmail();

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
                userprofile userprofile=new userprofile(Name,Email,Hostel,Room,Contact);
                reference.setValue(userprofile);
                firebaseStorage= FirebaseStorage.getInstance();
                final StorageReference storageReference=firebaseStorage.getReference();

                if(imagPath!=null){
                    StorageReference imageReference=storageReference.child(firebaseAuth.getUid()).child("Profile Pic");//user id/Image/progile pic
                    UploadTask uploadTask=imageReference.putFile(imagPath);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateProfileActivity.this, "upload Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UpdateProfileActivity.this, "upload success", Toast.LENGTH_SHORT).show();

                        }
                    });
                    finish();
                }else{
                    Toast.makeText(UpdateProfileActivity.this,"null",Toast.LENGTH_SHORT).show();
                }




            }
        });


    }
}
