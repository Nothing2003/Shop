package rj.com.store.services.servicesimp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rj.com.store.datatransferobjects.RefreshTokenDTO;
import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.enities.RefreshToken;
import rj.com.store.enities.User;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.repositories.RefreshTokenRepository;
import rj.com.store.repositories.UserRepositories;
import rj.com.store.services.RefreshTokenService;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImp implements RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepositories userRepositories;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public RefreshTokenDTO createRefreshToken(String userName) {
       User user= userRepositories.findByEmail(userName).orElseThrow(()->new ResourceNotFoundException("User not found"));
        RefreshToken refreshToken=  refreshTokenRepository.findByUser(user).orElse(null);
       if(refreshToken==null) {
           refreshToken = RefreshToken.builder()
                   .user(user)
                   .token(UUID.randomUUID().toString())
                   .expiresDate(Instant.now().plusSeconds(30 * 24 * 60 * 60))
                   .build();
       }else {
           refreshToken.setToken(UUID.randomUUID().toString());
           refreshToken.setExpiresDate(Instant.now().plusSeconds(30 * 24 * 60 * 60));
       }
           RefreshToken saveRefresh = refreshTokenRepository.save(refreshToken);

       return  modelMapper.map(saveRefresh, RefreshTokenDTO.class);
    }

    @Override
    public RefreshTokenDTO findByToken(String token) {
        RefreshToken refreshToken= refreshTokenRepository.findByToken(token).orElseThrow(()->new ResourceNotFoundException("Refresh Token not found"));
        return modelMapper.map(refreshToken, RefreshTokenDTO.class);
    }

    @Override
    public RefreshTokenDTO verifyRefreshToken(RefreshTokenDTO token) {
        var refreshToken=modelMapper.map(token, RefreshToken.class);
        if (token.getExpiresDate().compareTo(Instant.now())<0) {
            refreshTokenRepository.delete(refreshToken);
            throw  new ResourceNotFoundException("Refresh Token  Expired");
        }
        return token;
    }

    @Override
    public UserDTO getUserByToken(RefreshTokenDTO token) {
        RefreshToken refreshToken= refreshTokenRepository.findByToken(token.getToken()).orElseThrow(()->new ResourceNotFoundException("Refresh Token not found"));
        User user=refreshToken.getUser();
        return modelMapper.map(user, UserDTO.class);
    }
}
