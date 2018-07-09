package ch.hevs.fbonvin.disasterassistance.adapter;

public class SpinnerCategoryItem {

    private final String mCategoryName;
    private final int mIcon;
    private final int mColor;

    public SpinnerCategoryItem(String categoryName, int icon, int color) {
        mCategoryName = categoryName;
        mIcon = icon;
        mColor = color;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getColor() {
        return mColor;
    }
}
