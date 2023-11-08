package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.liga.exception.DataNotFoundException;
import ru.liga.model.Authority;
import ru.liga.model.User;
import ru.liga.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {

    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authority authority = roleRepository.findByUsername(username).orElseThrow(() -> new
                DataNotFoundException(String.format("Роль по имени пользователя '%s' не найдены", username)));
        User user = authority.getUser();
        List<Authority> authorities = List.of(authority);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities.stream()
                        .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                        .collect(Collectors.toList())
        );
    }
}
