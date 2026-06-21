package com.springboot.jobportal.entity;

import com.springboot.jobportal.enums.UserTypeRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_type_id")
    private int userTypeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private UserTypeRoleEnum userTypeRole;
}
