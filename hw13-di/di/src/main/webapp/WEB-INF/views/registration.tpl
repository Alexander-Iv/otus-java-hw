layout 'html-page.tpl',
title: 'Registration',
bodyContents:
    contents {
        h3 {
            'Registration'
        }
        form(method:"post", action:"registration") {
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
}