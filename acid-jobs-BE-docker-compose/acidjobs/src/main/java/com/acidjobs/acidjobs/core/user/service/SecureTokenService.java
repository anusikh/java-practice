package com.acidjobs.acidjobs.core.user.service;

import com.acidjobs.acidjobs.core.user.jpa.data.SecureToken;

public interface SecureTokenService {
	SecureToken createSecureToken();
	void saveSecureToken(final SecureToken token);
	SecureToken findByToken(final String token);
	void removeToken(final SecureToken token);
	void removeTokenByToken(final String token);
}
