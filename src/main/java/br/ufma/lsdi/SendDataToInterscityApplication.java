package br.ufma.lsdi;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.ufma.lsdi.smartbins.SmartBinsLevelService;

@SpringBootApplication
public class SendDataToInterscityApplication {
	
	@Autowired
	private SmartBinsLevelService smartBinsLevelService;
	
	Logger logger = LoggerFactory.getLogger(SendDataToInterscityApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SendDataToInterscityApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner run() {		
		return args -> {
			if(!smartBinsLevelService.isHasBinsSaved()) 
				smartBinsLevelService.sendDataToInterSCity();
			
			int delay = ((60 - LocalDateTime.now().getMinute()) + 10) * 60000;
			long period = 60 * 60000;
			
			new Timer().scheduleAtFixedRate(new TimerTask() {
				public void run() {
					if(smartBinsLevelService.sendDataToInterSCity()) 
						logger.info("The data has been sent to InterSCity!");
					else 
						logger.info("Error sending data to InterSCity!");
				}
			}, delay, period);
		};
	}
}
