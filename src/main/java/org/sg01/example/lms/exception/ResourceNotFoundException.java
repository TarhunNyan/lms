package org.sg01.example.lms.exception;

public class ResourceNotFoundException extends RuntimeException {

    public static String ERROR_TEMPLATE = "Not found resource: %s by %s = '%s'";

    public static ResourceNotFoundException of(String resource, String field, Object value) {
        return new ResourceNotFoundException(
                String.format(ResourceNotFoundException.ERROR_TEMPLATE, resource, field, value)
        );
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
