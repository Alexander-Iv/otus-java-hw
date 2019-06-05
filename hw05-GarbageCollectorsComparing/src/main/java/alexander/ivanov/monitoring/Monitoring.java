package alexander.ivanov.monitoring;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class Monitoring {
    private static final Logger logger = LoggerFactory.getLogger(Monitoring.class);

    private static GarbageCollector youngMBean;
    private static GarbageCollector oldMBean;
    private static GarbageCollector othersMBean;

    public static void run() {
        register();
        switchOnMonitoring();
        Thread youngThread = new Thread(youngMBean);
        Thread oldThread = new Thread(oldMBean);
        Thread others = new Thread(othersMBean);
        youngThread.start();
        oldThread.start();
        others.start();
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();

                    logger.info("notification.getSequenceNumber() = " + notification.getSequenceNumber() +
                            " info.getGcName() = " + info.getGcName() +
                            " info.getGcAction() = " + info.getGcAction() +
                            " info.getGcCause() = " + info.getGcCause() +
                            " notification.getMessage() = " + notification.getMessage()
                    );
                    
                    if (gcName.toUpperCase().contains(GC_TYPES.YOUNG.name())) {
                        youngMBean.setCount(youngMBean.getCount() + 1);
                        youngMBean.setDuration(youngMBean.getDuration() + info.getGcInfo().getDuration());
                    } else if (gcName.toUpperCase().contains(GC_TYPES.OLD.name())) {
                        oldMBean.setCount(oldMBean.getCount() + 1);
                        oldMBean.setDuration(oldMBean.getDuration() + info.getGcInfo().getDuration());
                    } else {
                        othersMBean.setCount(othersMBean.getCount() + 1);
                        othersMBean.setDuration(othersMBean.getDuration() + info.getGcInfo().getDuration());
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private static void register() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName youngName = null;
        ObjectName oldName = null;
        ObjectName othersName = null;
        try {
            youngName = new ObjectName("alexander.ivanov.YoungGC:type=GarbageCollectorMBean");
            oldName = new ObjectName("alexander.ivanov.OldGC:type=GarbageCollectorMBean");
            othersName =  new ObjectName("alexander.ivanov.OthersGC:type=GarbageCollectorMBean");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        youngMBean = new GarbageCollector(GC_TYPES.YOUNG);
        oldMBean = new GarbageCollector(GC_TYPES.OLD);
        othersMBean = new GarbageCollector(GC_TYPES.OTHERS);

        try {
            mbs.registerMBean(youngMBean, youngName);
            mbs.registerMBean(oldMBean, oldName);
            mbs.registerMBean(othersMBean, othersName);
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }
}