package com.cck.jogjog.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class D_panoramaDialog extends Dialog
{

	public Button btn_back;
	public VrPanoramaView vrPanoramaView;

	private static AlertDialog aDialog = null;
	private AlertDialog.Builder builder;
	private Handler poClicked = null;
	private Handler neClicked = null;
	private Handler woClicked = null;
	private Handler dmCallback = null;
	private Context parentcontext;


	//---------------------------------------------------------------------------------------------------------
	public D_panoramaDialog(Context context)
	{
		super(context);
		this.parentcontext =context;

		builder = new AlertDialog.Builder(context);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View content = inflater.inflate(R.layout.d_panoramadialog, null);
		builder.setView(content);

		btn_back = content.findViewById(R.id.btn_back);
		vrPanoramaView =  content.findViewById(R.id.vrPanoramaView);
		loadPhotoSphere();
		aDialog = builder.create();

		btn_back.setOnClickListener(dynamicimageOnclickListener);

	}

	View.OnClickListener dynamicimageOnclickListener = new View.OnClickListener() {
		@SuppressLint("ResourceType")
		public void onClick(View v) {
			cDismiss();
		}
	};


	private void loadPhotoSphere() {
		VrPanoramaView.Options options = new VrPanoramaView.Options();

		try {
			options.inputType = VrPanoramaView.Options.TYPE_MONO;
			vrPanoramaView.loadImageFromBitmap(GlobalVar.getpanoramabitmap(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		vrPanoramaView.setStereoModeButtonEnabled(false);
		vrPanoramaView.setFullscreenButtonEnabled(false);
		vrPanoramaView.setInfoButtonEnabled(false);
		vrPanoramaView.setPureTouchTracking(true);
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
