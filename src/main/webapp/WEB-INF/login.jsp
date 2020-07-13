<%@ page contentType="text/html;charset-UTF-8" language="java" %>
<html>
<head>
    <style>
        body {
            background-color: #E6E6FA;
        }
    </style>
    <title>Login Page</title>
</head>
<body>
<div align="center">
    <h1 style="color: darkorange"> Welcome !!!</h1>
    <h2 style="color: black"> Please fill in your username and password </h2>
<form action="/login" method="post">
    <input type="text" placeholder="Enter Your Username" name="username" required><br>
    <input type="password" placeholder="Enter Your Password" name="password" required><br>
    <button type="submit">Login</button>
</form>
<p style="color: red"> ${error} </p>
</div>
</body>
</html>