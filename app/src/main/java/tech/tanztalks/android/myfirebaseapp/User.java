package tech.tanztalks.android.myfirebaseapp;

public class User {
    private String uid;
    private String fullname;
    private  String gender;
    private String mobile;
    private String email;

    public User () {

    }


    public User(String uid, String fullname, String gender, String mobile, String email) {
        this.uid = uid;
        this.fullname = fullname;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

