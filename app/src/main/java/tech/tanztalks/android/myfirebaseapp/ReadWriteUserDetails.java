package tech.tanztalks.android.myfirebaseapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReadWriteUserDetails {

    public String uid, fullname, doB, gender, mobile, email;

    // constructor
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String uId, String textFullname, String textDoB, String textGender, String textMobile, String textEmail){
        this.uid = uId;
        this.email = textEmail;
        this.fullname=textFullname;
        this.doB=textDoB;
        this.gender=textGender;
        this.mobile=textMobile;
    }
}
