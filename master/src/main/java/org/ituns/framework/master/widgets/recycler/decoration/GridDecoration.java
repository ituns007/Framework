package org.ituns.framework.master.widgets.recycler.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ituns.framework.master.tools.android.IScreen;

import java.lang.reflect.Method;

public class GridDecoration extends RecyclerView.ItemDecoration {
    private int column;         //列数目
    private float rowSpacing;   //行间距
    private float colSpacing;   //列间距

    private GridDecoration() {}

    public static GridDecoration with(int column, float spacing) {
        return with(column, spacing, spacing);
    }

    public static GridDecoration with(int column, float rowSpacing, float colSpacing) {
        GridDecoration decoration = new GridDecoration();
        decoration.column = column;
        decoration.rowSpacing = rowSpacing;
        decoration.colSpacing = colSpacing;
        return decoration;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //行间距和列间距
        int rowSpacePixel = IScreen.dp2px(parent.getContext(), rowSpacing);
        int colSpacePixel = IScreen.dp2px(parent.getContext(), colSpacing);

        // 获取 view 在 adapter 中的位置。
        int position = parent.getChildAdapterPosition(view);

        // view 所在的列
        int posColumn = position % column;

        /**
         * position < 列数：第一行，top = 0
         * position >= 列数：不是第一行，top = xSpacingPixel
         * */
        int top = position >= column ? rowSpacePixel : 0;

        /**
         * 左间距：当前列 / 列数 x 列间距
         * */
        int left = posColumn  * colSpacePixel / column;

        /**
         * 右间距：(1 - (当前列 + 1) / 列数) * 列间距
         * */
        int right = colSpacePixel - (posColumn + 1) * colSpacePixel / column;

        if(isLayoutRTL(parent)) {
            outRect.set(right, top, left, 0);
        } else {
            outRect.set(left, top, right, 0);
        }
    }

    private boolean isLayoutRTL(RecyclerView parent) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if(manager instanceof LinearLayoutManager) {
            try {
                Class<?> clazz = LinearLayoutManager.class;
                Method method = clazz.getDeclaredMethod("isLayoutRTL");
                method.setAccessible(true);
                Object object = method.invoke(manager);
                if(object instanceof Boolean) {
                    return (Boolean) object;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
