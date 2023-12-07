package com.acidjobs.acidjobs.core.user.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.RefreshToken;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.repository.RefreshTokenRepository;
import com.acidjobs.acidjobs.core.user.repository.UserRepository;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class RefreshTokenService {
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Value("${acidjobs.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;



	@Autowired
	private UserRepository userRepository;

	public RefreshToken findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(String email) {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setUser(userRepository.findByEmail(email));
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		return  refreshTokenRepository.save(refreshToken);

	}

	public RefreshToken verifyExpiration(RefreshToken token) throws GenericException {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new GenericException(token.getToken()+"Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	@Transactional
	public int deleteByUserId(User userId) {
		return refreshTokenRepository.deleteByUser(userId);
	}
}
