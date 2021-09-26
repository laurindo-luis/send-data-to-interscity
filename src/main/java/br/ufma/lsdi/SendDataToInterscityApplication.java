package br.ufma.lsdi;

import java.util.Timer;
import java.util.TimerTask;

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
	
	public static void main(String[] args) {
		SpringApplication.run(SendDataToInterscityApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner run() {
		return args -> {
			long minute = 70;
			new Timer().scheduleAtFixedRate(new TimerTask() {
				public void run() {
					smartBinsLevelService.sendDataToInterSCity();	
				}
			}, 0, minute * 60000);
		};
	}

}
