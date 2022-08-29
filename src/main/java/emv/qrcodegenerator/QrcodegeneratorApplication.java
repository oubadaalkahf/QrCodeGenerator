package emv.qrcodegenerator;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
public class QrcodegeneratorApplication {
	
	 
	
	
	public static void main(String[] args) {
		SpringApplication.run(QrcodegeneratorApplication.class, args);
		
	}

	
}
	
	
	
	
	
	
	
	
