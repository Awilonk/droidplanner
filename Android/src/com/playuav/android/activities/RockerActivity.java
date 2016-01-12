package com.playuav.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.playuav.android.R;
import com.playuav.android.RockerView;
import com.playuav.android.lib.model.IDroneApi;

/**
 * This activity holds the SettingsFragment.
 */
public class RockerActivity extends DrawerNavigationUI {
	private RockerView rockerView1;
	private RockerView rockerView2;
	private TextView throttlView;
	private TextView yawView;
	private TextView rollView;
	private TextView pitchView;
	private int TIME=1000;
	private int screenWidth;
	private int screenHeight;
	private IDroneApi droneApi;


	private Context parrentContext;
	private com.o3dr.android.client.Drone drone;


	//private Drone drone  ;
	public int[] rcOutputs = new int[8];
	//public RockerActivity(Drone mydrone){
	//	this.drone=mydrone;

	//}

	//private DroidPlannerApp dpApp;
	private LocalBroadcastManager broadcastManager;

	//protected MissionProxy getMissionProxy() { return dpApp.getMissionProxy(); }
	//protected com.o3dr.android.client.Drone getDrone() {
	//	return dpApp.getDrone();
	//}

	protected LocalBroadcastManager getBroadcastManager() {
		return broadcastManager;
	}




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("lb", "create");

		setContentView(R.layout.fragment_rocker);
		rockerView1 = (RockerView)this.findViewById(R.id.rockerView1);
		rockerView2 = (RockerView) this.findViewById(R.id.rockerView2);
		throttlView=(TextView) this.findViewById(R.id.textViewRCThro);
		yawView=(TextView)this.findViewById(R.id.textViewRCYaw);
		rollView=(TextView)this.findViewById(R.id.textViewRCRoll);
		pitchView=(TextView)this.findViewById(R.id.textViewRCPitch);



		/*
		FragmentManager fm = getFragmentManager();
		Fragment rocker1Fragment = fm.findFragmentById(R.id.rockerView1);
		if (rocker1Fragment == null) {
			rocker1Fragment = new com.playuav.android.RockerFragment();
			fm.beginTransaction().add(R.id.rockerView1, rocker1Fragment).commit();
		}
		*/
	}

	@Override
	protected int getNavigationDrawerEntryId() {
		return R.id.navigation_rocker;
	}

	@Override
	public void onApiConnected() {
       // super.onApiConnected();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("lb", "resume");

		rockerView1.setRockerChangeListener(new RockerView.RockerChangeListener() {

			@Override
			public void report(float x, float y) {
				// TODO Auto-generated method stub
				// doLog(x + "/" + y);
				// setLayout(rockerView2, (int)x, (int)y);
				//  setLayout(rockerView2, (int)x, (int)y);
				rcOutputs[0] = (int) (x / rockerView1.getR() * 500 + 1500);
				rcOutputs[1] = (int) (y / rockerView1.getR() * 500 + 1500);
				yawView.setText(String.format("Yaw %d", rcOutputs[0]));
				throttlView.setText(String.format("Throttle %d", rcOutputs[1]));
			}
		});
		rockerView2.setRockerChangeListener(new RockerView.RockerChangeListener() {

			@Override
			public void report(float x, float y) {
				// TODO Auto-generated method stub
				// doLog(x + "/" + y);
				//   setLayout(rockerView1, (int) x, (int) y);
				rcOutputs[2] = (int) (x / rockerView2.getR() * 500 + 1500);
				rcOutputs[3] = (int) (y / rockerView2.getR() * 500 + 1500);
				rollView.setText(String.format("Roll %d", rcOutputs[2]));
				pitchView.setText(String.format("Pitch %d", rcOutputs[3]));

			}
		});
		enableRcOverride();

	}

	@Override
	public void onPause() {
		super.onPause();
		disableRcOverride();
		Log.e("lb", "pause");

	}
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				handler.postDelayed(this, TIME);
				rollView.setText(String.format("Roll %d", rcOutputs[2]++));
			//	 MavLinkRC.sendRcOverrideMsg(drone, rcOutputs);
//drone.getState().setArmed(true);
				  // drone.getState().sendRcOverrideMsg(rcOutputs);
				//drone.getState().getFlightTime();

				drone.arm(true);
				Log.e("lb", "run");
				//drone.getState().changeFlightMode("Land");
				//getDrone().arm(true);
				//droneApi.
				//     getDrone().
				// droneApi.arm(true);
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
		handler.postDelayed(runnable, TIME);
	}
}
