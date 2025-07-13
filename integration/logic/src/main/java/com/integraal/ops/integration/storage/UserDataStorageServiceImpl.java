package com.integraal.ops.integration.storage;

import com.integraal.ops.integration.storage.beans.UserDataRetrieveInBean;
import com.integraal.ops.integration.storage.beans.UserDataRetrieveOutBean;
import com.integraal.ops.integration.storage.beans.UserDataStoreInBean;
import com.integraal.ops.integration.storage.beans.UserDataStoreOutBean;
import com.integraal.ops.integration.storage.errors.StorageReadError;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserDataStorageServiceImpl implements UserDataStorageService {
    // ! TODO :: 22/06/2025 :: for now using hashMap in memory, later change for a better way
    private Map<UUID, UserDataStoreInBean> dataStore = new HashMap<>();
    @Override
    public Either<StorageWriteError, UserDataStoreOutBean> storeUnsanitizedUserData(UserDataStoreInBean userDataToStore) {
        UUID dataStoreId = UUID.randomUUID();
        dataStore.put(dataStoreId, userDataToStore);
        UserDataStoreOutBean userDataStoreOutBean = UserDataStoreOutBean.builder()
            .storedDataId(dataStoreId)
            .storageType(userDataToStore.getUserDataStorageType())
            .build();
        return Either.right(userDataStoreOutBean);
    }

    @Override
    public Either<StorageReadError, UserDataRetrieveOutBean> retrieveUnsanitizedStoredUserData(UserDataRetrieveInBean userDataRetrieveInBean) {
        UUID dataStoreId = userDataRetrieveInBean.getStoredDataId();
        if (dataStore.containsKey(dataStoreId)) {
            UserDataStoreInBean object = dataStore.get(dataStoreId);
            UserDataRetrieveOutBean result = UserDataRetrieveOutBean.builder()
                .userDataStorageType(userDataRetrieveInBean.getStorageType())
                .inputData(object.getInputData())
                .inputMetaData(object.getInputMetaData())
                .build();
            return Either.right(result);
        } else {
            String errorMessage = String.format("Could not find stored data id '%s'", dataStoreId);
            return Either.left(new StorageReadError.UserStorageDataNotFoundReadError(errorMessage));
        }
    }
}
