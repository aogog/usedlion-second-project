package project.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demo.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

}
