package com.digitaldark.ChambeaPe_Api.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Class that contains the security configuration of the application
 * @author Ray Del Carmen
 */
@Configuration
public class BeansConfig {
    /**
     * Bean that handles the authentication configuration
     * @param customUserDetailsService Custom user details service
     * @return Instance of AuthenticationProvider
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Bean that handles the authentication configuration
     * @param customUserDetailsService Custom user details service
     * @return Instance of AuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
