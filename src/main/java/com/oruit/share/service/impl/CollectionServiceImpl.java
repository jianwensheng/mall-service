package com.oruit.share.service.impl;

import com.oruit.share.dao.TbCollectionMapper;
import com.oruit.share.domain.TbCollection;
import com.oruit.share.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private TbCollectionMapper tbCollectionMapper;

    @Override
    public List<TbCollection> queryTbCollectionList(HashMap map) {
        return tbCollectionMapper.queryTbCollectionList(map);
    }
}
