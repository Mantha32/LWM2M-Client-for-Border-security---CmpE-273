package lwm2m.objects;

import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import lwm2m.client.ClientA;

public class UgsPersonObject {

	private int objectId;
	
	private Boolean PersonDetected;
	private int PersonCount;
	private int ObservationId;
	
	public UgsPersonObject(){
		this.objectId=13;
		this.PersonCount=0;
		this.ObservationId=0;
		this.PersonDetected=false;
	}
	
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	public Boolean getPersonDetected() {
		return PersonDetected;
	}
	public void setPersonDetected(Boolean personDetected) {
		PersonDetected = personDetected;
	}
	public int getPersonCount() {
		return PersonCount;
	}
	public void setPersonCount(int personCount) {
		PersonCount = personCount;
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
			a1.UGSPersonInfo.remove(new BasicDBObject());
		a1.UGSPersonInfo.insert(dbo);
	}

}
