package cn.enilu.flash.dao.system;

import cn.enilu.flash.bean.entity.system.Menu;
import cn.enilu.flash.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author enilu
 */
public interface MenuRepository extends BaseRepository<Menu, Long> {

    Menu findByCode(String code);

    List<Menu> findByPcodesLike(String code);

    @Query(nativeQuery = true, value = "select url from t_sys_relation rel inner join t_sys_menu m on rel.menuid = m.id where  rel.roleid = ?1")
    List<String> getResUrlsByRoleId(Long roleId);

    @Query(nativeQuery = true, value = "select code from t_sys_relation rel inner join t_sys_menu m on rel.menuid = m.id where   rel.roleid = ?1")
    List<String> getResCodesByRoleId(Long roleId);

    @Query(nativeQuery = true, value = "SELECT m1.id AS id, m1.icon AS icon, ( CASE WHEN (m2.id = 0 OR m2.id IS NULL) " +
            "THEN 0 ELSE m2.id END ) AS parentId, m1. NAME AS NAME, m1.url AS url, m1.levels AS levels, m1.ismenu AS " +
            "ismenu, m1.num AS num, m1. CODE AS CODE,m1.component,m1.hidden,m1.pcode FROM t_sys_menu m1 LEFT JOIN t_sys_menu m2 " +
            "ON " +
            "m1.pcode = m2. CODE ORDER BY levels, num ASC")
    List getMenus();

    @Query(nativeQuery = true, value = "SELECT m1.id AS id, m1.icon AS icon, ( CASE WHEN (m2.id = 0 OR m2.id IS NULL) " +
            "THEN 0 ELSE m2.id END ) AS parentId, m1. NAME AS NAME, m1.url AS url, m1.levels AS levels, m1.ismenu AS " +
            "ismenu, m1.num AS num, m1. CODE AS CODE,m1.component,m1.hidden FROM t_sys_menu m1 LEFT JOIN t_sys_menu m2 " +
            "ON " +
            "m1.pcode = m2. CODE " +
            " where m1.id in (select distinct(menuid) from t_sys_relation where roleid   in(:roleIds))" +
            "ORDER BY levels, num ASC")
    List getMenusByRoleids(@Param("roleIds") List<String> roleIds);

    @Query(nativeQuery = true, value = "select menuid from t_sys_relation where roleid=?1")
    List getMenuIdsByRoleId(Integer roleId);

    @Query(nativeQuery = true, value = "SELECT m1.id AS id, ( CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 0 ELSE m2.id END ) AS pId, m1. NAME AS NAME, ( CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 'true' ELSE 'false' END ) AS isOpen FROM t_sys_menu m1 LEFT JOIN t_sys_menu m2 ON m1.pcode = m2. CODE ORDER BY m1.id ASC")
    List menuTreeList();

    @Query(nativeQuery = true, value = "SELECT m1.id AS id, ( CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 0 ELSE m2.id END ) AS pId, m1. NAME AS NAME, ( CASE WHEN (m2.id = 0 OR m2.id IS NULL) THEN 'true' ELSE 'false' END ) AS isOpen, ( CASE WHEN (m3.ID = 0 OR m3.ID IS NULL) THEN 'false' ELSE 'true' END ) \"checked\" FROM t_sys_menu m1 LEFT JOIN t_sys_menu m2 ON m1.pcode = m2. CODE LEFT JOIN ( SELECT ID FROM t_sys_menu WHERE ID IN (?1)) m3 ON m1.id = m3.id ORDER BY m1.id ASC")
    List menuTreeListByMenuIds(List<Long> menuIds);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from t_sys_relation where menuid=?1")
    void deleteRelationByMenu(Long menuId);

}
