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