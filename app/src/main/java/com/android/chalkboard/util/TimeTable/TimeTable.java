package com.android.chalkboard.util.TimeTable;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.view.AddClassFragment;
import com.android.chalkboard.classtimings.model.ClassTimeItem;
import com.android.chalkboard.util.TimeRange;
import com.android.chalkboard.util.TimeTable.Interfaces.TimeTableEventListener;
import com.android.chalkboard.util.TimeTable.drawable.TextViewBackground;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeTable extends RelativeLayout {

    private boolean isTouched = false;
    public final String TAG = "TimeTable.java";
    public static int FORWARD = 0, BACKWARD = 1, scrollX = 0;
    private int setHeaderWidth, setHeaderHeight, setFirstColumnWidth, setFirstColumnHeight, setDataCellWidth, setDataCellHeight,
            setHeaderTextColor, setHeaderTextStyle, setFirstColumnTextColor, setDataCellBackgroundColor, setDataCellTextColor,
            setDataCellTextStyle, setSelectedBackgroundColor, setCellBorderColor, setTodayDateTextColor, setTodayColumnColor,
            setWeekendColor;

    Date startDate, endDate, today;
    private String timeString[] = {"12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM","6:00 AM", "7:00 AM", "8:00 AM",
            "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM",
            "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"};

    // set the header titles
    private String headers[];
    private Date dateHeader[];
    private TimeTableEventListener listener;
    private List<ClassTimeItem> classItems;

    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;

    Context context;

    List<SampleObject> sampleObjects = this.sampleObjects();

    int headerCellsWidth[] = new int[9];


    public TimeTable(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public TimeTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeTable, 0 , 0);
        setHeaderWidth = a.getInteger(R.styleable.TimeTable_setHeaderWidth, 0);
        setHeaderHeight = a.getInteger(R.styleable.TimeTable_setHeaderHeight, 0);
        setFirstColumnWidth = a.getInteger(R.styleable.TimeTable_setFirstColumnWidth, 0);
        setFirstColumnHeight = a.getInteger(R.styleable.TimeTable_setFirstColumnHeight, 0);
        setDataCellHeight = a.getInteger(R.styleable.TimeTable_setDataCellHeight, 0);
        setHeaderTextColor = a.getInteger(R.styleable.TimeTable_setHeaderTextColor, 0);
        setHeaderTextStyle = a.getInteger(R.styleable.TimeTable_setHeaderTextStyle, 0);
        setFirstColumnTextColor = a.getInteger(R.styleable.TimeTable_setFirstColumnTextColor, 0);
        setHeaderHeight = a.getInteger(R.styleable.TimeTable_setHeaderHeight, 0);
        setDataCellTextColor = a.getInteger(R.styleable.TimeTable_setDataCellTextColor, 0);
        setDataCellTextStyle = a.getInteger(R.styleable.TimeTable_setDataCellTextStyle, 0);
        setSelectedBackgroundColor = a.getInteger(R.styleable.TimeTable_setSelectedBackgroundColor, 0);
        setCellBorderColor = a.getInteger(R.styleable.TimeTable_setCellBorderColor, 0);
        setTodayDateTextColor = a.getInteger(R.styleable.TimeTable_setTodayDateTextColor, 0);
        setWeekendColor = a.getInteger(R.styleable.TimeTable_setWeekendColor, 0);
        setDataCellWidth = a.getInteger(R.styleable.TimeTable_setDataCellWidth, 0);
        setTodayColumnColor = a.getInteger(R.styleable.TimeTable_setTodayColumnColor, 0);
        setDataCellBackgroundColor = a.getInteger(R.styleable.TimeTable_setDataCellBackgroundColor, 0);

        a.recycle();
        init();
    }

    public TimeTable(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeTable, 0 , 0);
        setHeaderWidth = a.getInteger(R.styleable.TimeTable_setHeaderWidth, 0);
        setHeaderHeight = a.getInteger(R.styleable.TimeTable_setHeaderHeight, 0);
        setFirstColumnWidth = a.getInteger(R.styleable.TimeTable_setFirstColumnWidth, 0);
        setFirstColumnHeight = a.getInteger(R.styleable.TimeTable_setFirstColumnHeight, 0);
        setDataCellHeight = a.getInteger(R.styleable.TimeTable_setDataCellHeight, 0);
        setHeaderTextColor = a.getInteger(R.styleable.TimeTable_setHeaderTextColor, 0);
        setHeaderTextStyle = a.getInteger(R.styleable.TimeTable_setHeaderTextStyle, 0);
        setFirstColumnTextColor = a.getInteger(R.styleable.TimeTable_setFirstColumnTextColor, 0);
        setHeaderHeight = a.getInteger(R.styleable.TimeTable_setHeaderHeight, 0);
        setDataCellTextColor = a.getInteger(R.styleable.TimeTable_setDataCellTextColor, 0);
        setDataCellTextStyle = a.getInteger(R.styleable.TimeTable_setDataCellTextStyle, 0);
        setSelectedBackgroundColor = a.getInteger(R.styleable.TimeTable_setSelectedBackgroundColor, 0);
        setCellBorderColor = a.getInteger(R.styleable.TimeTable_setCellBorderColor, 0);
        setTodayDateTextColor = a.getInteger(R.styleable.TimeTable_setTodayDateTextColor, 0);
        setWeekendColor = a.getInteger(R.styleable.TimeTable_setWeekendColor, 0);
        setDataCellWidth = a.getInteger(R.styleable.TimeTable_setDataCellWidth, 0);
        setTodayColumnColor = a.getInteger(R.styleable.TimeTable_setTodayColumnColor, 0);
        setDataCellBackgroundColor = a.getInteger(R.styleable.TimeTable_setDataCellBackgroundColor, 0);

        a.recycle();

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeTable, 0 , 0);
        setHeaderWidth = a.getInteger(R.styleable.TimeTable_setHeaderWidth, 0);
        setHeaderHeight = a.getInteger(R.styleable.TimeTable_setHeaderHeight, 0);
        setFirstColumnWidth = a.getInteger(R.styleable.TimeTable_setFirstColumnWidth, 0);
        setFirstColumnHeight = a.getInteger(R.styleable.TimeTable_setFirstColumnHeight, 0);
        setDataCellHeight = a.getInteger(R.styleable.TimeTable_setDataCellHeight, 0);
        setHeaderTextColor = a.getInteger(R.styleable.TimeTable_setHeaderTextColor, 0);
        setHeaderTextStyle = a.getInteger(R.styleable.TimeTable_setHeaderTextStyle, 0);
        setFirstColumnTextColor = a.getInteger(R.styleable.TimeTable_setFirstColumnTextColor, 0);
        setHeaderHeight = a.getInteger(R.styleable.TimeTable_setHeaderHeight, 0);
        setDataCellTextColor = a.getInteger(R.styleable.TimeTable_setDataCellTextColor, 0);
        setDataCellTextStyle = a.getInteger(R.styleable.TimeTable_setDataCellTextStyle, 0);
        setSelectedBackgroundColor = a.getInteger(R.styleable.TimeTable_setSelectedBackgroundColor, 0);
        setCellBorderColor = a.getInteger(R.styleable.TimeTable_setCellBorderColor, 0);
        setTodayDateTextColor = a.getInteger(R.styleable.TimeTable_setTodayDateTextColor, 0);
        setWeekendColor = a.getInteger(R.styleable.TimeTable_setWeekendColor, 0);
        setDataCellWidth = a.getInteger(R.styleable.TimeTable_setDataCellWidth, 0);
        setTodayColumnColor = a.getInteger(R.styleable.TimeTable_setTodayColumnColor, 0);
        setDataCellBackgroundColor = a.getInteger(R.styleable.TimeTable_setDataCellBackgroundColor, 0);

        a.recycle();
        init();
    }

    public void setClassItems(List<ClassTimeItem> classItems, TimeTableEventListener listener){
        this.listener = listener;
        this.classItems = classItems;
        checkEvent();
    }

    public List<ClassTimeItem> getCLassItems(){
        return classItems;
    }

    private void init(){
        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
        today = new Date();
        startDate = today;
        setHeaders(today, FORWARD);
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        this.setBackgroundColor(Color.RED);


        // add some table rows
        this.addTableRowToTableA();
        this. addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();
        this.tableRowForTableC();
        this.generateTableD();

        this.resizeBodyTableRowHeight();
    }

    private void setHeaders(Date date, int direction){
        headers = new String[9];
        dateHeader = new Date[9];
        SimpleDateFormat format = new SimpleDateFormat("E dd/MM");
        Calendar c= Calendar.getInstance();
        c.setTime(date);
        if(direction == FORWARD)
            if(date.equals(today)){
                headers[0] = format.format(c.getTime());
                dateHeader[0] = c.getTime();
                for(int i=1; i< 9; i++) {
                    c.add(Calendar.DATE, 1);
                    headers[i] = format.format(c.getTime());
                    dateHeader[i] = c.getTime();
                    if(i == 8) {
                        endDate = c.getTime();
                    }
                }
            } else
                for(int i=0; i< 9; i++) {
                    c.add(Calendar.DATE, 1);
                    headers[i] = format.format(c.getTime());
                    dateHeader[i] = c.getTime();
                    if(i == 8)
                        endDate = c.getTime();
                }
        else
            for(int i=9; i> 0; i--) {
                c.add(Calendar.DATE, -1);
                headers[9 - i] = format.format(c.getTime());
                dateHeader[9 - i] = c.getTime();
                if(i == 1)
                    startDate = c.getTime();
            }
    }

    // this is just the sample data
    private List<SampleObject> sampleObjects(){

        List<SampleObject> sampleObjects = new ArrayList<SampleObject>();

        for(int x=1; x<=24; x++){

            SampleObject sampleObject = new SampleObject("","","","","","","","","");

            sampleObjects.add(sampleObject);
        }

        return sampleObjects;

    }

    // initalized components
    private void initComponents(){

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewB.setSmoothScrollingEnabled(true);
        this.horizontalScrollViewB.setHorizontalScrollBarEnabled(false);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD.setSmoothScrollingEnabled(true);
        this.horizontalScrollViewD.setHorizontalScrollBarEnabled(false);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewC.setSmoothScrollingEnabled(true);
        this.scrollViewC.setVerticalScrollBarEnabled(false);
        this.scrollViewD = new MyScrollView(this.context);
        this.scrollViewD.setSmoothScrollingEnabled(true);
        this.scrollViewD.setHorizontalScrollBarEnabled(false);

        this.tableA.setBackgroundColor(Color.GREEN);
        this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);

    }

    // set essential component IDs
    private void setComponentsId(){
        this.tableA.setId(R.id.tableA);
        this.horizontalScrollViewB.setId(R.id.horizontalScrollViewB);
        this.scrollViewC.setId(R.id.scrollViewC);
        this.scrollViewD.setId(R.id.scrollViewD);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag(){

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout(){

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }



    private void addTableRowToTableA(){
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB(){
        this.tableB.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow(){

        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView("Class Time");
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        TableRow.LayoutParams params;
        if(setHeaderHeight != 0 && setFirstColumnWidth != 0) {
            params = new TableRow.LayoutParams(setFirstColumnWidth, setHeaderHeight);
        }
        else if(setFirstColumnWidth != 0) {
            params = new TableRow.LayoutParams(setFirstColumnWidth, LayoutParams.MATCH_PARENT);
        }
        else if(setHeaderHeight != 0) {
            params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, setHeaderHeight);
        }
        else {
            params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        params.setMargins(2, 0, 0, 0);
        textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_right));
        componentATableRow.addView(textView, params);
        componentATableRow.setShowDividers(TableLayout.SHOW_DIVIDER_NONE);
        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow(){
        
        TableRow componentBTableRow = new TableRow(this.context);
        componentBTableRow.setBackgroundColor(getResources().getColor(R.color.light_gray));
        int headerFieldCount = this.headers.length;

        //TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        //params.setMargins(2, 0, 0, 0);
        SimpleDateFormat s = new SimpleDateFormat("E dd/MM");
        SimpleDateFormat s1= new SimpleDateFormat("dd/MM/yyyy");
        for(int x=0; x< headerFieldCount; x++){
            TextView textView = this.headerTextView(this.headers[x]);
            textView.setTag(s1.format(dateHeader[x]));
            TableRow.LayoutParams params;
            if(setHeaderTextColor != 0)
                textView.setTextColor(setHeaderTextColor);

            if(setTodayColumnColor != 0){
                if(headers[x].equals(s.format(today)))
                    textView.setBackgroundColor(setTodayColumnColor);
            }
            if(setHeaderHeight != 0 && setHeaderWidth != 0) {
                params = new TableRow.LayoutParams(setHeaderWidth, setHeaderHeight);
            }
            else if(setHeaderWidth != 0) {
                params = new TableRow.LayoutParams(setHeaderWidth, LayoutParams.MATCH_PARENT);
            }
            else if(setHeaderHeight != 0) {
                params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, setHeaderHeight);
            }
            else {
                params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            }
            params.setMargins(2, 0, 0, 0);
            componentBTableRow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bottom_border));
            componentBTableRow.addView(textView, params);
        }

        return componentBTableRow;
    }

    // generate table row of table C and table D
    private void generateTableD(){
        SimpleDateFormat s = new SimpleDateFormat("E dd/MM");
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        for(int j = 0; j < this.sampleObjects.size(); j++){

            //TableRow tableRowForTableC = this.tableRowForTableC(sampleObject);
            //TableRow taleRowForTableD = this.taleRowForTableD(sampleObject);
            TableRow tableRowForTableD = new TableRow(this.context);

            int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();

            String info[] = {
                    sampleObjects.get(j).head1,
                    sampleObjects.get(j).head2,
                    sampleObjects.get(j).head3,
                    sampleObjects.get(j).head4,
                    sampleObjects.get(j).head5,
                    sampleObjects.get(j).head6,
                    sampleObjects.get(j).head7,
                    sampleObjects.get(j).head8,
                    sampleObjects.get(j).head9
            };
            for(int x=0 ; x<loopCount; x++){
                TableRow.LayoutParams params;
                if(setFirstColumnHeight != 0 && setHeaderWidth != 0) {
                    params = new TableRow.LayoutParams(setHeaderWidth, setFirstColumnHeight);
                }
                else if(setHeaderWidth != 0) {
                    params = new TableRow.LayoutParams(setHeaderWidth, LayoutParams.MATCH_PARENT);
                }
                else if(setFirstColumnHeight != 0) {
                    params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, setFirstColumnHeight);
                }
                else {
                    params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                }
                params.setMargins(2, 2, 0, 0);

                TextView textViewB = this.bodyTextView(info[x]);
                textViewB.setTag(sf.format(dateHeader[x]) + " " + ((TextView)((TableRow)tableC.getChildAt(j)).getChildAt(0)).getText());

                if(setDataCellTextColor != 0)
                    textViewB.setTextColor(setDataCellTextColor);

                if(setTodayColumnColor != 0){
                    TableRow tr = (TableRow)tableB.getChildAt(0);
                    if(((TextView)tr.getChildAt(x)).getText().equals(s.format(today))) {
                        textViewB.setBackgroundColor(setTodayColumnColor);
                    }
                    else
                        textViewB.setBackgroundColor(context.getResources().getColor(R.color.appWhite));
                }
                tableRowForTableD.addView(textViewB,params);
            }
            tableRowForTableD.setBackgroundColor(Color.WHITE);

            //this.tableC.addView(tableRowForTableC);
            this.tableD.addView(tableRowForTableD);

        }
    }

    // a TableRow for table C
    void tableRowForTableC(){

        for(String time: timeString) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);

            TableRow tableRowForTableC = new TableRow(this.context);
            TextView textView = this.bodyTextView(time);
            textView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.TOP);
            if(setFirstColumnTextColor != 0)
                textView.setTextColor(setFirstColumnTextColor);
            tableRowForTableC.addView(textView, params);
            tableRowForTableC.setBackgroundColor(Color.LTGRAY);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRowForTableC.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_right));
            this.tableC.addView(tableRowForTableC);
        }
    }

    // table cell standard TextView
    TextView bodyTextView(String label){

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label){

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        if(setHeaderHeight != 0)
            this.matchLayoutHeight(tableRow, setHeaderHeight);
        else
            this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth(){

        int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();

        //for(int x=0; x<(tableAChildCount+tableBChildCount); x++){
        for(int x=0; x< tableBChildCount; x++){
            /*if(x==0){
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x));
            }else{
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x-1));
            }*/
            if(x==0)
                this.headerCellsWidth[x] = setFirstColumnWidth;
            else if(setHeaderWidth != 0)
                this.headerCellsWidth[x] = setHeaderWidth;
            else
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x));
        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight(){

        int tableC_ChildCount = this.tableC.getChildCount();

        for(int x=0; x<tableC_ChildCount; x++){
            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = x < tableB.getChildCount() ? (TableRow)this.tableD.getChildAt(x) : (TableRow)  this.tableD.getChildAt(tableB.getChildCount() - 1);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight =  this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
            //this.matchLayoutHeight(tableRow, setDataCellHeight);
            //if(setFirstColumnHeight != 0)
                //this.matchLayoutHeight(tableRow, setFirstColumnHeight);
            //else
                //this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if(tableRow.getChildCount()==1){

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return ;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView{

        public MyHorizontalScrollView(Context context) {
            super(context);
            this.setOverScrollMode(OVER_SCROLL_NEVER);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();
            View view;
            int diff;

                    view = this.getChildAt(this.getChildCount() - 1);
                diff = (view.getRight() - (this.getWidth() + this.getScrollX()));
                if(diff == 0)
                    setFutureTimeTable();

                view = this.getChildAt(0);
                diff = (view.getLeft() - this.getScrollX());
                if (diff == 0) {
                    if(scrollX == 0) {
                        setPastTimeTable();
                        scrollX++;
                    } else if(scrollX < 2)
                        scrollX++;
                    else if(scrollX >=2)
                        scrollX = 0;
                }
            //}

            if(tag.equalsIgnoreCase("horizontal scroll view b")){
                horizontalScrollViewD.scrollTo(l, 0);
            }else{
                horizontalScrollViewB.scrollTo(l, 0);
            }
            super.onScrollChanged(l, t, oldl, oldt);
        }

    }

    private void setFutureTimeTable(){
        setHeaders(endDate, FORWARD);
        sampleObjects = sampleObjects();
        TableRow tableRow = (TableRow)tableB.getChildAt(0);
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        for(int j = 0; j < headers.length; j++){
            TextView textView = headerTextView(headers[j]);
            textView.setTag(sf.format(dateHeader[j]));
            TableRow.LayoutParams params;
            if(setHeaderTextColor != 0)
                textView.setTextColor(setHeaderTextColor);
            //textView.setTextAppearance(setHeaderTextStyle);
            if(setHeaderHeight != 0 && setHeaderWidth != 0) {
                params = new TableRow.LayoutParams(setHeaderWidth, setHeaderHeight);
            }
            else if(setHeaderWidth != 0) {
                params = new TableRow.LayoutParams(setHeaderWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            else if(setHeaderHeight != 0) {
                params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, setHeaderHeight);
            }
            else {
                params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            params.setMargins(2, 0, 0, 0);
            tableRow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bottom_border));
            tableRow.addView(textView, params);

            for (int i = 0; i < timeString.length; i++) {
                TableRow row = (TableRow) tableD.getChildAt(i);

                if (setFirstColumnHeight != 0 && setHeaderWidth != 0) {
                    params = new TableRow.LayoutParams(setHeaderWidth, setFirstColumnHeight);
                } else if (setHeaderWidth != 0) {
                    params = new TableRow.LayoutParams(setHeaderWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                } else if (setFirstColumnHeight != 0) {
                    params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, setFirstColumnHeight);
                } else {
                    params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                }
                params.setMargins(2, 2, 0, 0);

                TextView textViewB = bodyTextView("");
                textViewB.setTag(sf.format(dateHeader[j]) + " " + ((TextView)((TableRow)tableC.getChildAt(i)).getChildAt(0)).getText());

                if (setDataCellTextColor != 0)
                    textViewB.setTextColor(setDataCellTextColor);
                row.addView(textViewB, params);

            }
        }
        checkEvent();
    }

    private void setPastTimeTable(){
//        if(this.getScrollX() != 1) {
            setHeaders(startDate, BACKWARD);
            sampleObjects = sampleObjects();
            TableRow tableRowB = (TableRow) tableB.getChildAt(0);
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            for (int j = 0; j < headers.length; j++) {
                TextView textView = headerTextView(headers[j]);
                TableRow.LayoutParams params;
                if (setHeaderTextColor != 0)
                    textView.setTextColor(setHeaderTextColor);
                //textView.setTextAppearance(setHeaderTextStyle);
                if (setHeaderHeight != 0 && setHeaderWidth != 0) {
                    params = new TableRow.LayoutParams(setHeaderWidth, setHeaderHeight);
                } else if (setHeaderWidth != 0) {
                    params = new TableRow.LayoutParams(setHeaderWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                } else if (setHeaderHeight != 0) {
                    params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, setHeaderHeight);
                } else {
                    params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                }
                params.setMargins(2, 0, 0, 0);
                textView.setTag(sf.format(dateHeader[j]));
                tableRowB.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bottom_border));
                tableRowB.addView(textView, 0, params);

                for (int i = 0; i < timeString.length; i++) {
                    TableRow row = (TableRow) tableD.getChildAt(i);

                    if (setFirstColumnHeight != 0 && setHeaderWidth != 0) {
                        params = new TableRow.LayoutParams(setHeaderWidth, setFirstColumnHeight);
                    } else if (setHeaderWidth != 0) {
                        params = new TableRow.LayoutParams(setHeaderWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                    } else if (setFirstColumnHeight != 0) {
                        params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, setFirstColumnHeight);
                    } else {
                        params = new TableRow.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    }
                    params.setMargins(2, 2, 0, 0);

                    TextView textViewB = bodyTextView("");
                    textViewB.setTag(sf.format(dateHeader[j]) + " " + ((TextView)((TableRow)tableC.getChildAt(i)).getChildAt(0)).getText());

                    if (setDataCellTextColor != 0)
                        textViewB.setTextColor(setDataCellTextColor);
                    row.addView(textViewB, 0, params);

                }
            }
            checkEvent();
    }

    // scroll view custom class
    class MyScrollView extends ScrollView{

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("scroll view c")){
                scrollViewD.scrollTo(0, t);
            }else{
                scrollViewC.scrollTo(0,t);
            }
        }
    }

    class SampleObject{
        String head1, head2, head3, head4,head5, head6, head7, head8,head9;

        public SampleObject(String head1, String head2, String head3, String head4, String head5, String head6, String head7, String head8, String head9) {
            this.head1 = head1;
            this.head2 = head2;
            this.head3 = head3;
            this.head4 = head4;
            this.head5 = head5;
            this.head6 = head6;
            this.head7 = head7;
            this.head8 = head8;
            this.head9 = head9;
        }
    }

   public void checkEvent(){

       DateTimeFormatter s = DateTimeFormat.forPattern("dd/MM/yyyy");
       //DateTimeFormatter innerFormater = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm a");
       for(int i = 0; i < classItems.size(); i++){
           final ClassTimeItem c = classItems.get(i);
           DateTime sDate = new DateTime(startDate.getTime()), eDate = new DateTime(endDate.getTime());
           DateTime cStartDate = new DateTime(c.getClassResponse().getStartDate()), cEndDate = new DateTime(c.getClassResponse().getEndDate());

           if((cStartDate.isBefore(sDate) && cEndDate.isAfter(eDate)) || (cStartDate.isBefore(sDate) && cEndDate.isAfter(sDate) && cEndDate.isBefore(eDate))
           || (cStartDate.isAfter(sDate) && cStartDate.isBefore(eDate) && cEndDate.isAfter(eDate)) || (cStartDate.isAfter(sDate) && cEndDate.isBefore(eDate))) {
               if (sDate.isBefore(c.getStartDate()))
                   sDate = c.getStartDate();
               if (eDate.isAfter(c.getEndDate()))
                   eDate = c.getEndDate();

               //View v = tableB.findViewWithTag(s.print(c.getStartDate()));
               while (!s.parseDateTime(s.print(sDate)).isAfter(eDate)) {

                   View v = tableB.findViewWithTag(s.print(sDate));
                   if (v != null) {

                       String amPm = c.getClassResponse().getStartTime().split(" ")[1];
                       String startHour = c.getClassResponse().getStartTime().split(":")[0];
                       int startMinute = Integer.parseInt(c.getClassResponse().getStartTime().split(":")[1].split(" ")[0]);
                       String endAmPm = c.getClassResponse().getEndTime().split(" ")[1];
                       String endHour = c.getClassResponse().getEndTime().split(":")[0];
                       int endMinute = Integer.parseInt(c.getClassResponse().getEndTime().split(":")[1].split(" ")[0]);
                       int timeIndex = amPm.equalsIgnoreCase("am") && startHour.equals("12") ? -1 :
                               amPm.equalsIgnoreCase("am") ? Integer.parseInt(startHour) - 1 : Integer.parseInt(startHour) + 11;
                       do {
                           timeIndex++;
                           final TextView textView = tableD.findViewWithTag(s.print(sDate) + " " + timeString[timeIndex]);
                           if (textView != null) {
                               int percent = timeString[timeIndex].split(":")[0].equals(startHour)
                                       && timeString[timeIndex].split(" ")[1].equals(amPm)?((startMinute * 100)/60) :
                                       timeString[timeIndex].split(":")[0].equals(endHour) && timeString[timeIndex].split(" ")[1].equals(endAmPm)
                                               ? ((endMinute * 100)/60) : 100;
                               int direction = timeString[timeIndex].split(":")[0].equals(startHour)
                                       && timeString[timeIndex].split(" ")[1].equals(amPm)? TextViewBackground.UPWARD :
                               timeString[timeIndex].split(":")[0].equals(endHour) && timeString[timeIndex].split(" ")[1].equals(endAmPm)
                                       ? TextViewBackground.DOWNWARD : TextViewBackground.NONE;


                               int color = setDataCellBackgroundColor != 0 ? setDataCellBackgroundColor : R.color.light_pink;
                               TextViewBackground tg = sDate.equals(today.getTime()) ? new TextViewBackground(context, color , direction, percent, textView.getHeight(), textView.getWidth()).setTodayColor(setTodayColumnColor) : new TextViewBackground(context, color , direction, percent, textView.getHeight(), textView.getWidth());
                               if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                                   textView.setBackground(tg);
                               else
                                   textView.setBackgroundDrawable(tg);


                               if(!((direction == 1 && percent > 60) || (direction == 0 && percent < 40))) {
                                   textView.setText(c.getHeader() + ", " + c.getSubHeader());

                                   textView.setOnLongClickListener(new View.OnLongClickListener() {
                                       @Override
                                       public boolean onLongClick(View view) {
                                           AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                           builder.setTitle("Class options");
                                           builder.setItems(new CharSequence[]{c.getOptions()[0], c.getOptions()[1], c.getOptions()[2]},
                                                   new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialogInterface, int which) {
                                                           switch (which) {
                                                               case 0:

                                                                   break;
                                                               case 1:

                                                                   break;
                                                               case 2:
                                                                   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                                                   Date sDate = new Date(c.getClassResponse().getStartDate());
                                                                   Date eDate = new Date(c.getClassResponse().getEndDate());
                                                                   Date d = new Date(c.getClassResponse().getDate());
                                                                   Class cl = new Class(c.getClassResponse().getName(), c.getClassResponse().getInstitutionId(), String.valueOf(c.getClassResponse().getStandard()), format.format(d), c.getClassResponse().getSubject(), c.getClassResponse().getStartTime(), c.getClassResponse().getEndTime(), format.format(sDate), format.format(eDate));
                                                                   FragmentManager fm = ((Activity) context).getFragmentManager();
                                                                   AddClassFragment ac = AddClassFragment.newInstance(cl, false);
                                                                   ac.show(fm, "");
                                                                   break;
                                                           }
                                                       }
                                                   });
                                           builder.create().show();
                                           return true;
                                       }
                                   });
                               }
                               textView.invalidate();
                           }
                       } while (timeIndex < timeString.length - 1 && !String.valueOf(endHour + ":00 " + endAmPm).equalsIgnoreCase(timeString[timeIndex]));

                   }
                   sDate = sDate.plusDays(1);
               }
           }
       }
   }

}