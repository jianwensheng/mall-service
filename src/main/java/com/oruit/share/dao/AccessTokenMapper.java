package com.oruit.share.dao;

import com.oruit.share.domain.AccessToken;
import java.util.List;
import java.util.Map;

public interface AccessTokenMapper {

    int update(Map<String, Object> paramMap);

    List<AccessToken> listById(AccessToken paramAccessToken);
}