package client.scenes;

import client.components.ComposeComponent;
import client.components.MessagesComponent;
import client.components.SidebarComponent;
import client.components.TopControls;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import server.user.User;


public class HomeScene extends Scene {
	public TopControls top;
	public SidebarComponent left;
	public ComposeComponent compose;
	public MessagesComponent messages;

	public HomeScene(Parent root, double width, double height) {
		super(root, width, height);
		//COMPOSE START
		this.compose = new ComposeComponent();
		//COMPOSE END

		//MESSAGES START
		this.messages = new MessagesComponent();

		//MESSAGES END

		// SIDEBAR START
		this.left = new SidebarComponent();
		this.left.onClick(e -> {
			User user = this.left.getSelectedItem();
			System.out.println(user.toString());
			this.compose.tfTo.setText(user.getUsername());
			this.compose.tfSubject.setText("Message");
			this.compose.taMessage.setText(String.format("Dear %s,\n", user.getFirstName()));
		});

		//SIDEBAR END

		//MESSAGES START
		this.messages = new MessagesComponent();
		this.messages.onClick(e -> {
			if (this.messages.getSelectedItem() != null)
				this.messages.taMessage.setText(this.messages.getSelectedItem().toPrettyString());
		});
		//MESSAGES END

		// TOP START
		this.top = new TopControls();
		this.top.btnCompose.setOnMouseClicked(e -> {
			((BorderPane) this.getRoot()).setCenter(this.compose);
		});
		this.top.btnMessages.setOnMouseClicked(e -> {
			((BorderPane) this.getRoot()).setCenter(this.messages);
		});
		this.top.btnRefresh.setOnMouseClicked(e -> {
			this.left.search.setText("");
			this.left.updateComponent(null);
			this.messages.updateComponent();
		});
		// TOP END

		((BorderPane) this.getRoot()).setLeft(this.left);
		((BorderPane) this.getRoot()).setCenter(this.compose);
		((BorderPane) this.getRoot()).setTop(this.top);
		this.compose.tfTo.requestFocus();
	}
}

