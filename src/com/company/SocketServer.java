package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SocketServer {
    private ServerSocket serverSocket;
    String hostname;

    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

        private void handleserver(Socket socket) {

        try {
            InputStream inputStream = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = socket.getOutputStream();//what erver i write here reached the socket
            PrintWriter writer = new PrintWriter(outputStream);
            String line;
            while ((line = reader.readLine()) != null) {
                if ("quit".equals(line)) {
                    break;
                }
                if(line.contains("GET"))
                {
                    String url=line.substring(line.indexOf("/")+1);
                    String response=methods(url,"GET");
                    writer.println(response);
                }
                if(line.contains("HEAD"))
                {
                    String url=line.substring(line.indexOf("/")+1);
                    String response=methods(url,"HEAD");
                    writer.println(response);
                }
                writer.flush();
            }

            outputStream.close();
            socket.close();
            System.out.println("soket closed!" + socket.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void handle(Socket socket) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            String line=" ";
            hostname =socket.toString();
            while ((line = reader.readLine()) != null) {
                if ("quit".equals(line)) {
                    break;
                } else if (line.contains("GET")) {
                    methods("GET", line, printWriter);
                } else if (line.contains("HEAD")) {
                    methods("HEAD", line, printWriter);
                }
                printWriter.flush();
            }
                printWriter.close();
                socket.close();
                System.out.println("soket closed!" + socket.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() throws IOException {
        System.out.println("server started!");
        while (true) {
            System.out.println("waiting for a connection...  ");
            Socket socket = serverSocket.accept();// waits for clients to join
            System.out.println("socket connected:" + socket.toString());
            Runnable target;
            new Thread(() -> handle(socket)).start();
        }
    }

    public String getFilePath(String s) {
        return s.substring(s.indexOf("/") + 1);
    }

    public String methods(String url, String method) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + url))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        String getresponse = "";
        getresponse += response.version().toString().replace("_", ".") + " " + response.statusCode() + "\n";
        getresponse += "cache-control : " + response.headers().allValues("cache-control") + "\n";
        getresponse += "content-language : " + response.headers().allValues("content-language") + "\n";
        getresponse += "content-type : " + response.headers().allValues("content-type") + "\n";
        getresponse += "date : " + response.headers().allValues("date") + "\n";
        getresponse += "expires : " + response.headers().allValues("expires") + "\n";
        getresponse += "server : " + response.headers().allValues(" server") + "\n";
        getresponse += "transfer-encoding : " + response.headers().allValues("transfer-encoding") + "\n";
        getresponse += "vary : " + response.headers().allValues(" vary") + "\n";
        getresponse += "x-content-type-options : " + response.headers().allValues("x-content-type-options") + "\n";
        getresponse += "x-frame-options : " + response.headers().allValues("x-frame-options") + "\n";
        getresponse += "x-generator : " + response.headers().allValues("x-generator") + "\n";
        getresponse += response.uri() + "\n";
        if (method.equals("GET")) {
            getresponse += response.body() + "\n";
        }
        return getresponse;
    }

    private void methods(String method, String line, PrintWriter printWriter) throws IOException {
        String fileName = getFilePath(line.toLowerCase());
        // open the file and read it
        File f = new File("./resources/" + fileName.replace(" ", ""));
        HEADERS headers=new HEADERS(hostname,"text/"+fileName.substring(fileName.indexOf(".")));
        if (f.exists()) {
            // if the file exists, response to the client with its content
//                    printWriter.println("HTTP/1.1 200 OK\n\n");
            headers.sendResponse(200);
            headers.showHeaders(printWriter);
            if (method.equals("GET")) {
                printWriter.flush();
                BufferedReader br = new BufferedReader(new FileReader(f));
                String fileLine;
                while ((fileLine = br.readLine()) != null) {
                        printWriter.println(fileLine);
                    printWriter.flush();
                }
            }
        } else {
            headers.sendResponse(404);
            headers.showHeaders(printWriter);
            printWriter.flush();
        }
    }
}


