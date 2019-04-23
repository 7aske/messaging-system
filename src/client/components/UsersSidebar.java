package client.components;


import client.Client;
import http.Request;
import http.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import server.user.User;
import server.user.UserUtils;

import java.io.IOException;
import java.util.ArrayList;

public class UsersSidebar extends ListView {
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
		if (query == null){
			queryString = "";
		}
		Request request = Request.generateRequest();
		System.out.println(queryString);
		request.setPath("/api/get?" + queryString);
		request.setMethod("GET");
		request.setHeader("User", Client.state.getUsername());
		request.setHeader("Auth", Client.state.getToken());
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

