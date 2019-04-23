package client.components;

import client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import server.user.User;

import java.util.ArrayList;

public class ComposeComponent extends VBox {
	public Label lblTo;
	public Label lblSubject;
	public Label lblMessage;
	public TextField tfTo;
	public TextField tfSubject;
	public TextArea taMessage;

	public Button btnSend;
	public Button btnReset;

	public ComposeComponent() {

		this.setMinWidth(550);
		this.setPadding(new Insets(5, 20, 20, 20));
		this.setAlignment(Pos.TOP_LEFT);
		this.setSpacing(10);

		this.lblTo = new Label("To:");
		this.lblSubject = new Label("Subject:");
		this.lblMessage = new Label("Message:");

		this.tfTo = new TextField();
		this.tfSubject = new TextField();
		this.taMessage = new TextArea();

		this.tfTo.setOnKeyReleased(e -> {
			String text = this.tfTo.getText();
			ArrayList<User> contacts = Client.state.getContacts();
			boolean contains = false;
			for (User u : contacts) {
				if (u.getUsername().equals(text)) {
					contains = true;
					break;
				}
			}
			if (!contains){
				this.tfTo.setStyle("-fx-text-fill: red");
			} else {
				this.tfTo.setStyle(null);
			}
		});


		this.btnSend = new Button("Send");
		this.btnReset = new Button("Reset");
		this.btnSend.setMaxWidth(75);
		this.btnReset.setMaxWidth(75);
		this.btnReset.setOnMouseClicked(e -> {
			this.tfTo.setText("");
			this.tfSubject.setText("");
			this.taMessage.setText("");
		});

		this.getChildren().addAll(this.lblTo, this.tfTo, this.lblSubject, this.tfSubject, this.lblMessage, this.taMessage, this.btnSend, this.btnReset);

	}
}