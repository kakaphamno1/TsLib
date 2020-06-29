package com.tsolution.base.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEventDTO {
    private String title;
    private String message;
    private String topic;
    private String token;
    private String senderId;
    private String senderName;
    private String type;
    private Long actionId;

    private Double totalQuantity;
    private Double totalAmountNext;
    private Long totalQuantityNext;
    private Double incentiveAmount;
    private Double incentivePercent;
    private Double incentiveAmountNext;
    private Double incentivePercentNext;

}
