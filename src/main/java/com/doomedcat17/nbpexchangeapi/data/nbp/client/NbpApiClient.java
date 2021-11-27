package com.doomedcat17.nbpexchangeapi.data.nbp.client;

import java.io.IOException;
import java.util.Map;

public interface NbpApiClient {

    String requestResource(String resourcePath) throws IOException;

    String requestResource(String resourcePath, Map<String, String> parameters) throws IOException;
}
