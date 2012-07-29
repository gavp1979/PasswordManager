/**
 * 
 */
package com.example.password.manager.unlock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class UnlockDialog extends AlertDialog implements OnClickListener
{
	private final EditText	_edtPassword;

	public UnlockDialog(Context context)
	{
		super(context);
		
		_edtPassword = new EditText(context);
		_edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

		setTitle("Please Unlock");
		setMessage("To unlock your passwords please enter the master password below:");
		setView(_edtPassword);
		setButton(BUTTON_POSITIVE, "Unlock", this);
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		final String strPasswod = _edtPassword.getText().toString().trim();
		
		if (strPasswod.equalsIgnoreCase("StupidFuckingPasswords"))
		{
			Toast.makeText(getContext(), "Program Unlocked", Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(getContext(), "Incorrect Password - Program  Still Locked", Toast.LENGTH_LONG).show();
	}

	
}
