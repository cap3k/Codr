package com.example.codr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    private RadioGroup mRadioGroupLanguages1,mRadioGroupLanguages2,mRadioGroupType;

    private FirebaseAuth mAuth;
    private String uid;

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
        mRadioGroupLanguages1= (RadioGroup) findViewById(R.id.languages1);
        mRadioGroupLanguages2=(RadioGroup) findViewById(R.id.languages2);
        mSaveProject.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view){

                int selectTypeId = mRadioGroupType.getCheckedRadioButtonId();
                int selectLanguage1Id = mRadioGroupLanguages1.getCheckedRadioButtonId();
                int selectLanguage2Id = mRadioGroupLanguages2.getCheckedRadioButtonId();

                final RadioButton radioButtonType = (RadioButton) findViewById(selectTypeId);
                final RadioButton radioButtonLanguage1 = (RadioButton) findViewById(selectLanguage1Id);
                final RadioButton radioButtonLanguage2 = (RadioButton) findViewById(selectLanguage2Id);

                if(radioButtonType.getText() == null ) {
                    return;
                }
                if(radioButtonLanguage1.getText() == null ) {
                    return;
                }
                if(radioButtonLanguage2.getText() == null ) {
                    return;
                }

                final String description = mDescription.getText().toString();
                final String name = mName.getText().toString();
                final String type= (String) radioButtonType.getText();
                final String language1 = (String) radioButtonLanguage1.getText();
                final String language2 = (String) radioButtonLanguage2.getText();
                ArrayList<String> languages= new ArrayList<String>();
                languages.add(language1);
                languages.add(language2);
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
