<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<%@ page import="java.util.*" %>
<% Map<String, String> message = ((Map<String,String>)request.getAttribute("msg")); %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%= message.get("weibo.view.index.label.weibo_list") %></title>
  </head>
  <body>
    <h3><%= message.get("weibo.view.index.label.weibo_list") %></h3>
    <% List<Weibo> posts = (List<Weibo>)request.getAttribute("obj"); %>
    <table>
      <tr>
        <th><%= message.get("weibo.id") %></th>
        <th><%= message.get("weibo.content") %></th>
        <th><%= message.get("weibo.created_at") %></th>
      </tr>
      <% for (Weibo weibo : posts) { %>
      <tr>
        <td><a href='../weibo/<%= weibo.getId() %>/show'><%= weibo.getId() %></a></td>
        <td><%= weibo.getContent() %></td>
        <td><%= weibo.getCreatedAt() %></td>
      </tr>
      <% } %>
    </table>
  </body>
</html>
