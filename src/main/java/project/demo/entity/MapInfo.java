package project.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapInfo {
    @Id
    private String id;
    private int postId;
    private String address;
    private double latitude;
    private double longitude;

}
