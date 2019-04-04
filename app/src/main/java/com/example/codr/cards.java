package com.example.codr;

public class cards {
    private String projectId;
    private String projectName;
    public cards(String projectId, String projectName){
        this.projectId=projectId;
        this.projectName=projectName;
    }

    public String getProjectId(){
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
