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
                        'User Name'
                    }
                    th(align:"left") {
                        input(type:"submit", value:"Register")
                    }
                }
            }
        }
        yield 'If you are new user, please '
        a(href:"/registration"){ register }
}