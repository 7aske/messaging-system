package http;

import java.util.HashMap;
import java.util.Map;

public class Response {
	public static final String CLRF = "\r\n";
	public static final String HTTP_VER = "HTTP/1.1";
	private String version;
	private int statusCode;
	private String reasonPhrase;

	private HashMap<String, String> headers;
	private String body;

	private Response(StatusCodes code) {
		this.statusCode = HttpStatus.getStatusCode(code);
		this.reasonPhrase = HttpStatus.getStatusText(code);
		this.version = HTTP_VER;
	}

	public static Response generateResponse(StatusCodes code) {
		Response response = new Response(code);
		response.headers = new HashMap<>();
		return response;
	}

	public static Response generateResponse(StatusCodes code, String body) {
		Response response = new Response(code);
		response.headers = new HashMap<>();
		response.setBody(body);
		return response;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public Map.Entry<String, String> getHeader(String header) {
		for (Map.Entry<String, String> h : this.headers.entrySet()) {
			if (h.getKey().toUpperCase().equals(header.toUpperCase())) {
				return h;
			}
		}
		return null;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.headers.put("Content-length", Integer.toString(body.length()));
		this.body = body;
	}

	public byte[] getBytes() {
		return this.toString().getBytes();
	}

	@Override
	public String toString() {
		StringBuilder headersString = new StringBuilder();
		for (Map.Entry<String, String> h : this.headers.entrySet()) {
			headersString.append(String.format("%s: %s\r\n", h.getKey(), h.getValue()));
		}
		return
				this.version + " " + this.statusCode + " " + this.reasonPhrase + " " + Response.CLRF
						+ headersString.toString()
						+ Response.CLRF
						+ (this.body == null ? "" : this.body);
	}

	public void setHeader(String key, String value) {
		this.headers.put(key, value);
	}
}