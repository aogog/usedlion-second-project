package project.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.demo.entity.MapInfo;
import project.demo.entity.Post;
import project.demo.entity.ReactionType;
import project.demo.entity.TargetType;
import project.demo.service.CommentService;
import project.demo.service.MapInfoService;
import project.demo.service.MemberService;
import project.demo.service.PostService;
import project.demo.service.ReactionService;
import project.demo.service.TagService;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private MapInfoService mapInfoService;

    @GetMapping("/post")
    public String getAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @GetMapping("/post/{postId}")
    public String getPostInfo(@PathVariable int postId, Model model) {
        Post post = postService.getPostById(postId);
        postService.savePost(post, 1, null); // 임시로 1번 멤버로 설정, 조회수

        int like = reactionService.getReactionCount(postId, TargetType.POST, ReactionType.LIKE);
        int dislike = reactionService.getReactionCount(postId, TargetType.POST, ReactionType.DISLIKE);

        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getCommentsByPostId(postId));
        model.addAttribute("likeCount", like);
        model.addAttribute("dislikeCount", dislike);

        MapInfo mapInfo = mapInfoService.findByPostId(postId);
        model.addAttribute("mapInfo", mapInfo);

        return "detail";
    }

    @GetMapping("/post/new")
    public String createPostForm(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        model.addAttribute("tags", tagService.getAllTags());
        return "create";
    }

    @PostMapping("/post/new")
    public String createPost(Post post, Model model,
            @RequestParam(name = "tags") List<Integer> tagIds, @RequestParam(name = "address") String address,
            @RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude) {

        postService.savePost(post, 1, tagIds);
        model.addAttribute("post", post);

        MapInfo mapInfo = new MapInfo();
        System.out.println("Address: " + address + ", Latitude: " + latitude + ", Longitude: " + longitude);
        mapInfo.setAddress(address);
        mapInfo.setPostId(post.getPostId());
        mapInfo.setLatitude(latitude);
        mapInfo.setLongitude(longitude);
        mapInfoService.save(mapInfo);
        return "redirect:/post/" + post.getPostId();
    }

    @GetMapping("/post/edit/{postId}")
    public String editPostForm(@PathVariable int postId, Model model) {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        model.addAttribute("tags", tagService.getAllTags());
        return "create";
    }

    @DeleteMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable int postId) {
        postService.deletePost(postId);
        return "redirect:/post";
    }

    @PostMapping("/post/{postId}/select/{commentId}")
    public String selectComment(@PathVariable int postId, @PathVariable int commentId) {
        commentService.addComment(commentId);
        postService.savePost(postId);
        return "redirect:/post/" + postId;
    }

}
