var stompClient;
var userId;
var isSound = true;
//webSocket連線
function connect() {
	var socket = new SockJS(ctx + '/ws');
	stompClient = Stomp.over(socket);
	var error_callback = function(error) {
		alert("連結關閉，刷新頁面重新建立連接");
	}

	stompClient.connect({}, function(frame) {
		stompClient.subscribe("/ws/web/singin/add/all", function(data) {
			toasterMessage(JSON.parse(data.body), "add");
			getParticipants();
		});
	}, error_callback);
}
//向server訂閱其它連線
function connect2() {
	var dest = "/ws/web/chat/" + userId;
	//顯示私訊
	stompClient.subscribe(dest, function(data) {
		showMessage(JSON.parse(data.body));
	});
	//顯示訊息
	stompClient.subscribe("/ws/web/chat/everyone", function(data) {
		showMessage(JSON.parse(data.body));
	});
	//取得線上用戶
	stompClient.subscribe("/ws/web/chat/participants/get", function(data) {
		showParticipants(JSON.parse(data.body));
	});
	//廣播用戶離開
	stompClient.subscribe("/ws/web/exit/remove/all", function(data) {
		toasterMessage(JSON.parse(data.body), "remove");
		getParticipants();
	});
}
//斷線
function disconnect() {
	stompClient.disconnect();
}
//toaster提示訊息
function toasterMessage(data, status) {
	console.log(data);
	var userPlus = "，加入";
	var userTimes = "，離開"
	if(status == "add") {
		soundSingin();
		$.toaster({ priority : 'info', title : '<i class="fa fa-user-plus"></i>', message : data.userName + userPlus, settings: getToasterSetting()});
	} else {
		$.toaster({ priority : 'info', title : '<i class="fa fa-user-times"></i>', message : data.userName + userTimes, settings: getToasterSetting()});
	}

}
//取得toaster提示訊息設定
function getToasterSetting() {
	var setting = {
			timeout: 8000
		}
	return setting;
}

//設定userName
function setUserName(fromUsername) {
	$('#fromUserName').val(fromUsername).trigger('change');
}
//設定要連線給私人用戶時的設定
function setToUserId(touserId, tousername) {
	$('#toUserId').val(touserId);
	$('#toUserName').val(tousername);
	$('#messageToUsername').text(tousername);
}
//顯示線上使用者資訊
function showParticipants(data) {
	var participantsLength = $(data).length;
	$('#h4participant').text('Participants['+participantsLength+']');

	var participantsHtml = '<ul class="list-unstyled">';
	$(data).each(function(i,v){
		console.info("index:" + i + ",userId:" + v.userId +",userName:"+ v.userName);
		participantsHtml += combinedUser(v);
	});
	participantsHtml += '</ul>';

	$('#panelbodyparticipants').html(participantsHtml);
}
//線上使用者資訊
function combinedUser(user) {
	var me = "";
	if(userId == user.userId) {
		me = " Me!"
	}
	return '<li><a href="javascript: void(0)" onclick="setToUserId(\''+ user.userId +'\',\'' + user.userName +'\')" ><i class="fa fa-user"></i> ' + user.userName + '</a>'+ me+'</li>';
}
//顯示訊息
function showMessage(data) {
	var html = '';
	if(data.fromUserId == userId) {
		html = selfMessage(data);
	} else {
		html = notSelfMessage(data);
		soundMessage();
	}
	$("#panelbodyMessage").append(html);
	autoScrollEnd();
}

//自己寫的訊息
function selfMessage(data) {
	var secret = '<span style = "color:#888"><i class="fa fa-user-secret"></i>'+ data.toUserName + '</span>';

	if (data.toUserId == "everyone") {
		secret = '';
	}
	var nowdate = new Date();
	var templatehtml = '<div class="row" style="margin-bottom:5px"><div class="col-xs-12 col-md-2">' + secret +
      '<span class="navbar-right label label-default">' +
      nowdate.getHours() + ':' + nowdate.getMinutes() + '</span>' +
      '</div>' +
      '<div class="col-xs-12 col-md-10"><div class="bubble">' + data.message + '</div></div></div>';
	return templatehtml;
}

//別人寫的訊息
function notSelfMessage(data) {
	var secret = '<i class="fa fa-user-secret"></i>';
	var nowdate = new Date();
	if(data.toUserId == 'everyone') {
		secret = '';
	}
	var templatehtml = '<div class="row col-xs-12">' + secret + '<span class="badge">' +
	data.fromUserName + '</span></div>'+
      '<div class="row" style="margin-bottom:5px"><div class="col-xs-10"><div class="bubbleleft">' + data.message + '</div></div>' +
      '<div class="col-xs-2"><div role="alert" class="navbar-left label label-default">' +
      nowdate.getHours() + ':' + nowdate.getMinutes() +'</div></div></div>';
	return templatehtml;
}
//取得線上所有的成員清單
function getParticipants() {
	stompClient.send("/ws/web/chat/participants", {}, "");
}

