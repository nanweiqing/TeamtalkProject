package com.mogujie.tt.imservice.event;

import com.mogujie.tt.imservice.entity.RecentInfo;

/**
 * @author : yingmu on 14-12-30.
 * @email : yingmu@mogujie.com.
 */
public class SessionDeleteEvent {

   //删除时的对象
   private RecentInfo recentInfo;

   public SessionDeleteEvent(RecentInfo recentInfo) {
      this.recentInfo = recentInfo;
   }

   public RecentInfo getRecentInfo() {
      return recentInfo;
   }

   public void setRecentInfo(RecentInfo recentInfo) {
      this.recentInfo = recentInfo;
   }
}
