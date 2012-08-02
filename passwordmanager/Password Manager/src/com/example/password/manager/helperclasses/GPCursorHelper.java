package com.example.password.manager.helperclasses;

import com.mentorbs.encryption.MBSEncryption;

import android.database.Cursor;

public class GPCursorHelper
{
	public static String getString(final Cursor cursor, final String strColName)
	{
		String strResult = cursor.getString(cursor.getColumnIndex(strColName));
		if (strResult == null)
			strResult = "";
		
		return strResult;
	}
	
	public static String getStringDecrypted(final Cursor cursor, final String strColName)
	{
		String strResult = getString(cursor, strColName);
		if (!strResult.equals(""))
			strResult = MBSEncryption.DecryptHexStringToString(strResult);
		
		return strResult;
	}
}
