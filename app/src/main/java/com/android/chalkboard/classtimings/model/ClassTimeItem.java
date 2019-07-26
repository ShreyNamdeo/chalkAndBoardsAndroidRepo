package com.android.chalkboard.classtimings.model;

import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.util.TimeRange;
import com.android.chalkboard.util.TimeTable.Interfaces.ItemInterface;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
public class ClassTimeItem implements ItemInterface{

    ClassResponse classResponse;
    TimeRange timeRange;
    DateTime startDate, endDate;

    public ClassTimeItem(){}

    public ClassTimeItem(ClassResponse classResponse){
        this.classResponse = classResponse;
        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        startDate = format.parseDateTime(dateFormat.format(new Date(classResponse.getStartDate())) + " " + classResponse.getStartTime());
        endDate = format.parseDateTime(dateFormat.format(new Date(classResponse.getEndDate()))+ " " + classResponse.getEndTime());
        this.timeRange = new TimeRange(startDate, endDate);
    }

    public DateTime getStartDate(){
        return startDate;
    }

    public void setStartDate(DateTime startDate){
        this.startDate = startDate;
    }

    public  DateTime getEndDate(){
        return endDate;
    }

    public void setEndDate(DateTime endDate){
        this.endDate = endDate;
    }

    public void setClassResponse(ClassResponse classResponse){
        this.classResponse = classResponse;
    }

    public ClassResponse getClassResponse(){
        return classResponse;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public String getHeader(){
        return classResponse.getName();
    }

    public String getSubHeader(){
        return classResponse.getInstitutionName();
    }

    public String[] getOptions(){
        return new String[]{"Class Agenda", "Attendance", "View Class"};
    }
}
