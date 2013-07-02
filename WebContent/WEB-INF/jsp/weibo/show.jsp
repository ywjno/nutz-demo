<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="app.models.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Show Weibo</title>
  </head>
  <body>
    <h3>show weibo</h3>
      <% Weibo weibo = (Weibo)request.getAttribute("obj"); %>
      <p>
        <label for="content">content</label><br />
        <%= weibo.getContent() %>
      </p>
      <a href='../../weibo/<%= weibo.getId() %>/edit'>Edit this weibo</a>
      <form action="../../weibo/<%= weibo.getId() %>/destroy" method="post">
        <input type="hidden" name="weibo.id" id="weibo.id" value='<%= weibo.getId() %>' />
        <input type="submit" name="submit" value="delete this weibo" />
      </form>
  </body>
</html>
