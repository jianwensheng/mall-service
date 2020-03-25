package com.oruit.share.service.impl;

import com.oruit.share.dao.AccessTokenMapper;
import com.oruit.share.domain.AccessToken;
import com.oruit.share.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    @Autowired
    public AccessTokenMapper accessTokenMapper;

    public int update(Map<String, Object> map)
    {
        return accessTokenMapper.update(map);
    }

    public List<AccessToken> listById(AccessToken accessToken)
    {
        return accessTokenMapper.listById(accessToken);
    }
}
