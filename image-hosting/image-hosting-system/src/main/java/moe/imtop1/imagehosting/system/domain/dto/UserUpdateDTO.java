package moe.imtop1.imagehosting.system.domain.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String userId;
    private String userRole;
    private Integer status;
}
