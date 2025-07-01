package project.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.demo.entity.MapInfo;
import project.demo.repository.MapInfoRepository;

@Service
public class MapInfoService {

    @Autowired
    private MapInfoRepository mapInfoRepository;

    public void save(MapInfo mapInfo) {
        System.out.println("Saving MapInfo: " + mapInfo);
        mapInfoRepository.save(mapInfo);
    }

    public MapInfo findByPostId(int postId) {
        return mapInfoRepository.findByPostId(postId);
    }

    public void deleteByPostId(int postId) {
        mapInfoRepository.deleteByPostId(postId);
    }

}
