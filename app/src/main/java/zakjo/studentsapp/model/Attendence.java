package zakjo.studentsapp.model;public class Attendence {    public String id;    public String st_id;    public String group_id;    public String created_at;    public String status;    public Attendence() {    }    public Attendence(String id, String st_id, String group_id, String created_at, String status) {        this.id = id;        this.st_id = st_id;        this.group_id = group_id;        this.created_at = created_at;        this.status = status;    }    public String getId() {        return id;    }    public void setId(String id) {        this.id = id;    }    public String getSt_id() {        return st_id;    }    public void setSt_id(String st_id) {        this.st_id = st_id;    }    public String getGroup_id() {        return group_id;    }    public void setGroup_id(String group_id) {        this.group_id = group_id;    }    public String getCreated_at() {        return created_at;    }    public void setCreated_at(String created_at) {        this.created_at = created_at;    }    public String getStatus() {        return status;    }    public void setStatus(String status) {        this.status = status;    }}