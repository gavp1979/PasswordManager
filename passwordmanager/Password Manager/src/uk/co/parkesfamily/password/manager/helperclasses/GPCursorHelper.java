package uk.co.parkesfamily.password.manager.helperclasses;

import com.mentorbs.encryption.MBSEncryption;

import android.database.Cursor;

public class GPCursorHelper
{
	/**
	 * Checks if the passed {@link Cursor} is not null and is open.
	 * @param cursor The {@link Cursor} to check.
	 * @return <code>true</code> if {@link Cursor} is open, <code>false</code> if
	 * it is closed.
	 */
	public static boolean isOpen(final Cursor cursor)
	{
		return (cursor != null) && (!cursor.isClosed());
	}
	
	/**
	 * Checks if the passed cursor has at least 1 record.
	 * @param cursor The {@link Cursor} to check.
	 * @return <code>true</code> if the {@link Cursor} has at least 1 record,
	 * <code>false</code> if not.
	 */
	public static boolean hasRecords(final Cursor cursor)
	{
		return (isOpen(cursor)) && (cursor.getCount() > 0);
	}
	
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
