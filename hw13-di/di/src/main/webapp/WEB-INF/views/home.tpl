layout 'html-page.tpl',
title: 'Home',
bodyContents:
    contents {
        yield "Hello, " + userName + "!"
        br()
        a(href: "/auth/logout") {
            yield 'logout'
        }
        br()
        a(href: "/registration") {
            yield 'Register new user'
        }
        br()
        if (users != null && !users.isEmpty()) {
            table(border:1) {
                tr {
                    th('Id')
                    th('Name')
                    th('Password')
                }
                users.each { user ->
                    tr {
                        td(user.id)
                        td(user.name)
                        td(user.password)
                    }
                }
            }
        }
}