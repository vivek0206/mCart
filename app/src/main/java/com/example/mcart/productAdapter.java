package com.example.mcart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class productAdapter extends RecyclerView.Adapter<productAdapter.productViewHolder> {
    private Context mctx;
    private List<Pro_content>productList;
    FirebaseUser fuser;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    public productAdapter(Context mctx, List<Pro_content> productList) {
        this.mctx = mctx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mctx);
        View view =inflater.inflate(R.layout.products,null);
        return new productViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final productViewHolder productViewHolder, int position) {
        final Pro_content pro_content=productList.get(position);
        productViewHolder.prodName.setText(pro_content.getPro_name());
        productViewHolder.prodPrice.setText("\u20B9" + pro_content.getPro_price());


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
//        DatabaseReference ref=firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(pro_content.getImg_url()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(productViewHolder.imageView);
            }
        });

        productViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mctx , ProductActivity.class);
                intent.putExtra("ProductName",pro_content.getPro_name());
                intent.putExtra("ProductPrice",pro_content.getPro_price());
                intent.putExtra("ProductInfo",pro_content.getPro_info());
                intent.putExtra("ProductImage",pro_content.getImg_url());
                intent.putExtra("ProductId",pro_content.getImg_url());
                intent.putExtra("Seller",pro_content.getSeller());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mctx.startActivity(intent);

            }
        });
       productViewHolder.wish_list.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               productViewHolder.wish_list.setImageResource(R.drawable.favred);
           }
       });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class productViewHolder extends RecyclerView.ViewHolder{
        TextView prodName,prodPrice;
        ImageView imageView,wish_list;


        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName=itemView.findViewById(R.id.prodName);
            prodPrice=itemView.findViewById(R.id.prodPrice);
            imageView=itemView.findViewById(R.id.imageView);
            wish_list=itemView.findViewById(R.id.wishlist);



        }
    }

    public void updateList(List<Pro_content> product){
        productList=new ArrayList<>();
        productList.addAll(product);
        notifyDataSetChanged();
    }



}