/*created by ahmad chaaban*/
package com.accountingmobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * This class to test if exist a connection on the mobile to let the user make a sync*/
public class ConnectionChangeReceiver extends BroadcastReceiver {
	
  public void onReceive( Context context, Intent intent ){
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    Globals g = Globals.getInstance();
    if ( activeNetInfo != null )
    {	
    	g.setCheck_Connection(true);
    }
    else
    {
    	g.setCheck_Connection(false);
    }
  }


}