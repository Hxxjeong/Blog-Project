package com.example.blogproject.userRole;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class UserRoleId implements Serializable {
    private Long user;
    private Long role;
}
