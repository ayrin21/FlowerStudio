package tech.tanztalks.android.myfirebaseapp;

public class Post {
    private String pId;
    private String pTitle;
    private String pDescr;
    private String pImage;
    private String pTime;
    private String uId;
    private String uName;
    private String uEmail;
    private String pLikes;
    private String pComments;

    public Post() {
        // Default constructor required for Firebase
    }

    public Post(String pId, String pTitle, String pDescr, String pImage, String pTime, String uId, String uName, String uEmail, String pLikes, String pComments) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pDescr = pDescr;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uId = uId;
        this.uName = uName;
        this.uEmail = uEmail;
        this.pLikes = pLikes;
        this.pComments = pComments;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescr() {
        return pDescr;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pComments) {
        this.pComments = pComments;
    }
}
