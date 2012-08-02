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
	public interface IUnlockDialog
	{
		public void unlock(final boolean bUnlocked);
	}
	
	private final EditText	_edtPassword;
	private IUnlockDialog	_interface;

	public UnlockDialog(Context context)
	{
		super(context);
		
		try
		{
			_interface = (IUnlockDialog)context;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(context.toString() + " must  implement " + 
					IUnlockDialog.class.toString());
		}
		
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
		final boolean bUnlocked;
		final String strPasswod = _edtPassword.getText().toString().trim();
		
		if (strPasswod.equalsIgnoreCase("StupidFuckingPasswords"))
		{
			Toast.makeText(getContext(), "Program Unlocked", Toast.LENGTH_LONG).show();
			bUnlocked = true;
		}
		else
		{
			Toast.makeText(getContext(), "Incorrect Password - Program  Still Locked", Toast.LENGTH_LONG).show();
			bUnlocked = false;
		}
		
		_interface.unlock(bUnlocked);
	}

	
}
