package zakjo.studentsapp.model;

import java.util.List;

public class GroupChats {



    public String admin ;
    public String id ;
    public String imageUrl ;
    public String lastmessage ;
    public List<String> members ;
    public String name ;
    public String timestamp ;


    public GroupChats() { }


    public GroupChats(String admin, String id, String imageUrl, String lastmessage, List<String> members, String name, String timestamp) {
        this.admin = admin;
        this.id = id;
        this.imageUrl = imageUrl;
        this.lastmessage = lastmessage;
        this.members = members;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
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
}
