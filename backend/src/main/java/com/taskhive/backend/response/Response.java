package com.taskhive.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Response {
    String message;
    Object mainBody;

    public Response(Object object, ResponseMessage responseMessage) {
        this.message = responseMessage.getMessage();
        this.mainBody = object;
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(ResponseMessage responseMessage) {
        this.message = responseMessage.getMessage();
    }

    public void setMessage(ResponseMessage responseMessage) {
        this.message = responseMessage.getMessage();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
