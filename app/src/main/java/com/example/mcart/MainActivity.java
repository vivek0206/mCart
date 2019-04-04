package com.example.mcart;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {

    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<Pro_content>Pro_content=new ArrayList<>();
    productAdapter adapter;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    ArrayAdapter<String> slist;


    DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle mDrawerToggle;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
       //
        // MenuItem menuItem=findViewById(R.id.search);
//        SearchView searchView=(SearchView)menuItem.getActionView();
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Profile){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }
        if(id == R.id.Sell){
            startActivity(new Intent(MainActivity.this,sellActivity.class));
        }
        if(id == R.id.wishlist){
            startActivity(new Intent(MainActivity.this,WishlistActivity.class));
        }
        if(id == R.id.logout){
            logout();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =  findViewById(R.id.bar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("sellContent");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // User user=dataSnapshot.getValue(User.class);
                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null) {
            Toast.makeText(MainActivity.this,"no user",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, startActivity.class));
           // finish();
        }
    }

    private void readMessage(){
        //Pro_content =
        reference= FirebaseDatabase.getInstance().getReference("sellContent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pro_content.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Pro_content Chat=snapshot.getValue(Pro_content.class);
                        Pro_content.add(Chat);
                    //}
                    adapter =new productAdapter(MainActivity.this,Pro_content);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.title) {
            Toast.makeText(MainActivity.this,"title",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.title1) {
            Toast.makeText(MainActivity.this,"title1",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.title2) {
            Toast.makeText(MainActivity.this,"title2",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        } else if (id == R.id.nav_logout) {
            logout();
        }
        else if (id == R.id.back) {
            drawer.closeDrawer(GravityCompat.START);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String userinput=s.toLowerCase();
        List<Pro_content> newList=new ArrayList<>();
        for(Pro_content pro:Pro_content)
        {
            if(pro.getPro_name().toLowerCase().contains(userinput))
            {
                newList.add(pro);
            }
        }
        adapter.updateList(newList);
        return true;
    }
    public void logout(){
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this,startActivity.class));
    }
}
