<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Details</title>
    <link rel="stylesheet" href="Adminstyle.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />
</head>
</head>
<body>
	<div class="navbar">
        <div class="navbar-text">SHN BANK</div>
    </div>
    <div class="wrapper">
        <header>User Details</header>
            <div class="field email">
                <div class="input-area">
                <h2>
                	Name<%= request.getAttribute("name") %>
                </h2>
                <h2>
                	Mobile<%= request.getAttribute("mobile") %>
                </h2>
                <h2>
                	Email<%= request.getAttribute("email") %>
                </h2>
                <h2>
                	Aadhaar<%= request.getAttribute("aadhaar") %>
                </h2>
                <h2>
                	Pan<%= request.getAttribute("pan") %>
                </h2>
                <h2>
                	Pin<%= request.getAttribute("pin") %>
                </h2>
                <h2>
                	Balance<%= request.getAttribute("balance") %>
                </h2>
                <h2>
                	Account Number
                	<input type="text" value="<%= request.getAttribute("accNo") %>">
                </h2>
                <h2>
                	Customer Id<%= request.getAttribute("cid") %>
                </h2>
                </div>
            </div>
        <!-- <div class="sign-txt">Not yet member? <a href="#">Signup now</a></div> -->
    </div>
</body>
</html>