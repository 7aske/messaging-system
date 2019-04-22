package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Client extends Application {
	public static final String DEFAULT_TITLE = "Client";
	public static int height = 600;
	public static int width = 800;


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

		// LOGIN START
		VBox login = new VBox();

		login.setPadding(new Insets(100, 300, 50, 300));
		login.setSpacing(10);

		Label lblUsername = new Label("Username");
		Label lblPassword = new Label("Password");

		TextField tfUsername = new TextField();
		TextField tfPassword = new TextField();

		Button btnLogin = new Button("Login");

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

		root.setTop(title);
		root.setCenter(login);
		root.setBottom(serverPicker);

		stage.setScene(scene);
	}
}
