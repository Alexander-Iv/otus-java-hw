layout 'html-page.tpl',
title: 'Home',
bodyContents:
    contents {
        a(href: "/di-service/logout") {
            yield 'logout'
        }
        newLine()
        a(href: "/di-service/registration") {
            yield 'add'
        }
        a(href: "/di-service/test") {
            yield 'test'
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