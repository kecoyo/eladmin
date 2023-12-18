package me.zhengjie.modules.system.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

/**
 * 用户区域
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user_area")
public class UserArea extends BaseEntity implements Serializable {

    @Id
    @Column(name = "area_id")
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "用户", hidden = true)
    private User user;

    @ApiModelProperty(value = "省")
    private Integer province;

    @ApiModelProperty(value = "市")
    private Integer city;

    @ApiModelProperty(value = "区县")
    private Integer county;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || user == null || ((UserArea) o).getUser() == null) {
            return false;
        }
        UserArea userArea = (UserArea) o;
        return Objects.equals(user.getId(), userArea.getUser().getId()) &&
                Objects.equals(province, userArea.province) &&
                Objects.equals(city, userArea.city) &&
                Objects.equals(county, userArea.county);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), province, city, county);
    }
}