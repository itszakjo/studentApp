package zakjo.studentsapp.model;

public class TalkingTo {


    public String phone;
    public String name;
    public String timestamp;
    public String imageURL;
    public String typing_to;
    public String lastMessage ;
    public String status;


    public TalkingTo() {
    }

    public TalkingTo(String phone, String name, String timestamp, String imageURL, String typing_to, String lastMessage, String status) {
        this.phone = phone;
        this.name = name;
        this.timestamp = timestamp;
        this.imageURL = imageURL;
        this.typing_to = typing_to;
        this.lastMessage = lastMessage;
        this.status = status;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTyping_to() {
        return typing_to;
    }

    public void setTyping_to(String typing_to) {
        this.typing_to = typing_to;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}