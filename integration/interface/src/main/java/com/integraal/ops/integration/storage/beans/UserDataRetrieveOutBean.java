package com.integraal.ops.integration.storage.beans;

import com.integraal.ops.integration.transversal.beans.GenericOutbean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@With
public class UserDataRetrieveOutBean extends GenericOutbean {
    private Map<String, String> inputMetaData;
    private Object inputData;
    private UserDataStorageType userDataStorageType;
}
