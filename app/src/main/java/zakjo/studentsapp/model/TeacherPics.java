package zakjo.studentsapp.model;

public class TeacherPics {

    public int id ;
    public String pic ;
    public int refteacher ;


    public TeacherPics() {
    }


    public TeacherPics(int id, String pic, int refteacher) {
        this.id = id;
        this.pic = pic;
        this.refteacher = refteacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getRefteacher() {
        return refteacher;
    }

    public void setRefteacher(int refteacher) {
        this.refteacher = refteacher;
    }
}
