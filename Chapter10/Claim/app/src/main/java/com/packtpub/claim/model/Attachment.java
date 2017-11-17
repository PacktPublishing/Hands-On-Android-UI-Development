package com.packtpub.claim.model;

import android.os.Parcel;
import android.os.Parcelable;

import android.arch.persistence.room.Index;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.File;

/**
 * Created by jason on 2017/11/07.
 */
@Entity(indices = @Index("claimItemId"))
public class Attachment implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long claimItemId;
    File file;
    Type type;

    public Attachment() {
        //empty
    }

    @Ignore
    public Attachment(final File file, final Type type) {
        this.file = file;
        this.type = Type.safe(type);
    }

    protected Attachment(final Parcel in) {
        id = in.readLong();
        claimItemId = in.readLong();
        file = new File(in.readString());
        type = Type.values()[in.readInt()];
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

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(id);
        dest.writeLong(claimItemId);
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