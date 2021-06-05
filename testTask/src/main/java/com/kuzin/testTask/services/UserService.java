package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.Role;
import com.kuzin.testTask.entities.User;
import com.kuzin.testTask.repositories.CartRepository;
import com.kuzin.testTask.repositories.RoleRepository;
import com.kuzin.testTask.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CartRepository cartRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository=roleRepository;
        this.cartRepository=cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }


    public boolean addUser(User user){

        if (getByUsername(user.getUsername()) != null | getByEmail(user.getEmail()) != null | user.getUsername().contains(" ")) {
            return  false;
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(new ArrayList<>());
            user.getRoles().add(roleRepository.getByName("ROLE_USER"));
            userRepository.save(user);
            Cart cart=new Cart();
            cart.setUser(getByUsername(user.getUsername()));
            cartRepository.save(cart);
            return true;
        }
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
