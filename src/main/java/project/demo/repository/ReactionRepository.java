package project.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.demo.entity.Reaction;
import project.demo.entity.ReactionType;
import project.demo.entity.TargetType;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    public Reaction findByMemberMemberIdAndTargetTypeAndTargetId(int memberId, TargetType targetType, int targetId);

    public int countByTargetIdAndTargetTypeAndReactionType(int targetId, TargetType targetType,
            ReactionType reactionType);

}
