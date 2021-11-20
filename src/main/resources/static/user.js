$(async function () {
    getUserPage();
    getUserBar();
})

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },

    findCurrentUser: async () => await fetch('user/userInfo')
}


//для вывода страницы вошедшего
async function getUserPage() {
    let table = $('#userTable tbody');
    table.empty();

    await userFetchService.findCurrentUser()
        .then(res => res.json())
        .then(user => {
            let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${user.roles.map(function (name) {
                return name.role
            })}
                        </tr>
                )`;
            table.append(tableFilling);
        })
}

async function getUserBar() {
    let userBar = $('#navBarUser');
    userBar.empty();

    await userFetchService.findCurrentUser()
        .then(res => res.json())
        .then(user => {
            let roles = user.roles.map(function (name) {
                return name.role
            })
            let barFilling = "<b>" + user.email + "</b> with roles: " + roles;
            userBar.append(barFilling);
        })
}