package moe.imtop1.imagehosting.system.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserListVO {
    private String userId;
    private String userName;
    private String userEmail;
    private String userRole;
    private Integer status;
    private LocalDateTime createTime;
    private String avatarUrl;
}
