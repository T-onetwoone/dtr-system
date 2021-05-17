package cn.edu.mju.ccce.dtrsystem.job;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import cn.edu.mju.ccce.dtrsystem.bmo.CourseBmo;
import cn.edu.mju.ccce.dtrsystem.bmo.ReservationBmo;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.MapTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.job.CourseDetectionJob<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>定时任务，用来更新课程的状态（到课程时间自动不能预约）<br>
 * <b>创建时间：</b>2020-03-13 15:16<br>
 */
@Component
public class CourseDetectionJob implements SchedulingConfigurer, Runnable, Trigger {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.CourseBmoImpl")
    private CourseBmo courseBmo;

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.ReservationBmoImpl")
    private ReservationBmo reservationBmo;

    @Override
    public void run() {
        Date nowTime = new Date();
        log.info("执行检测任务-开始");
        try {
            Map<String, Object> courseListMap = reservationBmo.getCanReservationCourseList();
            boolean courseListMapBoolean = G.bmo.returnMapBool(courseListMap);
            if (!courseListMapBoolean) {
                String msg = G.bmo.returnMapMsg(courseListMap);
                log.error("获取检测任务错误：", msg);
                return;
            }
            List<Course> courseList = (List<Course>) MapTool.getObject(courseListMap, "courseList");
            if (courseList.isEmpty()) {
                log.info("可预约课程列表为空");
                return;
            }
            for (Course c : courseList) {

                Date courseTime = c.getCOURSE_TIME();
                Long i = nowTime.getTime() - courseTime.getTime();
                if (i > -10 * 60 * 1000) {
                    String courseID = String.valueOf(c.getCOURSE_ID());
                    Map<String, Object> ralMap = courseBmo.passDueCourse(courseID);
                    if (!G.bmo.returnMapBool(ralMap)) {
                        log.error("更新状态异常：", G.bmo.returnMapMsg(ralMap));
                    }
                }
            }

        } catch (Exception e) {
            log.error("执行检测任务异常:", e);
        } finally {
            log.info("执行检测任务-结束，用时" + (new Date().getTime() - nowTime.getTime()) + "毫秒");
        }
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        String cronString = "0 0/1 * * * ?";
        log.debug("课程检测job任务时间cron表达式设定={}", cronString);
        return new CronTrigger(cronString).nextExecutionTime(triggerContext);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this, this);
    }
}
