package rj.com.store.services;

import rj.com.store.datatransferobjects.RefreshTokenDTO;
import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.enities.RefreshToken;

public interface RefreshTokenService {
    RefreshTokenDTO createRefreshToken(String userName);
    RefreshTokenDTO findByToken(String token);
    RefreshTokenDTO verifyRefreshToken(RefreshTokenDTO token);
    UserDTO getUserByToken(RefreshTokenDTO token);
}
