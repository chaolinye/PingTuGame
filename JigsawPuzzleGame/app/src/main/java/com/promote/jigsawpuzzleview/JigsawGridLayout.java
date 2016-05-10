package com.promote.jigsawpuzzleview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.promote.esmond.FuctionMainActivity;
import com.promote.esmond.RankActivity;
import com.promote.jigsawpuzzlegame.MainActivity;
import com.promote.jigsawpuzzlegame.R;
import com.promote.model.IrregularImageView;
import com.promote.utility.DensityUtil;
import com.promote.utility.ImagePiece;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 格子布局
 */
public class JigsawGridLayout extends RelativeLayout {

    private static String gridLayoutTag = "tag_GridLayout";
    /**
     * TimerTask列表
     */
    public Map<Integer, BlurTask> taskLists = new HashMap<>();

    /* 布局中子元素的类型 */
    private int itemShape;
    /* 设置Item的数量n*n；默认为3 */
    public static int mColumn = 3;
    /* item的数量 */
    private int length = 3 * 3;

    /* 布局的宽度 */
    private int mWidth;
    /* 布局的高度 */
    private int mHeight;
    /* 布局的padding */
    private int mPadding = 5;

    /* Item的宽度 */
    private int mItemWidth;
    /* Item的高度 */
    private int mItemHeight;
    /* 子布局控件的宽高比*/
    private double relative;
    /* Item横向与纵向的边距 */
    private int mMargin = 0;

    private View mDrapView;

    private boolean once;
    /**
     * 暂时没有用，是后面的切换动画部分，视情况而定
     * 存放切完以后的图片bean
     */
    private List<ImagePiece> mItemBitmaps;

    private Context mContext;


    public JigsawGridLayout(Context context) {
        this(context, null);
    }

    public JigsawGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    int id;
    SharedPreferences preferences;//存贮用户id
    public JigsawGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        preferences = mContext.getSharedPreferences("id", mContext.MODE_WORLD_READABLE);
       id = preferences.getInt("id", -2);

