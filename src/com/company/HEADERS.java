package com.company;

import com.sun.net.httpserver.Headers;

import java.io.PrintWriter;

public class HEADERS {
        public static final  String SERVER = "Server";
        public static final  String DATE = "Date";
        public static final  String HOST_NAME = "Host-Name";
        public static final String CONNECTION = "Connection";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String STATUS = "status";
        public static final String CONTENT_LENGHT = "contetnt-lrnght";
        public  String  content_type;
        public  String hostname;
        Headers headers;

    public HEADERS(String hostname,String content_type) {
        this.hostname = hostname;
        this.content_type = content_type;
    }

    public  void sendResponse(int statusCode)
    {
        headers=new Headers();
        if (statusCode == 200)
            headers.set(HEADERS.STATUS,Status.HTTP_200);
        else
            headers.set(HEADERS.STATUS,Status.HTTP_404);

        headers.set(HEADERS.DATE,"Mon, 16 Nov 2020 20:11:43 GMT");
        headers.set(HEADERS.SERVER,"Java Server");
        headers.set(HEADERS.CONNECTION,"Keep-alive");
        headers.set(HEADERS.CONTENT_TYPE,content_type);
        headers.set(HEADERS.CONTENT_LENGHT,"777");
        headers.set(HEADERS.HOST_NAME,hostname);
    }
    public  void showHeaders(PrintWriter printWriter)
    {
            printWriter.println(headers.get(HEADERS.STATUS));
            printWriter.println(HEADERS.DATE+" : "+headers.get(HEADERS.DATE));
            printWriter.println(HEADERS.SERVER+" : "+headers.get(HEADERS.SERVER));
            printWriter.println(HEADERS.CONNECTION+" : "+headers.get(HEADERS.CONNECTION));
            printWriter.println(HEADERS.CONTENT_TYPE+" : "+headers.get(HEADERS.CONTENT_TYPE));
            printWriter.println(HEADERS.CONTENT_LENGHT+" : "+headers.get(HEADERS.CONTENT_LENGHT));
            printWriter.println(HEADERS.HOST_NAME +" : "+headers.get(HEADERS.HOST_NAME));
    }
}
