package com.integraal.ops.integration.storage;

import com.integraal.ops.integration.storage.beans.UserDataRetrieveInBean;
import com.integraal.ops.integration.storage.beans.UserDataRetrieveOutBean;
import com.integraal.ops.integration.storage.beans.UserDataStoreInBean;
import com.integraal.ops.integration.storage.beans.UserDataStoreOutBean;
import com.integraal.ops.integration.storage.errors.StorageReadError;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import com.integraal.ops.integration.transversal.services.LogicService;
import io.vavr.control.Either;

public interface UserDataStorageService extends LogicService {
    Either<StorageWriteError, UserDataStoreOutBean> storeUnsanitizedUserData(UserDataStoreInBean userDataToStore);

    Either<StorageReadError,UserDataRetrieveOutBean> retrieveUnsanitizedStoredUserData(UserDataRetrieveInBean userDataRetrieveInBean);
}
