package com.dam1rka.SpringApp.controller;

import com.dam1rka.SpringApp.config.SecurityConfig;
import com.dam1rka.SpringApp.dto.AuthRequestDto;
import com.dam1rka.SpringApp.dto.AuthResponseDto;
import com.dam1rka.SpringApp.entity.RoleEntity;
import com.dam1rka.SpringApp.entity.RoleEnum;
import com.dam1rka.SpringApp.entity.UserEntity;
import com.dam1rka.SpringApp.security.jwt.JwtTokenProvider;
import com.dam1rka.SpringApp.service.RoleService;
import com.dam1rka.SpringApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Import(SecurityConfig.class)
@RestController
@RequestMapping("api/auth/")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("load/roles/")
    public ResponseEntity<?> loadRoles()
    {
        roleService.loadAllRoles();

        return ResponseEntity.ok(RoleEnum.values());
    }

    @PostMapping("register/")
    public ResponseEntity<UserEntity> register(@RequestBody AuthRequestDto requestDto)
    {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(requestDto.getUsername());
        newUser.setPassword(requestDto.getPassword());
        Date currDate = new Date();
        newUser.setCreated(currDate);
        newUser.setUpdated(currDate);

        return ResponseEntity.ok(userService.register(newUser));
    }

    @PostMapping("login/")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            UserEntity user = userService.findByUsername(username);

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            AuthResponseDto response = new AuthResponseDto();
            response.setUsername(requestDto.getUsername());
            response.setPassword(requestDto.getPassword());
            response.setToken(token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
