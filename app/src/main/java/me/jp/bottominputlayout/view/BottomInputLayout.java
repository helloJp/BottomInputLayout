package me.jp.bottominputlayout.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.jp.bottominputlayout.R;

/**
 * Created by jiangp on 15/12/1.
 */
public class BottomInputLayout extends RelativeLayout implements View.OnClickListener {
    private int MASK_COLOR = Color.parseColor("#b2000000");
    private int ID_BOTTOM_VIEW = R.mipmap.ic_launcher + 123;

    private SoftKeyboardStateHelper mKeyboardHelper;

    private View mViewMask;
    private View mLayoutPic;
    private ImageView mIvPic;
    private View mViewDelete;
    private View mViewAddPic;
    private EditText mEtInput;
    private TextView mTvAction;

    private String mPicPath;

    public BottomInputLayout(Context context) {
        this(context, null);
    }

    public BottomInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        initEvent();

    }

    private void initEvent() {
        mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext())
                .getWindow().getDecorView());
        mKeyboardHelper.addSoftKeyboardStateListener(new SoftKeyboardStateCallback());

        //mask
        mViewMask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewMask.setVisibility(View.GONE);
                if (null != mOnClickCallback) {
                    mOnClickCallback.onMaskClick(v);
                }
            }
        });

        //pic
        mIvPic.setOnClickListener(this);
        //add Pic
        mViewAddPic.setOnClickListener(this);
        //delete pic
        mViewDelete.setOnClickListener(this);
        //action
        mTvAction.setOnClickListener(this);
    }

    private void initView() {
        if (getChildCount() == 0) {
            Log.e("error", "底部评论布局字内容为空...");
            return;
        }
        //设置内容布局,ABOVE OF BOTTOM VIEW
        View contentView = getChildAt(0);  //内容布局

        //添加底部布局
        View bottomView = getBottomView();
        bottomView.setId(ID_BOTTOM_VIEW);
        RelativeLayout.LayoutParams bottomParams = new
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(bottomView, bottomParams);

        LayoutParams contentParams = (LayoutParams) contentView.getLayoutParams();
        contentParams.addRule(RelativeLayout.ABOVE, ID_BOTTOM_VIEW);

        //半透明蒙版
        mViewMask = new View(getContext());
        RelativeLayout.LayoutParams maskParams = new
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        maskParams.addRule(RelativeLayout.ABOVE, ID_BOTTOM_VIEW);
        mViewMask.setBackgroundColor(MASK_COLOR);
        mViewMask.setClickable(true);
        mViewMask.setVisibility(View.GONE);
        this.addView(mViewMask, maskParams);

        //图片容器布局（图片、删除按钮）
        mLayoutPic = getPicView();
        RelativeLayout.LayoutParams picParams = new
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        picParams.addRule(RelativeLayout.ABOVE, ID_BOTTOM_VIEW);
        picParams.setMargins(dip2px(10), 0, 0, dip2px(10));
        this.addView(mLayoutPic, picParams);
        mLayoutPic.setVisibility(View.GONE);
    }

    private View getBottomView() {
        View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_input, null);
        mViewAddPic = bottomView.findViewById(R.id.add_pic);
        mEtInput = (EditText) bottomView.findViewById(R.id.et_input);
        mTvAction = (TextView) bottomView.findViewById(R.id.tv_action);
        return bottomView;
    }

    private View getPicView() {
        View layoutPic = LayoutInflater.from(getContext()).inflate(R.layout.pic_container, null);
        mIvPic = (ImageView) layoutPic.findViewById(R.id.iv_pic);
        mViewDelete = layoutPic.findViewById(R.id.delete_pic);
        return layoutPic;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pic:
                if (null != mOnClickCallback) {
                    mOnClickCallback.onShowBigImg(v);
                }
                showToast("iv_pic");
                break;
            case R.id.add_pic:
                if (null != mOnClickCallback) {
                    mOnClickCallback.onAddPic(v);
                }
                addPic();
                break;
            case R.id.delete_pic:
                deletePic();
                showToast("delete_pic");
                break;
            case R.id.tv_action:
                if (null != mOnClickCallback) {
                    mOnClickCallback.onAction(v);
                }
                break;
        }
    }

    private void deletePic() {
        mIvPic.setImageBitmap(null);
        mLayoutPic.setVisibility(View.GONE);
        //TODO RESET mPicPath
    }

    private void addPic() {
        //TODO SET mPicPath
        mIvPic.setImageResource(R.mipmap.test_pic);
        mLayoutPic.setVisibility(View.VISIBLE);
    }

    private class SoftKeyboardStateCallback implements SoftKeyboardStateHelper.SoftKeyboardStateListener {
        @Override
        public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            setKeyBoardOpenStatus();
        }

        @Override
        public void onSoftKeyboardClosed() {

        }
    }

    private void setKeyBoardOpenStatus() {
        if (null != mViewMask)
            mViewMask.setVisibility(View.VISIBLE);
    }

    public EditText getInputView() {
        return mEtInput;
    }

    public String getPicPath() {
        return mPicPath;
    }

    private OnClickCallback mOnClickCallback;

    public void setOnClickCallback(OnClickCallback callback) {
        mOnClickCallback = callback;
    }

    public interface OnClickCallback {
        void onShowBigImg(View view);

        void onAddPic(View view);

        void onAction(View view);

        void onMaskClick(View view);
    }

    //------------auto hide keyboard start------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = ((Activity) getContext()).getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] location = {0, 0};
            v.getLocationInWindow(location);
            int top = location[1];
            int bottom = top + v.getMeasuredHeight() + dip2px(10);
            if (event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
    //------------auto hide keyboard end------------------------

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void showSoftInput() {
        this.getInputView().requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.getInputView(), InputMethodManager.SHOW_IMPLICIT);
    }


    private int dip2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    public void debug(String msg) {
        Log.i("debug", msg);
    }

    public void showToast(String msg) {
        if (null != msg)
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
