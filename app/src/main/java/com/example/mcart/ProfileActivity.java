package com.example.mcart;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgProfile;
    TextView pname,pemail,phostel,proom,pcontact;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseUser user;
    private Toolbar toolbar;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_dot_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Profile){
            startActivity(new Intent(ProfileActivity.this,UpdateProfileActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar)findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        setTitle("Profile");
       toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });


        imgProfile=findViewById(R.id.p_Profile);
        pname=findViewById(R.id.p_name);
        pemail=findViewById(R.id.p_email);
        phostel=findViewById(R.id.p_Hostel);
        proom=findViewById(R.id.p_Room);
        pcontact=findViewById(R.id.p_phone);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        pemail.setText(email);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();

        DatabaseReference ref=firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        // Do stuff
                        userprofile userprofile=dataSnapshot.getValue(userprofile.class);
                        pname.setText(userprofile.getUsername());
                        pemail.setText(userprofile.getEmail());
                        phostel.setText(userprofile.getHostel());
                        proom.setText(userprofile.getRoom());
                        pcontact.setText(userprofile.getContact());
                        Picasso.get().load(user.getPhotoUrl().toString()).centerInside().fit().into(imgProfile);
                        /*final StorageReference storageReference=firebaseStorage.getReference();


                        storageReference.child(firebaseAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).fit().centerCrop().into(imgProfile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure (@NonNull Exception exception){
                                // File not found
                            }

                        });*/
                    }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
