package project.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demo.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    public List<Comment> findByPost_PostId(int postId);

}
