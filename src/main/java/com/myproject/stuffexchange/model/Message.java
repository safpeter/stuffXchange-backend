package com.myproject.stuffexchange.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class Message {

    private String from;

    private String to;

    private String content;

    private String time;


}
