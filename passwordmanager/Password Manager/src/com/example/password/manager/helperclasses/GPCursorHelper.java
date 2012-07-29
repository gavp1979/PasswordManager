package com.example.password.manager.helperclasses;

import com.mentorbs.encryption.MBSEncryption;

import android.database.Cursor;

public class GPCursorHelper
{
	public static String getString(final Cursor cursor, final String strColName)
	{
		final String strResult = cursor.getString(cursor.getColumnIndex(strColName));
		return strResult;
	}
	
	public static String getStringDecrypted(final Cursor cursor, final String strColName)
	{
		return MBSEncryption.DecryptHexStringToString(getString(cursor, strColName));
	}
}
