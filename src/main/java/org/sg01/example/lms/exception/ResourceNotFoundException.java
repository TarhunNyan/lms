package org.sg01.example.lms.exception;

public class ResourceNotFoundException extends LmsException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of() {
        return new ResourceNotFoundException("not found");
    }

}
