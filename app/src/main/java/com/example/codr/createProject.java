package com.example.codr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createProject extends AppCompatActivity {

    private Button mSaveProject;
    private EditText mDescription,mName;
    private RadioGroup mRadioGroupType;
    private FirebaseAuth mAuth;
    private String uid;
    private CheckBox mJava,mJs,mKotlin,mCss,mHtml,mC,mPython,mCsharp,mCplus,mPhp,mScala,mSwift;
    private String key;
    //fields for image upload
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        mSaveProject = (Button) findViewById(R.id.saveProject);

        mDescription=(EditText)findViewById(R.id.projectDescription);
        mName=(EditText)findViewById(R.id.projectName);
        mRadioGroupType = (RadioGroup) findViewById(R.id.typeOfProject);
        mJava = findViewById(R.id.java);
        mJs = findViewById(R.id.js);
        mKotlin = findViewById(R.id.kotlin);
        mCss = findViewById(R.id.css);
        mHtml = findViewById(R.id.html);
        mC = findViewById(R.id.c);
        mPython = findViewById(R.id.python);
        mCsharp = findViewById(R.id.csharp);
        mCplus = findViewById(R.id.cplus);
        mPhp= findViewById(R.id.php);
        mScala = findViewById(R.id.scala);
        mSwift = findViewById(R.id.swift);

        btnChoose = (Button) findViewById(R.id.btnChooseImg);
        imageView = (ImageView) findViewById(R.id.imgView);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });



        mSaveProject.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view){

                int selectTypeId = mRadioGroupType.getCheckedRadioButtonId();


                final RadioButton radioButtonType = (RadioButton) findViewById(selectTypeId);


                if(radioButtonType.getText() == null ) {
                    return;
                }


                final String description = mDescription.getText().toString();
                final String name = mName.getText().toString();
                final String type= (String) radioButtonType.getText();
                ArrayList<String> languages= new ArrayList<String>();

                if(mJava.isChecked()) {
                    languages.add("java");
                }

                if(mJs.isChecked()) {
                    languages.add("JavaScript");
                }

                if(mHtml.isChecked()) {
                    languages.add("HTML");
                }
                if(mCss.isChecked()) {
                    languages.add("CSS");                }
                if(mC.isChecked()) {
                    languages.add("C");                }
                if(mCplus.isChecked()) {
                    languages.add("C++");                }
                if(mCsharp.isChecked()) {
                    languages.add("C#");                }
                if(mPython.isChecked()) {
                    languages.add("Python");                }
                if(mKotlin.isChecked()) {
                    languages.add("Kotlin");                }
                if(mScala.isChecked()) {
                    languages.add("Scala");                }
                if(mSwift.isChecked()) {
                    languages.add("Swift");                }
                if(mPhp.isChecked()) {
                    languages.add("PHP");                }



                Project myProject = new Project(uid,name,description,type,languages);

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference projectRef = rootRef.child("Projects");

                Map<String,Object> map = new HashMap<>();
                map.put(name,myProject);
                key = projectRef.push().getKey();
                projectRef.child(key).setValue(map);

                uploadImage();


                Intent intent = new Intent(createProject.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            StorageReference ref = storageReference.child("Projects/"+ key+"/images");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(createProject.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(createProject.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

}
