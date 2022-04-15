package zakjo.studentsapp.model;

public class Types {


    public int id;
    public String type;
    public String pic;


    public Types(int id, String type, String pic) {
        this.id = id;
        this.type = type;
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
