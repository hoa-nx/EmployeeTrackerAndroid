/**
 * Copyright (c) 2012 Vinayak Solutions Private Limited 
 * See the file license.txt for copying permission.
*/     


package com.ussol.employeetracker.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

import com.ussol.employeetracker.EmployeeTrackerApplication;
import com.ussol.employeetracker.models.MasterConstants;

public class FileHelper {
	
	public FileHelper() {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !EmployeeTrackerApplication.isInitialized) {
			EmployeeTrackerApplication.Initialize();
		}
	}
	
	public void copyAllFromFavorite(String _id,String targetId) {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			copy(getCameraFileLargeUser(_id),getCameraFileLargeEntry(targetId));
			copy(getCameraFileSmallUser(_id),getCameraFileSmallEntry(targetId));
			copy(getCameraFileThumbnailUser(_id),getCameraFileThumbnailEntry(targetId));
			copy(getAudioFileUser(_id),getAudioFileEntry(targetId));
		}
	}
	
	public void copyAllToFavorite(String _id,String targetId) {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			copy(getCameraFileLargeEntry(_id),getCameraFileLargeUser(targetId));
			copy(getCameraFileSmallEntry(_id),getCameraFileSmallUser(targetId));
			copy(getCameraFileThumbnailEntry(_id),getCameraFileThumbnailUser(targetId));
			copy(getAudioFileEntry(_id),getAudioFileUser(targetId));
		}
	}
	
	public void deleteAllEntryFiles(String _id) {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			delete(getCameraFileLargeEntry(_id));
			delete(getCameraFileSmallEntry(_id));
			delete(getCameraFileThumbnailEntry(_id));
			delete(getAudioFileEntry(_id));
		}
	}
	
	public void deleteAllFavoriteFiles(String _id) {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			delete(getCameraFileLargeUser(_id));
			delete(getCameraFileSmallUser(_id));
			delete(getCameraFileThumbnailUser(_id));
			delete(getAudioFileUser(_id));
		}
	}
	
	public File getAudioFileUser(String _id) {
		return new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + MasterConstants.DIRECTORY_AUDIO + _id + MasterConstants.AUDIO_FILE_SUFFIX);
	}
	
	public File getAudioFileEntry(String _id) {
		return new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_AUDIO + _id + MasterConstants.AUDIO_FILE_SUFFIX);
	}
	
	public File getCameraFileLargeUser(String _id) {
		return new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + _id + MasterConstants.IMAGE_LARGE_SUFFIX);
	}

	public File getCameraFileLargeEntry(String _id) {
		return new File(MasterConstants.DIRECTORY + _id + MasterConstants.IMAGE_LARGE_SUFFIX);
	}
	
	public File getCameraFileSmallUser(String _id) {
		return new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + _id + MasterConstants.IMAGE_SMALL_SUFFIX);
	}

	public File getCameraFileSmallEntry(String _id) {
		return new File(MasterConstants.DIRECTORY + _id + MasterConstants.IMAGE_SMALL_SUFFIX);
	}
	
	public File getCameraFileThumbnailUser(String _id) {
		return new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + _id + MasterConstants.IMAGE_THUMBNAIL_SUFFIX);
	}

	public File getCameraFileThumbnailEntry(String _id) {
		return new File(MasterConstants.DIRECTORY + _id + MasterConstants.IMAGE_THUMBNAIL_SUFFIX);
	}

	public void copy(File source,File target) {
		try {
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(target);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.flush();
			in.close();
			out.close();
		} catch (Exception e) {
			//Do Nothing
			e.printStackTrace();
		}
	}

	public void delete(File file) {
		try {
			file.delete();
		} catch (Exception e) {
			//Do Nothing
		}
	}
	
}
