package com.sunten.hrms.fnd.domain;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
public class FndUserAvatar extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String realName;

    private String path;

    private String avatarSize;


    public FndUserAvatar(FndUserAvatar fndUserAvatar, String realName, String path, String avatarSize) {
        this.id = ObjectUtil.isNotEmpty(fndUserAvatar) ? fndUserAvatar.getId() : null;
        this.realName = realName;
        this.path = path;
        this.avatarSize = avatarSize;
    }
}
