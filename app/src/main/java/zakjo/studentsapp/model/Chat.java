package zakjo.studentsapp.model;

public class Chat {


    private String user_phone;
    private String sender;
    private String message;
    private String type;
    private int iseen;
    private String timestamp;


    public Chat() {
    }

    public Chat(String user_phone, String sender, String message, String type, int iseen, String timestamp) {
        this.user_phone = user_phone;
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.iseen = iseen;
        this.timestamp = timestamp;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIseen() {
        return iseen;
    }

    public void setIseen(int iseen) {
        this.iseen = iseen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
