package pdp.pdprest;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdpRestApplication extends ResourceConfig {

	public PdpRestApplication() {
		packages("pdp.pdprest.resource");
	}

	public static void main(String[] args) {
		SpringApplication.run(PdpRestApplication.class, args);
	}

}
