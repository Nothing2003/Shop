package rj.com.store.services;

import rj.com.store.datatransferobjects.RefreshTokenDTO;
import rj.com.store.datatransferobjects.UserDTO;

/**
 * This interface defines methods for managing refresh tokens used in the authentication process.
 * It handles token creation, verification, and retrieval of associated users.
 */
public interface RefreshTokenService {

    /**
     * Creates a new refresh token for a given user.
     *
     * @param userName The username for whom the refresh token is created.
     * @return A DTO object representing the newly created refresh token.
     */
    RefreshTokenDTO createRefreshToken(String userName);

    /**
     * Retrieves a refresh token by its token string.
     *
     * @param token The token string to search for.
     * @return A DTO object representing the refresh token if found.
     */
    RefreshTokenDTO findByToken(String token);

    /**
     * Verifies if a refresh token is valid or expired.
     *
     * @param token The refresh token to verify.
     * @return A DTO object representing the verified refresh token.
     */
    RefreshTokenDTO verifyRefreshToken(RefreshTokenDTO token);

    /**
     * Retrieves the user associated with a given refresh token.
     *
     * @param token The refresh token used to identify the user.
     * @return A DTO object representing the user associated with the given token.
     */
    UserDTO getUserByToken(RefreshTokenDTO token);
}
