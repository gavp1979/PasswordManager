package com.example.password.manager.helperclasses;

import com.mentorbs.encryption.MBSEncryption;

import android.content.ContentValues;

/**
 * Class to automatically encrypt all values added to the {@link ContentValues}.
 * {@link ContentValues} is marked as final so I had to create a wrapper class 
 * instead of overriding it.
 * 
 * @author Administrator
 */
public class GPContentValues
{
	private final ContentValues _vals;

	/**
	 * Constructor, creates the internal {@link ContentValues}.
	 */
	public GPContentValues()
	{
		super();

		_vals = new ContentValues();
	}
	
	/**
	 * Gets the internal {@link ContentValues}.
	 * @return the internal {@link ContentValues}.
	 */
	public ContentValues getContentValues()
	{
		return _vals;
	}
	
	/**
	 * Encrypts strValue and adds it to the set.
	 * @param strKey the name of the value to put 
	 * @param strValue the data for the value to put
	 */
	public void put(final String strKey, final String strValue)
	{
		_vals.put(strKey, MBSEncryption.EncryptStringToHexString(strValue));
	}
}
