package CouponSys.Spring;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import CouponSystem.CouponSystem;

@SpringBootApplication
public class CoupSysApplication {

	@PostConstruct
	public void init()  {
		try {
			CouponSystem.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CoupSysApplication.class, args);
	}
}
