package com.aanglearning.parentapp.timetable;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import com.aanglearning.parentapp.dao.TimetableDao;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Timetable;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableActivity extends BaseActivity implements TimetableView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.tableLayout) FrameLayout tableLayout;
    @BindView(R.id.noTimetable) LinearLayout noTimetable;

    private TimetablePresenter presenter;
    private ChildInfo childInfo;

    LinkedHashMap<String, List<Timetable>> timetableMap = new LinkedHashMap<>();
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private int noOfPeriods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        if(childInfo.getSectionName().equals("none")) {
            getSupportActionBar().setTitle("[ " + childInfo.getClassName() + " ]");
        } else {
            getSupportActionBar().setTitle("[ " + childInfo.getClassName() + " - " + childInfo.getSectionName() + " ]");
        }

        presenter = new TimetablePresenterImpl(this, new TimetableInteractorImpl());

        setNavigationItem(3);

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getTimetable(childInfo.getSectionId());
        } else {
            showOfflineData();
        }
    }

    private void showOfflineData() {
        List<Timetable> timetableList = TimetableDao.getTimetable(childInfo.getSectionId());
        if(timetableList.size() == 0) {
            noTimetable.setVisibility(View.VISIBLE);
        } else {
            noTimetable.setVisibility(View.INVISIBLE);
            for(String day: days) {
                List<Timetable> timtableList = new ArrayList<>();
                for(Timetable timetable: timetableList) {
                    if(timetable.getDayOfWeek().equals(day)) {
                        timtableList.add(timetable);
                        if(timetable.getPeriodNo() > noOfPeriods) noOfPeriods = timetable.getPeriodNo();
                    }
                }
                timetableMap.put(day, timtableList);
            }
            tableLayout.addView(new TableMainLayout(this));
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
    public void showTimetable(List<Timetable> timetableList) {
        if(timetableList.size() == 0) {
            TimetableDao.delete(childInfo.getSectionId());
            noTimetable.setVisibility(View.VISIBLE);
        } else {
            noTimetable.setVisibility(View.INVISIBLE);
            for(String day: days) {
                List<Timetable> timtableList = new ArrayList<>();
                for(Timetable timetable: timetableList) {
                    if(timetable.getDayOfWeek().equals(day)) {
                        timtableList.add(timetable);
                        if(timetable.getPeriodNo() > noOfPeriods) noOfPeriods = timetable.getPeriodNo();
                    }
                }
                timetableMap.put(day, timtableList);
            }
            tableLayout.addView(new TableMainLayout(this));
            backupTimetable(timetableList);
        }
    }

    private void backupTimetable(final List<Timetable> timetableList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TimetableDao.delete(childInfo.getSectionId());
                TimetableDao.insert(timetableList);
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
            this.horizontalScrollViewB.setHorizontalScrollBarEnabled(false);

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
            TextView textView = this.headerTextView("Timetable");
            textView.setPadding(36, 24, 36, 24);
            textView.setTextSize(18);
            textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            componentATableRow.addView(textView);
            return componentATableRow;
        }

        TableRow componentBTableRow(){
            TableRow componentBTableRow = new TableRow(this.context);

            TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
            params.setMargins(2, 0, 0, 0);

            for(int x=1; x<=noOfPeriods; x++){
                TextView textView = this.headerTextView("Period  -  " + x );
                textView.setPadding(64, 16, 64, 16);
                textView.setTextSize(16);
                textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
                //textView.setLayoutParams(params);
                componentBTableRow.addView(textView, params);
            }
            return componentBTableRow;
        }

        // generate table row of table C and table D
        private void generateTableC_AndTable_D(){
            for (Map.Entry<String, List<Timetable>> entry : timetableMap.entrySet()) {
                TableRow tableRowForTableC = this.tableRowForTableC(entry.getKey());
                TableRow taleRowForTableD = this.taleRowForTableD(entry.getValue());

                tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                this.tableC.addView(tableRowForTableC);
                this.tableD.addView(taleRowForTableD);
            }
        }

        TableRow tableRowForTableC(String day){
            TableRow.LayoutParams params = new TableRow.LayoutParams( this.headerCellsWidth[0],LayoutParams.MATCH_PARENT);
            params.setMargins(0, 2, 0, 0);

            TableRow tableRowForTableC = new TableRow(this.context);
            TextView textView = this.bodyTextView(day);
            textView.setPadding(0, 32, 0, 32);
            textView.setTextSize(16);
            textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            tableRowForTableC.addView(textView,params);

            return tableRowForTableC;
        }

        TableRow taleRowForTableD(List<Timetable> timetables){
            TableRow taleRowForTableD = new TableRow(this.context);

            int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();

            SparseArray<String> subjectArray = new SparseArray<>();
            SparseArray<String> teacherArray = new SparseArray<>();
                for(Timetable timtbl: timetables){
                    if(teacherArray.get(timtbl.getPeriodNo()) != null) {
                        teacherArray.put(timtbl.getPeriodNo(), teacherArray.get(timtbl.getPeriodNo()).split("\\r?\\n")[1] + "/" + timtbl.getTeacherName() + " ");
                    } else {
                        subjectArray.put(timtbl.getPeriodNo(), timtbl.getSubjectName());
                        teacherArray.put(timtbl.getPeriodNo(), timtbl.getTeacherName());
                    }
            }

            for(int x=1 ; x<=loopCount; x++){
                TableRow.LayoutParams params = new TableRow.LayoutParams( headerCellsWidth[x],LayoutParams.MATCH_PARENT);
                params.setMargins(2, 2, 0, 0);

                LinearLayout ll = new LinearLayout(this.context);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setBackgroundColor(Color.WHITE);
                ll.setPadding(8, 30, 8, 32);

                TextView tv1 = this.bodyTextView(subjectArray.get(x)!=null?subjectArray.get(x):"");
                ll.addView(tv1);
                TextView tv2 = this.teacherTextView(teacherArray.get(x)!=null?teacherArray.get(x):"");
                if(!tv2.getText().equals("")){
                    ll.addView(tv2);
                }
                taleRowForTableD.addView(ll, params);
            }
            return taleRowForTableD;

        }

        TextView bodyTextView(String label){
            TextView bodyTextView = new TextView(this.context);
            bodyTextView.setBackgroundColor(Color.WHITE);
            bodyTextView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            bodyTextView.setText(label);
            bodyTextView.setTextSize(14);
            bodyTextView.setGravity(Gravity.CENTER);
            bodyTextView.setPadding(4, 4, 4, 4);
            return bodyTextView;
        }

        TextView teacherTextView(String label){
            TextView teacherTextView = new TextView(this.context);
            teacherTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.default_tv_color));
            teacherTextView.setTextColor(Color.WHITE);
            //teacherTextView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            teacherTextView.setText(label);
            teacherTextView.setTextSize(12);
            teacherTextView.setGravity(Gravity.CENTER);
            teacherTextView.setPadding(4, 4, 4, 4);
            return teacherTextView;
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
