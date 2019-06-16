package com.example.codr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private DatabaseReference userDb;

    List<cards> rowItems;
    private String uid;
    private List<String> preferences;
    private DatabaseReference projectDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //add the view via xml or programmatically
        rowItems = new ArrayList<cards>();
         userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        projectDb = FirebaseDatabase.getInstance().getReference().child("Projects");
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        findProjects();




        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );
        arrayAdapter.notifyDataSetChanged();

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "Lefty", Toast.LENGTH_SHORT).show();
                cards obj= (cards) dataObject;
                String projectId=obj.getProjectId();
                Map<String,Object> mapUser = new HashMap<>();
                mapUser.put(uid,false);


                projectDb.child(projectId).child(projectId).child("matches").updateChildren(mapUser);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "Righty", Toast.LENGTH_SHORT).show();
                cards obj= (cards) dataObject;
                String projectId=obj.getProjectId();
                Map<String,Object> mapProject = new HashMap<>();
                Map<String,Object> mapUser = new HashMap<>();
                mapProject.put(projectId,true);
                mapUser.put(uid,true);
                userDb.child(uid).child("matches").updateChildren(mapProject);

                projectDb.child(projectId).child(projectId).child("matches").updateChildren(mapUser);

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void findProjects(){

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(DataSnapshot snapshot) {

                                                      if (snapshot.hasChild("preferences")) {
                                                          preferences = (List<String>) snapshot.child("preferences").getValue();
                                                      }
                                                      addCards();
                                                  }
                                                  @Override
                                                  public void onCancelled(DatabaseError databaseError) {

                                                  }
                                              });
    };

    public void addCards(){
        DatabaseReference projectDb = FirebaseDatabase.getInstance().getReference().child("Projects");
        projectDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (!dataSnapshot.child(dataSnapshot.getKey()).child("matches").hasChild(uid) && !dataSnapshot.child(dataSnapshot.getKey()).child("creator").getValue().equals(uid) && dataSnapshot.child(dataSnapshot.getKey()).hasChild("languages")) {

                    List<String> languages = (List<String>) dataSnapshot.child(dataSnapshot.getKey()).child("languages").getValue();
                    if (preferences ==null || !Collections.disjoint(preferences,languages)) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference();
                        StorageReference ref = storageReference.child("Projects/" + dataSnapshot.getKey() + "/images");
                        String imgUrl = null;
                        if (dataSnapshot.child(dataSnapshot.getKey()).child("imgUrl").getValue() != null) {
                            imgUrl = dataSnapshot.child(dataSnapshot.getKey()).child("imgUrl").getValue().toString();
                        } else {
                            imgUrl = "https://firebasestorage.googleapis.com/v0/b/codr-d7afc.appspot.com/o/Default%2FSans%20titre.png?alt=media&token=6721e218-c26a-44b2-a648-71a7a2b84e09";
                        }


                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child(dataSnapshot.getKey()).child("name").getValue().toString(), imgUrl);

                        rowItems.add(Item);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            };
        });

    }

    public void settings(View view) {
        Intent intent = new Intent(MainActivity.this,Settings.class);
        startActivity(intent);
        finish();
        return;
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void createProject(View view) {
        Intent intent = new Intent(MainActivity.this,createProject.class);
        startActivity(intent);
        finish();
        return;

    }

}
