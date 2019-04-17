package com.example.codr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Project implements DatabaseReference.CompletionListener {
    private String name;
    private String description;
    private String type;
    private ArrayList<String> languages;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Project(String name, String description, String type, ArrayList<String> languages) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.languages = languages;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

    }
}
