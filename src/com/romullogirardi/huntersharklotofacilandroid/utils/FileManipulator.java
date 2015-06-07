package com.romullogirardi.huntersharklotofacilandroid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class FileManipulator {

	public synchronized void saveObject(final String filePath, final Object object) {
		Runnable rr = new Runnable() {

			@Override
			public void run() {
				FileOutputStream fileOutStream;
				ObjectOutputStream objOutStream;
				try {
					fileOutStream = new FileOutputStream(new File(filePath), false);
					objOutStream = new ObjectOutputStream(fileOutStream);
					objOutStream.writeObject(object);
					objOutStream.flush();
					objOutStream.close();
					fileOutStream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(rr).start();
	}

	public synchronized Object readObj(String filePath) {

		Object object = null;

		FileInputStream fileInStream;
		ObjectInputStream objInStream;
		try {
			fileInStream = new FileInputStream(new File(filePath));
			objInStream = new ObjectInputStream(fileInStream);
			object = objInStream.readObject();
			objInStream.close();
			fileInStream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public static File getAssetsFile(Context context, String fileName) {

		AssetManager am = context.getAssets();

	   try{
		  InputStream inputStream = am.open(fileName);
	      File file = new File(fileName);
	      OutputStream outputStream = new FileOutputStream(file);
	      byte buffer[] = new byte[1024];
	      int length = 0;

	      while((length=inputStream.read(buffer)) > 0) {
	        outputStream.write(buffer,0,length);
	      }

	      outputStream.close();
	      inputStream.close();

	      return file;
	   }catch (IOException e) {
		   e.printStackTrace();
	   }

	   return null;
	}
}