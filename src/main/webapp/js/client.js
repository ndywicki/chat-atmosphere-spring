(function($){	
	"use strict";

	var msgtpl = $('#msgtpl').html();	
	var lastmsg = false;
	$('#msgtpl').remove();

    var socket = $.atmosphere;
    var request = { url: document.location.toString() + 'chat',
                    contentType : "application/json",
                    logLevel : 'debug',
                    transport : 'websocket' ,
                    fallbackTransport: 'long-polling'};


    request.onOpen = function(response) {
    	//alert('onOpen response :'+response);
        
    };
    
    request.onMessage = function (response) {
        var message = response.responseBody;        
        try {
            var json = jQuery.parseJSON(message);
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message.data);
            return;
        }
        switch(json.event) {
        	case 'logged': logged(); break;
        	case 'newusr': newusr(json); break;
        	case 'disusr': disusr(json); break;
        	case 'newmsg': newmsg(json); break;
        }
    }   
    
    /**
     * Gestion des users
     */
    function logged(){
    	$('#login').fadeOut();
    	$('message').focus();
    };
    
    function newusr(user){
		$('#users').append('<img src="' + user.avatar + '" id="' + user.id + '">')
	};
	
	function disusr(user){
		$('#' + user.id).remove();
	};
	
	/**
	 * Nouveau de message
	 */
	function newmsg(message){
		if(lastmsg != message.user.id) {
			$('#messages').append('<div class="sep"</div>');
			lastmsg = message.user.id;
		}
		$('#messages').append('<div class=message>' + Mustache.render(msgtpl, message) + '</div>');
		$('#messages').animate({scrollTop : $('#messages').prop('scrollHeight') }, 100);
	};
    
    var subSocket = socket.subscribe(request);
    
    /**
     * Envoi du login
     */
    $('#loginform').submit(function(event){
		event.preventDefault();
		if ($('#username').val() == ''){
			alert('Vous devez entrer un pseudo !');
		} else {
			subSocket.push(jQuery.stringifyJSON({
				type		: 'user',
				event		: 'login',
				username	: $('#username').val(),
				mail		: $('#mail').val()
			}));
		}
		return false;
	});
    
    /**
	* Envoi de message
	*/
	$('#form').submit(function(event){
		event.preventDefault();
		//socket.emit('newmsg', {message: $('#message').val() })
		subSocket.push(jQuery.stringifyJSON({
			type		: 'message',			
			event		: 'newmsg',
			message		: $('#message').val()
		}));
		$('#message').val('');
		$('#message').focus();
	})
})(jQuery);