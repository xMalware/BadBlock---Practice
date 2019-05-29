package fr.toinetoine1.practice.data;

/*
    Created by Toinetoine1 on 02/04/2019
*/

public enum Mode {

    ONE("onevsone", false, 1, "§a1vs1"),
    TWO("twovstwo", false, 2, "§a2vs2"),
    THREE("threevsthree", false, 3, "§a3vs3"),
    RANKEDONE("rankedone", true, 1, "§a1vs1 §6Classé"),
    RANKEDTWO("rankedtwo", true, 2, "§a2vs2 §6Classé");

    private String colomnName;
    private boolean isRanked;
    private int size;
    private String formattedName;

    Mode(String colomnName, boolean isRanked, int size, String formattedName) {
        this.colomnName = colomnName;
        this.isRanked = isRanked;
        this.size = size;
        this.formattedName = formattedName;
    }

    public String getColomnName() {
        return colomnName;
    }

    public boolean isRanked() {
        return isRanked;
    }

    public int getSize() {
        return size;
    }

    public String getFormattedName() {
        return formattedName;
    }
}
