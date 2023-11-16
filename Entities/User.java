package Entities;

public class User {
    public String username;
    public String email;
    public String password;

    public User(String email,String username,String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
