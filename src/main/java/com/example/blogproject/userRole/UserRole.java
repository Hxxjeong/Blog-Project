package com.example.blogproject.userRole;

import com.example.blogproject.role.Role;
import com.example.blogproject.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles")
@IdClass(UserRoleId.class)
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
