package com.example.soleproprietorship.common;

import java.util.List;

public interface EntityModelValid<MODEL extends HasModel, IDENTITY> extends CommonEntity {

    MODEL executeEncode(MODEL model);

    List<MODEL> executeEncodeList(List<MODEL> models);

    MODEL getEntity(IDENTITY id);
    List<MODEL> getEntities();


}

