package project.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.demo.entity.Tag;
import project.demo.repository.TagRepository;

@Service

public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<Tag> getTagsByIds(List<Integer> tagIds) {
        return tagRepository.findAllById(tagIds);
    }
}
