package com.example.password.manager.helperclasses;

import com.mentorbs.encryption.MBSEncryption;

import android.database.Cursor;

public class GPCursorHelper
{
	/**
	 * Retrieves a {@link String} value from the {@link Cursor}.
	 * 
	 * @param cursor
	 *            The {@link Cursor} pointing at the correct record.
	 * @param strColName
	 *            The name of the column to retrieve.
	 * @return A {@link String} containing the value.
	 */
	public static String getString(final Cursor cursor, final String strColName)
	{
		String strResult = cursor.getString(cursor.getColumnIndex(strColName));
		if (strResult == null)
			strResult = "";

		return strResult;
	}

	/**
	 * Retrieves a {@link String} value from the {@link Cursor} and decrypts the
	 * value.
	 * 
	 * @param cursor
	 *            The {@link Cursor} pointing at the correct record.
	 * @param strColName
	 *            The name of the column to retrieve.
	 * @return A {@link String} containing the decrypted value.
	 * 
	 * @see #getStringDecrypted(Cursor, String, boolean)
	 */
	public static String getStringDecrypted(final Cursor cursor, final String strColName)
	{
		String strResult = getString(cursor, strColName);
		if (!strResult.equals(""))
			strResult = MBSEncryption.DecryptHexStringToString(strResult);

		return strResult;
	}

	/**
	 * Retrieves a {@link String} value from the {@link Cursor} and optionally
	 * decrypts the value.
	 * 
	 * @param cursor
	 *            The {@link Cursor} pointing at the correct record.
	 * @param strColName
	 *            The name of the column to retrieve.
	 * @param bDecrypt
	 *            <code>true</code> to decrypt the value, <code>false</code> to
	 *            not decrypt.
	 * @return A {@link String} containing the decrypted value.
	 * 
	 * @see #getStringDecrypted(Cursor, String)
	 */
	public static String getStringDecrypted(final Cursor cursor, final String strColName,
			final boolean bDecrypt)
	{
		final String strResult;
		if (bDecrypt)
			strResult = getStringDecrypted(cursor, strColName);
		else
			strResult = getString(cursor, strColName);
		
		return strResult;
	}
}
