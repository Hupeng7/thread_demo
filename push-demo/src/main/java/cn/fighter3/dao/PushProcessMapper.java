package cn.fighter3.dao;

import cn.fighter3.entity.PushProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description
 */
@Mapper
public interface PushProcessMapper {

    /**
     * 统计数量
     *
     * @param pushState
     * @return
     */
    Integer countPushRecordsByState(@Param("pushState") Integer pushState);

    /**
     * 分页获取推送数据
     *
     * @param pushState
     * @param start
     * @param limit
     * @return
     */
    List<PushProcess> findPushRecordsByStateLimit(@Param("pushState") Integer pushState, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 更新推送成功标识
     *
     * @param id
     * @param pushFlag
     * @return
     */
    Long updateFlagById(@Param("id") Long id, @Param("pushFlag") Integer pushFlag);

    /**
     * 更新所有未推送数据推送状态
     *
     * @param pushState
     */
    void updateAllState(Integer pushState);

    int batchUpdate(@Param("list") List<PushProcess> list);
}
