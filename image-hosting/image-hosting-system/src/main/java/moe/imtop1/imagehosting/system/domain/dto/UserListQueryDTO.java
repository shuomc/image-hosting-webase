package moe.imtop1.imagehosting.system.domain.dto;

import lombok.Data;

@Data
public class UserListQueryDTO {
    private Integer page = 1;
    private Integer size = 10;
    private String keyword;
}
