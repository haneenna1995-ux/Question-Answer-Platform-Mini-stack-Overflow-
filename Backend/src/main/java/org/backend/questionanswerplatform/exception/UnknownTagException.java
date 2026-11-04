package org.backend.questionanswerplatform.exception;

import java.util.List;

//custom exception, it will be thrown in the service and handled in GlobalException
public class UnknownTagException extends RuntimeException {
    private final List<Long> unknownIds;

    public UnknownTagException(List<Long> unknownIds) {
        super("Unknown tag id(s): " + unknownIds);
        this.unknownIds = unknownIds;
    }

    public List<Long> getUnknownIds() { return unknownIds; }
}
