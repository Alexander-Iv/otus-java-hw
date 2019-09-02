let stompClient = null;
let messageSystemName = "/message-system";

const connect = () => {
    console.log("Stomp = " + stompClient);
    if (stompClient == null) {
        stompClient = Stomp.over(new SockJS(messageSystemName + "/websocket"));
        stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/message-broker', (response) => {
                show(JSON.parse(response.body));
                //setAuth(JSON.parse(response.body));
                setUsers(JSON.parse(response.body));
                redirectIfPosible(JSON.parse(response.body));
            });
        });
    }
};

const show = (message) => {
    console.log("show(" + message.result + ")");
    $("#resultMessage").text(message.result);
    //$("#resultMessage").load();
};

const setAuth = (message) => {
    console.log("setAuth(" + message.auth + ")");
    if (message.auth !== undefined) {
        sessionStorage.setItem("name", message.auth.name);
    }
};

jQuery["postJSON"] = function( url, data, isAsync = true ) {
    return jQuery.ajax({
        url: url,
        type: "POST",
        contentType:"application/json;charset=UTF-8",
        dataType: "json",
        data: data,
        success: () => {},
        async: isAsync
    });
};

const setUsers = (message) => {
    console.log("setUsers(" + message.Users + ")");
    console.log("setUsers.Users = " + JSON.stringify(message.Users));
    console.log("setUsers.auth = " + JSON.stringify(message.auth));
    if (message.Users !== undefined && message.auth !== undefined) {
        $.postJSON("/auth/login", JSON.stringify(message.auth), false);
        $.postJSON("/home", JSON.stringify(message.Users), false);
        window.location.href = "/home";
    }
};

const redirectIfPosible = (message) => {
    console.log("redirect(" + message.redirect + ")");
    if (message.redirect !== undefined) {
        //$.get(message.redirect);
        //window.location.replace(message.redirect);
        window.location.href = message.redirect;
    }
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