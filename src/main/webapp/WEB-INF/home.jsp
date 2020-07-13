<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset-UTF-8" language="java" %>
<html>
<head>
    <style>
        body {
            background-color: #E6E6FA;
        }
    </style>
    <title>Home Page</title>
</head>
<body>
<div align="center">
    <h2 style="color: darkorange">Welcome, ${user.getUsername()} !!!</h2>

    <p>Register new user here </p>
    <form method="post">
        <input type="text" name="newUsername" placeholder="Username" required /><br>
        <input type="password" name="newPassword" placeholder="Password" required/><br>
        <input type="password" name="confirmPassword" placeholder="Confirm Password" required><br>
        <input type="submit" name="addUserInfo" value="Add user" required/>
    </form>
    <p style="color: red">${nameError}</p>

    <p style="color: red">${removing_error}</p>

    <h1 style="color: green"> Users Table</h1>
    <table border="2" style="background-color: darkorange">
        <tr><th> Username </th></tr>
        <c:forEach items="${userList}" var="eachUser">
            <tbody style="vertical-align: center">
            <tr>
                <td style="color: yellow" > ${eachUser} <td>
                    <form method="post">
                        <input type="hidden" name="user_to_use" value="${eachUser}"/>
                        <c:choose>
                            <c:when test="${eachUser != username}">
                                <input type="submit" name="removeUser" value="remove" onclick="{return confirm('Are you sure to remove this user ?')}"/>
                            </c:when>
                        </c:choose>
                    </form>
                </td>
                </td>
            </tr>
            </tbody>
        </c:forEach>
    </table>
    <br>

    <form method="post">
        <input type="submit" name="logout" value="Log out"/>
    </form>
</div>
</body>
</html>