package br.ufma.lsdi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.ufma.lsdi.smartbins.SmartBinsLevelEntity;
import br.ufma.lsdi.smartbins.SmartBinsLevelService;

@SpringBootApplication
public class SendDataToInterscityApplication {
	
	@Autowired
	private SmartBinsLevelService smartBinsLevelService;
	
	public static void main(String[] args) {
		SpringApplication.run(SendDataToInterscityApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner run() {
		return args -> {
			smartBinsLevelService.sendDataToInterSCity();
		};
	}

}
