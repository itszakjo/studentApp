package zakjo.studentsapp.model;

public class User {


    public String id ;
    public String username ;
    public String imageURL ;
    public String status ;
    public String phone ;
    public String typing_to ;
    public String search ;
    public String lastMessage ;
    public String timestamp ;
    public String user_type ;


    public User() {
    }


    public User(String id, String username, String imageURL, String status, String phone, String typing_to, String search, String lastMessage, String timestamp, String user_type) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.phone = phone;
        this.typing_to = typing_to;
        this.search = search;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.user_type = user_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTyping_to() {
        return typing_to;
    }

    public void setTyping_to(String typing_to) {
        this.typing_to = typing_to;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
