package tcc.radarsocial.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

@Configuration
@ComponentScan(basePackages = "tcc.radarsocial")
public class ConfigurationFile {
	 
	public @Bean MongoDbFactory getMongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient("localhost",27017), "RadarSocial");
	}

	public @Bean MongoTemplate getMongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(getMongoDbFactory());
		return mongoTemplate;
	}
}