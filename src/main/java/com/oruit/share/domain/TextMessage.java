package com.oruit.share.domain;

import lombok.Data;

@Data
public class TextMessage {

    private String content;

    private String toUserName;

    private String fromUserName;

    private String createTime;

    private String msgType;
}
