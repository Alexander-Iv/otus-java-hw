layout 'html-page.tpl',
title: 'Home',
bodyContents:
    contents {
        a(href: "registration") {
            yield 'Register new user'
        }
        newLine()
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