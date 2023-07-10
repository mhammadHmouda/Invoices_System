package com.harri.training1.security;

import com.harri.training1.models.entities.RefreshToken;
import com.harri.training1.repositories.RefreshTokenRepository;
import com.harri.training1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenUtils {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findByUsername(username).get());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(Instant.now().plusMillis(60000));

        refreshTokenRepository.deleteByUserId(refreshToken.getUser().getId());

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpireDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired! Please make a new login request.");
        }
        return token;
    }
}
