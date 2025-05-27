package com.taskhive.backend.response;

public enum ResponseMessage {

    // Inbox Controller Messages:

    // Success Messages
    PROJECT_INVITATION_SUCCESS("Project invitation successfully created."),
    INBOXES_FETCH_SUCCESS("Inboxes retrieved successfully."), // Added for the getInboxes endpoint
    GENERIC_SUCCESS("Operation completed successfully."),

    // Error Messages
    TARGET_PROJECT_NOT_FOUND("Target project with the provided PID doesn't exist."),
    TARGET_USER_NOT_FOUND("This user doesn't exist."),
    CANNOT_INVITE_OWNER("Can't create an invite for the owner of this project."),
    PROJECT_NOT_OWNED_BY_CURRENT_USER("This project isn't owned by you."),
    PROJECT_ALREADY_JOINED("Project already joined."),
    PROJECT_INVITE_ALREADY_SENT("Project invite already sent."),
    PROJECT_INVITATION_CREATION_FAILED("This project invitation could not be created."),


    AUTHENTICATION_REQUIRED("Authentication required to access this resource."), // Example for missing authentication

    //INBOX:
    INBOX_DOES_NOT_EXIST("Inbox does not exist"),

    INBOX_INVITATION_EXPIRED("Project invitation expired"),

    INBOX_INVITATION_ACCEPTED("Project invitation accepted"),

    INBOX_INVITATION_DELETED("Project invitation deleted"),

    INBOX_INVITATION_INVALID("Project invitation is invalid"),

    //PROJECT:
    PROJECT_FOUND("Project found."),

    PROJECT_NOT_FOUND("Project not found."),

    //GENERIC RESPONSES:
    ERROR_OCCURED("An error occurred while processing the request.");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
