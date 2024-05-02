package org.education.service;

import jakarta.persistence.EntityNotFoundException;
import org.education.bean.dto.CreatorResponseTo;
import org.education.bean.Creator;
import org.education.repository.CreatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public CreatorResponseTo signUp(CreatorResponseTo registrationRequest){
        CreatorResponseTo resp = new CreatorResponseTo();
        try {
            Creator creator = new Creator();
            creator.setLogin(registrationRequest.getLogin());
            creator.setFirstname(registrationRequest.getFirstname());
            creator.setLastname(registrationRequest.getLastname());
            creator.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            creator.setRole(registrationRequest.getRole());
            Creator ourUserResult = creatorRepository.save(creator);
            System.out.println(ourUserResult.getId());
            if (ourUserResult != null && ourUserResult.getId()>0) {
                resp.setCreator(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
                resp.setLogin(registrationRequest.getLogin());
                System.out.println(ourUserResult);
                System.out.println(resp);
            }
        }catch (Exception e){
            System.out.println("Ошибок нет");
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public CreatorResponseTo signIn(CreatorResponseTo signinRequest){
        CreatorResponseTo response = new CreatorResponseTo();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getLogin(),signinRequest.getPassword()));
            var user = creatorRepository.findByLogin(signinRequest.getLogin());
            if (user == null) {
                throw new EntityNotFoundException("User not found with login");
            }
            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public CreatorResponseTo refreshToken(CreatorResponseTo refreshTokenReqiest){
        CreatorResponseTo response = new CreatorResponseTo();
        String login = jwtUtils.extractLogin(refreshTokenReqiest.getToken());
        UserDetails creators = creatorRepository.findByLogin(login);
        if (creators == null) {
            throw new EntityNotFoundException("Creator not found with login: " + login);
        }
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), creators)) {
            var jwt = jwtUtils.generateToken(creators);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
