package org.openmrs.module.mapperoverridedemo.businesslogic;



public class HoldAuthState {


   private static HoldAuthState holdAuthState = new HoldAuthState();
   private String uuid = "";
   private boolean Authenticated = false;

   public static HoldAuthState getInstance() {
       return holdAuthState;
   }

   private  HoldAuthState() {

   }

   public boolean isAuthenticated(){
       return this.Authenticated;
   }
   public String getUuid() {
       return uuid;
   }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setAuthenticated(boolean authenticated) {
        Authenticated = authenticated;
    }
}

