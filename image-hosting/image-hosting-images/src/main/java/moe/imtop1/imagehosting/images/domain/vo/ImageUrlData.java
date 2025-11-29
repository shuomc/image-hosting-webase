package moe.imtop1.imagehosting.images.domain.vo;

import lombok.*;

/**
 * @author shuomc
 * @Date 2025/4/18
 * @Description
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageUrlData {
    private String imageId;
    // TODO：water
    private String originalMinioUrl;
    private String fileName;
    private String userId;
    private String contentType;
    private Long size;
    private Boolean isPublic;
    private String description;
}
