package example.model.mongo;


import org.springframework.data.annotation.Id;

import java.util.Date;

public class MongoCommit {
    @Id
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MongoCommit(Date date) {
        this.date = date;
    }
}
