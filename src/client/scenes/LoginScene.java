package client.scenes;

import http.Request;
import http.Response;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class LoginScene  extends Scene{
	private BorderPane root;
	public LoginScene(Parent root) {
		super(root);
	}

	public LoginScene(BorderPane root, double width, double height) {
		super(root, width, height);
		// TITLE START
		FlowPane title = new FlowPane();
		title.setAlignment(Pos.CENTER);

		Label lblTitle = new Label("Client App");
		lblTitle.setFont(new Font(44));


		title.getChildren().add(lblTitle);

		// TITLE END

		// LOGIN START
		VBox login = new VBox();

		login.setPadding(new Insets(100, 300, 50, 300));
		login.setSpacing(10);

		Label lblUsername = new Label("Username");
		Label lblPassword = new Label("Password");

		TextField tfUsername = new TextField();
		PasswordField tfPassword = new PasswordField();

		Button btnLogin = new Button("Login");

		btnLogin.setOnMouseClicked(e -> {
			String username = tfUsername.getText();
			String password = tfPassword.getText();

			HashMap<String, String> form = new HashMap<>();
			form.put("username", username);
			form.put("password", password);

			Request loginRequest = Request.generateRequest();
			loginRequest.setMethod("POST");
			loginRequest.setPath("/login");
			loginRequest.setFormData(form);
			loginRequest.setHeader("User", username);
			System.out.println(loginRequest.toString());
			String hostname = "127.0.0.1";
			int port = 8000;

			InetAddress addr = null;
			try {
				addr = InetAddress.getByName(hostname);
				Socket socket = new Socket(addr, port);
				DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer.writeBytes(loginRequest.toString());

				Response response = Response.generateResponse(reader);
				System.out.println(response.toString());

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		login.getChildren().addAll(lblUsername, tfUsername, lblPassword, tfPassword, btnLogin);
		//LOGIN END

		//SERVER PICKER START

		HBox serverPicker = new HBox();

		serverPicker.setPadding(new Insets(5));

		Label lblAddress = new Label("IP Address: ");
		Label lblPort = new Label("Port: ");

		TextField tfAddress = new TextField();
		TextField tfPort = new TextField();

		serverPicker.getChildren().addAll(lblAddress, tfAddress, lblPort, tfPort);

		//SERVER PICKER END

		this.root.setTop(title);
		this.root.setCenter(login);
		this.root.setBottom(serverPicker);
	}

	public LoginScene(Parent root, Paint fill) {
		super(root, fill);
	}

	public LoginScene(Parent root, double width, double height, Paint fill) {
		super(root, width, height, fill);
	}

	public LoginScene(Parent root, double width, double height, boolean depthBuffer) {
		super(root, width, height, depthBuffer);
	}

	public LoginScene(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
		super(root, width, height, depthBuffer, antiAliasing);
	}


}