//送出聊天用的名稱
function sendNickName() {
	var fromUserName = $.trim($("#userName").val());
	//不可為空
	if (fromUserName == "") {
		$("#userName").val("");
		return;
	}

	//通過websocket發送訊息
	var quote = {userId: userId, userName: fromUserName};
	stompClient.send("/ws/web/singin/add", {}, JSON.stringify(quote));
	setUserName(fromUserName);
	//清空userName
	$("#userName").val("");
	$("#btnCloseMyModal").click();
	connect2();
}
//送出訊息
function sendMessage(isFilter) {
	var toUserId = $("#toUserId").val();
	var fromUserName = $("#fromUserName").val();
	if (toUserId == "") {
		toUserId = "everyone";
	}

	var message = $.trim($("#message").val());
	if(message == ""){
		$("#message").val("");
		return;
	}
	var toUserName = $('#messageToUsername').text();
	//通過websocket發送訊息
	var quote = {fromUserId: userId, fromUserName:fromUserName, message: message, toUserId: toUserId, toUserName: toUserName, isFilter: isFilter};
	stompClient.send("/ws/web/chat", {}, JSON.stringify(quote));
	//發給別人的私訊
	if (toUserId != "everyone") {
		$("#panelbodyMessage").append(selfMessage(quote));
		autoScrollEnd();
	}

	//清空訊息框
	$("#message").val("");
}
//文字編輯區傳送訊息
function textEditSendMessage() {
	//取值
	var sHTML = $('#summernote').code();
	$("#message").val(sHTML);
	sendMessage("N");
	//清空
	$('#summernote').code("");
	//關閉dialog
	$("#btnCloseTextEditModal").click();

}
//當有新的訊息進入時，message移到最後一行
function autoScrollEnd() {
	$('#panelbodyMessage').animate({
		scrollTop: $('#panelbodyMessage').prop("scrollHeight")
	}, 600);

	return false;
}
//離開
function doExit() {
	disconnect();
	setUserName("");
}
//開啟dialog 填寫Text Edit
function doOpenDialogTextEdit() {
	$('#textEditModal').modal({ backdrop : 'static', keyboard: false });
}
//開啟dialog 填寫Nickname
function doOpenDialogSingIn() {
	$('#myModal').modal({ backdrop : 'static', keyboard: false });
	connect();
}
//登入的聲音
function soundSingin() {
	if(isSound)
		$('#chatAudioSingin')[0].play();
}
//發訊息的聲音
function soundMessage() {
	if(isSound)
		$('#chatAudioMessage')[0].play();
}
//聲音有聲無聲切換
function doChatSoundControl() {
	if(isSound) {
		$('#chatsoundcontrol').attr('class','fa fa-bell-slash');
		isSound = false;
	} else {
		$('#chatsoundcontrol').attr('class','fa fa-bell');
		isSound = true;
	}
}
$(document).ready(function() {
	$("#submit").click(function() {
		sendMessage("Y");
	});

	$("#message").keydown(function(event) {
		//keyCode=13為enter鍵
		if (event.keyCode == '13') {
			sendMessage("Y");
		}
	});
	//NickName若改變時
	$("#fromUserName").change(function() {
		if ($(this).val()=='')  {
			$("#message").attr("disabled", true);
			$("#submit").attr("disabled", true);
			$("#signout").hide();
			$("#signin").show();
			//清空
			showParticipants();
		} else {
			$("#message").attr("disabled", false);
			$("#submit").attr("disabled", false);
			$("#signout").show();
			$("#signin").hide();
		}
	});

	$("#userName").keydown(function(event) {
		//keyCode=13為enter鍵
		if (event.keyCode == '13') {
			sendNickName();
		}
	});

	//Initialize text edit

	$('#summernote').summernote({
		  height: 300,                 // set editor height
		  minHeight: 300,             // set minimum height of editor
		  maxHeight: 300,
		  toolbar: [
		            ['style', ['style']],
		            ['font', ['bold', 'italic', 'underline', 'clear']],
		            ['fontname', ['fontname']],
		            ['color', ['color']],
		            ['para', ['ul', 'ol', 'paragraph']],
		            ['height', ['height']],
		            ['insert', ['link', 'picture', 'hr']]
		          ]
		});

	userId = $("#userId").val();
	//NickName清空
	setUserName("");
	//開啟對話框
	doOpenDialogSingIn();

});