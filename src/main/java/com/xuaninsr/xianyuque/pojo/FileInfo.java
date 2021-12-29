package com.xuaninsr.xianyuque.pojo;

import org.joda.time.DateTime;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class FileInfo implements Serializable {
    private int ID;
    @Nullable
    private String title;
    @Nullable
    private String localName;
    @Nullable
    private boolean isFolder;
    @Nullable
    private boolean isTopLev;
    @Nullable
    private Timestamp lastEdit;
    @Nullable
    private int cacheFor;
    @Nullable
    private int insideOf;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public boolean isTopLev() {
        return isTopLev;
    }

    public void setTopLev(boolean topLev) {
        isTopLev = topLev;
    }

    public FileInfo() {}

    public FileInfo(int ID, @Nullable String title, @Nullable String localName,
                    boolean isFolder, boolean isTopLev, @Nullable Timestamp lastEdit,
                    @Nullable int cacheFor, @Nullable int insideOf) {
        this.ID = ID;
        this.title = title;
        this.localName = localName;
        this.isFolder = isFolder;
        this.isTopLev = isTopLev;
        this.lastEdit = lastEdit;
        this.cacheFor = cacheFor;
        this.insideOf = insideOf;
    }

    public String getLastEdit() {
        if (lastEdit == null)
            return "";

        Timestamp lastEdit = new Timestamp(this.lastEdit.getTime() - 60 * 60 * 8 * 1000);

        long currentTimestamps = System.currentTimeMillis();
        long oneDayTimestamps = (long) (60 * 60 * 24 * 1000);
        long todayBegin = currentTimestamps -
                (currentTimestamps + 60 * 60 * 8 * 1000) % oneDayTimestamps;
        if (lastEdit.after(new Timestamp(todayBegin))) {
            return new Time(lastEdit.getTime()).toString();
        }
        return new Date(lastEdit.getTime()).toString() + " "
                + new Time(lastEdit.getTime()).toString();
    }

    public void setLastEdit(Timestamp lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getCacheFor() {
        return cacheFor == -1 ? null : Integer.toString(cacheFor);
    }

    public void setCacheFor(int cacheFor) {
        this.cacheFor = cacheFor;
    }

    public String getInsideOf() {
        return insideOf == -1 ? null : Integer.toString(insideOf);
    }

    public void setInsideOf(int insideOf) {
        this.insideOf = insideOf;
    }

    public String toString() {
        return "{ title : " + title + ", last edited at " + lastEdit.toString() + " }";
    }
}
