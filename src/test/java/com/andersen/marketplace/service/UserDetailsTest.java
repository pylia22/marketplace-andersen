package com.andersen.marketplace.service;

import com.andersen.marketplace.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsTest {

    @Test
    void whenGetAuthoritiesThenReturnAllUserAuthorities() {
        User user = mock(User.class);
        when(user.getRoles()).thenReturn("ROLE_USER, ROLE_EDITOR");

        UserDetails userDetails = new UserDetails(user);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        Collection<? extends GrantedAuthority> expectedAuthorities = Stream.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_EDITOR")
        ).collect(Collectors.toList());

        assertEquals(expectedAuthorities, authorities);
    }

}