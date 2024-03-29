package restapi.team1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import controller.UserController;
import entity.User;

@Path("/team1/user")
public class UserService {
	UserController uc = new UserController();
	
	@GET
	@Produces("application/json")
	public Response getAllUser() throws JSONException {

		JSONObject jsonObject = uc.getAllUser();

		return createResponse(jsonObject);
	}

	@Path("/username/{s}")
	@GET
	@Produces("application/json")
	public Response getUserWithUsername(@PathParam("s") String user) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		if(!validateString(user)) {
			jsonObject.put("result", false); 
			jsonObject.put("msg", "Bad string");
			return createResponse(jsonObject);
		}

		
		jsonObject = uc.getUser(user); 
		
		return createResponse(jsonObject);
	}
	
	@Path("/uid/{s}")
	@GET
	@Produces("application/json")
	public Response getUserWithUID(@PathParam("s") String user) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		if(!validateString(user)) {
			jsonObject.put("result", false);
			jsonObject.put("msg", "Bad string");
			return createResponse(jsonObject);
		}
		
		jsonObject = uc.getUserWithUID(user); 

		return createResponse(jsonObject);
	}
	
	@Path("/uid/public/{s}")
	@GET
	@Produces("application/json")
	public Response getUserPublicInfoWithUID(@PathParam("s") String user) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		if(!validateString(user)) {
			jsonObject.put("result", false);
			jsonObject.put("msg", "Bad string");
			return createResponse(jsonObject);
		}
		
		jsonObject = uc.getUserPublicInfo(user); 

		return createResponse(jsonObject);
	}
	
	@Path("/create/{user}/{password : .+}/{salt}/{fname}/{lname}/{nric}/"
			+ "{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}"
			+ "/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}")
	@GET
	@Produces("application/json")
	public Response createUser(@PathParam("user") String user, @PathParam("password") String password, @PathParam("salt") String salt,
			@PathParam("fname") String fname, @PathParam("lname") String lname, @PathParam("nric") String nric, 
			@PathParam("dob") String dob, @PathParam("gender") String gender,
			@PathParam("phone1") String phone1, @PathParam("phone2") String phone2, @PathParam("phone3") String phone3,
			@PathParam("addr1") String addr1, @PathParam("addr2") String addr2, @PathParam("addr3") String addr3,
			@PathParam("zip1") int zip1, @PathParam("zip2") int zip2, @PathParam("zip3") int zip3,
			@PathParam("qualify") int qualify, @PathParam("bloodtype") String bloodType, @PathParam("nfcid") String nfcid
			) throws JSONException {

		JSONObject jsonObject = uc.createUser(user, password, salt, fname, lname, nric, dob, gender.charAt(0), phone1, phone2, phone3, 
				addr1, addr2, addr3, zip1, zip2, zip3, qualify, bloodType, nfcid);

		return createResponse(jsonObject);
	}
	
	@Path("/update/{uid}/{user}/{password : .+}/{salt}/{fname}/{lname}/{nric}/"
			+ "{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}"
			+ "/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}")
	@GET
	@Produces("application/json")
	public Response updateUser(@PathParam("uid") String uid, @PathParam("user") String user, @PathParam("password") String password,
			@PathParam("salt") String salt, @PathParam("fname") String fname, @PathParam("lname") String lname, @PathParam("nric") String nric, 
			@PathParam("dob") String dob, @PathParam("gender") String gender,
			@PathParam("phone1") String phone1, @PathParam("phone2") String phone2, @PathParam("phone3") String phone3,
			@PathParam("addr1") String addr1, @PathParam("addr2") String addr2, @PathParam("addr3") String addr3,
			@PathParam("zip1") int zip1, @PathParam("zip2") int zip2, @PathParam("zip3") int zip3,
			@PathParam("qualify") int qualify, @PathParam("bloodtype") String bloodType, @PathParam("nfcid") String nfcid
			) throws JSONException {

		JSONObject jsonObject = uc.updateUser(uid, user, password, salt, fname, lname, nric, dob, gender.charAt(0), phone1, phone2, phone3, 
				addr1, addr2, addr3, zip1, zip2, zip3, qualify, bloodType, nfcid);

		return createResponse(jsonObject);
	}
	
	@Path("/update/{user}/{password : .+}/{salt}")
	@GET
	@Produces("application/json")
	public Response updateUserPassword(@PathParam("user") String user, @PathParam("password") String password,
			@PathParam("salt") String salt) throws JSONException {

		JSONObject jsonObject = uc.updateUserPassword(user, password, salt);

		return createResponse(jsonObject);
	}
	
	@Path("/delete/{uid}")
	@GET
	@Produces("application/json")
	public Response deleteUser(@PathParam("uid") int uid) throws JSONException {

		JSONObject jsonObject = uc.deleteUser(uid);

		return createResponse(jsonObject);
	}
	
	@Path("/therapists")
	@GET
	@Produces("application/json")
	public Response getAllTherapists() throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject = uc.getTherapists(); 

		return createResponse(jsonObject);
	}
	
	@Path("/secret/set/{uid}/{secret : .+}")
	@GET
	@Produces("application/json")
	public Response setSecret(@PathParam("uid") int uid, @PathParam("secret") String secret) throws JSONException {

		JSONObject jsonObject = uc.userSetSecret(uid, secret);

		return createResponse(jsonObject);
	}
	
	@Path("/secret/{uid}/")
	@GET
	@Produces("application/json")
	public Response getSecret(@PathParam("uid") int uid) throws JSONException {

		JSONObject jsonObject = uc.userGetSecret(uid);
		
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
				.header("ALLOW", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}

	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response createUserPost(User newUser) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = uc.createUser(newUser);

		return createResponse(jsonObject);
	}
	
	private boolean validateString(String username) {
		return StringUtils.isAlphanumeric(username);
	}
	
	@Path("/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response updateUser(User newUser) throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject = uc.updateUser(newUser);

		return createResponse(jsonObject);
	}
	
	@Path("/update/password")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response updateUserPassword(User newUser) throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject = uc.updateUserPassword(newUser);

		return createResponse(jsonObject);
	}
	
}
