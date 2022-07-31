package springsecurity.demo.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="springsecurity.demo")
@PropertySource("classpath:persistence-mysql.properties")
public class DemoAppConfig implements WebMvcConfigurer {

	// Créer une variable pour retenir les propriétés
	@Autowired
	private Environment env;
	
	// mise en place d'un logger pour diagnostiquer
	private Logger logger = Logger.getLogger(getClass().getName());
	
	// bean pour la source des données de sécurité
	@Bean
	public DataSource securityDataSource() {
		
		// créer une connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
		
		// set la classe de pilote jdbc
		try {
			securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		
		// logger les propriétés de connexion
		// juste pour s'assurer qu'on lit les bonnes propriétés
		logger.info(">>jdbc.url = " + env.getProperty("jdbc.url"));
		logger.info(">>jdbc.user = " + env.getProperty("jdbc.user"));
		
		// set les propriétés de la connection à la BDD
		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		securityDataSource.setUser(env.getProperty("jdbc.user"));
		securityDataSource.setPassword(env.getProperty("jdbc.password"));
		
		// set les propriétés de la connection pool
		securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
		securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
		securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
		securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));
		
		return securityDataSource;
	}
	
	// methode d'assistance
	// lire une propriété d'environnement et la convertir en int
	private int getIntProperty(String nomProp) {
		String valeurProp = env.getProperty(nomProp);
		return Integer.parseInt(valeurProp);
	}
	
	// bean for view resolver
	@Bean
	public ViewResolver viewResolver() {
		
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
		
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		
	}
	
	// méthode pour l'encodage des projets
	public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, UnsupportedEncodingException, IOException {
				request.setCharacterEncoding("UTF-8");
				chain.doFilter(request, response);
}
	
}
