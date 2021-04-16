package com.mtpixels.firebasepractice;

import android.app.Application;
import android.content.Context;
import com.mtpixels.firebasepractice.Models.Users;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/14/2021.
 */
public class App extends Application {
    private static Users user;
    private static Context context;

    public static Users getUser() {
        if(user==null){
            user = new Users();
        }
        return user;
    }

    public static void setUser(Users user){
        App.user = user;
    }

    public static void setContext(Context context){
        App.context = context;
    }

    public static Context getContext(){
        return context;
    }
}
