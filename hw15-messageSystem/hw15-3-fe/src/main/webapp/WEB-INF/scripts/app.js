let stompClient = null;

const connect = () => {
    console.log("Stomp = " + stompClient);
    if (stompClient == null) {
        stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/response', (response) => show(JSON.parse(response.body)));
        });
    }
};

const show = (message) => {
    console.log("show(" + message.result + ")");
    $("#resultMessage").text(message.result);
    //$("#resultMessage").load();
};

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
};

const send = (target) => {
    let user = JSON.stringify({'userName': $("#userName").val(), 'userPassword': $("#userPassword").val()});
    console.log("user = " + user);
    stompClient.send(target, {}, user)
};

$(function () {
    $("#login-form").on('submit', (event) => {
        event.preventDefault();
        $("#login").click(send("/app/login/message"));
    });
});