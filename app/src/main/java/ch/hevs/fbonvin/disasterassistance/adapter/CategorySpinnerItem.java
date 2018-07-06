package ch.hevs.fbonvin.disasterassistance.adapter;

public class CategorySpinnerItem {

    private String mCategoryName;
    private int mIcon;
    private int mcolor;

    public CategorySpinnerItem(String categoryName, int icon, int color) {
        mCategoryName = categoryName;
        mIcon = icon;
        mcolor = color;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getColor() {
        return mcolor;
    }
}
