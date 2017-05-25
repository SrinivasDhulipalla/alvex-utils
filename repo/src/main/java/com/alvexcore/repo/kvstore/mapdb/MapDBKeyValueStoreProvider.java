package com.alvexcore.repo.kvstore.mapdb;

import com.alvexcore.repo.kvstore.AbstractKeyValueStoreProvider;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Required;

import java.util.concurrent.ConcurrentMap;

public class MapDBKeyValueStoreProvider extends AbstractKeyValueStoreProvider {
    public static final String ID = "mapdb";
    private String path;
    private DB mapDB;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public ConcurrentMap getStore(String storeName) {
        return new AutocommitConcurrentMap(mapDB.hashMap(storeName).createOrOpen(), mapDB);
    }

    @Required
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void initialize() throws Exception {
        mapDB = DBMaker.fileDB(path)
            .transactionEnable()
            .fileMmapEnableIfSupported()
            .fileMmapPreclearDisable()
            .cleanerHackEnable()
        .make();
    }
}
