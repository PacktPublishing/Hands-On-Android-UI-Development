package com.packtpub.claim.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by jason on 2017/11/07.
 */
public class Attachment implements Parcelable {

    File file;
    Type type;

    public Attachment(final File file, final Type type) {
        this.file = file;
        this.type = Type.safe(type);
    }

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = Type.safe(type);
    }

    protected Attachment(final Parcel in) {
        file = new File(in.readString());
        type = Type.values()[in.readInt()];
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(file.getAbsolutePath());
        dest.writeInt(type.ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public enum Type {

        IMAGE,
        UNKNOWN;

        public static Type safe(final Type type) {
            return type != null ? type : UNKNOWN;
        }
    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(final Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(final int size) {
            return new Attachment[size];
        }
    };
}