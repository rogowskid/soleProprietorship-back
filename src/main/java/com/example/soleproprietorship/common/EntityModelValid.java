package com.example.soleproprietorship.common;

import java.util.List;

public interface EntityModelValid<MODEL extends HasModel, IDENTITY> extends CommonEntity {

    /**
     * Metoda służąca do "Escapowania" nielegalnych znaków
     * @param model
     * @return
     */
    MODEL executeEncode(MODEL model);

    /**
     * Metoda służąca do "Escapowania" listy obiektów, które posiadają nielegalnye znaków
     * @param models
     * @return
     */
    List<MODEL> executeEncodeList(List<MODEL> models);

    /**
     * Metoda zwraca encję o podanym id
     * @param id
     * @return
     */
    MODEL getEntity(IDENTITY id);

    /**
     * Metoda zwraca wszystkie encje
     * @return
     */
    List<MODEL> getEntities();


}

