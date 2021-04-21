package com.cck.jogjog.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;

import com.cck.jogjog.GlobalVar.GlobalVar;
import com.cck.jogjog.R;

import static com.cck.jogjog.GlobalVar.Common.DOMAIN_NAME;

public class D_videoDialog extends Dialog
{

	public Button btn_imgback;
	public VideoView vid_douga;
	public TextView txv_guide;
    public FrameLayout fl_vidcontroller;

	private static AlertDialog aDialog = null;
	private AlertDialog.Builder builder;
	private Handler poClicked = null;
	private Handler neClicked = null;
	private Handler woClicked = null;
	private Handler dmCallback = null;
	private Activity parentactivity;


	//---------------------------------------------------------------------------------------------------------
	public D_videoDialog(Activity activity)
	{
		super(activity);
		this.parentactivity = activity;

		builder = new AlertDialog.Builder(activity);


		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View content = inflater.inflate(R.layout.d_videodialog, null);
		builder.setView(content);
        fl_vidcontroller = content.findViewById(R.id.fl_vidcontroller);
        btn_imgback = content.findViewById(R.id.btn_imgback);
		vid_douga =  content.findViewById(R.id.vid_douga);
		txv_guide =  content.findViewById(R.id.txv_guide);
        btn_imgback.setOnClickListener(dynamicimageOnclickListener);
		aDialog = builder.create();
        loadvideo();


	}

	View.OnClickListener dynamicimageOnclickListener = new View.OnClickListener() {
		@SuppressLint("ResourceType")
		public void onClick(View v) {
			cDismiss();
		}
	};


	private void loadvideo() {

		Uri video = Uri.parse(DOMAIN_NAME + GlobalVar.getmovie_files());
		vid_douga.setVideoURI(video);
		//vid_douga.requestFocus();
		vid_douga.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp,
                                                   int width, int height) {
                        /*
                         * add media controller
                         */
                        MediaController mc = new MediaController(parentactivity){
                        @Override
                        public void hide() {
                            this.show();
                        }
                    };
                        vid_douga.setMediaController(mc);
                        /*
                         * and set its position on screen
                         */
                        mc.setAnchorView(vid_douga);

                        ((ViewGroup) mc.getParent()).removeView(mc);

                       fl_vidcontroller.addView(mc);
                        mc.setVisibility(View.VISIBLE);
                        mc.show();
                    }
                });
				vid_douga.start();
			}
		});
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
