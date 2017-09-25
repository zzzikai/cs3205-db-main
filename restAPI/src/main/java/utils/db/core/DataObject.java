package utils.db.core;

import java.util.*;

public class DataObject extends HashMap<String, Object>{
  private DataTable table = null;
  public DataObject(DataTable table){
    this.table = table;
  }

  public int save(){
    return table.update(this);
  }

}