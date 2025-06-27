package project.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.demo.entity.Post;
import project.demo.entity.Tag;
import project.demo.repository.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TagService tagService;

    public Post getPostById(int postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void savePost(Post post, int memberId, List<Integer> tagIds) {
        post.setMember(memberService.getMemberById(memberId));
        post.setViewCnt(post.getViewCnt() + 1);
        if (tagIds != null) {
            List<Tag> tags = tagService.getTagsByIds(tagIds);
            post.setTags(tags);
            post.setCreatedAt(LocalDateTime.now());
        }

        postRepository.save(post);
    }

    public void savePost(int postId) {
        Post post = getPostById(postId);
        post.setSolved(true);
        postRepository.save(post);

    }

    public void deletePost(int postId) {
        postRepository.deleteById(postId);
    }

}
