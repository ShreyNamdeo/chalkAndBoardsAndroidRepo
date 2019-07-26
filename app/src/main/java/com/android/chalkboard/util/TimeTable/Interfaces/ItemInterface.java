package com.android.chalkboard.util.TimeTable.Interfaces;

import org.joda.time.DateTime;

public interface ItemInterface {
    String getHeader();
    String getSubHeader();
    String[] getOptions();
    DateTime getStartDate();
    DateTime getEndDate();
}
