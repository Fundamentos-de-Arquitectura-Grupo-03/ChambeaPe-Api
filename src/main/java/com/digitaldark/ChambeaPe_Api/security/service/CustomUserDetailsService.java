package com.digitaldark.ChambeaPe_Api.security.service;

import com.digitaldark.ChambeaPe_Api.security.util.Utilities;
import com.digitaldark.ChambeaPe_Api.user_security.repository.IUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Class that contains the security configuration of the application
 * @author Ray Del Carmen
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    public CustomUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtiene los datos del usuario autenticado por su username o email
     * @param email Email del usuario autenticado
     * @return Datos del usuario autenticado
     * @throws UsernameNotFoundException Excepción de usuario no encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //busca al usuario por su username o email
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró al usuario con el username o email: " + email));

        //crea y retorna un objeto que representa al usuario autenticado
        return new User(user.getEmail(), user.getPassword(), Utilities.mapRoles(user.getRoles()));
    }
}
