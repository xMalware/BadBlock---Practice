package fr.toinetoine1.practice.data;

/*
    Created by Toinetoine1 on 02/04/2019
*/

public enum Mode {

    ONE("onevsone", false),
    TWO("twovstwo", false),
    THREE("threevsthree", false),
    RANKEDONE("rankedone", true),
    RANKEDTWO("rankedtwo", true);

    private String databaseName;
    private boolean isRanked;

    Mode(String databaseName, boolean isRanked){
        this.databaseName = databaseName;
        this.isRanked = isRanked;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public boolean isRanked() {
        return isRanked;
    }
}
