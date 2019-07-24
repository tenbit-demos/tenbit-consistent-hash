package data.impl;

import cn.tenbit.hare.core.lite.util.HareSleepUtils;
import config.Config;
import data.DataDao;
import domain.Person;
import lombok.Getter;
import support.ExecuteThreadPool;
import support.Statusable;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author bangquan.qian
 * @Date 2019-07-24 11:25
 */
public class StatusablePersonDao extends PersonDao implements DataDao<Person, PersonQuery>, Statusable<StatusablePersonDao.Status> {

    private final Status status = new Status();
    private PersonDao personDao;

    public StatusablePersonDao(PersonDao personDao) {
        this.personDao = personDao;
        ExecuteThreadPool.getPool().execute(new PersonDaoMonitor(this));
    }

    @Override
    public Person select(PersonQuery query) {
        Person person = personDao.select(query);
        takeStatus();
        return person;
    }

    private void takeStatus() {
        status.times.incrementAndGet();
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Getter
    public static class Status {

        private final AtomicLong times = new AtomicLong();

        private final AtomicLong qps = new AtomicLong();
    }

    private static class PersonDaoMonitor implements Runnable {

        private final StatusablePersonDao personDao;

        private long lastTimes;

        public PersonDaoMonitor(StatusablePersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        public void run() {
            while (true) {
                lastTimes = personDao.status.times.get();
                HareSleepUtils.sleepSeconds(Config.QPS_MONITOR_S_SPEED);
                long currentTimes = personDao.status.times.get();
                personDao.status.qps.getAndSet(currentTimes - lastTimes);
            }
        }
    }
}
