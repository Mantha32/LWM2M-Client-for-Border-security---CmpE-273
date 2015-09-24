package lwm2m.objects;

import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import lwm2m.client.ClientA;

public class InraredSensorObject {
	
private int objectId;
	
	private Boolean HumanDetected;
	private double Intensity;
	private int ObservationId;
	
	public InraredSensorObject(){
		this.objectId=14;
		this.Intensity=0.0;
		this.ObservationId=0;
		this.HumanDetected=false;
	}
	
	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public Boolean getHumanDetected() {
		return HumanDetected;
	}

	public void setHumanDetected(Boolean humanDetected) {
		HumanDetected = humanDetected;
	}

	public double getIntensity() {
		return Intensity;
	}

	public void setIntensity(double intensity) {
		Intensity = intensity;
	}

	public int getObservationId() {
		return ObservationId;
	}

	public void setObservationId(int observationId) {
		ObservationId = observationId;
	}

	public void pushToDB(ClientA a1) throws UnknownHostException {

		Gson gson = new Gson();
		String myServer = gson.toJson(this);

		//System.out.println("\n"+myJson);
		DBObject dbo = (DBObject) JSON.parse(myServer);
			a1.InfraRedInfo.remove(new BasicDBObject());
		a1.InfraRedInfo.insert(dbo);
	}

}
