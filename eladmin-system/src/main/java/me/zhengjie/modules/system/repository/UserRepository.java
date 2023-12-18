/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.system.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.zhengjie.modules.system.domain.User;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

        /**
         * 根据用户名查询
         * 
         * @param username 用户名
         * @return /
         */
        User findByUsername(String username);

        /**
         * 根据邮箱查询
         * 
         * @param email 邮箱
         * @return /
         */
        User findByEmail(String email);

        /**
         * 根据手机号查询
         * 
         * @param phone 手机号
         * @return /
         */
        User findByPhone(String phone);

        /**
         * 修改密码
         * 
         * @param username              用户名
         * @param pass                  密码
         * @param lastPasswordResetTime /
         */
        @Modifying
        @Query(value = "update sys_user set password = ?2 , pwd_reset_time = ?3 where username = ?1", nativeQuery = true)
        void updatePass(String username, String pass, Date lastPasswordResetTime);

        /**
         * 修改邮箱
         * 
         * @param username 用户名
         * @param email    邮箱
         */
        @Modifying
        @Query(value = "update sys_user set email = ?2 where username = ?1", nativeQuery = true)
        void updateEmail(String username, String email);

        /**
         * 根据角色查询用户
         * 
         * @param roleId /
         * @return /
         */
        @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r WHERE" +
                        " u.user_id = r.user_id AND r.role_id = ?1", nativeQuery = true)
        List<User> findByRoleId(Long roleId);

        /**
         * 根据角色中的部门查询
         * 
         * @param deptId /
         * @return /
         */
        @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r, sys_roles_depts d WHERE " +
                        "u.user_id = r.user_id AND r.role_id = d.role_id AND d.dept_id = ?1 group by u.user_id", nativeQuery = true)
        List<User> findByRoleDeptId(Long deptId);

        /**
         * 根据菜单查询
         * 
         * @param id 菜单ID
         * @return /
         */
        @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles ur, sys_roles_menus rm WHERE\n" +
                        "u.user_id = ur.user_id AND ur.role_id = rm.role_id AND rm.menu_id = ?1 group by u.user_id", nativeQuery = true)
        List<User> findByMenuId(Long id);

        /**
         * 根据Id删除
         * 
         * @param ids /
         */
        void deleteAllByIdIn(Set<Long> ids);

        /**
         * 根据岗位查询
         * 
         * @param ids /
         * @return /
         */
        @Query(value = "SELECT count(1) FROM sys_user u, sys_users_jobs j WHERE u.user_id = j.user_id AND j.job_id IN ?1", nativeQuery = true)
        int countByJobs(Set<Long> ids);

        /**
         * 根据部门查询
         * 
         * @param deptIds /
         * @return /
         */
        @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id IN ?1", nativeQuery = true)
        int countByDepts(Set<Long> deptIds);

        /**
         * 根据角色查询
         * 
         * @param ids /
         * @return /
         */
        @Query(value = "SELECT count(1) FROM sys_user u, sys_users_roles r WHERE " +
                        "u.user_id = r.user_id AND r.role_id in ?1", nativeQuery = true)
        int countByRoles(Set<Long> ids);

        /**
         * 重置密码
         * 
         * @param ids 、
         * @param pwd 、
         */
        @Modifying
        @Query(value = "update sys_user set password = ?2 where user_id in ?1", nativeQuery = true)
        void resetPwd(Set<Long> ids, String pwd);

        /**
         * 根据指定用户区域查询用户
         */
        @Query(value = "select u.* from sys_user u,sys_user_area ua,sys_users_roles ur,sys_role r "
                        + "where u.user_id=ua.user_id and u.user_id=ur.user_id and r.role_id=ur.role_id "
                        + "and r.level>=:roleLevel "
                        + "and if(:county>0,ua.county=:county,if(:city>0,ua.city=:city,ua.province=:province)) "
                        + "and if(:blurry!='', (u.username like CONCAT('%',:blurry,'%') or u.nick_name like CONCAT('%',:blurry,'%') or u.phone like CONCAT('%',:blurry,'%')), 1=1) "
                        + "and if(:enabled >= 0, u.enabled=:enabled, 1=1) "
                        + "group by u.user_id order by u.user_id asc LIMIT :pageStart,:pageSize", nativeQuery = true)
        List<User> findByUserArea(@Param("province") int province, @Param("city") int city, @Param("county") int county,
                        @Param("roleLevel") int roleLevel, @Param("blurry") String blurry,
                        @Param("enabled") int enabled, @Param("pageStart") int pageStart,
                        @Param("pageSize") int pageSize);

        /**
         * 根据指定用户区域查询用户数量
         */
        @Query(value = "select count(distinct u.user_id) as cnt from sys_user u,sys_user_area ua,sys_users_roles ur,sys_role r "
                        + "where u.user_id=ua.user_id and u.user_id=ur.user_id and r.role_id=ur.role_id "
                        + "and r.level>=:roleLevel "
                        + "and if(:county>0,ua.county=:county,if(:city>0,ua.city=:city,ua.province=:province)) "
                        + "and if(:blurry!='', (u.username like CONCAT('%',:blurry,'%') or u.nick_name like CONCAT('%',:blurry,'%') or u.phone like CONCAT('%',:blurry,'%')), 1=1) "
                        + "and if(:enabled>=0, u.enabled=:enabled, 1=1) ", nativeQuery = true)
        int countByUserArea(@Param("province") int province, @Param("city") int city, @Param("county") int county,
                        @Param("roleLevel") int roleLevel, @Param("blurry") String blurry,
                        @Param("enabled") int enabled);

        /**
         * 根据全部用户区域查询用户
         */
        @Query(value = "select u.* from sys_user u,sys_user_area ua,sys_user_area ua2,sys_users_roles ur,sys_role r "
                        + "where u.user_id=ua.user_id and u.user_id=ur.user_id and r.role_id=ur.role_id "
                        + "and ua2.user_id=:userId and r.level>=:roleLevel "
                        + "and if(ua2.county>0,ua.county=ua2.county,if(ua2.city>0,ua.city=ua2.city,ua.province=ua2.province)) "
                        + "and if(:blurry!='', (u.username like CONCAT('%',:blurry,'%') or u.nick_name like CONCAT('%',:blurry,'%') or u.phone like CONCAT('%',:blurry,'%')), 1=1) "
                        + "and if(:enabled >= 0, u.enabled=:enabled, 1=1) "
                        + "group by u.user_id order by u.user_id asc LIMIT :pageStart,:pageSize", nativeQuery = true)
        List<User> findByAllUserArea(@Param("userId") long userId, @Param("roleLevel") int roleLevel,
                        @Param("blurry") String blurry, @Param("enabled") int enabled,
                        @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);

        /**
         * 根据全部用户区域查询用户数量
         */
        @Query(value = "select count(distinct u.user_id) as cnt from sys_user u,sys_user_area ua,sys_user_area ua2,sys_users_roles ur,sys_role r "
                        + "where u.user_id=ua.user_id and u.user_id=ur.user_id and r.role_id=ur.role_id "
                        + "and ua2.user_id=:userId and r.level>=:roleLevel "
                        + "and if(ua2.county>0,ua.county=ua2.county,if(ua2.city>0,ua.city=ua2.city,ua.province=ua2.province)) "
                        + "and if(:blurry!='', (u.username like CONCAT('%',:blurry,'%') or u.nick_name like CONCAT('%',:blurry,'%') or u.phone like CONCAT('%',:blurry,'%')), 1=1) "
                        + "and if(:enabled>=0, u.enabled=:enabled, 1=1) ", nativeQuery = true)

        int countByAllUserArea(@Param("userId") long userId, @Param("roleLevel") int roleLevel,
                        @Param("blurry") String blurry, @Param("enabled") int enabled);
}
