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

    public Response(String message) {
        this.message = message;
    }

    public Response(ResponseMessage responseMessage) {
        this.message = responseMessage.getMessage();
    }
}
