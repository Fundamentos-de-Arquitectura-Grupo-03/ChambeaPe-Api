package com.digitaldark.ChambeaPe_Api.security.util;

import com.digitaldark.ChambeaPe_Api.user_security.model.entity.Role;
import com.digitaldark.ChambeaPe_Api.user_security.model.enums.ERole;
import com.digitaldark.ChambeaPe_Api.user_security.repository.IRoleRepository;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.List;

/**
 * Class that handles for the security and authentication of users
 * @author Ray Del Carmen
 */

@Slf4j
public class Utilities {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Obtiene el token del header Authorization
     * @param request Solicitud http
     * @return Token obtenido
     */
    static public String getJwtTokenFromRequest(HttpServletRequest request) {
        //It gets the JWT token from the header
        String jwtTokenFromHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(jwtTokenFromHeader) && jwtTokenFromHeader.startsWith(BEARER_PREFIX)) {
            return jwtTokenFromHeader.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * Mapea los roles a una lista de GrantedAuthority
     * @param roles Roles a mapear
     * @return Lista de GrantedAuthority
     */
    static public List<SimpleGrantedAuthority> mapRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();
    }

    /**
     * It gets the roles from the authenticated user
     * @param authenticatedUser Authenticated user
     * @return List of roles
     */
    static public List<String> getRoles(User authenticatedUser) {
        return authenticatedUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    /**
     * It gets the key from the secret
     * @param secret Secret
     * @return Key
     */
    static public SecretKey getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * Inset a role if it doesn't exist
     * @param repository Role repository
     * @param roleName Role name
     */

    static public void insertRoleIfNotFound(IRoleRepository repository, ERole roleName) {
        if (!repository.existsByName(roleName)) {
            var newRole = new Role();
            newRole.setName(roleName);

            repository.save(newRole);
            log.info("Role {} inserted", roleName);
        }
    }
}
