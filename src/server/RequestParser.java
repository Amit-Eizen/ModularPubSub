package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.trim().isEmpty()) {
            throw new IOException("Empty request");
        }

        // Parse HTTP command and URI from first line
        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 2) {
            throw new IOException("Invalid request format");
        }

        String httpCommand = requestParts[0];
        String fullUri = requestParts[1];

        String uri;
        Map<String, String> parameters = new HashMap<>();

        if (fullUri.contains("?")) {
            String[] uriParts = fullUri.split("\\?", 2);
            uri = uriParts[0];
            parseParameters(uriParts[1], parameters);
        } else {
            uri = fullUri;
        }

        String[] uriSegments = uri.split("/");
        int startIndex = (uriSegments.length > 0 && uriSegments[0].isEmpty()) ? 1 : 0;
        String[] cleanSegments = new String[uriSegments.length - startIndex];
        System.arraycopy(uriSegments, startIndex, cleanSegments, 0, cleanSegments.length);

        // Read headers and look for Content-Length
        String line;
        Map<String, String> headers = new HashMap<>();
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0].trim().toLowerCase(), headerParts[1].trim());
            }
        }

        int contentLength = 0;
        if (headers.containsKey("content-length")) {
            try {
                contentLength = Integer.parseInt(headers.get("content-length"));
            } catch (NumberFormatException e) {
                //assume no content
            }
        }

        StringBuilder contentBuilder = new StringBuilder();

        if (contentLength > 0) {
            if ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                parseParameters(line, parameters);
            }

            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            }

            while (reader.ready() && (line = reader.readLine()) != null && !line.trim().isEmpty()) {
                contentBuilder.append(line);
                if (contentBuilder.length() > 0) {
                    contentBuilder.append("\n");
                }
            }
        }

        byte[] content = contentBuilder.toString().getBytes();

        return new RequestInfo(httpCommand, fullUri, cleanSegments, parameters, content);
    }

    private static void parseParameters(String paramString, Map<String, String> parameters) {
        if (paramString == null || paramString.trim().isEmpty()) {
            return;
        }

        String[] pairs = paramString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                parameters.put(key, value);
            } else if (keyValue.length == 1) {
                parameters.put(keyValue[0].trim(), "");
            }
        }
    }

    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
