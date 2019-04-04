package com.example.mcart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class sellActivity extends AppCompatActivity {
    EditText Pro_name,Pro_info,Pro_price,Pro_address;
    Button submit;
    String pro_name,pro_info,pro_price,pro_address;
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    DatabaseReference reference;
    ImageView prod_img;
    private static int CAMERA_REQUEST_CODE=1;
    Uri imagePath=null;
    FirebaseStorage firebaseStorage;
    String productId;
    String[] appPermissions={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int PERMISSIONS_REQUEST_CODE=1240;




   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK&&data.getData()!=null){
            imagePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                prod_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST_CODE&&resultCode==RESULT_OK) {
          // imagePath = data.getData();

            //prod_img.setImageURI(imagePath);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            prod_img.setImageBitmap(imageBitmap);
            imagePath=getImageUri(getApplicationContext(),imageBitmap);

            if(imagePath==null)
                Toast.makeText(sellActivity.this, "null", Toast.LENGTH_SHORT).show();
        }


    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Pro_name=findViewById(R.id.product_name);
        Pro_info=findViewById(R.id.Product_info);
        Pro_price=findViewById(R.id.Product_price);
        Pro_address=findViewById(R.id.Address);
        prod_img=findViewById(R.id.product_image);
        submit=findViewById(R.id.pro_submit);
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference storageReference=firebaseStorage.getReference();
        prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndReqestPermissions()) {
                    //Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent,CAMERA_REQUEST_CODE);
                    // intent.setType("image/*");
                    // intent.setAction(Intent.ACTION_GET_CONTENT);
                    // startActivityForResult(Intent.createChooser(intent,"Select Images"),PICK_IMAGE);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_name=Pro_name.getText().toString().trim();
                pro_info=Pro_info.getText().toString().trim();
                pro_price=Pro_price.getText().toString().trim();
                pro_address=Pro_address.getText().toString().trim();
                sell_prod(fuser.getUid(),pro_name,pro_info,pro_price,pro_address);
                StorageReference imageReference=storageReference.child(productId);
                UploadTask uploadTask=imageReference.putFile(imagePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(sellActivity.this, "upload Fail", Toast.LENGTH_SHORT).show();
                    }
                });
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(sellActivity.this, "upload success", Toast.LENGTH_SHORT).show();

                    }
                });
                finish();

            }
        });


    }
    private  void sell_prod(String seller,String pro_name,String pro_info,String pro_price,String pro_address){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("seller",seller);
        hashMap.put("pro_name",pro_name);
        hashMap.put("pro_info",pro_info);
        hashMap.put("pro_price",pro_price);
        hashMap.put("pro_address",pro_address);
        productId = reference.child("sellContent").push().getKey();
        hashMap.put("img_url",productId);
        reference.child("sellContent").child(productId).setValue(hashMap);

        Toast.makeText(sellActivity.this,"Thank you",Toast.LENGTH_SHORT).show();
    }
    public  boolean checkAndReqestPermissions(){
        List<String> listPermissionsNeeded=new ArrayList<>();
        for(String perm:appPermissions)
        {
            if(ContextCompat.checkSelfPermission(this,perm)!= PackageManager.PERMISSION_GRANTED)
            {
                listPermissionsNeeded.add(perm);
            }
        }
        if(!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }

}
