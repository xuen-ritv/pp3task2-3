package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.User;
import web.repositories.UserRep;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRep userRep;

    @Autowired
    public CustomUserDetailService(UserRep userRep) {
        this.userRep= userRep;
    }
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.getUserByUsername(username); // Поиск пользователя в БД
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}

