package com.doomedcat17.nbpexchangeapi.services.nbp.client;

import java.io.IOException;

public interface NbpApiClient {

    String requestResource(String resourcePath) throws IOException;
}
