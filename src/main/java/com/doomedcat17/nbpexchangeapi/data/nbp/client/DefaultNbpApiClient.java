package com.doomedcat17.nbpexchangeapi.data.nbp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DefaultNbpApiClient implements NbpApiClient {

    private final String NBP_API_ROOT = "http://api.nbp.pl/api/";

    @Override
    public String requestResource(String resourcePath, Map<String, String> parameters) throws IOException {
        URL nbpUrl = new URL(NBP_API_ROOT+resourcePath+parametersAsString(parameters));
        HttpURLConnection connection = (HttpURLConnection) nbpUrl.openConnection();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (InputStream requestInputStream = connection.getInputStream()) {
                BufferedReader responseBodyReader =
                        new BufferedReader(
                                new InputStreamReader(requestInputStream, StandardCharsets.UTF_8)
                        );
                return readBody(responseBodyReader);
            }
        } else throw new IOException();
    }

    @Override
    public String requestResource(String resourcePath) throws IOException {
        return requestResource(resourcePath, Map.of());
    }

    private String parametersAsString(Map<String, String> parameters) {
        if (parameters.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder("?");
        parameters.forEach((name, value) -> {
            stringBuilder.append("name")
                    .append("=")
                    .append(value)
                    .append("&");
        });
        stringBuilder.replace(
                stringBuilder.length()-1,
                stringBuilder.length(), "");
        return stringBuilder.toString();
    }


    private String readBody(BufferedReader responseBodyReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = responseBodyReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
