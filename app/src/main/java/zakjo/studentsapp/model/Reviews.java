package zakjo.studentsapp.model;public class Reviews {    public int id;    public String rate;    public String comment;    public int refsalon;    public String client;    public String rating_date;    public Reviews(int id, String rate, String comment, int refsalon, String client, String rating_date) {        this.id = id;        this.rate = rate;        this.comment = comment;        this.refsalon = refsalon;        this.client = client;        this.rating_date = rating_date;    }    public int getId() {        return id;    }    public void setId(int id) {        this.id = id;    }    public String getRate() {        return rate;    }    public void setRate(String rate) {        this.rate = rate;    }    public String getComment() {        return comment;    }    public void setComment(String comment) {        this.comment = comment;    }    public int getRefsalon() {        return refsalon;    }    public void setRefsalon(int refsalon) {        this.refsalon = refsalon;    }    public String getClient() {        return client;    }    public void setClient(String client) {        this.client = client;    }    public String getRating_date() {        return rating_date;    }    public void setRating_date(String rating_date) {        this.rating_date = rating_date;    }}