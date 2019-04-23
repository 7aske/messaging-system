package client;

import client.scenes.HomeScene;
import client.scenes.LoginScene;
import client.state.State;
import http.Request;
import http.Response;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

public class Client extends Application {
	public static final String DEFAULT_TITLE = "Client";

	public static final State state = new State();

	@Override
	public void start(Stage stage) throws Exception {
		renderLogin(stage);
		stage.setTitle(Client.DEFAULT_TITLE);
		stage.show();
	}

	public void renderLogin(Stage stage) {
		LoginScene scene = new LoginScene(new BorderPane(), Client.state.getWidth(), Client.state.getHeight());
		scene.btnLogin.setOnMouseClicked(e -> {
			String username = scene.tfUsername.getText();
			String password = scene.tfPassword.getText();

			Client.state.setUsername(username);

			HashMap<String, String> form = new HashMap<>();
			form.put("username", username);
			form.put("password", password);

			Request loginRequest = Request.generateRequest();
			loginRequest.setMethod("POST");
			loginRequest.setPath("/login");
			loginRequest.setFormData(form);
			loginRequest.setHeader("User", username);

			System.out.println(loginRequest.toString());

			String hostname = scene.tfAddress.getText();
			int port = Integer.parseInt(scene.tfPort.getText());

			try {
				Response response = loginRequest.send(hostname, port);
				if (response.getStatusCode() == 202) {
					Client.state.setToken(response.getHeader("Token").getValue());
					Client.state.setServer(hostname);
					Client.state.setPort(port);
					renderHome(stage);
				} else {
					showMessage("Invalid credentials", "Invalid credentials", "Invalid username or password", Alert.AlertType.ERROR);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}


		});

		scene.tfPassword.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				Event.fireEvent(scene.btnLogin, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
						0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
						true, true, true, true, true, true, null));

			}
		});
		scene.tfUsername.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				Event.fireEvent(scene.btnLogin, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
						0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
						true, true, true, true, true, true, null));

			}
		});

		stage.setScene(scene);
	}

	public void renderHome(Stage stage) throws IOException {
		HomeScene scene = new HomeScene(new BorderPane(), Client.state.getWidth(), Client.state.getHeight());

		stage.setScene(scene);
	}

	public static void showMessage(String title, String header, String text, Alert.AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);

		alert.showAndWait();
	}
}
