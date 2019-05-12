package com.example.codr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                projectRef.push().setValue(map);
                Intent intent = new Intent(createProject.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

}
