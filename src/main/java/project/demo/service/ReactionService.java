package project.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.demo.entity.Reaction;
import project.demo.entity.ReactionType;
import project.demo.entity.TargetType;
import project.demo.repository.ReactionRepository;

@Service
public class ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private MemberService memberService;

    public int saveReaction(int targetId, int memberId, TargetType type, String reactionType) {

        Reaction reaction = new Reaction();

        reaction.setMember(memberService.getMemberById(memberId));
        reaction.setTargetType(type);

        if (reactionType.equals("LIKE")) {
            reaction.setReactionType(ReactionType.LIKE);
        } else
            reaction.setReactionType(ReactionType.DISLIKE);

        reaction.setCreatedAt(LocalDateTime.now());
        reaction.setTargetId(targetId);

        if (reactionRepository.findByMemberMemberIdAndTargetTypeAndTargetId(memberId, type,
                targetId) != null) {
            return 1;
        }
        reactionRepository.save(reaction);
        return 0;

    }

    public void removeReaction(int memberId, TargetType type, String reactionType) {
        Reaction reaction = new Reaction();
        reaction.setMember(memberService.getMemberById(memberId));
        reaction.setTargetType(type);
        if (reactionType.equals("LIKE")) {
            reaction.setReactionType(ReactionType.LIKE);
        } else
            reaction.setTargetType(TargetType.COMMENT);

        if (reactionType.equals("LIKE")) {
            reaction.setReactionType(ReactionType.LIKE);
        } else
            reaction.setReactionType(ReactionType.DISLIKE);
        reactionRepository.delete(reaction);
    }

    public int getReactionCount(int targetId, TargetType type, ReactionType reactionType) {
        return reactionRepository.countByTargetIdAndTargetTypeAndReactionType(targetId, type, reactionType);
    }

}
