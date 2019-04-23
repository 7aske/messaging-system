package client.components;


import client.Client;
import http.Request;
import http.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import server.user.User;
import server.user.UserUtils;

import java.io.IOException;
import java.util.ArrayList;


public class Sidebar extends FlowPane {
	public UsersSidebar usersSidebar;
	public TextField search;

	public Sidebar() {
		this.usersSidebar = new UsersSidebar(FXCollections.observableArrayList(Client.state.getContacts()));
		this.usersSidebar.setMinHeight(Client.state.getHeight() - 30);


		this.search = new TextField();
		this.search.setPromptText("Search");
		this.search.setMinWidth(250);
		this.search.setMinHeight(30);
		this.search.setOnKeyReleased(e -> {
			try {
				this.usersSidebar.updateSidebar(this.search.getText());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		this.setMinWidth(250);
		this.setMaxWidth(250);


		this.getChildren().addAll(search, usersSidebar);
	}

	public void updateSidebar(String query) {
		try {
			this.usersSidebar.updateSidebar(query);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onClick(EventHandler<MouseEvent> fn) {
		this.usersSidebar.setOnMouseClicked(fn);
	}

	public User getSelectedItem() {
		return (User) this.usersSidebar.getSelectionModel().getSelectedItem();
	}

	static class UsersSidebar extends ListView {
		public UsersSidebar() {
		}

		public UsersSidebar(ObservableList<User> items) {
			super(items);
			this.setCellFactory(param -> new ListCell<User>() {
				@Override
				protected void updateItem(User item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null || item.getUsername() == null) {
						setText(null);
					} else {
						setText(item.getFirstName() + " " + item.getLastName());
					}
				}
			});
			try {
				this.updateSidebar(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void updateSidebar(String query) throws IOException {
			String queryString = String.format("username=%s&firstName=%s&lastName=%s&company=%s", query, query, query, query);
			if (query == null) {
				queryString = "";
			}
			Request request = Request.generateRequest();
			System.out.println(queryString);
			request.setPath("/api/users?" + queryString);
			request.setMethod("GET");
			request.setHeader("User", Client.state.getUsername());
			request.setHeader("Token", Client.state.getToken());
			Response resp = request.send(Client.state.getServer(), Client.state.getPort());
			System.out.println(resp.toString());
			if (resp.getStatusCode() == 200) {
				ArrayList<User> newContacts = new ArrayList<>();
				String[] userStrings = resp.getBody().split(Request.CLRF);

				for (String u : userStrings) {
					User newuser = UserUtils.fromResponseString(u);
					if (newuser.getUsername() != null)
						newContacts.add(newuser);
				}
				Client.state.setContacts(newContacts);
				this.setItems(FXCollections.observableArrayList(Client.state.getContacts()));
				System.out.println(Client.state.toString());
			} else {
				Client.showMessage("Error", "Error", "Error fetching data from server", Alert.AlertType.ERROR);
			}
		}

	}


}
