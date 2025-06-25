package project.demo.dto;

import lombok.Data;
import project.demo.entity.Comment;

@Data
public class CommentReaction {
    private Comment comment;
    private int likeCount;
    private int dislikeCount;

}
