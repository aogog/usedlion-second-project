package project.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import project.demo.entity.MapInfo;
import project.demo.entity.Post;
import project.demo.entity.ReactionType;
import project.demo.entity.TargetType;
import project.demo.service.BookService;
import project.demo.service.CommentService;
import project.demo.service.ImageService;
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

    @Autowired
    private ImageService imageService;

    @GetMapping("/post")
    public String getAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @GetMapping("/post/{postId}")
    public String getPostInfo(@PathVariable int postId, Model model) throws IOException {
        Post post = postService.getPostById(postId);
        postService.savePost(post, 1, null); // 임시로 1번 멤버로 설정, 조회수

        int like = reactionService.getReactionCount(postId, TargetType.POST, ReactionType.LIKE);
        int dislike = reactionService.getReactionCount(postId, TargetType.POST, ReactionType.DISLIKE);

        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getCommentReactionByPostId(postId));
        model.addAttribute("likeCount", like);
        model.addAttribute("dislikeCount", dislike);

        MapInfo mapInfo = mapInfoService.findByPostId(postId);
        System.out.println("MapInfo: " + mapInfo.getAddress());
        model.addAttribute("mapInfo", mapInfo);
        model.addAttribute("imageIds", getImageIds(postId));
        return "detail";
    }

    public List<String> getImageIds(int postId) throws IOException {
        List<GridFSFile> images = imageService.getImages(postId, null);
        return images.stream()
                .map(file -> file.getObjectId().toHexString())
                .collect(Collectors.toList());
    }

    @GetMapping("/post/new")
    public String createPostForm(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        model.addAttribute("tags", tagService.getAllTags());
        return "create";
    }

    @PostMapping("/test-upload")
    public String testPost(@RequestParam(name = "imgfile", required = false) List<MultipartFile> file)
            throws IOException {
        for (var a : file) {
            System.out.println("File: " + a.getOriginalFilename());
        }
        return "redirect:/post";
    }

    @GetMapping("/test-upload")
    public String testUploadForm(Model model) {
        return "test";
    }

    @PostMapping("/post/new3")
    public String createTest(@RequestParam(name = "imgfile", required = false) List<MultipartFile> files,
            @RequestParam(name = "title") String title, @RequestParam(name = "content") String content)
            throws IOException {
        for (MultipartFile file : files) {
            System.out.println("Uploaded file name: " + file.getOriginalFilename());
        }
        System.out.println(title);

        return "redirect:/post";
    }

    @PostMapping("/post/new")
    public String createPost(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(name = "content") String content,
            @RequestParam(name = "imgfile", required = false) List<MultipartFile> files,
            @RequestParam(name = "postId", required = false) Integer postId,
            @RequestParam(name = "tags") List<Integer> tagIds,
            @RequestParam(name = "deleteImageIds", required = false) List<String> deleteImageIds,
            Model model)
            throws IOException {

        Post post = (postId == 0 ? new Post() : postService.getPostById(postId));
        post.setTitle(title);
        post.setContent(content);
        post.setMember(memberService.getMemberById(1)); // 임시로 1
        post.setSolved(false);
        postService.savePost(post, 1, tagIds);
        if (mapInfoService.findByPostId(postId) != null) {
            mapInfoService.deleteByPostId(post.getPostId());
        }
        MapInfo mapInfo = new MapInfo();
        mapInfo.setAddress(address);
        mapInfo.setLatitude(latitude);
        mapInfo.setLongitude(longitude);
        mapInfo.setPostId(post.getPostId());
        mapInfoService.save(mapInfo);
        if (files == null || files.isEmpty() || files.get(0).isEmpty()) {
            System.out.println("No files");
        } else {
            imageService.saveImage(files, post.getPostId());
        }
        if (deleteImageIds != null && !deleteImageIds.isEmpty() &&
                deleteImageIds.get(0) != null) {
            for (String imageId : deleteImageIds) {
                imageService.deleteImage(imageId);
            }
        }

        return "redirect:/post/" + post.getPostId();
    }

    @GetMapping("/post/edit/{postId}")
    public String editPostForm(@PathVariable int postId, Model model) throws IOException {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("imageIds", getImageIds(postId));
        MapInfo mapInfo = mapInfoService.findByPostId(postId);
        model.addAttribute("address", mapInfo.getAddress());
        model.addAttribute("latitude", mapInfo.getLatitude());
        model.addAttribute("longitude", mapInfo.getLongitude());
        System.out.println(mapInfo.getLatitude() + ", " + mapInfo.getLongitude());
        model.addAttribute("update", true);
        return "create";
    }

    @PostMapping("/post/delete/{postId}")
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
