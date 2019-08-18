layout 'html-page.tpl',
title: 'Login',
bodyContents:
    contents {
        h3 {
            'Login'
        }
        form(method:"post", action:"/auth/login") {
            table {
                tr {
                    th(align:"left") {
                        'User Name'
                    }
                    td {
                        input(type:"text", id:"name", name:"userName")
                    }
                }
                tr {
                    th(align:"left") {
                        'Password'
                    }
                    td {
                        input(type:"password", id:"password", name:"userPassword")
                    }
                }
                tr {
                    th {
                        ''
                    }
                    td(align:"left") {
                        input(type:"submit", value:"Login")
                    }
                }
            }
        }
        yield 'If you are new user, please ' a(href:"/registration"){ yield 'register' }
        if (message != null && !message.isEmpty()) {
            br()
            yield message
        }
}