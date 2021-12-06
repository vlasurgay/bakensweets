package com.edu.netc.bakensweets.repository.interfaces;

import com.edu.netc.bakensweets.model.Account;
import com.edu.netc.bakensweets.model.AccountRole;
import com.edu.netc.bakensweets.model.Kitchenware;
import com.edu.netc.bakensweets.model.KitchenwareCategory;

import java.util.Collection;
import java.util.List;

public interface KitchenwareRepository extends  BaseCrudRepository<Kitchenware, Long>{
    Collection<String> getAllCategories();
    void reactivateById(Long id);
    Collection<Kitchenware> filterKitchenware (String name, List<Object> args, int limit, int offset, boolean order);
    int countFilteredKitchenware (String name, List<Object> args);
}
