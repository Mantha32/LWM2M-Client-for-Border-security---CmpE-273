package lwm2m.observe;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import lwm2m.client.ClientA;
import lwm2m.objects.DeviceObject;
import lwm2m.objects.LocationObject;
import lwm2m.objects.SensorObject;

public class NotifyToServer implements Runnable {

	int objectId;
	int objectInstanceId;
	int resourceId;
	int tokenNo;
	String Value;

	BasicDBObject query1 = new BasicDBObject();

	BasicDBObject fields1 = new BasicDBObject().append("_id", false);
	
	public NotifyToServer(int objectId, int objectInstanceId, int resourceId, int tokenNo) {
		this.objectId = objectId;
		this.objectInstanceId = objectInstanceId;
		this.resourceId = resourceId;
		this.tokenNo = tokenNo;

	}

	@Override
	public void run() {

		System.out.println("Notify Initiated");

		{
			ClientA a1 = new ClientA();
			try {
				a1.connectDb1();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}

			BasicDBObject query = new BasicDBObject();
			query.put("objectId", objectId);
			query.put("objectInstanceId", objectInstanceId);
			query.put("resourceId", resourceId);

			BasicDBObject fields = new BasicDBObject().append("_id", false);
			fields.put("pmin", true);
			fields.put("pmax", true);
			fields.put("cancel", true);
			DBCursor curs = a1.observationInfo.find(query, fields);

			int pmin = 0, pmax = 0;
			boolean cancel_field = true;
			while (curs.hasNext()) {
				DBObject dbo = curs.next();
				pmin = Integer.parseInt(dbo.get("pmin").toString());
				pmax = Integer.parseInt(dbo.get("pmax").toString());
				cancel_field = Boolean.parseBoolean(dbo.get("pmax").toString());
			}

			System.out.println("Notify Initiated for every " + pmin + " seconds with tokenNo: " + tokenNo);
			String resource = null;

			while (!cancel_field) {
				try {

					
					switch (objectId) {
					case 3:
						resource = new DeviceObject().getResourceDescription(resourceId);
						break;
					case 6:
						resource = new LocationObject().getResourceDescription(resourceId);
						break;
						
					case 10:
			
						DBObject fetch1 = a1.PressureInfo.findOne(query1, fields1);
						 Value = fetch1.toString();
						 break;

					case 11:
								
						DBObject fetch2 = a1.UGSVehicleInfo.findOne(query1, fields1);
						 Value = fetch2.toString();
						 break;
	
					case 12:
						
						DBObject fetch3 = a1.WeaponInfo.findOne(query1, fields1);
						 Value = fetch3.toString();
						 break;
	
					case 13:
									
						DBObject fetch4 = a1.UGSPersonInfo.findOne(query1, fields1);
						 Value =fetch4.toString();
						 break;
						 
					case 14:
							
						DBObject fetch5 = a1.InfraRedInfo.findOne(query1, fields1);
						 Value =fetch5.toString();
						 break;
					
					case 15:
						
						DBObject fetch6 = a1.VisualInfo.findOne(query1, fields1);
						 Value =fetch6.toString();
						 break;


					default:
						resource = null;
						break;
					}
					
					
					
					BasicDBObject queryForValue = new BasicDBObject();
					BasicDBObject fieldsForValue = new BasicDBObject(resource, true).append("_id", false);

					DBCursor cursorForValue = a1.getCollection(objectId).find(queryForValue, fieldsForValue);
					
					String v1="";
					while (cursorForValue.hasNext()) {
						DBObject dbo = cursorForValue.next();
					
						v1=dbo.get("predictedObjectCount").toString();
					}
					System.out.println("Notifying the value: " + Value);

					// ---------------------------Notify Started--------------------------//
						
					
					String status = "{ \""+tokenNo+"\" : "+Value+"}";
					String s1 = URLEncoder.encode(Value);
					String new_uri = a1.sec0.getLWM2MServerURI().replaceAll("bs", "");
					new_uri = new_uri + "notify?tokenNo="+tokenNo+"&value="+Value;

					System.out.println("uri for notify "+new_uri);
					
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(new_uri);

					client.execute(get);
//					PostMethod post = new PostMethod(new_uri);
//					@SuppressWarnings("deprecation")
//				StringRequestEntity entity = new StringRequestEntity(value);
//					//StringEntity entity = new StringEntity(string)
//					post.setRequestEntity(entity);
//					HttpClient httpclient = new HttpClient();
//				 	 
//		        	 try {
//						httpclient.executeMethod(post);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
					// ---------------------------Notify Ended--------------------------//


					// ----------check for the cancel flag---------//
		        	 BasicDBObject fieldsForCancelFlag = new BasicDBObject().append("_id", false);
		 			fieldsForCancelFlag.put("cancel", true);
		 			
					DBCursor cursorForCancelFlag = a1.observationInfo.find(query, fieldsForCancelFlag);
					while (cursorForCancelFlag.hasNext()) {
						DBObject dbo = cursorForCancelFlag.next();
						cancel_field = Boolean.parseBoolean(dbo.get("cancel").toString());
					}

					Thread.sleep(pmin * 1000);
			//			break;

				} catch (InterruptedException e) {
					System.out.println("going into exception");
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		System.out.println("Cancel Request Received: Notify Thread Ended.");
	}

}
