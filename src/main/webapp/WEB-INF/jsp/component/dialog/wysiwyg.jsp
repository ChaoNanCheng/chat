<%@ page pageEncoding="UTF-8"%>
<div class="container">
	<!-- Modal -->
	<div class="modal fade" id="textEditModal" tabindex="-1" role="dialog" aria-labelledby="textEditLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
				<div id="summernote"></div>
				</div>
				<div class="modal-footer">
					<button id="btnSendTextEdit" type="button" class="btn btn-primary" onclick="textEditSendMessage()">Send</button>
				    <button id="btnCloseTextEditModal" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>