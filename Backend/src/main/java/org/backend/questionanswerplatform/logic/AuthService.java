package org.backend.questionanswerplatform.logic;
import org.backend.questionanswerplatform.dto.register.LoginRequest;
import org.backend.questionanswerplatform.dto.register.LoginResponse;
import org.backend.questionanswerplatform.dto.register.RegisterRequest;
import org.backend.questionanswerplatform.dto.register.UserResponse;
import org.backend.questionanswerplatform.entities.Role;
import org.backend.questionanswerplatform.entities.User;
import org.backend.questionanswerplatform.mappers.UserMapper;
import org.backend.questionanswerplatform.repositories.RoleRepository;
import org.backend.questionanswerplatform.repositories.UserRepository;
import org.backend.questionanswerplatform.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_MINUTES = 15;


    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,UserMapper userMapper,JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper=userMapper;
        this.jwtService=jwtService;
    }

    public UserResponse register(RegisterRequest request,String roleName)
    {
         if (userRepository.existsByEmail(request.getEmail())){
         throw new IllegalStateException("Email already in use");}
         Role role = roleRepository.findByName(roleName).orElseThrow(()->new IllegalStateException("Role not found" + roleName));
          User user= new User(request.getUserName(),
                  request.getEmail(),
                  passwordEncoder.encode(request.getPassword()),
                  role
                  );
          User saved= userRepository.save(user);
          return userMapper.toResponse(saved);

    }

    public LoginResponse login(LoginRequest request)
    {
        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new BadCredentialsException("Invalid credentials"));

        if (user.getLockoutUntil() != null
                && user.getLockoutUntil().isAfter(LocalDateTime.now())) {
            throw new LockedException("Account locked. Try again later.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setLockoutUntil(LocalDateTime.now().plusMinutes(LOCKOUT_MINUTES));
            }
            userRepository.save(user);
            throw new BadCredentialsException("Invalid credentials");
        }

        if (user.getFailedLoginAttempts() != 0 || user.getLockoutUntil() != null) {
            user.setFailedLoginAttempts(0);
            user.setLockoutUntil(null);
            userRepository.save(user);
        }

        String token = jwtService.generateToken(user.getEmail(),user.getRole().getName());
        UserResponse userResponse= userMapper.toResponse(user);
        return new LoginResponse(token,userResponse);
    }

}

