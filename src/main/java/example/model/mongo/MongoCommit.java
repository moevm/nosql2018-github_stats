package example.model.mongo;


import example.model.mongo.abstractEntity.AnalyzedEntity;

import java.util.Date;

public class MongoCommit extends AnalyzedEntity {
    public MongoCommit(Date date) {
        super(date);
    }
}
