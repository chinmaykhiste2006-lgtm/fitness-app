package com.fitness.activityservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fitness.activityservice.model.Activity;
import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity, String> {

    boolean existsByUserIdAndTypeAndStartTime(String userId, String type, String startTime);

    @Query("{ '$or': [ "
     + "{ 'type': { '$regex': ?0, '$options': 'i' } }, "
     + "{ '$expr': { '$regexMatch': { "
     + "    'input': { '$convert': { 'input': '$duration', 'to': 'string', 'onError': '', 'onNull': '' } }, "
     + "    'regex': ?0, 'options': 'i' } } }, "
     + "{ '$expr': { '$regexMatch': { "
     + "    'input': { '$convert': { 'input': '$caloriesBurned', 'to': 'string', 'onError': '', 'onNull': '' } }, "
     + "    'regex': ?0, 'options': 'i' } } }, "
     + "{ '$expr': { '$gt': [ "
     + "    { '$size': { '$filter': { "
     + "        'input': { '$ifNull': [ { '$objectToArray': '$metrics' }, [] ] }, "
     + "        'as': 'm', "
     + "        'cond': { '$regexMatch': { "
     + "            'input': { '$convert': { 'input': '$$m.v', 'to': 'string', 'onError': '', 'onNull': '' } }, "
     + "            'regex': ?0, 'options': 'i' } } "
     + "    } } }, "
     + "    0 "
     + "] } } "
     + "] }")
    List<Activity> findByKeywords(String keywords);
}
