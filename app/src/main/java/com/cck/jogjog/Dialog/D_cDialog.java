package com.cck.jogjog.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cck.jogjog.R;

public class D_cDialog extends Dialog
{
	/********************************************************************
	 * 		Dialog with Fragment										*
	 *																	*
	 *					Created by Isao on 2017/06/07.					*
	 ********************************************************************/
	/**	Usage ----------------------------------------------------------------------------------------
	 private void method()
	 {
		 CD = new D_cDialog(G.AC);
		 CD.tvTitle.setText("Title Caption");
		 CD.tvMessage.setText("Dialog message.");
		 CD.AddPositiveButton("Ok", PositiveClicked);
		 CD.AddNegativeButton("No", NegativeClicked);
		 CD.AddDismisCallback(Dissmissed);
		 CD.cShow(true);							//Cancelable = false

		 Do something ......................

		 CD.cDismiss();
		 or
		 CD.cHide();
	 }

	//
	//  Callback
	//
	private static final Handler PositiveClicked = new Handler()
	{
		 @Override
		 public void handleMessage(Message msg)
		 {
			 Toast.makeText(G.CT, "Posirive", Toast.LENGTH_SHORT).show();
		 }
	};

	private static final Handler NegativeClicked = new Handler()
	{
		 @Override
		 public void handleMessage(Message msg)
		 {
			 Toast.makeText(G.CT, "Negative", Toast.LENGTH_SHORT).show();
		 }
	};

	private static final Handler Dissmissed = new Handler()
	{
		 @Override
		 public void handleMessage(Message msg)
		 {
			 Toast.makeText(G.CT, "Dismissed", Toast.LENGTH_SHORT).show();
		 }
	};

	*/

	public TextView tvTitle, tvMessage;
	public ProgressBar pbWait;
	public CheckBox cbNeverShow;

	private static AlertDialog aDialog = null;
	private AlertDialog.Builder builder;
	private Handler poClicked = null;
	private Handler neClicked = null;
	private Handler woClicked = null;
	private Handler dmCallback = null;

	//---------------------------------------------------------------------------------------------------------
	public D_cDialog(Context context)
	{
		super(context);

		builder = new AlertDialog.Builder(context);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View content = inflater.inflate(R.layout.d_cdialog, null);
		builder.setView(content);

		pbWait = (ProgressBar)content.findViewById(R.id.PB_DoingSomething);
		pbWait.setVisibility(View.GONE);
//		aDialog = builder.create();
	}

	public void cShow(boolean Cancelable)
	{
		if (aDialog == null) aDialog = builder.create();
		aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		aDialog.setCancelable(Cancelable);
		aDialog.show();
	}

	public void cHide()
	{
		aDialog.hide();

	}

	public void cDismiss()
	{
		aDialog.dismiss();
	}

	//	Buttons
	public void AddPositiveButton(String Caption, Handler Callback)
	{
		poClicked = Callback;
		builder.setPositiveButton(Caption, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				if (poClicked != null) poClicked.obtainMessage(1).sendToTarget();		//Send message to UI Activity
			}
		});
	}

	public void AddNegativeButton(String Caption, Handler Callback)
	{
		neClicked = Callback;
		builder.setNegativeButton(Caption, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				if (neClicked != null) neClicked.obtainMessage(1).sendToTarget();		//Send message to UI Activity
			}
		});
	}

	public void AddNeutralButton(String Caption, Handler Callback)
	{
		woClicked = Callback;
		builder.setNeutralButton(Caption, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				if (woClicked != null) woClicked.obtainMessage(1).sendToTarget();		//Send message to UI Activity
			}
		});
	}

	public void AddDismisCallback(Handler Callback)
	{
		dmCallback = Callback;
		builder.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				if (dmCallback != null) dmCallback.obtainMessage(1).sendToTarget();		//Send message to UI Activity
			}
		});
	}

}
