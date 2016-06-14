package com.mogujie.tt.imservice.event;

/**
 * @author : yingmu on 14-12-30.
 * @email : yingmu@mogujie.com.
 */
public enum  SessionEvent {

   RECENT_SESSION_LIST_SUCCESS,
   RECENT_SESSION_LIST_FAILURE,

   //回话人列表更新
   RECENT_SESSION_LIST_UPDATE,

   SET_SESSION_TOP,

   //最近会话列表上拉加载更多
   RECENT_SESSION_LIST_MORE,

   //最近会话列表下拉刷新
   RECENT_SESSION_LIST_PULL_DOWN,


}
