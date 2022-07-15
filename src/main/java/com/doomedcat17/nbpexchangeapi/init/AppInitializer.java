package com.doomedcat17.nbpexchangeapi.init;

import com.doomedcat17.nbpexchangeapi.update.RatesUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "doomedcat17.nbp-exchange-api.initialization", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppInitializer implements CommandLineRunner {

    private final RatesUpdater ratesUpdater;
    private volatile boolean isInitialized = false;

    @Override
    public void run(String... args) {
        synchronized (this) {
            if (!isInitialized) {
                init();
            }
            isInitialized = true;
        }
    }

    private void init() {
        log.info("Initializing...");
        ratesUpdater.update(true);
        log.info("Initialization complete!");
    }


}
