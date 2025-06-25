package project.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demo.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    // Additional query methods can be defined here if needed

}
