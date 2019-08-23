yieldUnescaped '<!DOCTYPE html>'
html(lang: "en") {
    head {
        meta('http-equiv':'"Content-Type" content="text/html; charset=utf-8"')
        title(title)
        //script(src:"/webjars/jquery/jquery.min.js")
        /*
        script(src:"/webjars/jquery/jquery.min.js")
        script(src:"/webjars/sockjs-client/sockjs.min.js")
        script(src:"/webjars/stomp-websocket/stomp.min.js")
        script(src:"/app.js")
        */
    }
    body {
        bodyContents()
        hr()
        input(id:"target", size="40", value="ws://localhost:8080/echo")
        br()
        button(id:"connect") { yield 'Connect' }
        button(id:"disconnect") { yield 'Disconnect' }
        br()
        br()
        yield 'Message:'
        input(id:"message", value="")
        button(id:"send") { yield 'Send' }
        br()
        p { yield 'Status output:' }
        pre {
            textarea(id:"statusOutput", rows="10", cols="50") {}
        }
        p { yield 'Message output:' }
        pre {
            textarea(id:"messageOutput", rows="10", cols="50") {}
        }
        p { yield 'Ping output:' }
        pre {
            textarea(id:"pingOutput", rows="10", cols="50") {}
        }
        script(type:'text/javascript', src:'/app.js')
        script()
    }
}