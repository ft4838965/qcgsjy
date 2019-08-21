package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.SsoAccountFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface Dao {

  public List<Map<String, Object>> selectBySQL(String sql);

  public int insertBySQL(String sql);

  public int updateBySQL(String sql);

  public int deleteBySQL(String sql);

  public void addSsoAccountFlows(@Param("safs") List<SsoAccountFlow> safs);
}
