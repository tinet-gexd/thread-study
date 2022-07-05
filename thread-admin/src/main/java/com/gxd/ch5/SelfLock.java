package com.gxd.ch5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 独占锁，不可重入
 */
public class SelfLock implements Lock {


    /**
     *
     */
    static class Sync extends AbstractQueuedSynchronizer{

        @Override
        protected boolean tryAcquire(int arg) {

            if (compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0){
                throw new IllegalMonitorStateException("无需解锁");
            }
            setExclusiveOwnerThread(null);
            setState(arg);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    static final Sync SYNC_LOCK = new Sync();

    @Override
    public void lock() {
        SYNC_LOCK.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        SYNC_LOCK.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return SYNC_LOCK.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
     return SYNC_LOCK.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        SYNC_LOCK.release(0);
    }

    @Override
    public Condition newCondition() {
        return SYNC_LOCK.newCondition();
    }
}
