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
			if (user != null) {
				Response response = Response.generateResponse(StatusCodes.Accepted);
				String token = UserUtils.generateToken(user);
				response.setHeader("Token", token);
				response.setHeader("Set-Cookie", String.format("Auth=%s", token));
				response.setBody(String.format("Auth=%s\r\n", token));
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

	public static void handleApi(Request request, DataOutputStream writer) throws IOException {
		String token = "";
		if (request.containsHeader("Auth")) {
			token = request.getHeader("Auth").getValue();
		} else if (request.containsHeader("Cookie")) {
			String[] cookie = request.getHeader("Cookie").getValue().split("=");
			if (cookie.length == 2) {
				token = cookie[1];
			}
		}

		if (token.equals("")) {
			System.out.println("HERE");
			Response resp = Response.generateResponse(StatusCodes.Unauthorized);
			resp.setBody("Unauthorized");
			writer.writeBytes(resp.toString());
			return;
		}

		// String user = request.getHeader("User").getValue();
		String user = "taske";
		System.out.println(token);
		if (!UserUtils.validateToken(user, token)) {
			System.out.println("HERE2");
			Response resp = Response.generateResponse(StatusCodes.Unauthorized);
			resp.setBody("Unauthorized");
			writer.writeBytes(resp.toString());
			return;
		}

		if (request.getPath().startsWith("/api/get?")) {
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
			response.setBody(responseBody.toString());
			writer.writeBytes(response.toString());
			return;
		}
		Response response = Response.generateResponse(StatusCodes.NotFound);
		response.setBody("( ͡° ʖ̯ ͡°) 404 Not Found");
		writer.writeBytes(response.toString());

	}
}
