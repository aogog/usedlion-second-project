package project.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import project.demo.entity.Image;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    public List<Image> findByPostId(int postId);
}
