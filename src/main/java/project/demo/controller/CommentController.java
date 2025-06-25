package project.demo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.demo.entity.Book;
import project.demo.entity.Comment;
import project.demo.service.BookService;
import project.demo.service.CommentService;
import project.demo.service.MemberService;
import project.demo.service.PostService;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BookService bookService;

    @PostMapping("/post/{postId}/comment")
    public String addComment(@PathVariable int postId, @RequestParam(name = "content") String content,
            @RequestParam(name = "title") String title, @RequestParam(name = "author") String author) {

        Comment comment = new Comment();
        Book book = new Book();

        book.setTitle(title);
        book.setAuthor(author);

        bookService.saveBook(book);

        comment.setPost(postService.getPostById(postId));
        comment.setContent(content);

        comment.setBook(book);
        comment.setMember(memberService.getMemberById(1)); // 임시로 1번 멤버로 설정
        comment.setCreatedAt(LocalDateTime.now());

        commentService.addComment(comment);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/comment/{commentId}")
    public String deleteComment(@PathVariable int postId, @PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/comment/edit/{commentId}")
    public String editComment(@PathVariable int postId, @PathVariable int commentId,
            @RequestParam(name = "newContent") String content) {
        Comment comment = commentService.getCommentById(commentId).getComment();
        comment.setContent(content);
        commentService.addComment(comment);
        return "redirect:/post/" + postId;
    }

}
