package zakjo.studentsapp.model;

public class Plans {


    public int id;
    public String feature1;
    public String feature2;
    public String price;
    public String image;



    public Plans(int id, String feature1, String feature2, String price , String image) {
        this.id = id;
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.price = price;
        this.image = image;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeature1() {
        return feature1;
    }

    public void setFeature1(String feature1) {
        this.feature1 = feature1;
    }

    public String getFeature2() {
        return feature2;
    }

    public void setFeature2(String feature2) {
        this.feature2 = feature2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


