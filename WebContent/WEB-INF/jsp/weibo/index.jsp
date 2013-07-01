<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<%@ page import="java.util.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Weibo List</title>
  </head>
  <body>
    <h3>Weibo List</h3>
    <% List<Weibo> posts = (List<Weibo>)request.getAttribute("obj"); %>
    <table>
      <tr>
        <th>Id</th>
        <th>Content</th>
        <th>Created At</th>
      </tr>
      <% for (Weibo weibo : posts) { %>
      <tr>
        <td><%= weibo.getId() %></td>
        <td><%= weibo.getContent() %></td>
        <td><%= weibo.getCreatedAt() %></td>
      </tr>
      <% } %>
    </table>
  </body>
</html>
