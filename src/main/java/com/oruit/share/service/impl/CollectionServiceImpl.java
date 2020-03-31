package com.oruit.share.service.impl;

import com.oruit.share.dao.TbCollectionMapper;
import com.oruit.share.domain.TbCollection;
import com.oruit.share.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CollectionServiceImpl extends BaseServiceImpl<TbCollection,Long> implements CollectionService {

    @Autowired
    private TbCollectionMapper tbCollectionMapper;

    @Override
    @Autowired
    public void setBaseMapper() {
        super.setBaseMapper(tbCollectionMapper);
    }

    @Override
    public List<TbCollection> queryTbCollectionList(HashMap map) {
        return tbCollectionMapper.queryTbCollectionList(map);
    }

    @Override
    public TbCollection queryTbCollection(Long userId, Long goodsId) {
        return tbCollectionMapper.queryTbCollection(userId,goodsId);
    }


}
