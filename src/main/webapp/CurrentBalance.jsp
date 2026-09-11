<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Check Balance</title>
    <link rel="stylesheet" href="Adminstyle.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />
</head>
</head>
<body>
	<div class="navbar">
        <div class="navbar-text">SHN BANK</div>
    </div>
    <div class="wrapper">
        <header>Current Balance</header>
            <div class="field email">
                <div class="input-area">
                <h1>
                	<%= request.getAttribute("balance") %>
                </h1>
                </div>
            </div>
        <!-- <div class="sign-txt">Not yet member? <a href="#">Signup now</a></div> -->
    </div>
</body>
</html>