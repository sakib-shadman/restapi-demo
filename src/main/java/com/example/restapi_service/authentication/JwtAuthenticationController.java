package com.example.restapi_service.authentication;


import com.example.restapi_service.dto.LoginDto;
import com.example.restapi_service.dto.UserDto;
import com.example.restapi_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDto loginDto) throws Exception {

       /* try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception ex){
            System.out.println(ex);
            return ResponseEntity.ok(ex.getMessage());

        }*/
       com.example.restapi_service.model.User user = userRepository.findUserByUserName(loginDto.getUserName());
       if(user == null){
           throw new UsernameNotFoundException("User not found with username: " + loginDto.getUserName());
       }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();// Strength set as 12
        if(encoder.matches(loginDto.getUserPassword(),user.getUserPassword())){
            final String token = jwtTokenUtil.generateToken(user);
            UserDto userDto = new UserDto();
            userDto.setAccessToken(token);
            userDto.setUserName(user.getUserName());
            userDto.setUserEmail(user.getUserEmail());
            userDto.setUserGender(user.getUserGender());
            userDto.setUserAddress(user.getUserAddress());
            return ResponseEntity.ok(userDto);
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
      /* try {
           final UserDetails userDetails = userDetailsService
                   .loadUserByUsername(authenticationRequest.getUsername());
           final String token = jwtTokenUtil.generateToken(userDetails);
           return ResponseEntity.ok(new JwtResponse(token));
       }catch (Exception ex){
           return ResponseEntity.ok(ex.getMessage());
       }*/



    }
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
