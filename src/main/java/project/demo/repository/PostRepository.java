package project.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demo.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // Additional query methods can be defined here if needed

}
