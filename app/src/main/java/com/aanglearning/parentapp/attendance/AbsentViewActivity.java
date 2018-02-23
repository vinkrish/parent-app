package com.aanglearning.parentapp.attendance;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aanglearning.parentapp.BaseActivity;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.AttendanceDao;
import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.util.DateUtil;
import com.aanglearning.parentapp.util.DividerItemDecoration;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AbsentViewActivity extends BaseActivity implements AttendanceView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.daily_attendance_layout) LinearLayout dailyAttendanceLayout;
    @BindView(R.id.session_attendance_layout) LinearLayout sessionAttendanceLayout;
    @BindView(R.id.period_attendance_layout) LinearLayout periodAttendanceLayout;
    @BindView(R.id.daily_attendance_view) RecyclerView dailyAttendanceView;
    @BindView(R.id.session_attendance_view) RecyclerView sessionAttendanceView;
    @BindView(R.id.no_attendance) LinearLayout noAttendance;

    private AttendancePresenter presenter;
    private ChildInfo childInfo;

    private DailyAttendanceAdapter dailyAttendanceAdapter;
    private SessionAttendanceAdapter sessionAttendanceAdapter;

    LinkedHashMap<String, List<Attendance>> attendanceMap = new LinkedHashMap<>();
    public int noOfPeriods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_view);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        //getSupportActionBar().setTitle(childInfo.getName()+ " - Attendance");

        presenter = new AttendancePresenterImpl(this, new AttendanceInteractorImpl());

        dailyAttendanceView.setLayoutManager(new LinearLayoutManager(this));
        dailyAttendanceView.setItemAnimator(new DefaultItemAnimator());
        dailyAttendanceView.addItemDecoration(new DividerItemDecoration(this));

        dailyAttendanceAdapter = new DailyAttendanceAdapter(new ArrayList<Attendance>(0));
        dailyAttendanceView.setAdapter(dailyAttendanceAdapter);

        sessionAttendanceView.setLayoutManager(new LinearLayoutManager(this));
        sessionAttendanceView.setItemAnimator(new DefaultItemAnimator());
        sessionAttendanceView.addItemDecoration(new DividerItemDecoration(this));

        sessionAttendanceAdapter = new SessionAttendanceAdapter(new LinkedHashMap<String, List<Attendance>>(0));
        sessionAttendanceView.setAdapter(sessionAttendanceAdapter);

        setNavigationItem(1);

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getStudentAbsentDays(childInfo.getStudentId());
        } else {
            showOfflineData();
        }
    }

    private void showOfflineData() {
        List<Attendance> attendanceList = AttendanceDao.getAttendance(childInfo.getStudentId());
        if(attendanceList.size() == 0) {
            noAttendance.setVisibility(View.VISIBLE);
        } else {
            noAttendance.setVisibility(View.INVISIBLE);
            showAbsentAttendance(attendanceList);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgess() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
        showOfflineData();
    }

    @Override
    public void showAttendance(List<Attendance> attendanceList) {
        if(attendanceList.size() == 0) {
            noAttendance.setVisibility(View.VISIBLE);
        } else {
            noAttendance.setVisibility(View.INVISIBLE);
            showAbsentAttendance(attendanceList);
            backupAttendance(attendanceList);
        }
    }

    private void showAbsentAttendance(List<Attendance> attendanceList) {
        String attendanceType = "";
        for(Attendance att: attendanceList) {
            switch (att.getType()) {
                case "Daily":
                    attendanceType = "Daily";
                    dailyAttendanceLayout.setVisibility(View.VISIBLE);
                    break;
                case "Session":
                    attendanceType = "Session";
                    if(!attendanceMap.containsKey(att.getDateAttendance())) {
                        List<Attendance> attList = new ArrayList<>();
                        for(Attendance at: attendanceList) {
                            if(at.getDateAttendance().equals(att.getDateAttendance())) {
                                attList.add(at);
                            }
                        }
                        attendanceMap.put(att.getDateAttendance(), attList);
                    }
                    break;
                case "Period":
                    attendanceType = "Period";
                    if(!attendanceMap.containsKey(att.getDateAttendance())) {
                        List<Attendance> attList = new ArrayList<>();
                        for(Attendance at: attendanceList) {
                            if(at.getDateAttendance().equals(att.getDateAttendance())) {
                                attList.add(at);
                                if(at.getSession() > noOfPeriods) noOfPeriods = at.getSession();
                            }
                        }
                        attendanceMap.put(att.getDateAttendance(), attList);
                    }
                    break;
                default:
                    break;
            }
        }
        if(attendanceType.equals("Daily")) {
            dailyAttendanceAdapter.setDataSet(attendanceList);
        } else if(attendanceType.equals("Session")) {
            sessionAttendanceLayout.setVisibility(View.VISIBLE);
            sessionAttendanceAdapter.setDataSet(attendanceMap);
        } else if(attendanceType.equals("Period")) {
            periodAttendanceLayout.setVisibility(View.VISIBLE);
            periodAttendanceLayout.removeAllViews();
            periodAttendanceLayout.addView(new TableMainLayout(this));
        }
    }

    private void backupAttendance(final List<Attendance> attendanceList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AttendanceDao.delete(childInfo.getSectionId());
                AttendanceDao.insert(attendanceList);
            }
        }).start();
    }

    public class TableMainLayout extends RelativeLayout {
        TableLayout tableA;
        TableLayout tableB;
        TableLayout tableC;
        TableLayout tableD;

        HorizontalScrollView horizontalScrollViewB;
        HorizontalScrollView horizontalScrollViewD;

        ScrollView scrollViewC;
        ScrollView scrollViewD;

        Context context;

        int headerCellsWidth[] = new int[noOfPeriods+1];

        public TableMainLayout(Context context) {
            super(context);

            this.context = context;

            // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
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
            //this.setBackgroundColor(Color.RED);

            // add some table rows
            this.addTableRowToTableA();
            this.addTableRowToTableB();

            this.resizeHeaderHeight();

            this.getTableRowHeaderCellWidth();

            this.generateTableC_AndTable_D();

            this.resizeBodyTableRowHeight();
        }

        private void initComponents(){
            this.tableA = new TableLayout(this.context);
            this.tableB = new TableLayout(this.context);
            this.tableC = new TableLayout(this.context);
            this.tableD = new TableLayout(this.context);

            this.horizontalScrollViewB = new TableMainLayout.MyHorizontalScrollView(this.context);
            this.horizontalScrollViewD = new TableMainLayout.MyHorizontalScrollView(this.context);

            this.scrollViewC = new TableMainLayout.MyScrollView(this.context);
            this.scrollViewD = new TableMainLayout.MyScrollView(this.context);

            this.tableA.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            this.horizontalScrollViewB.setBackgroundColor(Color.WHITE);
        }

        private void setComponentsId(){
            this.tableA.setId(R.id.tableA_id);
            this.horizontalScrollViewB.setId(R.id.horizontalScrollViewB_id);
            this.scrollViewC.setId(R.id.scrollViewC_id);
            this.scrollViewD.setId(R.id.scrollViewD_id);
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
            LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

            LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

            LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
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

        TableRow componentATableRow(){
            TableRow componentATableRow = new TableRow(this.context);
            TextView textView = this.headerTextView("Dates / Periods");
            textView.setPadding(32, 16, 32, 16);
            textView.setTextSize(16);
            textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            componentATableRow.addView(textView);
            return componentATableRow;
        }

        TableRow componentBTableRow(){
            TableRow componentBTableRow = new TableRow(this.context);

            TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
            params.setMargins(2, 0, 0, 0);

            for(int x=1; x<=noOfPeriods; x++){
                TextView textView = this.headerTextView(x + "");
                textView.setPadding(64, 16, 64, 16);
                textView.setTextSize(18);
                textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
                textView.setLayoutParams(params);
                componentBTableRow.addView(textView);
            }
            return componentBTableRow;
        }

        // generate table row of table C and table D
        private void generateTableC_AndTable_D(){
            for (Map.Entry<String, List<Attendance>> entry : attendanceMap.entrySet()) {
                TableRow tableRowForTableC = this.tableRowForTableC(DateUtil.getDisplayFormattedDate(entry.getKey()));
                TableRow taleRowForTableD = this.taleRowForTableD(entry.getValue());

                tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                this.tableC.addView(tableRowForTableC);
                this.tableD.addView(taleRowForTableD);
            }
        }

        TableRow tableRowForTableC(String date){
            TableRow.LayoutParams params = new TableRow.LayoutParams( this.headerCellsWidth[0],LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);

            TableRow tableRowForTableC = new TableRow(this.context);
            TextView textView = this.bodyTextView(date);
            textView.setPadding(0, 32, 0, 32);
            textView.setTextSize(16);
            textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            tableRowForTableC.addView(textView,params);

            return tableRowForTableC;
        }

        TableRow taleRowForTableD(List<Attendance> attendances){
            TableRow taleRowForTableD = new TableRow(this.context);

            int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();

            SparseArray<String> sparseArray = new SparseArray<>();
            for(Attendance attn: attendances){
                sparseArray.put(attn.getSession(), attn.getTypeOfLeave().equals("Absent")?"A":"");
            }

            for(int x=1 ; x<=loopCount; x++){
                TableRow.LayoutParams params = new TableRow.LayoutParams( headerCellsWidth[x],LayoutParams.MATCH_PARENT);
                params.setMargins(2, 2, 0, 0);

                TextView textViewB = this.bodyTextView(sparseArray.get(x)!=null?sparseArray.get(x):"");
                textViewB.setPadding(0, 32, 0, 32);
                textViewB.setTextSize(16);
                textViewB.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
                taleRowForTableD.addView(textViewB, params);
            }
            return taleRowForTableD;

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
            //   headerTextView.setBackgroundColor(Color.WHITE);
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

            this.matchLayoutHeight(tableRow, finalHeight);
        }

        void getTableRowHeaderCellWidth(){
            int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
            int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();;

            for(int x=0; x<(tableAChildCount+tableBChildCount); x++){
                if(x==0){
                    this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x));
                }else{
                    this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x-1));
                }
            }
        }

        // resize body table row height
        void resizeBodyTableRowHeight(){
            int tableC_ChildCount = this.tableC.getChildCount();

            for(int x=0; x<tableC_ChildCount; x++){
                TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
                TableRow productInfoTableRow = (TableRow)  this.tableD.getChildAt(x);

                int rowAHeight = this.viewHeight(productNameHeaderTableRow);
                int rowBHeight = this.viewHeight(productInfoTableRow);

                TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
                int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

                this.matchLayoutHeight(tableRow, finalHeight);
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
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            return view.getMeasuredHeight();
        }

        // read a view's width
        private int viewWidth(View view) {
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            return view.getMeasuredWidth();
        }

        // horizontal scroll view custom class
        class MyHorizontalScrollView extends HorizontalScrollView{
            public MyHorizontalScrollView(Context context) {
                super(context);
            }
            @Override
            protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                String tag = (String) this.getTag();
                if(tag.equalsIgnoreCase("horizontal scroll view b")){
                    horizontalScrollViewD.scrollTo(l, 0);
                }else{
                    horizontalScrollViewB.scrollTo(l, 0);
                }
            }
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}
