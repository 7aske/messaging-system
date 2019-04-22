package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
	public static final String CLRF = "\r\n";
	public static final String HTTP_VER = "HTTP/1.1";
	private String method;
	private String path;
	private String version;
	private HashMap<String, String> headers;
	private String body = null;

	private Request() {
		this.headers = new HashMap<>();
	}

	public static Request generateRequest(BufferedReader reader) throws IOException {
		Request request = new Request();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty()) {
				StringBuilder buffer = new StringBuilder();
				Map.Entry<String, String> cl = request.getHeader("Content-length");
				if (cl != null) {
					int length = Integer.parseInt(cl.getValue());
					while (length > 0) {
						buffer.append((char) (reader.read()));
						length--;
					}
					request.body = buffer.toString();
				}
				break;
			} else {
				String[] parts = line.split(": ");
				if (parts.length == 2) {
					request.headers.put(parts[0].toLowerCase(), parts[1]);
				} else {
					String[] requestLine = line.split(" ", -1);
					if (requestLine.length == 3) {
						request.method = requestLine[0];
						request.path = requestLine[1];
						request.version = requestLine[2];
					}

				}
			}
		}
		return request;
	}

	public static Request generateRequest() {
		Request request = new Request();
		request.version = Request.HTTP_VER;
		return request;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getVersion() {
		return version;
	}


	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}


	public Map.Entry<String, String> getHeader(String header) {
		for (Map.Entry<String, String> h : this.headers.entrySet()) {
			if (h.getKey().toUpperCase().equals(header.toUpperCase())) {
				return h;
			}
		}
		return null;
	}

	public HashMap<String, String> getFormData() {
		HashMap<String, String> form = new HashMap<>();
		if (this.method.toUpperCase().equals("POST")) {
			Map.Entry<String, String> cdata = this.getHeader("Content-type");
			if (cdata.getValue().compareToIgnoreCase("application/x-www-form-urlencoded") == 0) {
				String[] pairs = this.body.split("&");
				for (String pair : pairs) {
					String[] fields = pair.split("=");
					if (fields.length == 2) {
						form.put(fields[0], fields[1]);
					}
				}
			}
		}
		return form;
	}

	@Override
	public String toString() {
		StringBuilder headersString = new StringBuilder();
		for (Map.Entry<String, String> h : this.headers.entrySet()) {
			headersString.append(String.format("%s: %s\r\n", h.getKey(), h.getValue()));
		}
		return
				this.method + " " + this.path + " " + this.version + " " + Request.CLRF
						+ headersString.toString()
						+ Request.CLRF
						+ (this.body == null ? "" : this.body);
	}
}
