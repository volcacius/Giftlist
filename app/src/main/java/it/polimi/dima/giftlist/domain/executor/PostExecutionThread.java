package it.polimi.dima.giftlist.domain.executor;

import rx.Scheduler;

/**
 * Created by Alessandro on 18/03/16.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
