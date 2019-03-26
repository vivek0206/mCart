package com.example.mcart;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {
    TextView pinfo,pname,pprice;
    ImageView pimage;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        pinfo=findViewById(R.id.pinfo);
        pname=findViewById(R.id.pname);
        pimage=findViewById(R.id.pimage);
        pprice=findViewById(R.id.pprice);
        Intent intent = getIntent();
        String imageId= intent.getStringExtra("ProductImage");

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        DatabaseReference ref=firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(imageId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(pimage);
            }
        });
        String name= intent.getStringExtra("ProductName");
        String info = intent.getStringExtra("ProductInfo");
        String price = intent.getStringExtra("ProductPrice");
        pname.setText(name);
        pinfo.setText(info);
        pprice.setText(price);

    }
}
