package com.example.mcart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class wishlistAdapter extends RecyclerView.Adapter<wishlistAdapter.productViewHolder>  {

    private Context mctx;
    private List<Pro_content> productList;
    FirebaseUser fuser;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    public wishlistAdapter(Context mctx, List<Pro_content> productList) {
        this.mctx = mctx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public wishlistAdapter.productViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mctx);
        View view =inflater.inflate(R.layout.wishlist_content,null);
        return new wishlistAdapter.productViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final wishlistAdapter.productViewHolder productViewHolder, int position) {
        final Pro_content pro_content=productList.get(position);
        if(pro_content.getPro_name()!=null) {
            productViewHolder.prodName.setText(pro_content.getPro_name());
            productViewHolder.prodInfo.setText("\u20B9" +pro_content.getPro_price());
        }else{
            Toast.makeText(mctx,"no content",Toast.LENGTH_SHORT).show();
        }






        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        DatabaseReference ref=firebaseDatabase.getReference(firebaseAuth.getUid());
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

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("wishlist");
                String prod_id=pro_content.getImg_url();
                ref.child(prod_id).removeValue();
                productViewHolder.wish_list.setImageResource(R.drawable.fav);
                Toast.makeText(mctx,"removed for wishlist",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class productViewHolder extends RecyclerView.ViewHolder{
        TextView prodName,prodInfo;
        ImageView imageView,wish_list;


        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName=itemView.findViewById(R.id.pname);
            prodInfo=itemView.findViewById(R.id.pInfo);
            imageView=itemView.findViewById(R.id.p_img);
            wish_list=itemView.findViewById(R.id.pwishlist);



        }
    }

    public void updateList(List<Pro_content> product){
        productList=new ArrayList<>();
        productList.addAll(product);
        notifyDataSetChanged();
    }



}
