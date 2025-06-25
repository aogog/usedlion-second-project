package project.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.demo.entity.TargetType;
import project.demo.service.ReactionService;

@Controller
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping("/reaction/{postId}")
    public String addReaction(@PathVariable int postId,
            @RequestParam(name = "reactionType") String reactionType) {
        int res = reactionService.saveReaction(postId, 1, TargetType.POST, reactionType); // 임시로 1번 멤버로 설정
        return "redirect:/post/" + postId;
    }

    @PostMapping("/reaction/{postId}/{commentId}")
    public String addReaction(@PathVariable int postId, @PathVariable int commentId,
            @RequestParam(name = "reactionType") String reactionType) {
        reactionService.saveReaction(commentId, 1, TargetType.COMMENT, reactionType); // 임시로 1번 멤버로 설정
        return "redirect:/post/" + postId;
    }

    @PostMapping("reaction/remove/{postId}")
    public void removeReaction(@PathVariable int postId,
            @RequestParam(name = "reactionType") String reactionType) {
        reactionService.removeReaction(1, TargetType.POST, reactionType); // 임시로 1번 멤버로 설정
    }

    @PostMapping("reaction/remove/{postId}/{commentId}")
    public void removeReaction(@PathVariable int postId, @PathVariable int commentId,
            @RequestParam(name = "reactionType") String reactionType) {
        reactionService.removeReaction(1, TargetType.COMMENT, reactionType); // 임시로 1번 멤버로 설정
    }

}
