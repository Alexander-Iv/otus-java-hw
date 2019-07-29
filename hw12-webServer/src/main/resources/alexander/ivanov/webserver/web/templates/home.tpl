layout 'html-page.tpl',
title: 'Home',
bodyContents:
    contents {
        a(href: "/logout") {
            yield 'logout'
        }
        newLine()
        a(href: "/registration.html") {
            yield 'add'
        }
        newLine()
        if (!users.isEmpty()) {
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