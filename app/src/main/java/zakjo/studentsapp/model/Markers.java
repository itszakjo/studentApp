package zakjo.studentsapp.model;

public class Markers {










    public int id ;
    public String heldisname ;
    public Double lat ;
    public Double lng ;
    public String helid ;


    public Markers(int id, String heldisname, Double lat, Double lng, String helid) {
        this.id = id;
        this.heldisname = heldisname;
        this.lat = lat;
        this.lng = lng;
        this.helid = helid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeldisname() {
        return heldisname;
    }

    public void setHeldisname(String heldisname) {
        this.heldisname = heldisname;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getHelid() {
        return helid;
    }

    public void setHelid(String helid) {
        this.helid = helid;
    }
}


