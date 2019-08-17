package com.goldze.base.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CatelogEntity {
    private int id;
    private String title;
    private int parentId;
    private List<ItemsEntity> items;

    public List<ItemsEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public static class ItemsEntity implements Parcelable {
        private int id;
        private String title;
        private int parentId;
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.id);
            parcel.writeInt(this.parentId);
            parcel.writeString(this.title);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        protected ItemsEntity(Parcel in) {
            this.id = in.readInt();
            this.parentId = in.readInt();
            this.title = in.readString();
        }

        public static final Creator<ItemsEntity> CREATOR = new Creator<ItemsEntity>() {
            @Override
            public ItemsEntity createFromParcel(Parcel source) {
                return new ItemsEntity(source);
            }

            @Override
            public ItemsEntity[] newArray(int size) {
                return new ItemsEntity[size];
            }
        };
    }


}
