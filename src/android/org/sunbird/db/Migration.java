package org.sunbird.db;

import java.util.List;

public class Migration {

  private List<String> queryList;
  private int targetDbVersion;

  public List<String> getQueryList() {
    return queryList;
  }

  public Migration setQueryList(List<String> queryList) {
    this.queryList = queryList;
    return this;
  }

  public Migration setTargetDbVersion(int targetDbVersion) {
    this.targetDbVersion = targetDbVersion;
    return this;
  }

  public boolean isRequired(int oldVersion, int newVersion) {
    return oldVersion < this.targetDbVersion && this.targetDbVersion <= newVersion;
  }

}
