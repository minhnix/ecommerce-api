package com.nix.ecommerceapi.model.response;

public class ApiResponse {
    private String message;
    private int code;
    private Object metadata;

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ApiResponse(Object metadata, String message, int code ) {
        this.message = message;
        this.code = code;
        this.metadata = metadata;
    }

    public ApiResponse(String message, int code ) {
        this.message = message;
        this.code = code;
    }

    public static ApiResponse success(Object metadata, String message) {
        return new ApiResponse(metadata, message, 200);
    }

    public static ApiResponse successWithOutMetadata(String message) {
        return new ApiResponse(message, 200);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
