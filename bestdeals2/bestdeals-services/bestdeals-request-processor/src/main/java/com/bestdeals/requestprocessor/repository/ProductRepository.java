package com.bestdeals.requestprocessor.repository;

import com.bestdeals.requestprocessor.models.Product;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // aggregation pipeline for autocorrect search
    @Aggregation(pipeline = {
            "{\n" + "\"$search\": {\n" + " \"index\": \"productnameindex\",\n" + " \"autocomplete\": " + "{\n"
                    + " \"query\": ?0,\n" + "\"path\": \"productName\",\n" + " \"fuzzy\": {\n" + "  \"maxEdits\": 2,\n"
                    + " \"prefixLength\": 3\n" + " }\n" + " }\n" + " }\n" + "}" + "}",
            "{\n" + "\"$limit\":5,\n" + "}" })
    AggregationResults<Product> searchForProducts(String productName);

    // query for productName and userQueryName
    @Query(value = "{ $or: [ {'userQueryName':{'$regex':'?0','$options':'i'}}, {'productName':{'$regex':'?0','$options':'i'}} ] }")
    List<Product> findByProductNameOrUserQueryName(String name);

    // pipeline for getting unique products
    @Aggregation(pipeline = { "{ '$group': { '_id' : '$userQueryName' } }" })
    List<String> findByUniqueUserQueryName();

}