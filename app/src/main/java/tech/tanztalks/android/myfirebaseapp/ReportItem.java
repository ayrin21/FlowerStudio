package tech.tanztalks.android.myfirebaseapp;
//notused
public class ReportItem {
    private String fullName;
    private int likes;
    private int comments;

    public ReportItem(String fullName, int likes, int comments) {
        this.fullName = fullName;
        this.likes = likes;
        this.comments = comments;
    }

    public String getFullName() {
        return fullName;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }
}
