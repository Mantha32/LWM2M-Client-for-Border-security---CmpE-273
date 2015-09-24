package lwm2m.objects;

import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import lwm2m.client.ClientA;

public class VisualSensorObject {
	
private int objectId;
	
	private Boolean DeviceOn;
	private int HorizontalMovement;
	private int VerticalMovement;
	private int ObservationId;
	
	public VisualSensorObject(){
		this.objectId=15;
		this.HorizontalMovement=0;
		this.VerticalMovement=0;
		this.ObservationId=0;
		this.DeviceOn=false;
	}
	
	public void pushToDB(ClientA a1) throws UnknownHostException {

		Gson gson = new Gson();
		String myServer = gson.toJson(this);

		//System.out.println("\n"+myJson);
		DBObject dbo = (DBObject) JSON.parse(myServer);
			a1.VisualInfo.remove(new BasicDBObject());
		a1.VisualInfo.insert(dbo);
	}


}
