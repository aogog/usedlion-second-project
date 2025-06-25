package project.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demo.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    // Additional query methods can be defined here if needed

}
