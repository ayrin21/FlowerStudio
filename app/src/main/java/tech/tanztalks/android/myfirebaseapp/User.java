package tech.tanztalks.android.myfirebaseapp;

public class User {
    private String fullname;
    private  String gender;
    private String mobile;
    private String email;

    public User () {

    }

    public User(String fullname, String gender, String mobile, String email) {

        this.fullname = fullname;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
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

