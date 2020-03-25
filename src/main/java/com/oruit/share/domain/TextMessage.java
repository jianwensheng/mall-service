package com.oruit.share.domain;

import lombok.Data;

@Data
public class TextMessage {

    private String Content;

    private String ToUserName;

    private String FromUserName;

    private String CreateTime;

    private String MsgType;

}
