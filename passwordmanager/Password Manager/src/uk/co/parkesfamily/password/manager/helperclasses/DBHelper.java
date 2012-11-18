package uk.co.parkesfamily.password.manager.helperclasses;

import uk.co.parkesfamily.password.manager.database.providers.ConfigurationContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;

public class DBHelper
{
	public static String getMasterPassword(ContentResolver resolver)
	{
		String strResult = "";
		
		String[] lstCols = new String[] {ConfigurationContentProvider._ID, 
				ConfigurationContentProvider.PASSWORD};
		
		final Cursor cur = resolver.query(ConfigurationContentProvider.CONTENT_URI, lstCols, null, null, null);
		
		if (GPCursorHelper.hasRecords(cur))
		{
			cur.moveToFirst();
			
			strResult = GPCursorHelper.getString(cur, ConfigurationContentProvider.PASSWORD);
		}
		
		return strResult;
	}
}
