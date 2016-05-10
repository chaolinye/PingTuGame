package com.promote.jigsawpuzzleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.promote.jigsawpuzzlegame.MainActivity;
import com.promote.model.IrregularImageView;
import com.promote.utility.ImagePiece;
import com.promote.utility.ImageUtil;
import com.promote.jigsawpuzzlegame.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 整个游戏界面布局
 */
public class JigsawPuzzleView extends LinearLayout {

    /* 图片的形状，矩形，俄罗斯方块形 */
    public static int itemShape;
    /* 设置Item的数量n*n；默认为3 */
    public static int mColumn = 3;

    /* 格子布局 */
    private JigsawGridLayout gridLayout;
    /* 滚动布局 */
    private JigsawScrollLayout scrollLayout;

    /* 整个布局的宽度 */
    private int mWidth;
    /* 整个布局的高度 */
    private int mHeight;
    /* Actionbar的高度 */
    private int mActionBarHeight;
    /* 格子布局的高度 */
    private int mGridHeight;
    /* 滚动布局的高度 */
    private int mScrollHeight;

    /* 子布局Item用的宽高比 */
    private double relative;
    /* 当前拼图的图片 */
    private Bitmap mBitmap;
    public List<View> itemBitmapView;
    /* 切分好的图片 */
    public static List<ImagePiece> mItemBitmaps;

    private boolean once = false;
    private Context mContext;

    Timer timer = new Timer();

    LinearLayout layoutLin;

    int enterShape = R.drawable.item_droptarget_tip_shape;
    int normalShape = R.drawable.puzzle_item_shape;

    public JigsawPuzzleView(Context context) {
        this(context, null);
    }

    public JigsawPuzzleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JigsawPuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        // 创建布局对象
        gridLayout = new JigsawGridLayout(mContext);
        scrollLayout = new JigsawScrollLayout(mContext);
        addView(gridLayout);
        //addView(puzzleGridLayout);
        addView(scrollLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        if (!once) {
            // 计算活动条的高度及设置参数
            mActionBarHeight = getActionbarHeight(getContext());
            setPadding(0, mActionBarHeight, 0, 0);
            Log.e("ActionBar", "ActionBarHeight = " + mActionBarHeight);

            // 设置格子布局为剩余空间的3/4，滚动布局占用1/4
            mGridHeight = (mHeight - mActionBarHeight) / 4 * 3;
            // 计算滚动布局中Item的高度
            mScrollHeight = mHeight - mActionBarHeight - mGridHeight;
            // 计算图片显示的宽高比
            relative = mWidth / (double) mGridHeight;
            // 初始化布局
            initView();
        }
        once = true;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        //if(isAniming)
        //    return true;
        return false;
    }

    /**
     * 初始化布局
     */
    public void initView() {
        // 默认拼图图片
        if (mBitmap == null)
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.p_a);
        itemBitmapView = new ArrayList<View>();
        // 初始化图片
        initBitmap();

        // 添加格子布局,并为子布局设置参数及初始化
        gridLayout.setLayoutWidth(mWidth);
        gridLayout.setLayoutHeight(mGridHeight);
        gridLayout.setColumn(mColumn);
        // gridLayout.setRelative(relative);
        gridLayout.initView();

