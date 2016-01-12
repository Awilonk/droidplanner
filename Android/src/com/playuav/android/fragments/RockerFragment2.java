package com.playuav.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
import com.o3dr.android.client.Drone;
import com.o3dr.services.android.lib.mavlink.MavlinkMessageWrapper;
import com.playuav.android.R;
import com.playuav.android.RockerView;
import com.playuav.android.fragments.helpers.ApiListenerFragment;

public class RockerFragment2 extends ApiListenerFragment {
	private RockerView rockerView1;
	private RockerView rockerView2;
	private TextView throttlView;
	private TextView yawView;
	private TextView rollView;
	private TextView pitchView;
	private Button stopRocker;
	private int TIME=20;
	private int screenWidth;
	private int screenHeight;
	public static  int[] rcOutputs = new int[8];
	public MavlinkMessageWrapper mymessageWrapper;
	public  MAVLinkMessage mymavLinkMessage;
	public MAVLinkPacket mymavLinkPacket;
	public MAVLinkPayload mymavLinkPayload;
	private Vibrator vibrator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_rocker, container, false);
		Log.e(RockerFragment.class.getSimpleName(), "create");
		rockerView1 = (RockerView)view.findViewById(R.id.rockerView1);
		rockerView2 = (RockerView) view.findViewById(R.id.rockerView2);
		throttlView=(TextView) view.findViewById(R.id.textViewRCThro);
		yawView=(TextView)view.findViewById(R.id.textViewRCYaw);
		rollView=(TextView)view.findViewById(R.id.textViewRCRoll);
		pitchView=(TextView)view.findViewById(R.id.textViewRCPitch);

		for(int i=0;i<8;i++) {
			rcOutputs[i] = 0;
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		vibrator=(Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		stopRocker=(Button)view.findViewById(R.id.buttonRockerStop);
		stopRocker.setEnabled(false);
		stopRocker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i=0;i<8;i++) {
					rcOutputs[i] = 0;
				}
				long[] pattern ={200, 2000, 2000, 200, 200, 200};
				vibrator.vibrate(pattern , -1);
				stopRocker.setEnabled(false);
				Log.e(RockerFragment.class.getSimpleName(), "stop");
			}
		});
	}

	@Override
	public void onApiConnected() {
        Drone drone = getDrone();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onApiDisconnected() {

	}
	@Override
	public void onResume() {
		super.onResume();



		Log.e(RockerFragment.class.getSimpleName(), "resume");
		enableRcOverride();
		rockerView1.setRockerChangeListener(new RockerView.RockerChangeListener() {

			@Override
			public void report(float x, float y) {
				// TODO Auto-generated method stub
				// doLog(x + "/" + y);
				// setLayout(rockerView2, (int)x, (int)y);
				//  setLayout(rockerView2, (int)x, (int)y);
				rcOutputs[3]=(int)(x/rockerView1.getR()*400+1500);
				rcOutputs[2]=(int)(-y/rockerView1.getR()*400+1500);
				yawView.setText( String.format("Yaw %d", rcOutputs[3]));
				throttlView.setText( String.format("Throttle %d", rcOutputs[2]));
			}
		});
		rockerView2.setRockerChangeListener(new RockerView.RockerChangeListener() {

			@Override
			public void report(float x, float y) {
				// TODO Auto-generated method stub
				// doLog(x + "/" + y);
				//   setLayout(rockerView1, (int) x, (int) y);
				Log.e(RockerFragment.class.getSimpleName(), "rocker");
				rcOutputs[0]=(int)(x/rockerView2.getR()*400+1500);
				rcOutputs[1]=(int)(y/rockerView2.getR()*400+1500);
				rollView.setText( String.format("Roll %d", rcOutputs[0]));
				pitchView.setText( String.format("Pitch %d",  rcOutputs[1]));

			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(RockerFragment.class.getSimpleName(), "pause");
		disableRcOverride();
		for(int i=0;i<8;i++) {
			rcOutputs[i] = 0;
		}
		getDrone().stopMagnetometerCalibration();


	}


	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				handler.postDelayed(this, TIME);
				if(rcOutputs[0]!=0||rcOutputs[2]!=0){
					stopRocker.setEnabled(true);
				}
				//rollView.setText(String.format("Roll %d", rcOutputs[2]++));
				// MavLinkRC.sendRcOverrideMsg(myDrone, rcOutputs);
			//	MavLinkRC.sendRcOverrideMsg(getDrone()., rcOutputs);
				//   drone.getState().sendRcOverrideMsg(rcOutputs);

				//Log.e(RockerFragment.class.getSimpleName(), "ddd");
				//drone.getState().changeFlightMode("Land");

				//droneApi.

				     getDrone().stopMagnetometerCalibration();
				/*
				mymavLinkPayload.putShort((short) rcOutputs[0]);
				mymavLinkPayload.putShort((short) rcOutputs[1]);
				mymavLinkPayload.putShort((short) rcOutputs[2]);
				mymavLinkPayload.putShort((short) rcOutputs[3]);
				mymavLinkPayload.putShort((short) rcOutputs[4]);
				mymavLinkPayload.putShort((short) rcOutputs[5]);
				mymavLinkPayload.putShort((short) rcOutputs[6]);
				mymavLinkPayload.putShort((short) rcOutputs[7]);

				mymavLinkPacket.len=18;
				mymavLinkPacket.msgid=70;
				mymavLinkPacket.payload=mymavLinkPayload;
				mymavLinkPacket.generateCRC();

				mymavLinkMessage.pack();
				msg_rc_channels_override msg = new msg_rc_channels_override();
				msg.chan1_raw = (short) rcOutputs[0];
				msg.chan2_raw = (short) rcOutputs[1];
				msg.chan3_raw = (short) rcOutputs[2];
				msg.chan4_raw = (short) rcOutputs[3];
				msg.chan5_raw = (short) rcOutputs[4];
				msg.chan6_raw = (short) rcOutputs[5];
				msg.chan7_raw = (short) rcOutputs[6];
				msg.chan8_raw = (short) rcOutputs[7];

				;


				mymavLinkMessage.pack();



				mymessageWrapper.setMavLinkMessage(mymavLinkMessage);
				//droneApi.arm(true);
				//getDrone().sendMavlinkMessage(mymessageWrapper);
*/
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	};
	public void disableRcOverride() {
		handler.removeCallbacks(runnable);
	}

	public void enableRcOverride() {
		//  Toast.makeText(getActivity().getApplicationContext(), "LB", Toast.LENGTH_LONG).show();
		//   MavLinkRC.sendRcOverrideMsg(drone, rcOutputs);
		handler.postDelayed(runnable,TIME);
	}

}
