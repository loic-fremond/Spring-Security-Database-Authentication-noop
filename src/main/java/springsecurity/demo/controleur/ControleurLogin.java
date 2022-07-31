package springsecurity.demo.controleur;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControleurLogin {
	
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	
	@GetMapping("/accesRefuse")
	public String showAccesRefuse() {
		return "acces-refuse";
	}

}
