package org.ituns.framework.master.mvvm.viewitem;

public interface SplitterItem {
    int TYPE_SPLITTER = 201001001;
    float height();
    int backColor();
    int faceColor();
    float leftMargin();
    float rightMargin();
}
