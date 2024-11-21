<%--
  Created by IntelliJ IDEA.
  User: csr22
  Date: 27.10.2024
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index</title>
</head>
<body>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 20px;
    }
    h1 {
        color: #333;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    th, td {
        padding: 10px;
        border: 1px solid #ddd;
        text-align: left;
    }
    th {
        background-color: #f2f2f2;
    }
    a {
        text-decoration: none;
        color: #007bff;
        padding: 5px;
        border: 1px solid #007bff;
        border-radius: 5px;
    }
    a:hover {
        background-color: #007bff;
        color: white;
    }
</style>
<h1>User List</h1>
<a href="${pageContext.request.contextPath}/users/new">Add New User</a>
<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Actions</th>
    </tr>
    <th:block th:each="user : ${users}">
        <tr>
            <td th:text="${user.id}"></td>
            <td th:text="${user.name}"></td>
            <td th:text="${user.email}"></td>
            <td>
                <a th:href="@{/users/{id}/edit(id=${user.id})}">Edit</a>
                <a th:href="@{/users/{id}/delete(id=${user.id})}">Delete</a>
            </td>
        </tr>
    </th:block>
</table>
</body>
</html>
