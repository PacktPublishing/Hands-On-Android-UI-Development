package com.packtpub.claim.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.packtpub.claim.model.Category;
import com.packtpub.claim.model.ClaimItem;
import com.packtpub.claim.model.Attachment;

import java.io.File;

import java.util.Date;

/**
 * Created by jason on 2017/11/09.
 */
@Database(
        entities = {ClaimItem.class, Attachment.class},
        version = 1,
        exportSchema = false)
@TypeConverters(ClaimDatabase.class)
public abstract class ClaimDatabase extends RoomDatabase {

    public abstract ClaimItemDao claimItemDao();

    public abstract AttachmentDao attachmentDao();

    @TypeConverter
    public static Long fromDate(final Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date toDate(final Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static String fromFile(final File value) {
        return value == null ? null : value.getAbsolutePath();
    }

    @TypeConverter
    public static File toFile(final String path) {
        return path == null ? null : new File(path);
    }

    @TypeConverter
    public static String fromCategory(final Category value) {
        return value == null ? null : value.name();
    }

    @TypeConverter
    public static Category toCategory(final String name) {
        return name == null ? null : Category.valueOf(name);
    }

    @TypeConverter
    public static String fromAttachmentType(final Attachment.Type value) {
        return value == null ? null : value.name();
    }

    @TypeConverter
    public static Attachment.Type toAttachmentType(final String name) {
        return name == null ? null : Attachment.Type.valueOf(name);
    }

    public Runnable createClaimItemTask(final ClaimItem claimItem) {
        return new Runnable() {
            @Override
            public void run() {
                beginTransaction();
                try {
                    final long claimId = claimItemDao().insert(claimItem);
                    claimItem.id = claimId; // assign the generated ID to the ClaimItem

                    for (final Attachment attachment : claimItem.getAttachments()) {
                        attachment.claimItemId = claimId; // assign the parent ID to the Attachments
                        attachment.id = attachmentDao().insert(attachment);
                    }

                    setTransactionSuccessful(); // COMMIT rather than ROLLBACK
                } finally {
                    endTransaction();
                }
            }
        };
    }
}
