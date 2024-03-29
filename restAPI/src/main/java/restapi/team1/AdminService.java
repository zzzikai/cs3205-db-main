package restapi.team1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import controller.AdminController;

@Path("/team1/admin")
public class AdminService {

	private AdminController ac = new AdminController();

	@GET
	@Produces("application/json")
	public Response getAllAdmin() throws JSONException {

		JSONObject jsonObject = ac.getAllAdmin();

		return createResponse(jsonObject);
	}

	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getAdminFromUsername(@PathParam("s") String adminuser) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		if(!validateString(adminuser)) {
			jsonObject.put("result", false);
			//String result = "@Produces(\"application/json\") Incorrect username";
			return createResponse(jsonObject);
		}

		System.out.println("Retrieving details of admin account: " + adminuser);
		jsonObject = ac.getAdmin(adminuser);

		return createResponse(jsonObject);
	}
	
	@Path("/secret/set/{adminId}/{secret : .+}")
	@GET
	@Produces("application/json")
	public Response setSecret(@PathParam("adminId") int adminId, @PathParam("secret") String secret) throws JSONException {

		JSONObject jsonObject = ac.adminSetSecret(adminId, secret);

		return createResponse(jsonObject);
	}
	
	@Path("/secret/{adminId}/")
	@GET
	@Produces("application/json")
	public Response getSecret(@PathParam("adminId") int adminId) throws JSONException {

		JSONObject jsonObject = ac.adminGetSecret(adminId);
		
		return createResponse(jsonObject);
	}

	public Response createResponse(JSONObject jsonObject) {
		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			jsonObject.put("msg", "Null object");
		}
		
		return Response.status(200).entity(jsonObject.toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}
	
	private boolean validateString(String username) {
		return StringUtils.isAlphanumeric(username);
	}
}
