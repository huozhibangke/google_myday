package com.manage.calendar.bean;

public class DrawerBean {
   private int icon;
   private String title;
   private int type;

   public DrawerBean(int icon, String title, int type) {
      this.icon = icon;
      this.title = title;
      this.type = type;
   }

   public int getIcon() {
      return icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
