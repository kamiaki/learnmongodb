package com.aki.mongodb.learnmongodb.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSONResult {
    private int code;
    private String msg;
    private String url;
}
