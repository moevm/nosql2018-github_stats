package example.model.mongo.abstractEntity;

import org.springframework.data.annotation.Id;

import java.util.Date;

public abstract class AnalyzedEntity {
    @Id
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AnalyzedEntity(Date date) {
        this.date = date;
    }
}