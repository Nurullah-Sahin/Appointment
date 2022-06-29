package com.solidsoft.domain;


import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TimeRange {
    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public TimeRange(final String startTime, final String endTime) throws Exception {
        this(LocalDateTime.parse(startTime), LocalDateTime.parse(endTime));
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TimeRange(LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        if (startTime.isAfter(endTime)){
            throw new Exception("start time cannot be after end time");
        }

        this.startTime=startTime;
        this.endTime=endTime;
    }

    public static TimeRange intersect(TimeRange... timeRanges) throws Exception {
        if (timeRanges.length<2){
            throw new Exception("at least two time ranges required to calculate intersection");
        }

        TimeRange previousTimeRange = timeRanges[0];


        for (int i=1; i<timeRanges.length; i++){
            final TimeRange nextTimeRange = timeRanges[i];

            final LocalDateTime effectiveStartDate = previousTimeRange.getStartTime().isAfter(nextTimeRange.getStartTime()) ?
                previousTimeRange.getStartTime() : nextTimeRange.getStartTime();

            final LocalDateTime effectiveEndDate = previousTimeRange.getEndTime().isBefore(nextTimeRange.getEndTime()) ?
                previousTimeRange.getEndTime() : nextTimeRange.getEndTime();

            if (effectiveStartDate.equals(effectiveEndDate) || effectiveStartDate.isAfter(effectiveEndDate)){
                return null;
            }

            previousTimeRange = new TimeRange(effectiveStartDate, effectiveEndDate);

        }


        return previousTimeRange;
    }
}
