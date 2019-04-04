package com.example.mcart;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Pro_content> Pro_content;
    wishlistAdapter adapter;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        setTitle("WishList");
        recyclerView=findViewById(R.id.rcylrView_wishlist);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        reference= FirebaseDatabase.getInstance().getReference("Users").child("wishlist");
        Pro_content =new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wishlist_Id();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void wishlist_Id(){
        //Pro_content =new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid()).child("wishlist");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pro_content.clear();
                if(!dataSnapshot.equals("")){
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    // Toast.makeText(WishlistActivity.this,"start",Toast.LENGTH_SHORT).show();
                    wishList_prod Chat = snapshot.getValue(wishList_prod.class);
                    String prod_id = Chat.getProduct_id();
                    getProd(prod_id);
                }
                }else{
                        Toast.makeText(WishlistActivity.this, "No items", Toast.LENGTH_SHORT).show();
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void getProd(final String product_id){
        if(product_id!=null) {
            firebaseAuth = FirebaseAuth.getInstance();
            reference = FirebaseDatabase.getInstance().getReference("sellContent").child(product_id);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   // Toast.makeText(WishlistActivity.this, "getting", Toast.LENGTH_SHORT).show();
                    Pro_content chat = dataSnapshot.getValue(Pro_content.class);
                    Pro_content.add(chat);
                    adapter = new wishlistAdapter(WishlistActivity.this, Pro_content);
                    recyclerView.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }else{
            Toast.makeText(WishlistActivity.this, "No items in WishList", Toast.LENGTH_SHORT).show();
        }
    }

}
