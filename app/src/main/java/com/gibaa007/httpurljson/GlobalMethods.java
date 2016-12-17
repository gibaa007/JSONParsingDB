package com.gibaa007.httpurljson;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gibaa007 on 17/3/16.
 */
public class GlobalMethods {

    public static boolean isNetworkAvailable(Context context) {
        Boolean isNetworkAvailable = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isNetworkAvailable = true;
                        return true;
                    }
                }
            }
        }
        return isNetworkAvailable;
    }
}
