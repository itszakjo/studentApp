package zakjo.studentsapp.model;public class Teachers {    public String t_id ;    public String c_id ;    public String t_name ;    public String t_address ;    public String t_telephone ;    public String t_mobile ;    public String t_email ;    public String t_facebook ;    public String country_id ;    public String bio ;    public Teachers() {    }    public Teachers(String t_id, String c_id, String t_name, String t_address, String t_telephone, String t_mobile, String t_email, String t_facebook, String country_id, String bio) {        this.t_id = t_id;        this.c_id = c_id;        this.t_name = t_name;        this.t_address = t_address;        this.t_telephone = t_telephone;        this.t_mobile = t_mobile;        this.t_email = t_email;        this.t_facebook = t_facebook;        this.country_id = country_id;        this.bio = bio;    }    public String getT_id() {        return t_id;    }    public void setT_id(String t_id) {        this.t_id = t_id;    }    public String getC_id() {        return c_id;    }    public void setC_id(String c_id) {        this.c_id = c_id;    }    public String getT_name() {        return t_name;    }    public void setT_name(String t_name) {        this.t_name = t_name;    }    public String getT_address() {        return t_address;    }    public void setT_address(String t_address) {        this.t_address = t_address;    }    public String getT_telephone() {        return t_telephone;    }    public void setT_telephone(String t_telephone) {        this.t_telephone = t_telephone;    }    public String getT_mobile() {        return t_mobile;    }    public void setT_mobile(String t_mobile) {        this.t_mobile = t_mobile;    }    public String getT_email() {        return t_email;    }    public void setT_email(String t_email) {        this.t_email = t_email;    }    public String getT_facebook() {        return t_facebook;    }    public void setT_facebook(String t_facebook) {        this.t_facebook = t_facebook;    }    public String getCountry_id() {        return country_id;    }    public void setCountry_id(String country_id) {        this.country_id = country_id;    }    public String getBio() {        return bio;    }    public void setBio(String bio) {        this.bio = bio;    }}