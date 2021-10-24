<%@ page pageEncoding="UTF-8"%>
<div class="container">
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				    <h4 class="modal-title" id="myModalLabel"><i class="fa fa-sign-in"></i> Join Spring WebSocket Chat</h4>
				</div>
				<div class="modal-body">
					<div class="input-group">
					    <input type="text" id="userName" class="form-control" placeholder="Nickname">
					    <span class="input-group-btn">
					        <button class="btn btn-primary" type="button" onClick="sendNickName()">Join!</button>
					    </span>
					</div><!-- /input-group -->
				</div>
				<div class="modal-footer">
				    <button id="btnCloseMyModal" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>