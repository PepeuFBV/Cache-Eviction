package entities;

import exceptions.AuthenticationException;
import exceptions.authentication.InvalidPasswordException;

public class User {

    private int id;
    private String name;
    private String password;

    private User(int id, String name, String password) throws AuthenticationException {
        this.setId(id);
        this.setName(name);
        try {
            this.setPassword(password);
        } catch (InvalidPasswordException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void setPassword(String password) throws InvalidPasswordException {
        if (password == null || password.isEmpty()) {
            throw new InvalidPasswordException("Password cannot be empty or null");
        }
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
