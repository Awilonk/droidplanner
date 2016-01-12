package com.playuav.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.playuav.android.DroidPlannerApp;
import com.playuav.android.R;
import com.playuav.android.RockerView;
import com.playuav.android.lib.model.IDroneApi;

import org.droidplanner.core.model.Drone;

/**
 * Used to calibrate the drone's compass and accelerometer.
 */
public class RockerFragment extends Fragment {
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
    private DroidPlannerApp dpApp;
    protected com.o3dr.android.client.Drone getDrone() {
        return dpApp.getDrone();
    }

    private Drone drone  ;
    public int[] rcOutputs = new int[8];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view= inflater.inflate(R.layout.fragment_rocker, container, false);

        rockerView1 = (RockerView)view.findViewById(R.id.rockerView1);
        rockerView2 = (RockerView) view.findViewById(R.id.rockerView2);
        throttlView=(TextView) view.findViewById(R.id.textViewRCThro);
        yawView=(TextView)view.findViewById(R.id.textViewRCYaw);
        rollView=(TextView)view.findViewById(R.id.textViewRCRoll);
        pitchView=(TextView)view.findViewById(R.id.textViewRCPitch);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
       // for(int i=0;i<8;i++){
        //    rcOutputs[i] =0xffff;
       // }



    }


    @Override
    public void onResume() {
        super.onResume();

        enableRcOverride();
        rockerView1.setRockerChangeListener(new RockerView.RockerChangeListener() {

            @Override
            public void report(float x, float y) {
                // TODO Auto-generated method stub
                // doLog(x + "/" + y);
                // setLayout(rockerView2, (int)x, (int)y);
              //  setLayout(rockerView2, (int)x, (int)y);
                rcOutputs[0]=(int)(x/rockerView1.getR()*500+1500);
                rcOutputs[1]=(int)(y/rockerView1.getR()*500+1500);
                yawView.setText( String.format("Yaw %d", rcOutputs[0]));
                throttlView.setText( String.format("Throttle %d", rcOutputs[1]));
            }
        });
        rockerView2.setRockerChangeListener(new RockerView.RockerChangeListener() {

            @Override
            public void report(float x, float y) {
                // TODO Auto-generated method stub
                // doLog(x + "/" + y);
             //   setLayout(rockerView1, (int) x, (int) y);
                rcOutputs[2]=(int)(x/rockerView2.getR()*500+1500);
                rcOutputs[3]=(int)(y/rockerView2.getR()*500+1500);
                rollView.setText( String.format("Roll %d", rcOutputs[2]));
                pitchView.setText( String.format("Pitch %d",  rcOutputs[3]));

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        disableRcOverride();

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, TIME);
                rollView.setText(String.format("Roll %d", rcOutputs[2]++));
               // MavLinkRC.sendRcOverrideMsg(myDrone, rcOutputs);

                     //   drone.getState().sendRcOverrideMsg(rcOutputs);

                Log.e(RockerFragment.class.getSimpleName(), "ddd");
                //drone.getState().changeFlightMode("Land");
                //getDrone().arm(true);
                //droneApi.
                   //     getDrone().
                droneApi.arm(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public void setLayout(View v, int dx, int dy) {
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
        if (left < 0) {
            left = 0;
            right = left + v.getWidth();
        }
        if (right > screenWidth) {
            right = screenWidth;
            left = right - v.getWidth();
        }
        if (top < 0) {
            top = 0;
            bottom = top + v.getHeight();
        }
        if (bottom > screenHeight) {
            bottom = screenHeight;
            top = bottom - v.getHeight();
        }
        v.layout(left, top, right, bottom);
    }

    public RockerFragment() {

    }

    public void disableRcOverride() {
        handler.removeCallbacks(runnable);
    }

    public void enableRcOverride() {
          //  Toast.makeText(getActivity().getApplicationContext(), "LB", Toast.LENGTH_LONG).show();
                 //   MavLinkRC.sendRcOverrideMsg(drone, rcOutputs);
            handler.postDelayed(runnable,TIME);
    }







}
