package com.awen.push.core.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.awen.push.core.entity.PushEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PUSH_ENTITY".
*/
public class PushEntityDao extends AbstractDao<PushEntity, Void> {

    public static final String TABLENAME = "PUSH_ENTITY";

    /**
     * Properties of entity PushEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Type = new Property(0, int.class, "type", false, "push_type");
        public final static Property BaseData = new Property(1, String.class, "baseData", false, "push_base64_data");
        public final static Property Id = new Property(2, int.class, "id", false, "ID");
    }


    public PushEntityDao(DaoConfig config) {
        super(config);
    }
    
    public PushEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PUSH_ENTITY\" (" + //
                "\"push_type\" INTEGER NOT NULL ," + // 0: type
                "\"push_base64_data\" TEXT," + // 1: baseData
                "\"ID\" INTEGER NOT NULL );"); // 2: id
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_PUSH_ENTITY_ID ON \"PUSH_ENTITY\"" +
                " (\"ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PUSH_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PushEntity entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getType());
 
        String baseData = entity.getBaseData();
        if (baseData != null) {
            stmt.bindString(2, baseData);
        }
        stmt.bindLong(3, entity.getId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PushEntity entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getType());
 
        String baseData = entity.getBaseData();
        if (baseData != null) {
            stmt.bindString(2, baseData);
        }
        stmt.bindLong(3, entity.getId());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public PushEntity readEntity(Cursor cursor, int offset) {
        PushEntity entity = new PushEntity( //
            cursor.getInt(offset + 0), // type
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // baseData
            cursor.getInt(offset + 2) // id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PushEntity entity, int offset) {
        entity.setType(cursor.getInt(offset + 0));
        entity.setBaseData(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setId(cursor.getInt(offset + 2));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(PushEntity entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(PushEntity entity) {
        return null;
    }

    @Override
    public boolean hasKey(PushEntity entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
