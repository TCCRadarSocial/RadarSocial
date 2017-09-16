package tcc.radarsocial.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import tcc.radarsocial.db.ConnectionFactory;

public class RedeSocialDao {
	
	public String listar(){
		AggregationOutput output = null;
//		DBCollection collection = ConnectionFactory.connectDB().getCollection("FeedsMetricas");
//        // $group operation
//        BasicDBObject groupFieldsFace = new BasicDBObject( "_id", "$nomePagina");
//        groupFieldsFace.put("sum", new BasicDBObject( "$sum", "$reactions"));
//
//        BasicDBObject groupFieldsTwitter = new BasicDBObject( "_id", "$nomeTwitter");
//        groupFieldsTwitter.put("sum", new BasicDBObject( "$sum", "$retweets"));
//        
//        BasicDBObject groupFace = new BasicDBObject("$group", groupFieldsFace);
//        BasicDBObject groupTwitter = new BasicDBObject("$group", groupFieldsTwitter);
//        
//        
//        List<BasicDBObject> aggregateList = new ArrayList<BasicDBObject>();
//        aggregateList.add(groupFace);
//        aggregateList.add(groupTwitter);
//        
//        
//        // run aggregation
//        output = collection.aggregate(aggregateList );
       
        return output.results().toString();
            
    }

}
