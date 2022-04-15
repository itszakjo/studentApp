package zakjo.studentsapp.model;

public class MServices {


    public String id ;
    public String service ;
    public double price;
    public String refsalon ;


    public MServices(String id, String service, double price, String refsalon) {
        this.id = id;
        this.service = service;
        this.price = price;
        this.refsalon = refsalon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRefsalon() {
        return refsalon;
    }

    public void setRefsalon(String refsalon) {
        this.refsalon = refsalon;
    }
}
