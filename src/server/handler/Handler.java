package server.handler;

import http.Request;
import http.Response;
import http.StatusCodes;
import server.database.DBController;
import server.user.User;
import server.user.UserUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Handler {
	public static void handleRegister(Request request, DataOutputStream writer) throws IOException {
		HashMap<String, String> form = request.getFormData();
		if (User.isFormValid(form)) {
			User user = User.fromForm(form);
			System.out.println(user.toString());
			DBController.addUser(user);

			Response response = Response.generateResponse(StatusCodes.Created);
			writer.writeBytes(response.toString());
		} else {
			Response response = Response.generateResponse(StatusCodes.BadRequest);
			try {
				writer.writeBytes(response.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void handleLogin(Request request, DataOutputStream writer) {
		if (UserUtils.verifyLoginForm(request.getFormData())) {
			User user = DBController.getUser(request.getFormData().get("username"));
			if (user != null){
				Response response = Response.generateResponse(StatusCodes.Accepted);
				String token = UserUtils.generateToken(user);
				response.setHeader("Token", token);
				response.setHeader("Set-Cookie", String.format("Auth=%s", token));
//				response.setBody("Successfully logged in!");
				System.out.println(response.toString());
				try {
					writer.writeBytes(response.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		Response resp = Response.generateResponse(StatusCodes.Unauthorized);
		resp.setBody("Unauthorized");
		try {
			writer.writeBytes(resp.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
