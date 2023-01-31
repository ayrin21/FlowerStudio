package tech.tanztalks.android.myfirebaseapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;
    public static final String SESSION_USSERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "userRememberMeSession";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_DATE = "dob";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONEMAIL= "email";
    public static final String KEY_SESSIONPASSWORD = "password";

    public SessionManager (Context _context, String sessionName){
        context = _context;
        userSession= context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

   void createRememberMeSession(String useremail, String userpassword){
    editor.putBoolean(IS_REMEMBERME, true);
    editor.putString(KEY_EMAIL, useremail);
    editor.putString(KEY_PASSWORD, userpassword);
    editor.commit();
   }

   public HashMap<String, String> getRememberMeDetailsFromSession(){
        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_SESSIONEMAIL, userSession.getString(KEY_EMAIL, null));
        userData.put(KEY_SESSIONPASSWORD, userSession.getString(KEY_PASSWORD, null));

        return userData;
   }

   public boolean checkRememberMe() {
        if (userSession.getBoolean(IS_REMEMBERME, false)){
            return true;
        } else {
            return false;
        }
   }

   public void logoutUserSession(){
        editor.clear();
        editor.commit();
   }

}