        // 将dp值转化为px
        mMargin = DensityUtil.dip2px(mContext, mMargin);
        mPadding = DensityUtil.dip2px(mContext, mPadding);
        // 设置布局参数
        setVerticalGravity(Gravity.CENTER_VERTICAL);
        setGravity(Gravity.CENTER);
        setPadding(mPadding, mPadding, mPadding, mPadding);
        setTag(gridLayoutTag);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 初始化布局
     */
    public void initView() {
        // 清空布局
        this.removeAllViews();

        itemShape = JigsawPuzzleView.itemShape;
        Log.e("COLUMN_TAG","current columns = " + mColumn);
        // 计算Item的高度和宽度
        mItemHeight = (mHeight - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        if(itemShape == 1)
            mItemWidth = mItemHeight = Math.min(mItemHeight,mItemWidth);
        length = mColumn * mColumn;

        for (int i = 0; i < length; i++) {
            //生成item
            LinearLayout newImageLin = new LinearLayout(mContext);
            ;
            newImageLin.setOrientation(LinearLayout.VERTICAL);
            newImageLin.setBackgroundResource(R.drawable.puzzle_item_shape);

            //设置布局item的id
            newImageLin.setId(i + 1);
            //设置拖拽事件监听器
            newImageLin.setOnDragListener(mOnDragListener);

            //设置生成布局item的相关参数
            RelativeLayout.LayoutParams lp = new LayoutParams(mItemWidth, mItemHeight);
            // 设置横向边距,不是最后一列
            if ((i + 1) % mColumn != 0) {
                lp.rightMargin = mMargin;
            }
            // 如果不是第一列
            if (i % mColumn != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, i);
            }
            // 如果不是第一行，设置纵向边距，非最后一行
            if ((i + 1) > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, i + 1 - mColumn);
            }
            //添加item
            addView(newImageLin, lp);
        }
    }

    /**
     * 设置布局的宽度
     *
     * @param width
     */
    public void setLayoutWidth(int width) {
        mWidth = width;
    }

    /**
     * 设置布局的高度
     *
     * @param height
     */
    public void setLayoutHeight(int height) {
        mHeight = height;
    }

    /**
     * 设置列数
     */
    public void setColumn(int column) {
        mColumn = column;
    }

    /**
     * 设置高宽比
     *
     * @param relative
     */
    public void setRelative(double relative) {
        this.relative = relative;
    }

    /**
     * 获取子控件布局的宽度
     *
     * @return
     */
    public int getItemWidth() {
        return mItemWidth;
    }

    /**
     * 获取子控件布局的高度
     *
     * @return
     */
    public int getItemHeight() {
        return mItemHeight;
    }

    /**
     * 定义触屏事件监听器
     */
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            mDrapView = v;
            //v = ((IrregularImageView) v).getIrregularView(JigsawPuzzleView.mItemBitmaps,3);
            // 设置拖拽阴影
            IrDragShadowBuilder shadowBuilder = new IrDragShadowBuilder(
                    mDrapView);
            // 开始拖拽view控件
            v.startDrag(null, shadowBuilder, v, 0);
            return true;
        }
    };


    /**
     * 拖拽事件监听器
     */
    private View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int enterShape = R.drawable.item_droptarget_shape;
            int normalShape = R.drawable.puzzle_item_shape;

            switch (event.getAction()) {
                // 开始拖拽控件，获得一个拖动阴影
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.e("TAG","ACTION_DRAG_STARTED");
                    // 记忆模式
                    if (MainActivity.flag == MainActivity.FLAG_BLUR) {
                        View view1 = (View) event.getLocalState();
                        if (taskLists.containsKey(view1.getId())) {
                            taskLists.get(view1.getId()).cancel();
                            taskLists.remove(view1.getId());
                            RelativeLayout r1 = (RelativeLayout) view1;
                            ImageView iv1 = (ImageView) r1.getChildAt(0);
                            iv1.setAlpha(1.0f);
                        }
                    }
                    break;
                // 拖动阴影进入了某View的屏幕边界内时
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.e("TAG","ACTION_DRAG_ENTERED");
                    // 如果当前布局内已放有子控件，则不可以再次加入
                    if (((ViewGroup) v).getChildAt(0) == null)
                        v.setBackgroundResource(enterShape);
                    break;

                // 拖动阴影移到了View的边界之外时
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.e("TAG","ACTION_DRAG_EXITED");
                    v.setBackgroundResource(normalShape);
                    break;

                // 在View对象上面释放拖动阴影时
                case DragEvent.ACTION_DROP:
                    Log.e("TAG","ACTION_DROP");
                    // 获取拖拽的View
                    final  ViewGroup view = (ViewGroup) event.getLocalState();
                    if (itemShape == 0) {
                        addRectChildView((ViewGroup) v, view);
                    } else if (itemShape == 1) {
                        addIrregularView((ViewGroup) v, view);
                    }

                    /* 判断拼图完成 */
                    checkSuccess();
                    break;

                // 系统停止拖动操作时
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundResource(normalShape);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    public boolean addRectChildView(ViewGroup curLayout, final ViewGroup dragView) {

        ViewGroup owner = (ViewGroup) dragView.getParent();

        /* 设置子view的参数 */
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        // 设置布局参数
        TextView textView = (TextView) dragView.getChildAt(1);
        String oldTextTag = (String) textView.getTag();
        String newTextTag = "1" + oldTextTag.substring(1);
        textView.setTag(newTextTag);
        textView.setText(newTextTag);

        /* 从其父组件中移除，并添加到当前组件 */
        owner.removeView(dragView);
        curLayout.addView(dragView, lp);

        timeBlur(dragView);

        return true;
    }

    private void timeBlur(final View dragView){
        //记忆模式
        if (MainActivity.flag == MainActivity.FLAG_BLUR) {
            RelativeLayout r = (RelativeLayout) dragView;
            final ImageView iv = (ImageView) r.getChildAt(0);

            Log.e("ID_TAG",dragView.getId() + "");

            if (iv.getAlpha() == 1.0f)
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        BlurTask b = new BlurTask(iv);
                        taskLists.put(dragView.getId(), b);
                        new Timer().schedule(b, 0, mColumn*500/3);
                    }
                }.start();
            else if (iv.getAlpha() <= 0.005f) {
                iv.setAlpha(1.0f);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        BlurTask b = new BlurTask(iv);
                        taskLists.put(dragView.getId(), b);
                        new Timer().schedule(b, 0, mColumn*500/3);
                    }
                }.start();
            }
        }
    }

    /**
     * 添加不规则的子view
     *
     * @param curLayout 当前拖动view下方的Layout
     * @param dragView  当前正在拖动的View
     * @return
     */
    private boolean addIrregularView(ViewGroup curLayout, View dragView) {

        /* 拖拽的view即将加入的父容器的Id */
        int curId = curLayout.getId();
        List<View> rovViewList = null;

        /* 判断dragview原本所在的布局 */
        int layoutTag = Integer.parseInt(dragView.getTag().toString());
        IrregularImageView curView;
        /* 判断拖拽的图形是什么类型 */
        if (layoutTag == 0) {
            curView = (IrregularImageView) ((ViewGroup) dragView).getChildAt(0);
            dragView.setVisibility(INVISIBLE);
        } else {
            curView = (IrregularImageView) dragView;
            setIrViewVisible(curView,0);
        }

        int[][] da = IrregularImageView.getStatus(curView.getStatusIndex());
        int curX = curView.getIndexX();
        int curY = curView.getIndexY();
        int columns = IrregularImageView.statusItemColumns;

        if (isEmptyToAdd(curId, da, curX, curY, columns)) {
            /* 设置子view的参数 */
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            /* 即将要加入的父容器的Id */
            int addId;
            IrregularImageView cView;

            if (layoutTag == 0) {
                /* 获取要添加的子view的数量 */
                int nums = ((ViewGroup) dragView).getChildCount();
                for (int i = 0; i < nums; i++) {
                    cView = (IrregularImageView) ((ViewGroup) dragView).getChildAt(0);
                    int x = cView.getIndexX();
                    int y = cView.getIndexY();
                    /* 计算即将要加入的父容器的Id */
                    addId = curId;
                    addId += y - curY;
                    addId += mColumn * (x - curX);
                    Log.e("DIFFERENT_TAG", "x = " + x + " y = " + y + " AddId = " + addId);

                    /* 从原父view中移除，并加入新的父view */
                    ((ViewGroup) dragView).removeView(cView);
                    ViewGroup curPView = (ViewGroup) this.findViewById(addId);
                    curPView.addView(cView, lp);
                    /* 设置参数及监听事件 */
                    ((View) cView).setVisibility(VISIBLE);
                    cView.setTag("1");
                    cView.setOnTouchListener(mOnTouchListener);
                    Log.e("ADDVIEW_TAG","add a view from scrolllayout");
                }
                /* 从其父组件中移除 */
                ViewGroup owner = (ViewGroup) dragView.getParent();
                owner.removeView(dragView);
            } else {
                rovViewList = removeIrView(curView);
                da = IrregularImageView.getStatus(curView.getStatusIndex());
                int k=0;
                for (int i = 0; i < columns; i++) {
                    for (int j = 0; j < columns; j++) {
                        if (da[i][j] == 1) {
                            /* 计算即将要加入的父容器的Id */
                            addId = curId;
                            addId += j - curY;
                            addId += mColumn * (i - curX);
                            Log.e("SAME_TAG", "i = " + i + " j = " + j + " AddId = " + addId);

                            /* 加入新的父view */
                            ViewGroup curPView = (ViewGroup) this.findViewById(addId);
                            cView = (IrregularImageView) rovViewList.get(k++);
                            curPView.addView(cView, lp);
                            /* 设置参数及监听事件 */
                            cView.setVisibility(VISIBLE);
                            cView.setTag("1");
                            cView.setOnTouchListener(mOnTouchListener);
                        }
                    }
                }
            }
            Log.e("EMPTY_TAG", " Empty to add!");
            Toast.makeText(mContext, "Empty to add!",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else {
            /* 放置图片不成功则显示原图 */
            if (layoutTag == 0) {
                dragView.setVisibility(VISIBLE);
            } else{
                setIrViewVisible(curView,1);
            }
            Log.e("EMPTY_TAG", "No empty enough to add!");
            Toast.makeText(mContext, "No empty enough to add!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 移除不规则图形的图片
     * @param dragView
     * @return
     */
    public static List<View> removeIrView(IrregularImageView dragView){

        View pPView = (View)dragView.getParent().getParent();
        List<View> removeViewList = new ArrayList<View>();

        int curId = ((ViewGroup)dragView.getParent()).getId();

        /* 获取形状数组，当前所在数组位置的坐标 */
        int[][] da = IrregularImageView.getStatus(dragView.getStatusIndex());
        int curX = dragView.getIndexX();
        int curY = dragView.getIndexY();
        int columns = IrregularImageView.statusItemColumns;

        int rovId;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                if (da[i][j] == 1) {
                    /* 计算当前所在的父容器的Id */
                    rovId = curId;
                    rovId += j - curY;
                    rovId += mColumn * (i - curX);

                    /* 在原父view中移除 */
                    ViewGroup prePView = (ViewGroup) pPView.findViewById(rovId);
                    View cView  =prePView.getChildAt(0);
                    prePView.removeView(cView);
                    removeViewList.add(cView);
                }
            }
        }
        return removeViewList;
    }

    /**
     * 设置背景，暂时没实现
     * @param curView
     * @param dragView
     * @param isVisible
     * @return
     */
    public boolean setIrViewGround(View curView,IrregularImageView dragView,int isVisible){

        int curId = ((ViewGroup)dragView.getParent()).getId();

        /* 获取形状数组，当前所在数组位置的坐标 */
        int[][] da = IrregularImageView.getStatus(dragView.getStatusIndex());
        int curX = dragView.getIndexX();
        int curY = dragView.getIndexY();
        int columns = IrregularImageView.statusItemColumns;

        int visibleId;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                if (da[i][j] == 1) {
                    /* 计算当前所在的父容器的Id */
                    visibleId = curId;
                    visibleId += j - curY;
                    visibleId += mColumn * (i - curX);

                    /* 在原父view中隐藏或显示 */
                    ViewGroup prePView = (ViewGroup) this.findViewById(visibleId);
                    View cView  =prePView.getChildAt(0);
                    if(isVisible == 0){
                        cView.setVisibility(INVISIBLE);
                    } else {
                        cView.setVisibility(VISIBLE);
                    }

                }
            }
        }
        return true;
    }

    /**
     * 设置不规则图片显示
     * @param dragView
     * @param isVisible
     * @return
     */
    public boolean setIrViewVisible(IrregularImageView dragView,int isVisible){

        int curId = ((ViewGroup)dragView.getParent()).getId();

        /* 获取形状数组，当前所在数组位置的坐标 */
        int[][] da = IrregularImageView.getStatus(dragView.getStatusIndex());
        int curX = dragView.getIndexX();
        int curY = dragView.getIndexY();
        int columns = IrregularImageView.statusItemColumns;

        int visibleId;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                if (da[i][j] == 1) {
                    /* 计算当前所在的父容器的Id */
                    visibleId = curId;
                    visibleId += j - curY;
                    visibleId += mColumn * (i - curX);

                    /* 在原父view中隐藏或显示 */
                    ViewGroup prePView = (ViewGroup) this.findViewById(visibleId);
                    View cView  =prePView.getChildAt(0);
                    if(isVisible == 0){
                        cView.setVisibility(INVISIBLE);
                    } else {
                        cView.setVisibility(VISIBLE);
                    }

                }
            }
        }
        return true;
    }

    /**
     * 判断当前位置是否可以放入当前拖拽的不规则图形
     *
     * @param locateViewId 当前拖动view下方的Layout的Id
     * @param status       当前拖动的不规则view所属形状
     * @param indexX       子view所属形状数组中的横坐标
     * @param indexY       子view所属形状数组中的纵坐标
     * @param columns      用于表示二维形状数组的大小（columns * columns）
     * @return 可以放置不规则view返回true，否则false
     */
    private boolean isEmptyToAdd(int locateViewId, int[][] status,
                                 int indexX, int indexY, int columns) {
        int emptyId;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                if (status[i][j] == 1) {
                    /* 计算当前所在的父容器的Id */
                    emptyId = locateViewId;
                    emptyId += j - indexY;
                    emptyId += mColumn * (i - indexX);

                    /* 父View索引超出范围和父view内已有子view时返回false */
                    if(emptyId <= 0 || emptyId > length)
                        return false;
                    /* 不能从最左边跳到上一行 */
                    if(j>0 && status[i][j-1] == 1 && emptyId % mColumn == 1)
                        return false;
                    /* 不能从最左边跳到下一行 */
                    if(j < columns -1 && status[i][j+1] == 1 && emptyId % mColumn == 0)
                        return false;

                    /* 判断当前控件内是否已有子控件 */
                    ViewGroup prePView = (ViewGroup) this.findViewById(emptyId);
                    View cView  =prePView.getChildAt(0);
                    if(cView != null){
                        if(cView.getVisibility() != INVISIBLE){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 定义拖拽事件阴影生成器
     */
    private class IrDragShadowBuilder extends View.DragShadowBuilder {

        private final WeakReference<View> mView;

        /**
         * 创建一个默认与传入的参数View大小相同的拖动阴影，
         * 其中触摸点位于拖动阴影的中心。
         *
         * @param view
         */
        public IrDragShadowBuilder(View view) {
            super(view);
            // 创建对当前拖拽view的弱引用
            mView = new WeakReference<View>(view);
        }

        /**
         * 定义回调方法，用于在Canvas上绘制拖动阴影，
         * Canvas由系统根据onProvideShadowMetrics()传入的尺寸参数创建。
         *
         * @param canvas
         */
        @Override
        public void onDrawShadow(Canvas canvas) {
            // 阴影大小转换矩阵，及转换比例
            Matrix matrix = new Matrix();
            float scaleWidth = mItemWidth / (float) JigsawPuzzleView.mItemBitmaps.get(0).getBitmap().getWidth();
            float scaleHeight = mItemHeight / (float) JigsawPuzzleView.mItemBitmaps.get(0).getBitmap().getHeight();
            matrix.postScale(scaleWidth, scaleHeight);

            /* 获取当前图片所属形状数组，所在数组的坐标，及其图片索引 */
            final View view = mView.get();
            int index = ((IrregularImageView) view).getStatusIndex();
            int indexX = ((IrregularImageView) view).getIndexX();
            int indexY = ((IrregularImageView) view).getIndexY();
            int[][] status = IrregularImageView.getStatus(index);
            int curPieceIndex = ((IrregularImageView) view).getImagePiece().getIndex();

            /* 生成当前图片所属不规则图形的阴影 */
            Bitmap bm;
            int nums = IrregularImageView.statusItemColumns;
            for (int i = 0; i < nums; i++) {
                for (int j = 0; j < nums; j++) {
                    if (status[i][j] == 1) {
                        /* 计算不规则图形中的各个图片索引，及获取其图片 */
                        int addIndex = curPieceIndex;
                        addIndex += j - indexY;
                        addIndex += mColumn * (i - indexX);
                        bm = JigsawPuzzleView.mItemBitmaps.get(addIndex).getBitmap();
                        /* 将图片画到画布上 */
                        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                                bm.getHeight(), matrix, true);
                        canvas.drawBitmap(resizedBitmap,
                                j * mItemWidth, i * mItemHeight, null);
                    }
                }
            }
        }

        /**
         * 定义回调方法，用于把拖动阴影的大小和触摸点位置返回给系统
         *
         * @param shadowSize
         * @param shadowTouchPoint
         */
        @Override
        public void onProvideShadowMetrics(Point shadowSize,
                                           Point shadowTouchPoint) {
            final View view = mView.get();
            if (view != null) {
                // 阴影的宽和高
                shadowSize.set(mItemWidth * 4, mItemHeight * 4);
                // 用户手指在拖动过程中所触及的点在拖动阴影中的相对位置
                shadowTouchPoint.set(shadowSize.x / 4, shadowSize.y / 4);
            } else {
                Log.e(View.VIEW_LOG_TAG, "Asked for drag but no view");
            }
        }
    }



    /**
     * 判断游戏是否成功
     */
    boolean checkSuccess() {
        boolean isSuccess = true;
        for (int i = 1; i <= mColumn * mColumn; i++) {
            if (!checkItem(i)) {
                isSuccess = false;
                break;
            }
        }

        Log.e("SUCCESS_TAG", String.valueOf(isSuccess));
        if (isSuccess) {
            Toast.makeText(getContext(), "Congratulations to success!",
                    Toast.LENGTH_LONG).show();
            MainActivity.mTextView.stop();
            Toast.makeText(getContext(), "Time:" +
                            MainActivity.mTextView.getText().toString(),
                    Toast.LENGTH_LONG).show();
            upGrade();
            //nextLevel();
        }
        return isSuccess;
    }

    int getGrade(){
        int grade = 1;
        String a =   MainActivity.mTextView.getText().toString();
        String b[] = a.split(":");
        grade+=60*Integer.parseInt(b[0])+Integer.parseInt(b[1]);

        int m = mColumn*100-grade*5;
        return m;
    }
    void upGrade(){
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        int k =  MainActivity.grade;
        k+=getGrade();
        Log.e("TAG",k+"");
        String url_login = RankActivity.xinlang_url+"grade&" + "id=" + id + "&grade=" + k;
        StringRequest stringRequest = new StringRequest(url_login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String id_str = response.toString();
                        if (id_str.equals("true")){
                            Toast.makeText(mContext, "分数已上传!",
                                    Toast.LENGTH_SHORT).show();
                            //跳转主界面
                            Intent intent = new Intent(mContext
                                    , FuctionMainActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "连接错误!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    /**
     * 判断某一个图片块是否放置正确
     *
     * @param index
     * @return
     */
    boolean checkItem(int index) {
        LinearLayout layoutLin = (LinearLayout) findViewById(index + 0);
        View mView = layoutLin.getChildAt(0);
        if (mView == null || mView.getId() != index + 1000) {
            return false;
        }
        return true;
    }

    /**
     * 提升游戏等级
     */
    public void nextLevel() {
        this.removeAllViews();
        // mAnimLayout = null;
        mColumn++;
        initView();
    }

    class BlurTask extends TimerTask {
        View v;

        BlurTask() {
        }

        BlurTask(View v) {
            this.v = v;
        }

        float alpha = 1.0f;

        public void run() {
            v.post(new Runnable() {
                @Override
                public void run() {
                    v.setAlpha(alpha);
                    if(alpha<=0) {
                        //  v.setAlpha(1.0f);
                        Drawable b = getResources().getDrawable(R.drawable.top);
                        ViewGroup owner = (ViewGroup) v.getParent();
                        ImageView iv = (ImageView) owner.getChildAt(2);
                        iv.setImageDrawable(b);
                        iv.setVisibility(View.VISIBLE);
                    }else{
                        ViewGroup owner = (ViewGroup) v.getParent();
                        ImageView iv = (ImageView) owner.getChildAt(2);
                        iv.setVisibility(View.GONE);
                    }
                }
            });

            if (alpha >= 0.0)
                alpha -= 0.025f;
            else {
                this.cancel();
            }
        }
    }
}
