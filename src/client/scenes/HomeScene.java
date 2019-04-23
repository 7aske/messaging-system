package client.scenes;

import client.Client;
import client.components.UsersSidebar;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;


public class HomeScene extends Scene {
	public FlowPane left;

	public TextField search;
	public UsersSidebar usersSidebar;

	public HomeScene(Parent root, double width, double height) {
		super(root, width, height);

		this.left = new FlowPane();

		this.search = new TextField();
		this.search.setPromptText("Search");
		this.search.setMinWidth(250);
		this.search.setMinHeight(30);
		this.usersSidebar = new UsersSidebar(FXCollections.observableArrayList(Client.state.getContacts()));
		this.usersSidebar.setMinHeight(Client.state.getHeight()- 30);
		this.search.setOnKeyReleased(e -> {
			try {
				this.usersSidebar.updateSidebar(this.search.getText());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		this.left.getChildren().addAll(search, usersSidebar);


		((BorderPane)this.getRoot()).setLeft(left);

		this.usersSidebar.requestFocus();
	}
}
