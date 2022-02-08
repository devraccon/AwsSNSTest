package com.devracoon.awssnstest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnsMessage {
    private String messageId;
    private String messageText;
    private String eventTime;
}
