package moe.imtop1.imagehosting.images.domain.vo;

import lombok.*;

/**
 * @author shuomc
 * @Date 2025/12/1
 * @Description 预签名vo
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImagePresignedUrlData {
    String imageId;
    String presignedUrl;
}
