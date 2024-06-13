public class UserInfo {
    private String name;
    private String password;
    private int id;

    public UserInfo(String name, String password, int id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
