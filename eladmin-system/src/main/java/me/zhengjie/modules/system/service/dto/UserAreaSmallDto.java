package me.zhengjie.modules.system.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Zheng Jie
 * @date 2019-04-10
 */
@Getter
@Setter
public class UserAreaSmallDto implements Serializable {

    private Long id;

    private Integer province;

    private Integer city;

    private Integer county;
}