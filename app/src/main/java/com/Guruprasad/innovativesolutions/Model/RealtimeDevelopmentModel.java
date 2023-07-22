package com.Guruprasad.innovativesolutions.Model;

public class RealtimeDevelopmentModel {

    private String id ;
    private String topic ;
    private String requirement ;
    private String phoneNo;
    private String email ;
    private String whatsapp ;
    private String budget ;
    private String purpose;

    public RealtimeDevelopmentModel(String id, String topic, String requirement, String phoneNo, String email, String whatsapp, String budget, String purpose) {
        this.id = id;
        this.topic = topic;
        this.requirement = requirement;
        this.phoneNo = phoneNo;
        this.email = email;
        this.whatsapp = whatsapp;
        this.budget = budget;
        this.purpose = purpose;
    }

    public RealtimeDevelopmentModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
