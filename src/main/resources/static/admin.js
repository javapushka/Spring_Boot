$(async function () {
    await getTableWithUsers();
    getDefaultModal();
    addNewUser();
    getAdminPage();
    getAdminBar();
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },

    findAllUsers: async () => await fetch('admin/users'),
    findOneUser: async (id) => await fetch(`admin/getUser/${id}`),
    addNewUser: async (user) => await fetch('admin/new',
        {method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser: async (user, id) => await fetch(`admin/edit/${id}`,
        {method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(`admin/delete/${id}`,
        {method: 'DELETE', headers: userFetchService.head}),
    findCurrentUser: async () => await fetch('admin/getCurrentUser')
}

//вывод всех юзеров в таблицу
async function getTableWithUsers() {
    let table = $('#usersTable tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${user.roles.map(function (name) {
                    return name.role
                })}</td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-primary"
                                data-toggle="modal" data-target="#someDefaultModal">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger"
                                data-toggle="modal" data-target="#someDefaultModal">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })


    // обрабатываем нажатие на любую из кнопок edit или delete
    // достаем из нее данные и отдаем модалке, которую к тому же открываем
    $("#usersTable").find('button').on('click', (event) => {
        let defaultModal = $('#someDefaultModal');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })

}


// что то делаем при открытии модалки и при закрытии
// основываясь на ее дата атрибутах
async function getDefaultModal() {
    $('#someDefaultModal').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}


// редактируем юзера из модалки редактирования, забираем данные, отправляем
async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();


    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-primary" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
  <div class="container col-md-6 text-center">
            <form class="form-group text-center" id="editUser">
    <div>
        <label for="idEdit"><b>ID</b></label>
        <input type="text" value="${user.id}"
               class="form-control" id="idEdit"
               name="id" readonly><br/>
    </div>
    <div>
        <label for="firstNameEdit"><b>Firstname</b></label>
        <input type="text"
                value="${user.firstName}"
                class="form-control"
                id="firstNameEdit"
                name="firstName"><br/>
    </div>
    <div>
        <label for="lastNameEdit"><b>Lastname</b></label>
        <input type="text"
                value="${user.lastName}"
                class="form-control"
                id="lastNameEdit"
                name="lastName"><br/>
    </div>
    <div>
        <label for="ageEdit"><b>Age</b></label>
        <input type="number"
               value="${user.age}"
                class="form-control"
                id="ageEdit"
                name="age"><br/>
    </div>
    <div>
        <label for="emailEdit"><b>Email</b></label>
        <input type="text"
               value="${user.email}"
               class="form-control"
               id="emailEdit"
               name="email"><br/>
    </div>
    <div>
        <label for="passwordEdit"><b>Password</b></label>
        <input type="password"
               class="form-control"
               id="passwordEdit"
               name="password"><br/>
    </div>
    <div>
        <label for="roleEdit"><b>Role</b></label>
        <select size="2" class="form-control"
                id="roleEdit"
                name="role">
            <option value="ADMIN">ADMIN</option>
            <option value="USER">USER</option>
        </select>
    </div>
   </form>
  </div> 
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#editButton").on('click', async () => {
        let id = modal.find("#idEdit").val().trim();
        let firstName = modal.find("#firstNameEdit").val().trim();
        let lastName = modal.find("#lastNameEdit").val().trim();
        let age = modal.find("#ageEdit").val().trim();
        let email = modal.find("#emailEdit").val().trim();
        let password = modal.find("#passwordEdit").val().trim();
        let roles = modal.find("#roleEdit").val();

        //костыль
        let adminRole = [{
            id: "1",
            role: "ADMIN"
        }]
        let userRole = [{
            id: "2",
            role: "USER"
        }]
        if (roles === "ADMIN") {
            roles = adminRole
        } else {
            roles = userRole
        }

        let data = {
            id: id,
            firstName: firstName,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: roles
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

// удаляем юзера из модалки удаления
async function deleteUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();


    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-primary" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
  <div class="container col-md-6 text-center">
            <form class="form-group text-center" id="editUser">
    <div>
        <label for="idDelete"><b>ID</b></label>
        <input type="text" value="${user.id}"
               class="form-control" id="idDelete"
               name="id" readonly><br/>
    </div>
    <div>
        <label for="firstNameDelete"><b>Firstname</b></label>
        <input type="text"
                value="${user.firstName}"
                class="form-control"
                id="firstNameDelete"
                name="firstName" readonly><br/>
    </div>
    <div>
        <label for="lastNameDelete"><b>Lastname</b></label>
        <input type="text"
                value="${user.lastName}"
                class="form-control"
                id="lastNameDelete"
                name="lastName" readonly><br/>
    </div>
    <div>
        <label for="ageDelete"><b>Age</b></label>
        <input type="number"
               value="${user.age}"
                class="form-control"
                id="ageDelete"
                name="age" readonly><br/>
    </div>
    <div>
        <label for="emailDelete"><b>Email</b></label>
        <input type="text"
               value="${user.email}"
               class="form-control"
               id="emailDelete"
               name="email" readonly><br/>
    </div>
    <div>
        <label for="roleDelete"><b>Role</b></label>
        <select size="2" class="form-control"
                id="roleDelete"
                name="role" readonly>
            <option value="ADMIN">ADMIN</option>
            <option value="USER">USER</option>
        </select>
    </div>
   </form>
  </div> 
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#deleteButton").on('click', async () => {
        const response = await userFetchService.deleteUser(id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

// добавляем юзера и отправляем
async function addNewUser() {
    $('#submitAdd').click(async () => {
        let addUserForm = $('#formNewUser')
        let firstName = addUserForm.find("#firstNameAdd").val().trim();
        let lastName = addUserForm.find("#lastNameAdd").val().trim();
        let age = addUserForm.find("#ageAdd").val().trim();
        let email = addUserForm.find("#emailAdd").val().trim();
        let password = addUserForm.find("#passwordAdd").val().trim();
        let roles = addUserForm.find("#roleAdd").val();

        let adminRole = [{
            id: "1",
            role: "ADMIN"
        }]
        let userRole = [{
            id: "2",
            role: "USER"
        }]
        if (roles === "ADMIN") {
            roles = adminRole
        } else {
            roles = userRole
        }

        let data = {
            firstName: firstName,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: roles
        }
        const response = await userFetchService.addNewUser(data);
        if (response.ok) {
            getTableWithUsers();
            // open('#admin');
            addUserForm.find('#firstNameAdd').val('');
            addUserForm.find('#lastNameAdd').val('');
            addUserForm.find('#ageAdd').val('');
            addUserForm.find('#emailAdd').val('');
            addUserForm.find('#passwordAdd').val('');
            addUserForm.find('#roleAdd').val('');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert)
        }
    })
}

//для вывода страницы вошедшего
async function getAdminPage() {
    let table = $('#adminTable tbody');
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

async function getAdminBar() {
    let adminBar = $('#navBarAdmin');
    adminBar.empty();

    await userFetchService.findCurrentUser()
        .then(res => res.json())
        .then(user => {
            let roles = user.roles.map(function (name) {
                    return name.role
                    })
            let barFilling = "<b>" + user.email + "</b> with roles: " + roles;
            adminBar.append(barFilling);
        })
}