<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<%@ page import="java.util.*" %>
<% Map<String, String> message = ((Map<String,String>)request.getAttribute("msg")); %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%= message.get("weibo.view.edit.label.edit_weibo") %></title>
  </head>
  <body>
    <h3><%= message.get("weibo.view.edit.label.edit_weibo") %></h3>
    <% Weibo weibo = (Weibo)request.getAttribute("obj"); %>
    <form action="../../weibo/<%= weibo.getId() %>/update" method="post">
      <input type="hidden" name="weibo.id" id="weibo.id" value='<%= weibo.getId() %>' />
      <p>
        <label for="content"><%= message.get("weibo.content") %></label>
        <input type="text" name="weibo.content" id="weibo.content" value='<%= weibo.getContent() %>' />
      </p>
      <input type="submit" name="submit" value="<%= message.get("weibo.view.edit.button.edit_weibo") %>" />
    </form>
  </body>
</html>