        // 添加滚动布局，并为子布局设置参数及初始化
        scrollLayout.setLayoutWidth(mWidth);
        scrollLayout.setLayoutHeight(mScrollHeight);
        scrollLayout.setRelative(relative);
        scrollLayout.initView(itemBitmapView);
    }

    /**
     * 初始化图片，切分图片
     *
     * @return
     */
    private void initBitmap() {
        // 默认列数
        if (mColumn <= 0) {
            mColumn = 3;
        }

        itemBitmapView.clear();
        ImageUtil.itemShape = itemShape;
        // 将图片切成mColumn*mColumn份,并生成图形
        mItemBitmaps = ImageUtil.split(mBitmap, mColumn);

        if(itemShape == 0){
            genRectView();
        } else if(itemShape == 1){
            genIrregularView();
        }

        // 对图片进行随机排序
        if (itemShape == 0) {
            Collections.sort(itemBitmapView, new Comparator<View>() {
                @Override
                public int compare(View lhs, View rhs) {
                    return Math.random() > 0.5 ? 1 : -1;
                }
            });
        }
    }
    /**
     * 生成规则图片
     */
    private void genRectView(){
        for (int i = 0; i < mItemBitmaps.size(); i++) {
            // 设置子控件的布局
            View convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_index_gallery_item, this, false);
            ImageView mImage = (ImageView) convertView
                    .findViewById(R.id.id_index_gallery_item_image);
            TextView mText = (TextView) convertView
                    .findViewById(R.id.id_index_gallery_item_text);
            mImage.setImageBitmap(mItemBitmaps.get(i).bitmap);
            /**
             * 1 现在所在的布局，0是scroll，1是grid
             * 2 记录在图片数组里的索引
             * 3 完成拼图用的真正图片索引
             */
            String textTag = "0_" + i + "_" + (mItemBitmaps.get(i).index + 1);
            mText.setTag(textTag);
            mText.setText(textTag);

            // 设置id用于判断拼图完成
            convertView.setId(1001 + mItemBitmaps.get(i).index);
            convertView.setBackgroundColor(Color.TRANSPARENT);

            itemBitmapView.add(convertView);
        }
    }

    /**
     * 生成不规则图片
     */
    private void genIrregularView(){
        IrregularImageView.mColumn = mColumn;
        for (int i = 0; i < 16; i++) {
            Random random = new Random();
            /* 随机产生图片，仅测试用 */
            int statusIndex = random.nextInt(IrregularImageView.status.length);
            int startIndex = 15;
            IrregularImageView imageItem = new IrregularImageView(mContext);
            imageItem.setStatusIndex(statusIndex);
            imageItem.setPiece(mItemBitmaps.get(i));
            imageItem.setOffsetID(1001);
            View convertView = imageItem.getIrregularView(mItemBitmaps, startIndex);
            if(convertView!=null){
                itemBitmapView.add(convertView);
                Log.e("ADD_IRVIEW_TAG","add irview sucessful");
            } else{
                Log.e("ADD_IRVIEW_TAG","add irview unsucessful");
            }
        }
    }
    /**
     * 获取ActionBar的高度
     *
     * @param context
     * @return
     */
    public static int getActionbarHeight(Context context) {
        int actionBarHeight = 0;
        // 计算活动条的高度
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize,
                tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * 设置列数
     *
     * @param column
     */
    public static void setColumn(int column) {
        mColumn = column;
    }

    /**
     * 设置要拼图的图片
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }


    /**
     * 提示
     */
    public void tip() {
        Random random = new java.util.Random();// 定义随机类
        int result = random.nextInt(mColumn * mColumn);
        int temp = result;
        boolean loop = false;

        //获取tip的位置
        while (true) {
            LinearLayout layoutLin = (LinearLayout) findViewById(result + 1);
            View mView = layoutLin.getChildAt(0);
            if (mView == null || mView.getId() != result + 1000) {
                break;
            } else {
                result++;
            }
            if (result == mColumn * mColumn) {//从头开始
                result = 0;
                loop = true;
            }
            if (loop && result == temp) {
                Toast.makeText(mContext, "You have finished !", Toast.LENGTH_LONG).show();
                return;
            }
        }

        layoutLin = (LinearLayout) findViewById(result + 1);
        View mView = layoutLin.getChildAt(0);

        if (mView == null) {//该位置为空
            boolean isInGrid = false;

            for (int i = 0; i < mColumn * mColumn; i++) {
                LinearLayout layoutLTrue = (LinearLayout) findViewById(i + 1);
                View mViewTrue = layoutLTrue.getChildAt(0);
                if (mViewTrue != null && mViewTrue.getId() == result + 1001) {//找到
                    isInGrid = true;

                    ViewGroup owner = (ViewGroup) mViewTrue.getParent();
                    // 设置参数
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    owner.removeView(mViewTrue);

                    //     ViewGroup mViewowner = (ViewGroup) mView.getParent();

                    layoutLin.addView(mViewTrue, lp);

                    // 判断拼图完成
                    gridLayout.checkSuccess();
                    break;
                }
            }

            if (!isInGrid) {//若gridLayout找不到
                // 当前拖拽的View插入到布局内与其接触子控件
                for (int i = 0, j = scrollLayout.mContainer.getChildCount(); i < j; i++) {
                    if (scrollLayout.mContainer.getChildAt(i).getId() == result + 1001) {
                        // 设置参数
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        final View v = scrollLayout.mContainer.getChildAt(i);
                        ViewGroup owner = (ViewGroup) v.getParent();
                        owner.removeView(v);
                        layoutLin.addView(v, lp);

                        if (MainActivity.flag == MainActivity.FLAG_BLUR) {
                            RelativeLayout r = (RelativeLayout) v;
                            final ImageView iv = (ImageView) r.getChildAt(0);

                            if (iv.getAlpha() == 1.0f)
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        JigsawGridLayout.BlurTask b =  gridLayout.new BlurTask(iv);
                                        gridLayout.taskLists.put(v.getId(), b);
                                        new Timer().schedule(b, 0, gridLayout.mColumn * 500 / 3);
                                    }
                                }.start();
                        }

                        break;
                    }
                }
            }
        } else {//该位置有不恰当的view

            boolean isInGrid = false;
            for (int i = 0; i < mColumn * mColumn; i++) {
                LinearLayout layoutLTrue = (LinearLayout) findViewById(i + 1);
                View mViewTrue = layoutLTrue.getChildAt(0);
                if (mViewTrue != null && mViewTrue.getId() == result + 1001) {//找到
                    isInGrid = true;

                    ViewGroup owner = (ViewGroup) mViewTrue.getParent();
                    // 设置参数
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);

                    ViewGroup mViewowner = (ViewGroup) mView.getParent();
                    mViewowner.removeView(mView);
                    owner.removeView(mViewTrue);
                    mViewowner.addView(mViewTrue, lp);
                    owner.addView(mView, lp);

                    // 判断拼图完成
                    gridLayout.checkSuccess();

                    break;
                }
            }

            if (!isInGrid) {//若gridLayout找不到

                for (int i = 0, j = scrollLayout.mContainer.getChildCount(); i < j; i++) {
                    if (scrollLayout.mContainer.getChildAt(i).getId() == result + 1001) {
                        // 设置参数
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        final  View v = scrollLayout.mContainer.getChildAt(i);
                        ViewGroup owner = (ViewGroup) v.getParent();

                        owner.removeView(v);
                        layoutLin.removeView(mView);

                        layoutLin.addView(v, lp);

                        // 设置要加入的view的参数
                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(scrollLayout.mChildWidth,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        lp1.setMargins(scrollLayout.mItemMargin, 0, scrollLayout.mItemMargin, 0);
                        owner.addView(mView, i, lp1);

                        if (MainActivity.flag == MainActivity.FLAG_BLUR) {
                            RelativeLayout r = (RelativeLayout) v;
                            final ImageView iv = (ImageView) r.getChildAt(0);

                            if (iv.getAlpha() == 1.0f)
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        JigsawGridLayout.BlurTask b =  gridLayout.new BlurTask(iv);
                                        gridLayout.taskLists.put(v.getId(), b);
                                        new Timer().schedule(b, 0, gridLayout.mColumn * 500 / 3);
                                    }
                                }.start();

                            View view1 = (View) mView;
                            if (gridLayout.taskLists.containsKey(view1.getId())) {
                                gridLayout.taskLists.get(view1.getId()).cancel();
                                gridLayout.taskLists.remove(view1.getId());
                                RelativeLayout r1 = (RelativeLayout) view1;
                                ImageView iv1 = (ImageView) r1.getChildAt(0);
                                ImageView iv2 = (ImageView) r1.getChildAt(2);
                                iv1.setAlpha(1.0f);
                                iv2.setVisibility(View.GONE);
                            }
                        }

                        break;
                    }
                }
            }

        }

        layoutLin.setBackgroundResource(enterShape);

        //1.5s后提示
        timer.schedule(new RemindTask(), 1500);

        gridLayout.checkSuccess();
    }

    class RemindTask extends TimerTask {
        public void run() {
            layoutLin.post(new Runnable() {
                @Override
                public void run() {
                    layoutLin.setBackgroundResource(normalShape);
                }
            });
        }
    }
}
