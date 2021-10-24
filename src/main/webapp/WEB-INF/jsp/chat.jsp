<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 手持裝置優先 -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>chat room</title>
<%@ include file="/WEB-INF/jsp/common/initcss.jsp" %>
<script>
	var ctx = "${pageContext.request.contextPath}";
</script>
</head>
<body>
<input id="userId" type="hidden" value="${pageContext.session.id}"/>
<input id="fromUserName" type="hidden" value=""/>
<input id="toUserId" type="hidden" value="" />
<input id="toUserName" type="hidden" value="" />
<div class="container">

	<nav class="navbar navbar-default">
	  <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
	      <span class="navbar-brand">
	      	<i class="fa fa-weixin"></i> Spring WebSocket Chat
				</span>
	    </div>
	    <div id="bs-example-navbar-collapse-1" class="collapse navbar-collapse">
	      <ul class="nav navbar-nav navbar-right">
	      	<!-- What You See Is What You Get WYSIWYG-->
	        <li style="display: block">
            <a onclick="doOpenDialogTextEdit()" href="javascript: void(0)"><i class="fa fa-pencil-square-o"></i> WYSIWYG</a>
            </li>
	      	<li style="display: block">
            <a onclick="doChatSoundControl()" href="javascript: void(0)"><i id="chatsoundcontrol" class="fa fa-bell"></i> Bell</a>
            </li>
	        <li id="signout"><a href="javascript: void(0)" onclick="doExit()"><i class="fa fa-sign-out"></i> Exit</a></li>
	        <li id="signin"><a href="javascript: void(0)" onclick="doOpenDialogSingIn()"><i class="fa fa-sign-in"></i> Sing In</a></li>
	      </ul>
	    </div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>

	<div class="row">
		<div class="col-xs-12 col-md-4" >
   			<div class="panel panel-info">
				<div class="panel-heading">
					<h4 id="h4participant">Participants[0]</h4>
				</div>
				<div id="panelbodyparticipants" class="panel-body participant-box">

		  		</div>
		  		<div class="panel-footer">
		  			<a href="javascript: void(0)" onclick="setToUserId('','everyone')" ><i class="fa fa-users"></i> Everone</a>
		  		</div>
  			</div>
  		</div>
  		<div class="col-xs-12 col-md-8">
    		<div class="panel panel-default">
				<div class="panel-heading">
					<h4>Messages</h4>
				</div>
				<div id="panelbodyMessage" class="panel-body chat-box">

		  		</div>
  			</div>
  		</div>
	</div>
	<div class="row">
		<div class="col-xs-12">
			<span>You will send this message to </span><span id="messageToUsername" style="font-weight:bold;">everyone</span>
			<div class="input-group">
				<input id="message" type="text" disabled="disabled" class="form-control" placeholder="Write your message...">
				<span class="input-group-btn">
				   <button id="submit" class="btn btn-primary" type="button" disabled="disabled">Send</button>
				</span>
		    </div><!-- /input-group -->
	    </div>
	</div>
</div>

<%@ include file="/WEB-INF/jsp/component/dialog/nickname.jsp" %>
<%@ include file="/WEB-INF/jsp/component/dialog/wysiwyg.jsp" %>
<%@ include file="/WEB-INF/jsp/component/sounds/chatsound.jsp" %>
</body>
<%@ include file="/WEB-INF/jsp/common/initscript.jsp" %>
</html>