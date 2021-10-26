package com.edmanager.model;

public class Response {

	private String status;
	
	private Object data;
	
	private String message;

	@Override
	public String toString() {
		return "Response [status=" + status + ", data=" + data + ", message=" + message + "]";
	}

	public Response() {
		super();
	}
	
	public Response(String status, String message, Object data) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
