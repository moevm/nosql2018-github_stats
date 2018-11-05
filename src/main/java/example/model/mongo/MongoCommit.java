package example.model.mongo;


import org.springframework.data.annotation.Id;

import java.util.Date;

public class MongoCommit {
    @Id
    private String id;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MongoCommit(String id, Date date) {
        this.id = id;
        this.date = date;
    }
}
