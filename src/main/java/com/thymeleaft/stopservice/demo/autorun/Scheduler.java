package com.thymeleaft.stopservice.demo.autorun;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
	
	private final Logger log = LoggerFactory.getLogger(Scheduler.class);
	
	@Value("${server.port}")
	private String serverPort; // get value port set on application.properties

	@Bean
	public void runOnStartApplication() {
		
		log.info("I am first action on start up the application :P");
		
		String os = System.getProperty("os.name").toLowerCase();
		
		if(serverPort.isEmpty()) {// check if value in application.properties is not empty else setport default 8080
			serverPort = "8080";
		}
		
		String baseUrl = "http://localhost:" + serverPort + "/";

        try {
        	 if (os.contains("win")) {
                 // Sistema operativo Windows
                 log.info("Sistema operativo: Windows");
                 Runtime.getRuntime().exec("cmd /c start " + baseUrl);
             } else if (os.contains("nix") || os.contains("nux")) {
                 // Sistema operativo basato su Unix (Linux)
                 log.info("Sistema operativo: Unix (Linux)");
                 Runtime.getRuntime().exec("xdg-open " + baseUrl);
             } else if (os.contains("mac")) {
                 // Sistema operativo macOS
                 log.info("Sistema operativo: MacOS");
                 Runtime.getRuntime().exec("open " + baseUrl);
             } else {
                 // Altro sistema operativo non supportato
                 log.error("Sistema operativo non supportato");
             }
        	 
        	 // codice fatto da chat gpt ma utilissimo apre il browser su mac e windows sulla pagina localhost dove questo
        	 // applicativo si appogia 
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
}
