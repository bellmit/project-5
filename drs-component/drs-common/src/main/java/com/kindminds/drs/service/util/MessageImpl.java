package com.kindminds.drs.service.util;

import com.kindminds.drs.api.v1.model.message.Message;
import com.kindminds.drs.api.v1.model.message.MessageCode;

public class MessageImpl implements Message {
    private int code;
    private String description;

    public MessageImpl(MessageCode messageCodes) {
        this.code = messageCodes.getCode();
        this.description = messageCodes.getDescription();
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
