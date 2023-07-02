package tech.tanztalks.android.myfirebaseapp;

public class RegisteredUser {
    private String fullname;
    private String mobile;
    private String gender;
    private String doB;
    private String email;

    public RegisteredUser() {
        // Default constructor required for Firebase
    }

    public RegisteredUser(String fullname, String mobile, String gender, String doB, String email) {
        this.fullname = fullname;
        this.mobile = mobile;
        this.gender = gender;
        this.doB = doB;
        this.email = email;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
