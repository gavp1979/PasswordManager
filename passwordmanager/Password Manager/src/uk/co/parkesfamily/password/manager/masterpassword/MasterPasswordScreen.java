package uk.co.parkesfamily.password.manager.masterpassword;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.password.manager.R;

/**
 * @author Administrator
 *
 */
public class MasterPasswordScreen extends FragmentActivity
{
	public static Intent createIntent(Context context)
	{
		Intent intResult = new Intent(context, MasterPasswordScreen.class);
		
		return intResult;
	}

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.master_password_screen);
	}

}
