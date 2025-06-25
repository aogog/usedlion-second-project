package project.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.demo.dto.CommentReaction;
import project.demo.entity.Comment;
import project.demo.entity.ReactionType;
import project.demo.entity.TargetType;
import project.demo.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private ReactionService reactionService;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void addComment(int commentId) {
        Comment comment = getCommentById(commentId).getComment();
        comment.setSelected(true);
        commentRepository.save(comment);
    }

    public CommentReaction getCommentById(int commentId) {
        CommentReaction commentReaction = new CommentReaction();
        Comment comment = commentRepository.findById(commentId).orElse(null);
        commentReaction.setComment(comment);
        int likeCount = reactionService.getReactionCount(comment.getCommentId(),
                TargetType.COMMENT,
                ReactionType.LIKE);
        int dislikeCount = reactionService.getReactionCount(comment.getCommentId(),
                TargetType.COMMENT,
                ReactionType.DISLIKE);
        commentReaction.setLikeCount(likeCount);
        commentReaction.setDislikeCount(dislikeCount);

        return commentReaction;
    }

    public List<CommentReaction> getCommentsByPostId(int postId) {
        List<CommentReaction> commentReactions = new ArrayList<>();
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        for (Comment comment : comments) {
            CommentReaction commentReaction = getCommentById(comment.getCommentId());
            commentReactions.add(commentReaction);
        }
        return commentReactions;
    }

    public void deleteComment(int commentId) {
        int bookId = getCommentById(commentId).getComment().getBook().getBookId();
        commentRepository.deleteById(commentId);
        bookService.deleteBook(bookId);
    }

}
