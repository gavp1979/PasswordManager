/**
 * 
 */
package com.example.password.manager;

import com.example.password.manager.database.providers.PasswordsContentProvider;
import com.mentorbs.encryption.MBSEncryption;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class PasswordRowAdapter extends CursorAdapter
{
	boolean _bUnlocked;

	public PasswordRowAdapter(Context context, Cursor c, final boolean bUnlocked)
	{
		super(context, c, 0);

		_bUnlocked = bUnlocked;
	}
	
	public void setUnlocked(final boolean bUnlocked)
	{
		_bUnlocked = bUnlocked;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		TextView NameData = (TextView)view.findViewById(R.id.NameData);
		
		String strNameData = cursor.getString(cursor.getColumnIndex(PasswordsContentProvider.NAME));
		
		if (_bUnlocked)
			strNameData = MBSEncryption.EncryptStringToHexString(strNameData);
		
		NameData.setText(strNameData);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		View view =  LayoutInflater.from(context).inflate(R.layout.password_row, parent, false);
		return view;
	}

}
