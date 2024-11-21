package com.digitaldark.ChambeaPe_Api.user_security.repository;

import com.digitaldark.ChambeaPe_Api.user_security.model.entity.Role;
import com.digitaldark.ChambeaPe_Api.user_security.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Role repository interface
 * @author Ray Del Carmen
 * @version 1.0
 */

public interface IRoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find a role by its name
     * @param name Name of the role
     * @return Role found (if exists)
     */
    Optional<Role> findByName(ERole name);

    /**
     * Verify if a role exists by its name
     * @param name Name of the role
     * @return true if exists, false if not
     */
    boolean existsByName(ERole name);
}
