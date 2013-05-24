package com.aalife.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DataBackupAndRestore {
	private static final String FILENAME = "aalife.bak";
	private static final String PATH = "aalife";

	public static void dbBackup(Context context) {
		ItemTableAccess itemtableAccess = new ItemTableAccess(new MyDatabaseHelper(context).getReadableDatabase());
		List<CharSequence> list = itemtableAccess.backupItemTable();
		
		FileOutputStream output = null;
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + PATH + File.separator + FILENAME);
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				
				output = new FileOutputStream(file, false);
			} else {
				output = context.openFileOutput(FILENAME, Activity.MODE_PRIVATE);
			}

			PrintStream out = new PrintStream(output);
			for (int i = 0; i < list.size(); i++) {
				out.println(list.get(i));
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static boolean dbRestore(Context context) {
		boolean success = false;
		
		FileInputStream input = null;
		File file = null;
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + PATH + File.separator + FILENAME);
			} else {
				file = context.getFileStreamPath(FILENAME);
			}
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(!file.exists()) {
				file.createNewFile();
			}
			
			input = new FileInputStream(file);
			
			List<CharSequence> list = new ArrayList<CharSequence>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String str = "";
			while((str = reader.readLine()) != null) {
				list.add(str);
			}
			reader.close();
			
			SQLiteOpenHelper helper = new MyDatabaseHelper(context);
			ItemTableAccess itemtableAccess = new ItemTableAccess(helper.getWritableDatabase());
			success = itemtableAccess.restoreItemTable(list);
			helper.close();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}

}