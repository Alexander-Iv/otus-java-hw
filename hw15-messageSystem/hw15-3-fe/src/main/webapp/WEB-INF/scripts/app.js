let stompClient = null;
let messageSystemName = "/message-system";

const connect = () => {
    console.log("Stomp = " + stompClient);
    if (stompClient == null) {
        stompClient = Stomp.over(new SockJS(messageSystemName + "/websocket"));
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/message-broker', (response) => show(JSON.parse(response.body)));
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
    let user = JSON.stringify({'User': {'userName': $("#userName").val(), 'userPassword': $("#userPassword").val()}});
    console.log("user = " + user);
    stompClient.send(messageSystemName + target, {}, user)
};

$(function () {
    $("#login-form").on('submit', (event) => {
        event.preventDefault();
        $("#login").click(send("/login/message"));
    });
    $("#registration-form").on('submit', (event) => {
        event.preventDefault();
        $("#register").click(send("/register/message"));
    });
});