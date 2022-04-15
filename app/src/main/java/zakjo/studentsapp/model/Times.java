package zakjo.studentsapp.model;

public class Times {


    public int id;
    public String refday;
    public String refsalon;
    public String time;
    public String day;


    public Times(int id, String refday, String refsalon, String time, String day) {
        this.id = id;
        this.refday = refday;
        this.refsalon = refsalon;
        this.time = time;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRefday() {
        return refday;
    }

    public void setRefday(String refday) {
        this.refday = refday;
    }

    public String getRefsalon() {
        return refsalon;
    }

    public void setRefsalon(String refsalon) {
        this.refsalon = refsalon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}




