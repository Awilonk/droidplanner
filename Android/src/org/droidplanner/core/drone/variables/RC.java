package org.droidplanner.core.drone.variables;

import com.MAVLink.common.msg_rc_channels_raw;
import com.MAVLink.common.msg_servo_output_raw;

import org.droidplanner.core.MAVLink.MavLinkRC;
import org.droidplanner.core.drone.DroneInterfaces.DroneEventsType;
import org.droidplanner.core.drone.DroneVariable;
import org.droidplanner.core.model.Drone;

public class RC extends DroneVariable {

	//private final RockerActivity rockerActivity;
	public int in[] = new int[8];
	public int out[] = new int[8];

	public RC(Drone myDrone) {
		super(myDrone);
	//	this.rockerActivity=new RockerActivity(myDrone);
	}

	public void setRcInputValues(msg_rc_channels_raw msg) {
		in[0] = msg.chan1_raw;
		in[1] = msg.chan2_raw;
		in[2] = msg.chan3_raw;
		in[3] = msg.chan4_raw;
		in[4] = msg.chan5_raw;
		in[5] = msg.chan6_raw;
		in[6] = msg.chan7_raw;
		in[7] = msg.chan8_raw;
		myDrone.notifyDroneEvent(DroneEventsType.RC_IN);
	}

	public void setRcOutputValues(msg_servo_output_raw msg) {
		out[0] = msg.servo1_raw;
		out[1] = msg.servo2_raw;
		out[2] = msg.servo3_raw;
		out[3] = msg.servo4_raw;
		out[4] = msg.servo5_raw;
		out[5] = msg.servo6_raw;
		out[6] = msg.servo7_raw;
		out[7] = msg.servo8_raw;
		myDrone.notifyDroneEvent(DroneEventsType.RC_OUT);
	}
	public void sendRcOverrideMsg(int[] rcOutputs ){
		MavLinkRC.sendRcOverrideMsg(myDrone, rcOutputs);

	}

}
