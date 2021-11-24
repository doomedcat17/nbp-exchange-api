package com.doomedcat17.nbpexchangeapi.data.nbp.client;

import java.io.IOException;

public interface NbpApiClient {

    String requestResource(String resourcePath) throws IOException;
}
