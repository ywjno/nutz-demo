<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<%@ page import="java.util.*" %>
<% Map<String, String> message = ((Map<String,String>)request.getAttribute("msg")); %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%= message.get("weibo.view.show.label.show_weibo") %></title>
  </head>
  <body>
    <h3><%= message.get("weibo.view.show.label.show_weibo") %></h3>
      <% Weibo weibo = (Weibo)request.getAttribute("obj"); %>
      <p>
        <label for="content"><%= message.get("weibo.content") %></label><br />
        <%= weibo.getContent() %>
      </p>
      <a href='../../weibo/<%= weibo.getId() %>/edit'><%= message.get("weibo.view.show.button.edit_weibo") %></a>
      <form action="../../weibo/<%= weibo.getId() %>/destroy" method="post">
        <input type="hidden" name="weibo.id" id="weibo.id" value='<%= weibo.getId() %>' />
        <input type="submit" name="submit" value="<%= message.get("weibo.view.show.button.delete_weibo") %>" />
      </form>
    <hr />
    <h4><%= message.get("weibo.view.show.label.comment_list") %></h4>
    <table>
      <tr>
        <th><%= message.get("comment.id") %></th>
        <th><%= message.get("comment.content") %></th>
      </tr>
      <% for (Comment comment : weibo.getComments()) { %>
      <tr>
        <td><%= comment.getId() %></td>
        <td><%= comment.getContent() %></td>
      </tr>
      <% } %>
    </table>
    <hr />
    <h4><%= message.get("weibo.view.show.label.add_comment") %></h4>
    <form action="../../weibo/<%= weibo.getId() %>/comment/save" method="post">
      <input type="hidden" name="comment.weiboId" id="comment.weiboId" value='<%= weibo.getId() %>' />
      <p>
        <label for="comment.content"><%= message.get("comment.content") %></label>
        <input type="text" name="comment.content" id="comment.content" />
      </p>
      <input type="submit" name="submit" value="<%= message.get("weibo.view.show.button.add_comment") %>" />
    </form>
  </body>
</html>
