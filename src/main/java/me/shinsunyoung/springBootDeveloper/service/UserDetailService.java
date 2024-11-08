package me.shinsunyoung.springBootDeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDeveloper.domain.User;
import me.shinsunyoung.springBootDeveloper.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// UserDetailsService는 인증과 관련된 책임만을 처리하므로,
// 비즈니스 로직을 포함하지 않으며, 주로 사용자 정보를 로드하고 변환하는 역할
// 인증, 인가와 관련 -> 스프링 시큐리티와 관련
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }
}
