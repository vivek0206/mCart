package com.example.mcart;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {
    TextView pinfo,pname,pprice;
    ImageView pimage;
    Button btn_wishlist,btn_buy;
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
        btn_wishlist=findViewById(R.id.btn_wishlist);
        btn_buy=findViewById(R.id.btn_buy);
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
        final String productId = intent.getStringExtra("ProductId");
        pname.setText(name);
        pinfo.setText("* "+info);
        pprice.setText("Rs "+price);
        btn_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("Product_id",productId);

                reference.child("Users").child(firebaseAuth.getUid()).child("wishlist").child(productId).setValue(hashMap);

                Toast.makeText(ProductActivity.this,"added to wishlist",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
