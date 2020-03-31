package com.oruit.share.service;

import com.oruit.share.domain.TbCollection;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CollectionService extends BaseService<TbCollection,Long>{

    List<TbCollection> queryTbCollectionList(HashMap map);

    TbCollection queryTbCollection(Long userToken,Long goodsId);

}
