package smu.example.cabling;

public class User {
    public String username;
    public String email;
    public int point;
    public String photo;

    public User(){
    }

    public User(String username, String email, int point, String photo){
        this.username = username;
        this.email = email;
        this.point = point;
        this.photo = photo;
    }
}
