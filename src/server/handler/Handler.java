package server.handler;

import http.Request;
import http.Response;
import http.StatusCodes;
import server.database.DBController;
import server.user.User;
import server.user.UserUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Handler {
	public static void handleRegister(Request request, DataOutputStream writer) throws IOException {
		HashMap<String, String> form = request.getFormData();
		if (HandlerUtils.verifyRegisterForm(form) && request.getMethod().compareToIgnoreCase("POST") == 0) {
			User user = User.fromForm(form);
			if (User.isValid(user)){
				System.out.println(user.toString());
				DBController.addUser(user);

				Response response = Response.generateResponse(StatusCodes.Created);
				String token = UserUtils.generateToken(user);
				response.setHeader("Token", token);
				response.setHeader("Set-Cookie", String.format("Auth=%s", token));
				writer.writeBytes(response.toString());
				return;
			} else {
				Response resp = Response.generateResponse(StatusCodes.BadRequest);
				resp.setBody("Invalid form");
				writer.writeBytes(resp.toString());
				return;
			}
		}
		Response resp = Response.generateResponse(StatusCodes.Unauthorized);
		resp.setBody("Unauthorized");
		writer.writeBytes(resp.toString());
	}

	public static void handleLogin(Request request, DataOutputStream writer) throws IOException {
		if (HandlerUtils.verifyLoginForm(request.getFormData()) && request.getMethod().compareToIgnoreCase("POST") == 0) {
			User user = DBController.getUser(request.getFormData().get("username"));
			if (user != null) {
				Response response = Response.generateResponse(StatusCodes.Accepted);
				String token = UserUtils.generateToken(user);
				response.setHeader("Token", token);
				response.setHeader("Set-Cookie", String.format("Auth=%s", token));
				response.setBody(String.format("Auth=%s\r\n", token));
				System.out.println(response.toString());
				writer.writeBytes(response.toString());
				return;
			}
		}
		Response resp = Response.generateResponse(StatusCodes.Unauthorized);
		resp.setBody("Unauthorized");
		writer.writeBytes(resp.toString());
	}

	public static void handleApi(Request request, DataOutputStream writer) throws IOException {
		String token = HandlerUtils.getToken(request);

		if (token.equals("")) {
			Response resp = Response.generateResponse(StatusCodes.Unauthorized);
			resp.setBody("Unauthorized");
			writer.writeBytes(resp.toString());
			return;
		}

		String user = request.getHeader("User").getValue();

		System.out.println(token);
		if (!UserUtils.validateToken(user, token)) {
			Response resp = Response.generateResponse(StatusCodes.Unauthorized);
			resp.setBody("Unauthorized");
			writer.writeBytes(resp.toString());
			return;
		}

		if (request.getPath().startsWith("/api/users?")) {
			String[] requestParams = request.getPath().split("\\?");
			HashMap<String, String> sqlParams = new HashMap<>();
			if (requestParams.length == 2) {
				String[] queryParams = requestParams[1].split("&");
				for (String q : queryParams) {
					String[] keyValuePair = q.split("=");
					if (keyValuePair.length == 2)
						sqlParams.put(keyValuePair[0], keyValuePair[1]);
				}
			} else {
				sqlParams = null;
			}

			ArrayList<User> users = DBController.getUsers(sqlParams);
			StringBuilder responseBody = new StringBuilder();
			for (User u : users) {
				responseBody.append(u.asResponseString());
			}
			Response response = Response.generateResponse(StatusCodes.OK);
			response.setHeader("Content-Type", "application/x-www-form-urlencoded");
			response.setBody(responseBody.toString());
			writer.writeBytes(response.toString());
			return;
		} else if (request.getPath().startsWith("/api/messages?")) {

		}
		Response response = Response.generateResponse(StatusCodes.NotFound);
		response.setBody("( ͡° ʖ̯ ͡°) 404 Not Found");
		writer.writeBytes(response.toString());

	}
	public static void handleMessage(Request request, DataOutputStream writer) {

	}
}
