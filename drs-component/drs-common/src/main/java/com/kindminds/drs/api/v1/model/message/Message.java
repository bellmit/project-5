package com.kindminds.drs.api.v1.model.message;

public interface Message {
    /**
     * @see MessageCode *_CODE
     * @return Message Code
     */
    int getCode();
    /**
     * @see MessageCode *_DESC
     * @return Message Description
     */
    String getDescription();
}
