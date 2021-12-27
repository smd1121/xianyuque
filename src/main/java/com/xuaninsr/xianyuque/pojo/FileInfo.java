package com.xuaninsr.xianyuque.pojo;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.sql.Date;

public class FileInfo implements Serializable {
    private int ID;
    @Nullable private String title;
    @Nullable private String localName;
    @Nullable private boolean isFolder;
    @Nullable private boolean isTopLev;
    @Nullable private Date lastEdit;
    @Nullable private int cacheFor;
    @Nullable private int insideOf;

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

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public int getCacheFor() {
        return cacheFor;
    }

    public void setCacheFor(int cacheFor) {
        this.cacheFor = cacheFor;
    }

    public int getInsideOf() {
        return insideOf;
    }

    public void setInsideOf(int insideOf) {
        this.insideOf = insideOf;
    }

    public String toString() {
        return "{ title : " + title + ", last edited at " + lastEdit.toString() + " }";
    }
}
