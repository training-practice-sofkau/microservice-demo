package org.example.generic;



public class ExecutionNoFound extends RuntimeException {

    public ExecutionNoFound(String type) {
        super(String.format("The type [%s] to be executed does not have a handler", type));
    }
}
