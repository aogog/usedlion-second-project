package project.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;

import jakarta.servlet.http.HttpServletResponse;
import project.demo.entity.Image;
import project.demo.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private ImageRepository imageRepository;

    public int saveImage(List<MultipartFile> files, int postId) throws IOException {
        int fileCount = 0;
        for (MultipartFile file : files) {
            Document metadata = new Document();
            metadata.put("postId", postId);
            metadata.put("filename", file.getOriginalFilename());
            ObjectId fileId = gridFsTemplate.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    metadata);
            fileCount += fileId != null ? 1 : 0;
        }
        return fileCount;
    }

    public List<GridFSFile> getImages(@PathVariable int id, HttpServletResponse response) throws IOException {
        GridFSFindIterable findIterable = gridFsTemplate.find(
                Query.query(Criteria.where("metadata.postId").is(id)));
        return findIterable.into(new ArrayList<>());
    }

    public void deleteImage(String imageId) {
        ObjectId objectId = new ObjectId(imageId);
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(objectId)));
        imageRepository.deleteById(imageId);
    }

}
