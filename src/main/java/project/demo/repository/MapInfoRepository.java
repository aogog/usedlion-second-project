package project.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import project.demo.entity.MapInfo;

@Repository
public interface MapInfoRepository extends MongoRepository<MapInfo, String> {

    MapInfo findByPostId(int postId);

    void deleteByPostId(int postId);

}
