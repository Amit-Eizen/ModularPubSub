package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import servlets.Servlet;
import server.RequestParser.RequestInfo;

public class MyHTTPServer extends Thread implements HTTPServer {
    private int port;
    private int nThreads;
    private Map<String, Map<String, Servlet>> servlets;
    private ServerSocket serverSocket;
    private boolean running;

    public MyHTTPServer(int port, int nThreads) {
        this.port = port;
        this.nThreads = nThreads;
        this.servlets = new HashMap<>();
        this.running = false;

        // Initialize servlet maps for different HTTP commands
        servlets.put("GET", new HashMap<>());
        servlets.put("POST", new HashMap<>());
        servlets.put("DELETE", new HashMap<>());
    }

    @Override
    public void addServlet(String httpCommand, String uri, Servlet s) {
        Map<String, Servlet> commandServlets = servlets.get(httpCommand);
        if (commandServlets == null) {
            commandServlets = new HashMap<>();
            servlets.put(httpCommand, commandServlets);
        }
        commandServlets.put(uri, s);
    }

    @Override
    public void removeServlet(String httpCommand, String uri) {
        Map<String, Servlet> commandServlets = servlets.get(httpCommand);
        if (commandServlets != null) {
            commandServlets.remove(uri);
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error accepting client: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {

            RequestInfo requestInfo = RequestParser.parseRequest(reader);

            Servlet servlet = findServlet(requestInfo.getHttpCommand(), requestInfo.getUri());
            if (servlet != null) {
                servlet.handle(requestInfo, outputStream);
            } else {
                String response = "HTTP/1.1 404 Not Found\r\n\r\nServlet not found";
                outputStream.write(response.getBytes());
            }

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private Servlet findServlet(String httpCommand, String uri) {
        Map<String, Servlet> commandServlets = servlets.get(httpCommand);
        if (commandServlets == null) {
            return null;
        }

        Servlet servlet = commandServlets.get(uri);
        if (servlet != null) {
            return servlet;
        }

        String bestMatch = null;
        int bestMatchLength = -1;

        for (String servletUri : commandServlets.keySet()) {
            if (uri.startsWith(servletUri) && servletUri.length() > bestMatchLength) {
                bestMatch = servletUri;
                bestMatchLength = servletUri.length();
            }
        }

        return bestMatch != null ? commandServlets.get(bestMatch) : null;
    }

    @Override
    public void close() {
        running = false;

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }

        for (Map<String, Servlet> commandServlets : servlets.values()) {
            for (Servlet servlet : commandServlets.values()) {
                try {
                    servlet.close();
                } catch (IOException e) {
                    System.err.println("Error closing servlet: " + e.getMessage());
                }
            }
        }
    }
}
