package ims.owen.thejavatest.domain;

import ims.owen.thejavatest.study.StudyStatus;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class Study {
    private StudyStatus status = StudyStatus.DRAFT;
    private int limit;
    private String name;
    private LocalDateTime opendDataTime;
    @ManyToOne
    private Member owner;

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }

    public Study(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this.limit = limit;
    }

    public void open() {
        this.status = StudyStatus.OPEND;
        this.opendDataTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Study{" +
                "limit=" + limit +
                ", name='" + name + '\'' +
                '}';
    }

    public StudyStatus getStatus() {
        return this.status;
    }

    public int getLimit() {
        return limit;
    }

    public Member getOwner() {
        return owner;
    }
    public void setOwner(Member member) {
        this.owner = member;
    }

    public LocalDateTime getOpendDataTime() {
        return opendDataTime;
    }
}
