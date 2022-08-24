package cn.fighter3.service.impl;

import cn.fighter3.dao.PushProcessMapper;
import cn.fighter3.entity.PushProcess;
import cn.fighter3.service.PushProcessService;
import cn.fighter3.util.PushUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description #####################################
 */
@Service
public class PushProcessServiceImpl implements PushProcessService {
    @Autowired
    private PushUtil pushUtil;
    @Autowired
    private PushProcessMapper pushProcessMapper;

    private final static Logger logger = LoggerFactory.getLogger(PushProcessServiceImpl.class);

    //每个线程每次查询的条数
    private static final Integer LIMIT = 200;
    //核心线程数:设置为操作系统CPU数乘以2
    private static final Integer CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    //private static final Integer CORE_POOL_SIZE = 8;
    //最大线程数:设置为和核心线程数相同
    private static final Integer MAXIMUM_POOl_SIZE = CORE_POOL_SIZE;

    //创建线程池
    ThreadPoolExecutor pool = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOl_SIZE * 2,
            0,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100));

    @Override
    public void pushData() throws ExecutionException, InterruptedException {
        //计数器，需要保证线程安全
        int count = 0;
        //未推送数据总数
        Integer total = pushProcessMapper.countPushRecordsByState(0);
        logger.info("未推送数据条数：{}", total);

        //计算需要多少轮
        int num = total / (LIMIT * CORE_POOL_SIZE) + 1;
        logger.info("要经过的轮数:{}", num);

        //统计总共推送成功的数据条数
        int totalSuccessCount = 0;
        for (int i = 0; i < num; i++) {
            //接收线程返回结果
            List<Future<Integer>> futureList = new ArrayList<>(32);

            logger.info("轮数开始==================>{}", i);
            //起CORE_POOL_SIZE个线程并行查询更新库，加锁
            for (int j = 0; j < CORE_POOL_SIZE; j++) {
                synchronized (PushProcessServiceImpl.class) {
                    int start = count * LIMIT;
                    count++;
                    //提交线程，用数据起始位置标识线程
                    logger.info("轮数：{},推送区间：[{},{}]数据", i, start, start + LIMIT);
                    Future<Integer> future = pool.submit(new PushDataTask(start, LIMIT, start));
                    //先不取值，防止阻塞,放进集合
                    futureList.add(future);
                }
            }
            //统计本轮推送成功数据
            for (Future f : futureList) {
                totalSuccessCount = totalSuccessCount + (int) f.get();
            }
            logger.info("轮数结束<================{}", i);
        }
        //关闭线程池
        //pool.shutdown();

        //更新推送标志
        // pushProcessMapper.updateAllState(1);
        logger.info("推送数据完成，需推送数据:{},推送成功：{}", total, totalSuccessCount);
    }

    /**
     * 推送数据线程类
     */
    class PushDataTask implements Callable<Integer> {
        int start;
        int limit;

        PushDataTask(int start, int limit, int threadNo) {
            this.start = start;
            this.limit = limit;
        }

        @Override
        public Integer call() throws Exception {
            //设置线程名字
            Thread.currentThread().setName("Thread" + start);
            int updateCount = 0;
            //推送的数据
            List<PushProcess> pushProcessList = pushProcessMapper.findPushRecordsByStateLimit(0, start, limit);
            if (CollectionUtils.isEmpty(pushProcessList)) {
                return updateCount;
            }

            int successCount = 0, failCount = 0;
            for (PushProcess process : pushProcessList) {
                /**
                 * 请求第三方接口，根据返回值处理内部逻辑
                 */
                boolean isSuccess = pushUtil.sendRecord(process);

//                单笔处理
//                if (isSuccess) {   //推送成功
//                    //更新推送标识
//                    pushProcessMapper.updateFlagById(process.getId(), 1);
//                    count++;
//                } else {  //推送失败
//                    pushProcessMapper.updateFlagById(process.getId(), 2);
//                }

                process.setPushState(1);
                if (isSuccess) {
                    process.setPushFlag(1);
                    successCount++;
                    updateCount++;
                } else {
                    failCount++;
                    process.setPushFlag(2);
                }
            }

            // 批量更新数据库,单条处理效率太低
            int updateSuccess = pushProcessMapper.batchUpdate(pushProcessList);
            logger.info("推送区间[{},{}]数据,推送成功{}条！失败{}条！更新数据库成功{}条！", start, start + limit, successCount, failCount, updateSuccess);

            return updateCount;
        }
    }
}
