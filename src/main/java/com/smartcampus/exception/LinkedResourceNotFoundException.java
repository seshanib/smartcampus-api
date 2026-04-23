package com.smartcampus.exception;


public class LinkedResourceNotFoundException extends RuntimeException {
    
    private final String missingRoomId;

    public LinkedResourceNotFoundException(String missingRoomId) {
        super("Referenced roomId '" + missingRoomId + "' does not exist.");
        this.missingRoomId = missingRoomId;
    }

    public String getMissingRoomId() { return missingRoomId; }
    
}
