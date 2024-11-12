package me.shinsunyoung.springBootDeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDeveloper.domain.RefreshToken;
import me.shinsunyoung.springBootDeveloper.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findyByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
