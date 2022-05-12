package com.example.walkddog_server.Config;

import lombok.Data;

public @Data
class Constants { //Paths that are used in several locations across the server.
    public static final String uploadPath = "C:/Spring Boot Server/DogPics/";
    public static final String fileSystemPath = "file:///" + uploadPath;
    public static final String staticPath = "classpath:/static/";
}
