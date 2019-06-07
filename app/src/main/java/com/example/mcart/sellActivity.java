package com.example.mcart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class sellActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText Pro_name,Pro_info,Pro_price,Pro_address;
    Button submit;
    Spinner spinner;
    String pro_name,pro_info,pro_price,pro_address,pro_cat;
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    DatabaseReference reference;
    ImageView prod_img;
    private static int CAMERA_REQUEST_CODE=1;
    Uri imagePath=null;
    FirebaseStorage firebaseStorage;
    Toolbar toolbar;
    String productId;
    String[] appPermissions={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int PERMISSIONS_REQUEST_CODE=1240;
    private static final int GALLERY_REQUEST_CODE=0421;






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST_CODE&&resultCode==RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            prod_img.setImageBitmap(imageBitmap);
            imagePath=getImageUri(getApplicationContext(),imageBitmap);
            if(imagePath==null)
                Toast.makeText(sellActivity.this, "null", Toast.LENGTH_SHORT).show();


        }else if(requestCode==GALLERY_REQUEST_CODE&&resultCode== Activity.RESULT_OK&& data != null && data.getData() != null){

            imagePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                prod_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
       Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
       // BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inScaled = false;
       // Bitmap OutImage = BitmapFactory.decodeResource(inContext.getResources(),inImage, options);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Sell Product");
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                startActivity(new Intent(sellActivity.this,MainActivity.class));
            }
        });
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

        spinner =findViewById(R.id.product_cat);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Calculator");
        categories.add("Books");
        categories.add("Drawfter");
        categories.add("Cycle");
        categories.add("Cooler");
        categories.add("Router");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        final String[] colors = {"Choose from gallery", "open Camera"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndReqestPermissions()) {

                   // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]
                            if(which == 0){
                                //first option clicked, do this...
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent,GALLERY_REQUEST_CODE);

                            }else if(which == 1){
                                //second option clicked, do this...
                                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,CAMERA_REQUEST_CODE);

                            }
                        }
                    });
                    builder.show();
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

                sell_prod(fuser.getUid(),pro_cat,pro_name,pro_info,pro_price,pro_address);
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

    private  void sell_prod(String seller,String pro_cat,String pro_name,String pro_info,String pro_price,String pro_address){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Category",pro_cat);
        hashMap.put("seller",seller);
        hashMap.put("pro_name",pro_name);
        hashMap.put("pro_info",pro_info);
        hashMap.put("pro_price",pro_price);
        hashMap.put("pro_address",pro_address);
        productId = reference.child("sellContent").push().getKey();
        hashMap.put("img_url",productId);
        reference.child("sellContent").child(productId).setValue(hashMap);
        HashMap<String,Object> hashMap1=new HashMap<>();
        hashMap1.put("Product_id",productId);
        reference.child("Users").child(firebaseAuth.getUid()).child("sell_items").child(productId).setValue(hashMap1);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        pro_cat = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + pro_cat, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
