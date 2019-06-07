package com.example.mcart;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {
    TextView pinfo,pname,pprice,sname,shostel,sphone;
    ImageView pimage;
    Button btn_wishlist,btn_call;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    Toolbar toolbar;
    String phonenum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Product");
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                startActivity(new Intent(ProductActivity.this,MainActivity.class));
            }
        });
        pinfo=findViewById(R.id.pinfo);
        pname=findViewById(R.id.pname);
        pimage=findViewById(R.id.pimage);
        pprice=findViewById(R.id.pprice);
        sname=findViewById(R.id.seller_name);
        shostel=findViewById(R.id.seller_hostel);
        sphone=findViewById(R.id.seller_phone);
        btn_wishlist=findViewById(R.id.btn_wishlist);
        btn_call=findViewById(R.id.btn_call);
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
        String sellerId = intent.getStringExtra("Seller");
        final String productId = intent.getStringExtra("ProductId");
        pname.setText(name);
        pinfo.setText("* "+info);
        pprice.setText("\u20B9"+price);

        DatabaseReference seller=firebaseDatabase.getReference().child("Users").child(sellerId);
        seller.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // Do stuff
                    userprofile userprofile=dataSnapshot.getValue(userprofile.class);
                    sname.setText(userprofile.getUsername());
                    shostel.setText(userprofile.getHostel());
                    sphone.setText(userprofile.getContact());
                    phonenum=userprofile.getContact();

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });






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
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone= phonenum.trim();
                Intent dialIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
                startActivity(dialIntent);
            }
        });

    }
}
