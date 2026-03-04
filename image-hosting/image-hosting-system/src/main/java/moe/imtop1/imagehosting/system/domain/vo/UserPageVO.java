package moe.imtop1.imagehosting.system.domain.vo;

import lombok.Data;
import java.util.List;

@Data
public class UserPageVO {
    private Long total;
    private List<UserListVO> list;
}
