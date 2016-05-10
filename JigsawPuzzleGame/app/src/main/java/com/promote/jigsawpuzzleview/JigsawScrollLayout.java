package com.promote.jigsawpuzzleview;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.promote.jigsawpuzzlegame.R;
import com.promote.model.IrregularImageView;
import com.promote.utility.DensityUtil;
import com.promote.utility.ImagePiece;
import com.promote.utility.ImageUtil;

/**
 * 滚动布局
 */
public class JigsawScrollLayout extends HorizontalScrollView {

    private static String scrollLayoutTag = "tag_ScrollLayout";

    /* 滚动布局中的LinearLayout */
    public LinearLayout mContainer;

    /* 布局中子元素的类型 */
    private int itemShape;

    /* 布局的高度 */
    private int mHeight;
    /* 布局的宽度 */
    private int mWidth;

    /* 布局的Toppadding */
    private int mTopPadding = 15;
    /* 布局的Bottompadding */
    private int mBottomPadding = 5;
    /* 子控件之间的间距 */
    public int mItemMargin = 10;

    /* 子元素的宽度 */
    public int mChildWidth;
    /* 子元素的高度 */
    private int mChildHeight;
    /* 子控件的宽高比 */
    private double relative;

    private List<View> mImages;
    /**
     * 当前拖拽的view
     */
    private View mDrapView;

    private Context mContext;

    public JigsawScrollLayout(Context context) {
        this(context, null);
    }

    public JigsawScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        // 设置HorizontalScrollView一直在布局中显示，即使没有子元素
        setFillViewport(true);
        setBackgroundResource(R.drawable.scroll_layout_shape);
        setTag(scrollLayoutTag);

        // 将dp值转化为px
        mItemMargin = DensityUtil.dip2px(mContext, mItemMargin);
        mTopPadding = DensityUtil.dip2px(mContext, mTopPadding);
        mBottomPadding = DensityUtil.dip2px(mContext, mBottomPadding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 初始化数据，设置数据适配器
     */
    public void initView(List<View> mImages) {
        // 清空布局
        this.removeAllViews();
        this.mImages = mImages;

        itemShape = JigsawPuzzleView.itemShape;;

        // 滚动布局中添加一个线性布局，用于放置Item
        mContainer = new LinearLayout(mContext);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setPadding(0, mTopPadding, 0, mBottomPadding);
        // 设置插入的参数
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                mWidth, mHeight);
        addView(mContainer, lp);
        // 防止布局内的元素全部移走后的特殊情况，设置拖拽监听事件
        mContainer.setOnDragListener(mOnDragListener);

        /* 初始化布局子元素 */
        initItems();
    }

    /**
     * 初始化子控件
     */
    private void initItems() {
        // 计算当前子元素View的宽和高，用于保持宽高比例显示
        mChildHeight = mHeight - mTopPadding - mBottomPadding;
        if (itemShape == 0) {
            if (relative < 1) {
                mChildWidth = (int) (mChildHeight * relative);
            } else {
                mChildWidth = (int) (mChildHeight / relative);
            }
        } else {
            mChildWidth = mChildHeight;
        }

        // 设置插入子控件的参数
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mChildWidth,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(mItemMargin, 0, mItemMargin, 0);

        for(int i=0;i<mImages.size();i++){
            View convertView =mImages.get(i);
            convertView.setTag("0");

            // 为子控件设置触屏监听事件和拖拽监听事件
            bindDrapListener(convertView);
            mContainer.addView(convertView, lp);
        }
    }


    /**
     * 设置触屏和拖拽事件监听器
     *
     * @param v
     */
    private void bindDrapListener(View v) {
        v.setOnTouchListener(mOnTouchListener);
        v.setOnDragListener(mOnDragListener);
    }

    /**
     * 定义触屏事件监听器
     */
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mDrapView = v;
            //Log.e("distance_TAG","x = " + event.getX() + " rawX = " + event.getRawX());
            // 设置拖拽阴影
            DragShadowBuilder shadowBuilder = new DragShadowBuilder(
                    mDrapView);
            // 开始拖拽view控件
            mDrapView.startDrag(null, shadowBuilder, mDrapView, 0);
            return true;
        }
    };

    /**
     * 定义拖拽事件监听器
     */
    private View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {

                // 开始拖拽控件，获得一个拖动阴影
                case DragEvent.ACTION_DRAG_STARTED:
                    // Do something
                    break;

                // 拖动阴影进入了某View的屏幕边界内时
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (!(v instanceof LinearLayout))
                        v.setAlpha(0.5F);
                    break;

                // 拖动阴影移到了View的边界之外时
                case DragEvent.ACTION_DRAG_EXITED:
                    if (!(v instanceof LinearLayout))
                        v.setAlpha(1F);
                    break;

                // 在View对象上面释放拖动阴影时
                case DragEvent.ACTION_DROP:
                    // 获取拖拽的View及其父组件
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();

                    if (itemShape == 0) {

                        RelativeLayout r = (RelativeLayout) view;
                        ((ImageView) r.getChildAt(0)).setAlpha(1.0f);
                        ((ImageView)r.getChildAt(2)).setVisibility(View.GONE);

                        // 设置布局参数
                        TextView textView = (TextView) ((ViewGroup)view).getChildAt(1);
                        String oldTextTag = (String) textView.getTag();
                        String newTextTag = "0" + oldTextTag.substring(1);
                        textView.setTag(newTextTag);
                        textView.setText(newTextTag);
                    } else if(itemShape == 1){
                        /* 判断是从哪个布局拖动过来的 */
                        int layoutTag = Integer.parseInt(view.getTag().toString());
                        if(layoutTag == 1){
                            List<View> irViewList = JigsawGridLayout.removeIrView(
                                    (IrregularImageView) view);
                            view = IrregularImageView.genIrregularView(mContext,irViewList);
                            Log.e("ADDVIEW_TAG","add a view from gridlayout");
                        }
                    }

                    // 设置要加入的view的参数
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mChildWidth,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(mItemMargin, 0, mItemMargin, 0);

                    // 当前拖拽的View没有接触到布局内没有任何子控件时
                    if (v instanceof LinearLayout) {
                        // 从其父组件中移除，并添加到布局中
                        owner.removeView(view);
                        ((LinearLayout) v).addView(view, lp);
                    } else { // 当前拖拽的View插入到布局内与其接触子控件
                        for (int i = 0, j = mContainer.getChildCount(); i < j; i++) {
                            if (mContainer.getChildAt(i) == v) {
                                // 从其父组件中移除，并插入当前位置
                                owner.removeView(view);
                                mContainer.addView(view, i, lp);
                                break;
                            }
                        }
                    }
                    view.setTag("0");
                    bindDrapListener(view);
                    break;

                // 系统停止拖动操作时
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setAlpha(1F);
                    break;
                default:
                    break;
            }
            return true;
        }
    };


    /**
     * 设置布局高度
     *
     * @param height
     */
    public void setLayoutHeight(int height) {
        mHeight = height;
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
     * 设置高宽比
     *
     * @param relative
     */
    public void setRelative(double relative) {

        this.relative = relative;
    }

    public void setItemShape(int shape) {
        itemShape = shape;
    }

    /**
     * 获取子控件的宽度
     *
     * @return
     */
    public int getItemWidth() {
        return mChildWidth;
    }

    /**
     * 获取子控件的高度
     *
     * @return
     */
    public int getItemHeight() {
        return mChildHeight;
    }

    /**
     * 获取拖拽监听器
     * @return
     */
    public View.OnDragListener getOnDragListener(){
        return mOnDragListener;
    }

}
