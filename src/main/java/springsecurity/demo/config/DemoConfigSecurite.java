package springsecurity.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DemoConfigSecurite {

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		
		UserDetails user = User.withUsername("employe").password("{noop}test123").roles("EMPLOYE").build();
		UserDetails admin = User.withUsername("admin").password("{noop}test123").roles("EMPLOYE", "ADMIN").build();
		UserDetails manager = User.withUsername("manager").password("{noop}test123").roles("EMPLOYE", "MANAGER").build();
		return new InMemoryUserDetailsManager(user, admin, manager);
		
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(authorize -> authorize
				.antMatchers("/resources/**")
				.permitAll()
				.antMatchers("/")
				.hasRole("EMPLOYE")
				.antMatchers("/managers")
				.hasRole("MANAGER")
				.antMatchers("/admins")
				.hasRole("ADMIN")
				.anyRequest()
				.authenticated()
			        )
			        .formLogin(form -> form.loginPage("/login")
			        .loginProcessingUrl("/authenticateLogin")
			        .permitAll()
			)
			        .logout().permitAll()
			        .and()
			        .exceptionHandling().accessDeniedPage("/accesRefuse");
			 
			    return http.build();
	    
	}

}
