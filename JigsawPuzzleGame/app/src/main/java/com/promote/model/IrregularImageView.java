package com.promote.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.promote.jigsawpuzzlegame.R;
import com.promote.jigsawpuzzleview.JigsawPuzzleView;
import com.promote.utility.ImagePiece;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IrregularImageView extends ImageView {

    /* 不规则图片的形状数组，俄罗斯方块形状 */
    public final static int status[][][] = {
            /* Z型 */
            {{1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {1, 1, 0, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{1, 0, 0, 0}, {1, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}},
            /* L型 */
            {{1, 0, 0, 0}, {1, 0, 0, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 1, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{1, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}},
            {{1, 1, 1, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            /* 土型 */
            {{0, 1, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {1, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}},
            {{1, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{1, 0, 0, 0}, {1, 1, 0, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}},
            /* 一型 */
            {{1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}},
            {{1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            /* 田型 */
            {{1, 1, 0, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}
    };
    /* 每个形状数组的行列数，默认4 * 4 */
    public final static int statusItemColumns = 4;
    /* 矩阵的列数 */
    public static int mColumn;

    /* 属于哪个形状,形状数组的索引 */
    private int statusIndex = -1;
    /* 该图片出于其属于形状数组的横纵坐标 */
    private int indexX = -1;
    private int indexY = -1;

    /* 图片块 */
    private ImagePiece piece;
    /* 图片id偏移量 */
    private int offsetID = 1001;

    private Context mContext;

    public IrregularImageView(Context context) {
        super(context);
        mContext = context;
    }

    public IrregularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IrregularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 返回不规则图形的中心坐标x
     *
     * @param statusIndex
     * @return
     */
    public static int getStatusCenterX(int statusIndex) {
        int x = 1;
        switch (statusIndex) {
            case 0:
            case 2:
            case 7:
            case 10:
                x = 0;
                break;
            default:
                break;
        }
        return x;
    }

    /**
     * 返回不规则图形的中心坐标y
     *
     * @param statusIndex
     * @return
     */
    public static int getStatusCenterY(int statusIndex) {
        int y = 1;
        switch (statusIndex) {
            case 1:
            case 3:
            case 4:
            case 10:
            case 11:
            case 13:
            case 14:
                y = 0;
                break;
            default:
                break;
        }
        return y;
    }

    /**
     * 获取所属形状的数组
     *
     * @param statusIndex
     * @return
     */
    public static int[][] getStatus(int statusIndex) {
        int nums = statusItemColumns;
        int statusItem[][] = new int[nums][nums];
        for (int i = 0; i < nums; i++) {
            for (int j = 0; j < nums; j++)
                statusItem[i][j] = status[statusIndex][i][j];
        }
        return statusItem;
    }

    /**
     * 设置该mageView属于哪个形状
     *
     * @param statusIndex1 形状数组索引
     */
    public void setStatusIndex(int statusIndex1) {
        statusIndex = statusIndex1;
    }

    /**
     * 设置横坐标
     *
     * @param indexX1
     */
    public void setIndexX(int indexX1) {
        indexX = indexX1;
    }

    /**
     * 设置纵坐标
     *
     * @param indexY1
     */
    public void setIndexY(int indexY1) {
        indexY = indexY1;
    }

    /**
     * 设置该View的图片资源
     *
     * @param piece1 封装的图片
     */
    public void setPiece(ImagePiece piece1) {
        piece = piece1;
    }

    /**
     * 设置图片ID与其索引的偏移量
     *
     * @param offsetID1 偏移量
     */
    public void setOffsetID(int offsetID1) {
        offsetID = offsetID1;
    }

    /**
     * 获取所属形状的索引
     *
     * @return
     */
    public int getStatusIndex() {
        return statusIndex;
    }

    /**
     * 获取横坐标
     *
     * @return 横坐标
     */
    public int getIndexX() {
        return indexX;
    }

    /**
     * 获取纵坐标
     *
     * @return
     */
    public int getIndexY() {
        return indexY;
    }

    /**
     * 获取图片资源
     *
     * @return
     */
    public ImagePiece getImagePiece() {
        return piece;
    }

    /**
     * 获取图片ID与其索引的偏移量
     *
     * @return
     */
    public int getOffsetID() {
        return offsetID;
    }

    /**
     * 获取当前imageView所属的不规则View
     *
     * @param imagePieceList  所有图片块的列表
     * @param startPieceIndex 所属不规则View第一块添加的imageView在列表中的索引值
     * @return
     */
    public View getIrregularView(List<ImagePiece> imagePieceList, int startPieceIndex) {
        if (statusIndex < 0 || statusIndex >= status.length)
            return null;
        if (piece == null)
            return null;

        if (indexX < 0 || indexX >= statusItemColumns)
            indexX = getStatusCenterX(statusIndex);
        if (indexY < 0 || indexY >= statusItemColumns)
            indexY = getStatusCenterY(statusIndex);

        View irView = createIrregularView(mContext, statusIndex, indexX, indexY,
                imagePieceList, startPieceIndex, offsetID);
        return irView;
    }

    /**
     * 根据图片的索引位置及属于的形状构造整个不规则图形
     *
     * @param context
     * @param statusIndex     所属的形状索引
     * @param indexX          形状二维数组中的横坐标
     * @param indexY          形状二维数组中的纵坐标
     * @param imagePieceList  保存整张图片的链表
     * @param startPieceIndex 开始图片的索引
     * @param offsetID        生成子控件的ID与其图片索引的偏移量,防止ID相同
     * @return 不规则的View
     */
    public static View createIrregularView(Context context, int statusIndex, int indexX, int indexY,
                                           List<ImagePiece> imagePieceList,
                                           int startPieceIndex, int offsetID) {
        /* 没有设置图片分割的列数 */
        if (mColumn <= 0) return null;
        /* 不存在此形状的索引 */
        if (statusIndex < 0 || statusIndex > status.length)
            return null;
        /* 形状数组索引坐标超限 */
        if (indexX < 0 || indexY < 0 || indexX >= statusItemColumns
                || indexY >= statusItemColumns) {
            Log.e("CREATE_IMAGE_TAG","create image index error");
            return null;
        }
        /* 获取形状数组 */
        int depth[][] = getStatus(statusIndex);

        int curPieceIndex;
        List<View> irViewList = new ArrayList<View>();
        for (int i = 0; i < statusItemColumns; i++) {
            for (int j = 0; j < statusItemColumns; j++) {
                if (depth[i][j] == 1) {

                    /* 计算这次加入的子控件的图片索引 */
                    curPieceIndex = startPieceIndex;
                    curPieceIndex += j - indexY;
                    curPieceIndex += mColumn * (i - indexX);

                    if(curPieceIndex < 0 || curPieceIndex >= imagePieceList.size()){
                        Log.e("PIECE_LIST_TAG","piece index exceed");
                        return null;
                    }

                    ImagePiece piece = imagePieceList.get(curPieceIndex);
                    IrregularImageView currentImageView = new IrregularImageView(context);

                    /* 设置各个参数 */
                    currentImageView.setStatusIndex(statusIndex);
                    currentImageView.setIndexX(i);
                    currentImageView.setIndexY(j);
                    currentImageView.setPiece(piece);
                    currentImageView.setImageBitmap(piece.getBitmap());
                    /* 设置本次子控件的Id值 */
                    int currentViewId = piece.getIndex() + offsetID;
                    currentImageView.setId(currentViewId);

                    currentImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    /* 添加子控件 */
                    irViewList.add(currentImageView);
                }
            }
        }

        return genIrregularView(context, irViewList);
    }

    /**
     * 生成不规则图形
     *
     * @param irViewList
     * @return
     */
    public static View genIrregularView(Context context, List<View> irViewList) {
        RelativeLayout convertView = new RelativeLayout(context) {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                super.onInterceptTouchEvent(ev);
                return true;
            }
        };
        convertView.setGravity(Gravity.CENTER);

        /* 链表为空 */
        if (irViewList == null || irViewList.size() == 0) {
            Log.e("GEN_IRVIEW_TAG","irViewList is null");
            return null;
        }

        /* 根据形状数组构造不规则图形 */
        IrregularImageView curView = (IrregularImageView)irViewList.get(0);
        int[][] depth = IrregularImageView.getStatus(curView.getStatusIndex());
        addDepthIrView(convertView, irViewList, depth, 0, 0, 0, curView.getIndexX(), curView.getIndexY());

        return convertView;
    }

    /**
     * 递归添加不规则图形块
     *
     * @param convertView
     * @param irViewList
     * @param depth
     * @param preId
     * @param offsetX
     * @param offsetY
     * @param curX
     * @param curY
     */
    private static void addDepthIrView(ViewGroup convertView, List<View> irViewList,
                                       int depth[][], int preId, int offsetX, int offsetY, int curX, int curY) {

        /* 索引超出范围则返回 */
        if (curX < 0 || curY < 0 || curX >= statusItemColumns ||
                curY >= statusItemColumns)
            return;
        /* 此处不需要添加子控件 */
        if (depth[curX][curY] != 1)
            return;

        /* 生成这次加入父控件的子控件 */
        IrregularImageView curImageView = null;
        int curViewId = -1;
        /* 找到对应的图片 */
        for (int i = 0; i < irViewList.size(); i++) {
            curImageView = (IrregularImageView)irViewList.get(i);

            if (curImageView.getIndexX() == curX && curImageView.getIndexY() == curY) {
                curViewId = curImageView.getId();
                break;
            }
        }
        if (curImageView == null || curViewId == -1){
            Log.e("GET_IMAGE_TAG","can not find right image");
            return;
        }

        /* 设置子控件的相关参数 */
        curImageView.setPadding(1, 1, 1, 1);
        curImageView.setBackgroundResource(R.drawable.puzzle_item_shape);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(60, 60);

        /* 不是第一个加入的子控件 */
        if (!(offsetX == 0 && offsetY == 0)) {
            /* 在上个子控件的左边 */
            if (offsetY == -1) {
                lp.addRule(RelativeLayout.LEFT_OF, preId);
                lp.addRule(RelativeLayout.ALIGN_BOTTOM, preId);
            }
            /* 在上个子控件的右边 */
            else if (offsetY == 1) {
                lp.addRule(RelativeLayout.RIGHT_OF, preId);
                lp.addRule(RelativeLayout.ALIGN_BOTTOM, preId);
            }
            /* 在上个子控件的上面 */
            else if (offsetX == -1) {
                lp.addRule(RelativeLayout.ABOVE, preId);
                lp.addRule(RelativeLayout.ALIGN_LEFT, preId);
            }
            /* 在上个子控件的下面 */
            else if (offsetX == 1) {
                lp.addRule(RelativeLayout.BELOW, preId);
                lp.addRule(RelativeLayout.ALIGN_LEFT, preId);
            } else {
                /* 布局错误 */
                Log.e("Layout_TAG", "Layout ImageView position error!");
                return;
            }
        }
        /* 第一个加入的子控件 */
        else {
            /* 防止会有子控件无法加入 */
            lp.setMargins(curY * 60, curX * 60, 0, 0);
        }
        /* 添加子控件 */
        convertView.addView(curImageView, lp);
        /* 防止循环 */
        depth[curX][curY] = 0;

        /* 添加左边的控件 */
        if (offsetY != 1)
            addDepthIrView(convertView, irViewList, depth, curViewId, 0, -1, curX, curY - 1);
        /* 添加右边的控件 */
        if (offsetY != -1)
            addDepthIrView(convertView, irViewList, depth, curViewId, 0, 1, curX, curY + 1);
        /* 添加上面的控件 */
        if (offsetX != 1)
            addDepthIrView(convertView, irViewList, depth, curViewId, -1, 0, curX - 1, curY);
        /* 添加下面的控件 */
        if (offsetX != -1)
            addDepthIrView(convertView, irViewList, depth, curViewId, 1, 0, curX + 1, curY);
    }

    /**
     * 初始Bitmap对象的缩放裁剪过程
     *
     * @param bmp    初始Bitmap对象
     * @param radius 圆形图片直径大小
     * @return 返回一个圆形的缩放裁剪过后的Bitmap对象
     */
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        //比较初始Bitmap宽高和给定的圆形直径，判断是否需要缩放裁剪Bitmap对象
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
                sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
        //核心部分，设置两张图片的相交模式，在这里就是上面绘制的Circle和下面绘制的Bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

}
