package com.oruit.share.service;

import com.oruit.share.domain.TbCollection;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CollectionService {

    List<TbCollection> queryTbCollectionList(HashMap map);

    TbCollection queryTbCollection(String userToken,String goodsId);

}
