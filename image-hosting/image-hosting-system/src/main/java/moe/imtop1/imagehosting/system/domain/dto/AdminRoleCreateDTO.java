package moe.imtop1.imagehosting.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminRoleCreateDTO {
    @NotBlank(message = "角色名不能为空")
    private String rolesName;
    
    private String description;
}
