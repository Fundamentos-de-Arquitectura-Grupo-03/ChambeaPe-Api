package com.digitaldark.ChambeaPe_Api.user_security.model.entity;

import com.digitaldark.ChambeaPe_Api.user_security.model.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for user roles
 * @author Ray Del Carmen
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    //It will be taken as a String the values of this enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole name;
}
