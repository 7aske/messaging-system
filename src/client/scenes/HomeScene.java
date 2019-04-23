package client.scenes;

import client.components.ComposeComponent;
import client.components.MessagesComponent;
import client.components.Sidebar;
import client.components.TopControls;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;


public class HomeScene extends Scene {
	public TopControls top;
	public Sidebar left;
	public ComposeComponent compose;
	public MessagesComponent messages;

	public HomeScene(Parent root, double width, double height) {
		super(root, width, height);
		// SIDEBAR START
		this.left = new Sidebar();

		//SIDEBAR END

		//COMPOSE START
		this.compose = new ComposeComponent();

		//COMPOSE END

		//MESSAGES START
		this.messages = new MessagesComponent();

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
			this.left.updateSidebar(null);
		});
		// TOP END

		((BorderPane) this.getRoot()).setLeft(this.left);
		((BorderPane) this.getRoot()).setCenter(this.compose);
		((BorderPane) this.getRoot()).setTop(this.top);
		this.compose.tfTo.requestFocus();
	}
}

