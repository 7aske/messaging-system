package client;

import http.Request;
import http.Response;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import server.user.User;
import server.user.UserUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Client extends Application {
	public static final String DEFAULT_TITLE = "Client";
	public static int height = 600;
	public static int width = 800;


	// TODO: state machine
	private String token;
	private String username;

	@Override
	public void start(Stage stage) throws Exception {
		renderLogin(stage);

		stage.setTitle(Client.DEFAULT_TITLE);
		stage.show();
	}

	public void renderLogin(Stage stage) {

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, Client.width, Client.height);

		// TITLE START
		FlowPane title = new FlowPane();
		title.setAlignment(Pos.CENTER);

		Label lblTitle = new Label("Client App");
		lblTitle.setFont(new Font(44));


		title.getChildren().add(lblTitle);

		// TITLE END

		//SERVER PICKER START

		HBox serverPicker = new HBox();

		serverPicker.setPadding(new Insets(5));

		Label lblAddress = new Label("IP Address: ");
		Label lblPort = new Label("Port: ");


		lblAddress.setPadding(new Insets(5));

		lblPort.setPadding(new Insets(5));

		TextField tfAddress = new TextField();
		TextField tfPort = new TextField();

		tfAddress.setMaxWidth(100);
		tfAddress.setMinWidth(100);

		tfPort.setMaxWidth(100);
		tfPort.setMinWidth(100);
		tfAddress.setText("127.0.0.1");
		tfPort.setText("8000");

		serverPicker.getChildren().addAll(lblAddress, tfAddress, lblPort, tfPort);

		//SERVER PICKER END

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

			this.username = username;

			HashMap<String, String> form = new HashMap<>();
			form.put("username", username);
			form.put("password", password);

			Request loginRequest = Request.generateRequest();
			loginRequest.setMethod("POST");
			loginRequest.setPath("/login");
			loginRequest.setFormData(form);
			loginRequest.setHeader("User", username);

			System.out.println(loginRequest.toString());

			String hostname = tfAddress.getText();
			int port = Integer.parseInt(tfPort.getText());

			try {
				Response response = loginRequest.send(hostname, port);
				if (response.getStatusCode() == 202) {
					this.token = response.getHeader("Token").getValue();
					renderHome(stage);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}


		});
		login.getChildren().addAll(lblUsername, tfUsername, lblPassword, tfPassword, btnLogin);
		//LOGIN END


		root.setTop(title);
		root.setCenter(login);
		root.setBottom(serverPicker);

		stage.setScene(scene);
	}

	public void renderHome(Stage stage) throws IOException {
		BorderPane borderPane = new BorderPane();
		Scene scene = new Scene(borderPane, Client.width, Client.height);

		ArrayList<User> contacts = new ArrayList<>();

		ListView<User> sidebar = new ListView<>(FXCollections.observableArrayList(contacts));

		sidebar.setCellFactory(new Callback<ListView<User>, ListCell<User>>(){

			@Override
			public ListCell<User> call(ListView<User> p) {

				ListCell<User> cell = new ListCell<User>(){

					@Override
					protected void updateItem(User t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.getFirstName() + " " + t.getLastName());
						}
					}

				};

				return cell;
			}
		});

		borderPane.setLeft(sidebar);

		Request request = Request.generateRequest();

		request.setPath("/api/get?");
		request.setMethod("GET");
		request.setHeader("User", this.username);
		request.setHeader("Auth", this.token);
		Response resp = request.send("127.0.0.1", 8000);

		String[] userStrings = resp.getBody().split(Request.CLRF);

		for(String u : userStrings){
			contacts.add(UserUtils.fromResponseString(u));
		}

		sidebar.setItems(FXCollections.observableArrayList(contacts));

		System.out.println(Arrays.toString(userStrings));


		stage.setScene(scene);
		stage.show();
	}
}
