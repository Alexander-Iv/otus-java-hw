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
    if (message.Users !== undefined) {
        setAuth(message);
        $.postJSON("/home", JSON.stringify(message.Users), false);
        console.log("location.pathname = " + window.location.pathname)
        window.location.href = "/home";
    }
};

const setAuth = (message) => {
    console.log("setAuth(" + message.auth + ")");
    if (message.auth !== undefined) {
        let sessionName = sessionStorage.getItem("name");
        console.log("sessionName = " + sessionName);
        if (sessionName == null || sessionName != JSON.stringify(message.auth)) {
            $.postJSON("/auth/login", JSON.stringify(message.auth), false);
            sessionStorage.setItem("name", message.auth);
            console.log("sessionStorage = " + sessionStorage.getItem("name"));
        }
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

const send = (target, body) => {
    console.log("body = " + body);
    stompClient.send(messageSystemName + target, {}, body)
};

const sendUser = (target) => {
    let user = JSON.stringify({'User': {'userName': $("#userName").val(), 'userPassword': $("#userPassword").val()}});
    send(target, user);
};

$(function () {
    $("#login-form").on('submit', (event) => {
        event.preventDefault();
        $("#login").click(sendUser("/login/message"));
    });
    $("#registration-form").on('submit', (event) => {
        event.preventDefault();
        if (sessionStorage.getItem("name")) {
            $("#register").click(sendUser("/registerWithSession/message"));
        } else {
            $("#register").click(sendUser("/register/message"));
        }
    });
    $("#logout").on("click", (event) => {
        event.preventDefault();
        console.log("on click logout");
        sessionStorage.setItem("name", null);
        send("/logout/message", "logout");
    });
});