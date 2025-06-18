package com.jcgfdev.flycommerce.service.impl;

import com.jcgfdev.flycommerce.dto.UserDetailsModel;
import com.jcgfdev.flycommerce.model.jpa.Users;
import com.jcgfdev.flycommerce.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    //Repositorios
    private final UserRepository userRepository;
    //Mensajes
    private static final String USERNOTFOUND = "El usuario no existe";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USERNOTFOUND));
        return UserDetailsModel.build(user);
    }
}
