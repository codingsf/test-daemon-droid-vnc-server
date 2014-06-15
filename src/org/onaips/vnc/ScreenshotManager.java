package org.onaips.vnc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenshotManager extends Service {
	private final IBinder mBinder = new MyBinder();
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public void takeShot() { 
		
		// Lets see if i need to boot daemon...
		try {
			Process sh;
			log("getting absolute path");

			String files_dir = getFilesDir().getAbsolutePath();
			log("got absolute path");
			
			//our exec file is disguised as a library so it will get packed to lib folder according to cpu_abi
			String droidvncserver_exec = getFilesDir().getParent() + "/lib/libscreenshot.so";
			File f=new File (droidvncserver_exec);
			log("got file");

			
			if (!f.exists())
			{
				String e="Error! Could not find daemon file, " + droidvncserver_exec;
				log(e);
				return;
			}
			
			
			Runtime.getRuntime().exec("chmod 777 " + droidvncserver_exec);
 
			String permission_string="chmod 777 " + droidvncserver_exec;
			String server_string= droidvncserver_exec;
 
			boolean root= true;
 
			if (root)     
			{ 
				log("Running as root...");
				sh = Runtime.getRuntime().exec("su",null,new File(files_dir));
				OutputStream os = sh.getOutputStream();
				writeCommand(os, permission_string);
				writeCommand(os, server_string);
			}
			else
			{
				log("Not running as root...");
				Runtime.getRuntime().exec(permission_string);
				Runtime.getRuntime().exec(server_string,null,new File(files_dir));
			}
			// dont show password on logcat
			log("Starting " + droidvncserver_exec);

		} catch (IOException e) {
			log("startServer():" + e.getMessage());
		} catch (Exception e) {
			log("startServer():" + e.getMessage());
		}
		//takeScreenShot();
	}
	
	static void writeCommand(OutputStream os, String command) throws Exception {
		os.write((command + "\n").getBytes("ASCII"));
	}
	
  /*  public static native void takeScreenShot();
    static {
        System.loadLibrary("screenshot");
   }
    */
    
	public void log(String s) {
		Log.v(MainActivity.VNC_LOG, s);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public class MyBinder extends Binder {
		ScreenshotManager getService() {
			return ScreenshotManager.this;
		}
	}

}
