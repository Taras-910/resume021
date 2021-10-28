package ua.training.top.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import ua.training.top.model.Role;
import ua.training.top.model.User;

import java.util.Collections;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;

public class UserUtil {
    public static final int ADMIN_ID = START_SEQ;

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder, User userDb) {
        if(user.getRoles() == null) {
            user.setRoles(Collections.singleton(Role.USER));
        }
        String passwordDb = userDb == null ? null : userDb.getPassword();
        String password = user.getPassword();
        if(passwordDb == null) {
            user.setPassword(passwordEncoder.encode(password));
        } else {
            if(passwordDb.equals(password)) {
                user.setPassword(passwordDb);
            }
            else {
                // ToDo: send confirm form to change password ('Are you sure?')
                user.setPassword(passwordEncoder.encode(password));
            }
        }
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

    public static User asAdmin() {
        return new User(100000, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
    }

}
